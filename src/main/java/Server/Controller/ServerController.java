package Server.Controller;

import Server.Model.*;
import Server.Service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;

/**
 * Represents server controller class to procces requests and send answer to client.
 */
public class ServerController {

    private Logger logger = Logger.getLogger(this.getClass());

    private String firstPartOfMessage = "";

    /**
     * Get correct entity by class name
     *
     * @param className
     * @param json
     * @return
     * @throws IOException
     */
    public static IEntity getCorrectEntity(String className, String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        switch (className) {
            case "Reservation":
                return objectMapper.readValue(json, Reservation.class);
            case "Room":
                return objectMapper.readValue(json, Room.class);
            case "Customer":
                return objectMapper.readValue(json, Customer.class);
            case "Reception":
                return objectMapper.readValue(json, Reception.class);
            default:
                return null;
        }
    }

    /**
     * Process request from client.
     *
     * @param message
     * @return
     */
    public String processMessage(String message, Connection connection) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // convert message to IntegrationRequest
            if (this.firstPartOfMessage.isBlank()) {
                this.firstPartOfMessage = message;
            } else {
                this.firstPartOfMessage += message;
            }
            IntegrationRequest integrationRequest = objectMapper.readValue(this.firstPartOfMessage, IntegrationRequest.class);
            this.firstPartOfMessage = "";
            return selectCorrectClass(integrationRequest, connection);
        } catch (IOException e) {
            this.setLog(e);
        }

        return null;
    }

    /**
     * Select correct class by data from message
     *
     * @return String
     */
    private String selectCorrectClass(IntegrationRequest integrationRequest, Connection connection) {
        Method method;
        switch (integrationRequest.getClassName()) {
            case "Reservation":
                ReservationService reservationService = (ReservationService) ReservationService.getService();
                method = reservationService.selectMethod(integrationRequest);
                return reservationService.methodInvoke(method, integrationRequest.getEntity(), connection);
            case "Room":
                RoomService roomService = (RoomService) RoomService.getService();
                method = roomService.selectMethod(integrationRequest);
                return roomService.methodInvoke(method, integrationRequest.getEntity(), connection);
            case "Customer":
                CustomerService customerService = (CustomerService) CustomerService.getService();
                method = customerService.selectMethod(integrationRequest);
                return customerService.methodInvoke(method, integrationRequest.getEntity(), connection);
            case "Reception":
                ReceptionService receptionService = (ReceptionService) ReceptionService.getService();
                method = receptionService.selectMethod(integrationRequest);
                return receptionService.methodInvoke(method, integrationRequest.getEntity(), connection);
            default:
                return "";
        }
    }

    /**
     * Save logs
     *
     * @param ex
     */
    void setLog(Exception ex) {
//        logger.info(ex.getMessage());
    }
}
