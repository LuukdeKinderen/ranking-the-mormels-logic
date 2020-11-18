package nl.luukdekinderen.rankingthemormels.resources;

import nl.luukdekinderen.rankingthemormels.models.GameRoom;
import nl.luukdekinderen.rankingthemormels.models.Player;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RoomService roomService;


    @Autowired
    public void RoomController() {

    }

    @MessageMapping("/room/create")
    public void addRooms(@Payload GameRoom gameRoom, SimpMessageHeaderAccessor headerAccessor) {
        String roomId = gameRoom.getId();
        Player player = gameRoom.getPlayers().get(0);
        player.setImageIndex(0);

        headerAccessor.getSessionAttributes().put("room_id", roomId);
        headerAccessor.getSessionAttributes().put("username", player.getName());

        boolean added = roomService.addRoom(gameRoom);

        if (added) {
            logger.info("Room created: " + roomId);

            JSONObject message = new JSONObject();
            message.put("players", gameRoom.getPlayerObjects());
            message.put("joinRoom", player.toJSONObject());

            messagingTemplate.convertAndSend("/room/" + roomId, message.toString());
        } else {
            sendError(
                    roomId,
                    player,
                    "Room already exists"
            );
        }
    }


    @MessageMapping("/room/{roomId}/addPlayer")
    public void joinRoom(@DestinationVariable String roomId, @Payload Player newPlayer, SimpMessageHeaderAccessor headerAccessor) {

        headerAccessor.getSessionAttributes().put("room_id", roomId);
        headerAccessor.getSessionAttributes().put("username", newPlayer.getName());

        GameRoom gameRoom = roomService.getRoom(roomId);

        if (gameRoom == null) {
            sendError(
                    roomId,
                    newPlayer,
                    "Room " + roomId + " does not exist"
            );
            return;
        }

        boolean added = gameRoom.AddPlayer(newPlayer);

        if (added) {
            logger.info("Player \"" + newPlayer.getName() + "\" joined room: " + roomId);

            JSONObject message = new JSONObject();
            message.put("players", gameRoom.getPlayerObjects());
            message.put("joinRoom", newPlayer.toJSONObject());

            messagingTemplate.convertAndSend("/room/" + roomId, message.toString());
        } else {
            sendError(
                    roomId,
                    newPlayer,
                    "Player name \"" + newPlayer.getName() + "\" already exists in room: " + roomId
            );
        }
    }


    private void sendError(String roomId, Player player, String message) {
        logger.info(message);

        JSONObject error = new JSONObject();
        error.put("error", message);
        error.put("player", player.toJSONObject());

        messagingTemplate.convertAndSend("/room/" + roomId, error.toString());
    }

}
