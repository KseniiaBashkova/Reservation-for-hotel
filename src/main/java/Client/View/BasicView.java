package Client.View;

import Help.IntegrationResponse;
import Server.Model.Customer;
import Server.Model.IEntity;
import Server.Model.Reception;
import Server.Model.Reservation;
import Server.Model.Room;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

class BasicView extends javax.swing.JFrame {

    String response;
    protected ObjectMapper objectMapper;
    private Logger logger = Logger.getLogger(this.getClass());


    BasicView() {
        this.objectMapper = new ObjectMapper();
        this.response = "";
    }

    /**
     * Count time to get response from server.
     *
     * @return Time of wait response from server.
     */
    double timer() {
        double time = 0;
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
                IntegrationResponse integrationResponse = this.objectMapper.readValue(this.response, IntegrationResponse.class);
                break;
            } catch (InterruptedException | IOException e) {
            }
            time += 0.1;
        }
        return time;
    }

    /**
     * Parse data from response.
     *
     * @return Array of correct IEntity object.
     */
    IEntity[] parseResponse(double time) {

        IntegrationResponse integrationResponse;
        try {
            integrationResponse = objectMapper.readValue(this.response, IntegrationResponse.class);
            switch (integrationResponse.getCode()) {
                case 1:
                    return getDataFromResponse(integrationResponse);
                case 3:
                    JOptionPane.showMessageDialog(null, integrationResponse.getDescription() + "\nResponse was received in " + time + " seconds.", "Information Dialog",
                            JOptionPane.INFORMATION_MESSAGE);
                    return null;
                case 4:
                    JOptionPane.showMessageDialog(null, integrationResponse.getDescription() + "\nResponse was received in " + time + " seconds.", "Information Dialog",
                            JOptionPane.INFORMATION_MESSAGE);
                    return new IEntity[]{};
                default:
                    JOptionPane.showMessageDialog(null, integrationResponse.getDescription() + "\nResponse was received in " + time + " seconds.", "Information Dialog",
                            JOptionPane.ERROR_MESSAGE);
                    return null;
            }
        } catch (IOException e) {
            this.setLog(e);
        }

        return null;
    }

    /**
     * Get parsed data from server response.
     *
     * @param integrationResponse IntegrationResponse object with parsed response from server.
     * @return Corrected array of IEntity
     */
    IEntity[] getDataFromResponse(IntegrationResponse integrationResponse) throws IOException {
        // Select right conversion path by Entity type.
        switch (integrationResponse.getEntity()) {
            case ROOM:
                return this.objectMapper.readValue(integrationResponse.getData(), Room[].class);
            case RECEPTION:
                return this.objectMapper.readValue(integrationResponse.getData(), Reception[].class);
            case CUSTOMER:
                return this.objectMapper.readValue(integrationResponse.getData(), Customer[].class);
            case RESERVATION:
                return this.objectMapper.readValue(integrationResponse.getData(), Reservation[].class);
        }

        return null;
    }

    /**
     * Set response from server, after for each request we must to clean this value.
     *
     * @param response Response from Server.
     */
    void setResponse(String response) {
        if (this.response.isBlank()) {
            this.response = response;
        } else {
            this.response += response;
        }
    }

    /**
     * Save logs
     *
     * @param ex
     */
    void setLog(Exception ex) {
        logger.error(ex.getMessage());
    }
}