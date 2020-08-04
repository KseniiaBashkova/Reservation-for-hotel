package Client.Controller;

import Client.View.Home;
import io.netty.channel.ChannelHandlerContext;

/**
 * Represents client controller class.
 */
public class ClientController {

    // Home View represents frontend of application;
    private Home home;

    /**
     * Init Home View.
     *
     * @param channelHandlerContext channel to connection with Server.
     */
    public ClientController(ChannelHandlerContext channelHandlerContext) {
        this.home = new Home(channelHandlerContext);
    }

    /**
     * Process response from server.
     *
     * @param answer String with server response.
     */
    public void processResponseFromServer(String answer) {
        this.home.sendAnswer(answer);
    }
}
