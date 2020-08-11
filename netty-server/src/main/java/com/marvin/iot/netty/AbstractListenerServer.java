package com.marvin.iot.netty;

import com.marvin.iot.netty.handler.ChannelManagerHandler;
import com.marvin.iot.netty.handler.EchoHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author marvinwjs
 */
public abstract class AbstractListenerServer implements BasicServer{
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractListenerServer.class);

    private ServerBootstrap serverBootstrap;
    private NioEventLoopGroup bossEventLoopGroup;
    private NioEventLoopGroup workerEventLoopGroup;

    private int port;
    private boolean isRunning;

    AbstractListenerServer(int port){
        this.port = port;
    }
    AbstractListenerServer(int port,NioEventLoopGroup boss,NioEventLoopGroup worker){
        this.port = port;
        this.bossEventLoopGroup = boss;
        this.workerEventLoopGroup = worker;
    }
    /**
     * 启动端口监听
     * @return
     */
    @Override
    public boolean startServer(){
        serverBootstrap = new ServerBootstrap();
        if(this.port == 0){
            throw new RuntimeException("the port has not been initialized.");
        }
        if(bossEventLoopGroup == null || workerEventLoopGroup == null) {
            bossEventLoopGroup = new NioEventLoopGroup();
            workerEventLoopGroup = new NioEventLoopGroup();
        }
        try {
            serverBootstrap.group(bossEventLoopGroup,workerEventLoopGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);

            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    beforeInitChannel(socketChannel);
                    addHandle(socketChannel);
                    afterInitChannel(socketChannel);
                }
            });
            serverBootstrap.bind(this.port).sync();
            LOGGER.info("Netty server start on:"+this.port);
        } catch (InterruptedException e) {
            LOGGER.error("Start Netty server Fail.",e);
            this.stopServer();
        }
        return isRunning = true;
    }

    @Override
    public void stopServer() {
        if(bossEventLoopGroup != null) {
            bossEventLoopGroup.shutdownGracefully();
        }
        if(workerEventLoopGroup != null) {
            workerEventLoopGroup.shutdownGracefully();
        }
        this.isRunning = false;
    }

    private void beforeInitChannel(SocketChannel socketChannel){
        socketChannel.pipeline()
                .addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS))
        .addLast(new ChannelManagerHandler());

    }

    public abstract void addHandle(SocketChannel socketChannel);

    private void afterInitChannel(SocketChannel socketChannel){}

}
