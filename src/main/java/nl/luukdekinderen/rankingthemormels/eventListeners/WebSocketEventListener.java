package nl.luukdekinderen.rankingthemormels.eventListeners;

import nl.luukdekinderen.rankingthemormels.services.RoomService;
import nl.luukdekinderen.rankingthemormels.models.GameRoom;
import nl.luukdekinderen.rankingthemormels.models.Player;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private RoomService roomService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {

        logger.info("New web socket connection.");

    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String id = (String) headerAccessor.getSessionAttributes().get("id");
        String roomId = (String) headerAccessor.getSessionAttributes().get("room_id");

        if (id != null && roomId != null) {
            logger.info("Web socket disconnection: " + id + " room " + roomId);

            GameRoom room = roomService.getRoom(roomId);
            room.setPlayerActiveStatus(id, false);
            Boolean isAllInActive = room.getIsAllInActive();

            if (isAllInActive) {
                roomService.deleteRoom(roomId);
                logger.info("Room deleted: " + roomId);
            }else{
                JSONObject message = new JSONObject();
                List<JSONObject> players = room.getPlayers().stream()
                        .map(Player::toJSONObject)
                        .collect(Collectors.toList());

                message.put("players", players);

                messagingTemplate.convertAndSend("/room/" + roomId, message.toString());
            }

        } else {
            logger.info("Web socket disconnection");
        }
    }
}
