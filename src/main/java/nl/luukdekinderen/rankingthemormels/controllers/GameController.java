package nl.luukdekinderen.rankingthemormels.controllers;

import nl.luukdekinderen.rankingthemormels.eventListeners.WebSocketEventListener;
import nl.luukdekinderen.rankingthemormels.services.RoomService;
import nl.luukdekinderen.rankingthemormels.models.GameRoom;

import nl.luukdekinderen.rankingthemormels.models.Player;
import nl.luukdekinderen.rankingthemormels.models.Ranking;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RoomService roomService;

    @MessageMapping("/game/{roomId}/new-question")
    @SendTo("/room/{roomId}")
    public String newQuestion(@DestinationVariable String roomId, @Payload Player player) {

        GameRoom room = roomService.getRoom(roomId);

        if (room.isRealHost(player.getId())) {

            room.nextQuestion();

            JSONObject message = new JSONObject();
            message.put("question", room.getQuestion().getQuestion());

            return message.toString();
        }
        return null;
    }


    @MessageMapping("/game/{roomId}/state")
    @SendTo("/room/{roomId}")
    public String gameState(@DestinationVariable String roomId, @Payload String playerId, SimpMessageHeaderAccessor headerAccessor) {

        headerAccessor.getSessionAttributes().put("room_id", roomId);
        headerAccessor.getSessionAttributes().put("id", playerId);

        GameRoom gameRoom = roomService.getRoom(roomId);
        Player player = gameRoom.getPlayer(playerId);

        player.setActive(true);
        System.out.println("player rejoin: " + playerId + " room " + roomId);


        JSONObject message = new JSONObject();

        String question = gameRoom.getQuestion().getQuestion();
        message.put("question", question);

        List<JSONObject> players = getPlayerObjs(gameRoom);
        message.put("players", players);


        JSONObject playerObj = player.toJSONObject();
        message.put("player", playerObj);

        return message.toString();
    }

    @MessageMapping("/game/{roomId}/ranking")
    public void addRanking(@DestinationVariable String roomId, @Payload Ranking ranking, SimpMessageHeaderAccessor headerAccessor) {

        GameRoom room = roomService.getRoom(roomId);
        String id = (String) headerAccessor.getSessionAttributes().get("id");

        room.addRanking(id, ranking);


        if(room.isDoneRanking()){

            JSONObject message = new JSONObject();

            message.put("result", room.getRoundResult().toJson() );

            messagingTemplate.convertAndSend("/room/" + roomId, message.toString());
        }
    }



    private List<JSONObject> getPlayerObjs(GameRoom gameRoom) {
        return gameRoom.getPlayers().stream()
                .map(Player::toJSONObject)
                .collect(Collectors.toList());
    }

}
