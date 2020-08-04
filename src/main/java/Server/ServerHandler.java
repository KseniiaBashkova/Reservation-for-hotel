package Server;

import Help.DatabaseConnection;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import Server.Controller.ServerController;

/**
 * Represents server handler to connection with Client.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private ServerController serverController;

    ServerHandler(ServerController serverController) {
        this.serverController = serverController;
    }

    /**
     * Read channel
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        StringBuilder message = new StringBuilder();

        try {
            while (in.isReadable()) {
                message.append((char) in.readByte());
                System.out.flush();
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
        String answer = this.serverController.processMessage(message.toString(), DatabaseConnection.getConnection());
        if (answer != null) {
            ctx.write(Unpooled.copiedBuffer(answer, CharsetUtil.UTF_8));
            ctx.flush();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
