package Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;

/**
 * Represents Client side of project.
 *
 * @author Nikita Grigoryev
 */
public class Client implements Runnable {

    /**
     * Connection port with server.
     */
    private int port;

    public Client(int port) {
        this.port = port;
    }

    /**
     * Start client side.
     */
    private void startClient() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {

            Bootstrap clientBootstrap = new Bootstrap();

            clientBootstrap.group(group);
            clientBootstrap.channel(NioSocketChannel.class);
            clientBootstrap.remoteAddress(new InetSocketAddress("localhost", this.port));
            clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
                // Init channel to connect with server side.
                protected void initChannel(SocketChannel socketChannel) {
                    socketChannel.pipeline().addLast(new ClientHandler());
                }
            });
            ChannelFuture channelFuture = clientBootstrap.connect().sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    /**
     * Run client thread.
     */
    @Override
    public void run() {
        try {
            startClient();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}