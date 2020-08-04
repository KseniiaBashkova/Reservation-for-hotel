package Server.Service;

import Server.Model.IEntity;
import Server.Model.Room;

public class RoomService extends BaseService {

    static RoomService singleInstance;

    public static BaseService getService() {

        if (singleInstance == null) {
            singleInstance = new RoomService();
        }

        return singleInstance;
    }

    /**
     * Get correct attribute
     *
     * @param room
     * @return
     */
    @Override
    public String getCorrectConditionToUpdateOrFindEntity(IEntity room) {
        return " WHERE room_number = " + ((Room) room).getRoom_number();
    }
    
}