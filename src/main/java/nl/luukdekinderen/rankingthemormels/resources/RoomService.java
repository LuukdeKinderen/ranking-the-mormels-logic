package nl.luukdekinderen.rankingthemormels.resources;

import nl.luukdekinderen.rankingthemormels.models.GameRoom;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    private List<GameRoom> rooms;

    @Autowired
    public void RoomService(){
        rooms = new ArrayList<GameRoom>();
    }

    public GameRoom getRoom(String roomId){
        for (GameRoom gameRoom: rooms) {
            if(gameRoom.getId().equals(roomId)){
                return gameRoom;
            }
        }
        return null;
    }

    public boolean addRoom(GameRoom newGameRoom){
        boolean flag = false;
        for (GameRoom gameRoom: rooms) {
            if(gameRoom.getId().equals(newGameRoom.getId())){
                flag = true;
            }
        }
        if(!flag){
            rooms.add(newGameRoom);
            return true;
        }
        return false;
    }

}
