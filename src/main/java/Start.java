import Help.Config;
import org.apache.log4j.Logger;
import Client.Client;
import Server.Server;

import java.util.Properties;

/**
 * Main class to start application.
 */
public class Start {

    private static Logger logger = Logger.getLogger(Start.class);

    public static void main(String[] args) {
        Properties properties = new Properties();
        Config config = Config.getConfig(properties);
        logger.trace("Get configuration: \n" + config.toString());

        startServer(config.port);
        startClient(config.port);
    }


    /**
     * Start server thread.
     *
     * @param port
     */
    private static void startServer(int port) {
        Thread server = new Thread(new Server(port));
        server.start();
        logger.trace("Server starts\n");
    }

    /**
     * Start client thread.
     *
     * @param port
     */
    private static void startClient(int port) {
        Thread client = new Thread(new Client(port));
        client.start();
        logger.trace("Client starts\n");
    }
}
