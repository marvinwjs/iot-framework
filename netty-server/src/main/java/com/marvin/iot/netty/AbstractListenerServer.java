package com.marvin.iot.netty;

import com.marvin.iot.netty.handler.ChannelManagerHandler;
import com.marvin.iot.thread.ThreadNames;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author marvinwjs
 */
public abstract class AbstractListenerServer extends BasicServer implements IServer{
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractListenerServer.class);

    private ServerBootstrap serverBootstrap;
    private NioEventLoopGroup bossEventLoopGroup;
    private NioEventLoopGroup workerEventLoopGroup;
    private int bossThreadNum;
    private int workerThreadNum;

    private int port;

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
        //has start
        if(this.isRunning){
            return true;
        }

        serverBootstrap = new ServerBootstrap();
        if(this.port == 0){
            throw new RuntimeException("the port has not been initialized.");
        }
        if(bossEventLoopGroup == null || workerEventLoopGroup == null) {
            bossEventLoopGroup = new NioEventLoopGroup(bossThreadNum,defaultThreadFactory(ThreadNames.T_BOSS));
            workerEventLoopGroup = new NioEventLoopGroup(workerThreadNum,defaultThreadFactory(ThreadNames.T_WORKER));
        }
        try {
            serverBootstrap.group(bossEventLoopGroup,workerEventLoopGroup);
            serverBootstrap.channelFactory(getChannelFactory());
            this.initOptions(serverBootstrap);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    initPipeline(socketChannel.pipeline());
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind(this.port).sync();
            LOGGER.info("Netty server start on:"+channelFuture.channel().localAddress());
            //监听通道关闭事件 应用会一直等待知道channel关闭
            ChannelFuture closeFuture =
                    channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (InterruptedException e) {
            LOGGER.error("Start Netty server Fail.",e);
            this.stopServer();
        } finally{
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

    protected void initPipeline(ChannelPipeline pipeline){
        pipeline.addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS))
                .addLast(new ChannelManagerHandler());
    }

    /**
     * option()是提供给NioServerSocketChannel用来接收进来的连接。
     * childOption()是提供给由父管道ServerChannel接收到的连接，
     * @param b
     */
    protected void initOptions(ServerBootstrap b) {
        /**
         * Netty默认不使用内存池，需要在创建客户端或者服务端的时候进行指定
         */
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        b.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
    }

    protected void beforeInitChannel(SocketChannel socketChannel){
        socketChannel.pipeline()
                .addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS))
        .addLast(new ChannelManagerHandler());

    }

    public abstract void addHandle(SocketChannel socketChannel);

    private void afterInitChannel(SocketChannel socketChannel){}

    /**
     * 获取channel
     * @return
     */
    private ChannelFactory getChannelFactory(){
        return NioServerSocketChannel::new;
    }

    public void setBossThreadNum(int bossThreadNum){
        this.bossThreadNum = bossThreadNum;
    }
    public void setWorkerThreadNum(int workerThreadNum){
        this.workerThreadNum = workerThreadNum;
    }

    /**
     * 默认线程factory
     * @param prefix
     * @return
     */
    protected ThreadFactory defaultThreadFactory(String prefix){
        return new DefaultThreadFactory(prefix);
    }
}
