package nl.luukdekinderen.rankingthemormels.controllers;

import nl.luukdekinderen.rankingthemormels.eventListeners.WebSocketEventListener;
import nl.luukdekinderen.rankingthemormels.services.QuestionService;
import nl.luukdekinderen.rankingthemormels.services.RoomService;
import nl.luukdekinderen.rankingthemormels.models.GameRoom;
import nl.luukdekinderen.rankingthemormels.models.Player;
import nl.luukdekinderen.rankingthemormels.models.Question;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;


@Controller
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RoomService roomService;

    @Autowired
    private QuestionService questionService;


    @Bean
    public void RoomController() {

    }

    @MessageMapping("/room/create")
    public void addRooms(@Payload GameRoom gameRoom, SimpMessageHeaderAccessor headerAccessor) {
        //TODO: varialble questioncount
        Question[] questions = questionService.getQuestions(10);
        gameRoom.setQuestions(questions);

        String roomId = gameRoom.getId();
        Player player = gameRoom.getPlayers().get(0);
        player.setImageIndex(0);

        headerAccessor.getSessionAttributes().put("room_id", roomId);
        headerAccessor.getSessionAttributes().put("id", player.getId());

        boolean added = roomService.addRoom(gameRoom);

        if (added) {
            logger.info("Room created: " + roomId);

            JSONObject message = new JSONObject();

            List<JSONObject> players = getPlayerObjs(gameRoom);
            message.put("players", players);

            JSONObject playerObj = player.toJSONObject();
            message.put("joinRoom", playerObj);

            messagingTemplate.convertAndSend("/room/" + roomId, message.toString());
        } else {
            messagingTemplate.convertAndSend("/room/" + roomId, getError(
                    player,
                    "Room already exists"
            ));
        }
    }


    @MessageMapping("/room/{roomId}/addPlayer")
    @SendTo("/room/{roomId}")
    public String joinRoom(@DestinationVariable String roomId, @Payload Player newPlayer, SimpMessageHeaderAccessor headerAccessor) {

        headerAccessor.getSessionAttributes().put("room_id", roomId);
        headerAccessor.getSessionAttributes().put("id", newPlayer.getId());

        GameRoom gameRoom = roomService.getRoom(roomId);

        if (gameRoom == null) {
            return getError(
                    newPlayer,
                    "Room " + roomId + " does not exist"
            );
        }

        boolean added = gameRoom.addPlayer(newPlayer);

        if (added) {
            logger.info("Player \"" + newPlayer.getName() + "\" joined room: " + roomId);

            JSONObject message = new JSONObject();

            List<JSONObject> players = getPlayerObjs(gameRoom);
            message.put("players",players);

            JSONObject playerObj = newPlayer.toJSONObject();
            message.put("joinRoom", playerObj);

            return message.toString();

        } else {
            return getError(
                    newPlayer,
                    "Player name \"" + newPlayer.getName() + "\" already exists in room: " + roomId
            );
        }

    }

    private List<JSONObject> getPlayerObjs(GameRoom gameRoom) {
        return gameRoom.getPlayers().stream()
                .map(Player::toJSONObject)
                .collect(Collectors.toList());
    }

    private String getError(Player player, String message) {
        logger.info(message);

        JSONObject error = new JSONObject();
        error.put("error", message);
        error.put("player", player.toJSONObject());

        return error.toString();
    }

}
