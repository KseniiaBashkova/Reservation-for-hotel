package Server.Service;

import Help.Entity;
import Help.IntegrationDBHelper;
import Help.IntegrationResponse;
import Help.Operation;
import Server.Model.IEntity;
import Server.Model.Reservation;
import Server.Model.Room;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Represent reservations service to connection with database.
 */
public class ReservationService extends BaseService {

    static ReservationService singleInstance;

    public static BaseService getService() {

        if (singleInstance == null) {
            singleInstance = new ReservationService();
        }

        return singleInstance;
    }
    
    /**
     * Get all row from database
     *
     * @param entity
     * @param connection
     * @return String
     */
    @Override
    public String getAll(IEntity entity, Connection connection) {

        String SQL = "SELECT reservation.id, check_in, check_out, customer_id, room_id, passport as customerpassport, room_number as roomnumber\n" +
                " FROM public.reservation\n" +
                " INNER JOIN customer ON reservation.customer_id = customer.id\n" +
                " INNER JOIN room ON reservation.room_id = room.id;";

        try {
            ArrayList<IEntity> rows = getRows(SQL, connection);
            if (rows.isEmpty()) {
                return createAnswer(3, "Does not exist any " + this.className.toLowerCase() + "s.", "", Operation.GET_ALL, getTypeOfEntity());
            } else {
                return createAnswer(1, this.className + "s has been got", this.objectMapper.writeValueAsString(rows), Operation.GET_ALL, getTypeOfEntity());
            }
        } catch (Exception ex) {
            setLog(ex);
            return createAnswer(2, "Error during getting all " + this.className, "", Operation.GET_ALL, getTypeOfEntity());
        }
    }

    /**
     * Get correct attribute
     *
     * @param reservation
     * @return
     */
    public String getCorrectConditionToUpdateOrFindEntity(IEntity reservation) {
        return " WHERE customer_id = " + ((Reservation) reservation).getCustomer_id();
    }
    
     /**
     * Get row by Id
     *
     * @param entity
     * @return String
     */
    @Override
    public String getById(IEntity entity, Connection connection) {

        String SQL = "SELECT reservation.id, check_in, check_out, customer_id, room_id, passport as customerpassport, room_number as roomnumber\n" +
                " FROM public.reservation\n" +
                " INNER JOIN customer ON reservation.customer_id = customer.id\n" +
                " INNER JOIN room ON reservation.room_id = room.id WHERE reservation.id = " + entity.getId() + ";";
        try {
            IntegrationDBHelper integrationDBHelper = this.getBasicData(SQL, connection);
            assert integrationDBHelper != null;
            if (integrationDBHelper.getResultSet().next()) {
                return createAnswer(1, this.className + " has been got", objectMapper.writeValueAsString(new ArrayList<>(Arrays.asList(getRow(integrationDBHelper)))), Operation.GET_BY_ID, getTypeOfEntity());
            }

            return createAnswer(3, this.className + " has not been found", "", Operation.GET_BY_ID, getTypeOfEntity());
        } catch (Exception ex) {
            setLog(ex);
            return createAnswer(2, "Error during getting by id", "", Operation.GET_BY_ID, getTypeOfEntity());
        }
    }
    /**
     * Get rooms which are not reserved this dates
     *
     * @param reservation
     * @param connection
     * @return
     */
    public String getFreeRooms(IEntity reservation, Connection connection) {
        String reservationsJson = this.getAll(reservation, connection);
        try {
            IntegrationResponse integrationResponseReservation = this.objectMapper.readValue(reservationsJson, IntegrationResponse.class);
            this.className = "Room";
            String roomsJson = super.getAll(new Room(), connection);
            IntegrationResponse integrationResponseRoom = this.objectMapper.readValue(roomsJson, IntegrationResponse.class);
            ArrayList<Room> rooms = new ArrayList<>(Arrays.asList(this.objectMapper.readValue(integrationResponseRoom.getData(), Room[].class)));
            ArrayList<Room> validRooms = new ArrayList<>();
            if (integrationResponseReservation.getData() != null && !integrationResponseReservation.getData().isBlank()) {
                Reservation[] reservations = this.objectMapper.readValue(integrationResponseReservation.getData(), Reservation[].class);
                Set<Integer> busyRooms = new HashSet<>();

                // find all reserved rooms this dates
                for (Reservation resDB : reservations) {
                    Date reservationCheckIn = ((Reservation)reservation).getCheck_in();
                    Date reservationCheckOut = ((Reservation)reservation).getCheck_out();
                    if (
                            resDB.getCheck_in().toString().equals(reservationCheckIn.toString()) && resDB.getCheck_out().toString().equals(reservationCheckOut.toString()) ||
                                    resDB.getCheck_in().after(reservationCheckIn) && resDB.getCheck_in().before(reservationCheckOut) ||
                                    resDB.getCheck_out().after(reservationCheckIn) && resDB.getCheck_out().before(reservationCheckOut) ||
                                    resDB.getCheck_in().before(reservationCheckIn) && resDB.getCheck_out().after(reservationCheckOut)) {

                        busyRooms.add(resDB.getRoom_id());
                    }
                }

                for (Room room : rooms) {
                    if (busyRooms.contains(room.getId())) {
                        busyRooms.remove(room.getId());
                    } else {
                        validRooms.add(room);
                    }
                }
                this.className = "Reservation";
            } else {
                validRooms = rooms;
            }

            if (validRooms.isEmpty()) {
                return createAnswer(3, "Does not exist any rooms.", this.objectMapper.writeValueAsString(validRooms.toArray(new Room[0])), Operation.GET_BY_SPECIFIC_ATTRIBUTE, Entity.ROOM);
            } else {
                return createAnswer(1, "Rooms has been got", this.objectMapper.writeValueAsString(validRooms), Operation.GET_BY_SPECIFIC_ATTRIBUTE, Entity.ROOM);
            }

        } catch (Exception ex) {
            setLog(ex);
            return createAnswer(2, "Error during getting free rooms " + this.className, "", Operation.GET_BY_SPECIFIC_ATTRIBUTE, Entity.ROOM);
        }
    }
}
