package Client;

import Client.Controller.ClientController;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * Represents clientHandler class. We use netty library to connection between server and client.
 */
public class ClientHandler extends SimpleChannelInboundHandler {

    private ClientController clientController;
    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * Activate channel to connection with server.
     *
     * @param channelHandlerContext channel.
     */
    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        this.clientController = new ClientController(channelHandlerContext);
    }

    /**
     * Read message from server.
     *
     * @param channelHandlerContext channel context.
     * @param object object.
     */
    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) {

        StringBuilder message = new StringBuilder();
        // Read by byte.
        ByteBuf in = (ByteBuf) object;

        try {
            // If input is Readable, start to read message.
            while (in.isReadable()) {
                // Convert each byte to usual character.
                message.append((char) in.readByte());
                System.out.flush();
            }
        } catch (Exception ex) {
            this.setLog(ex);
        }

        // Call method to parse response from Server.
        this.clientController.processResponseFromServer(message.toString());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) {
        cause.printStackTrace();
        channelHandlerContext.close();
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