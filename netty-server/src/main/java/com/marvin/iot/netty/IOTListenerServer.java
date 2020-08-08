package com.marvin.iot.netty;

import io.netty.channel.socket.SocketChannel;


/**
 * 监听服务
 *
 * @author marvinwjs
 */
public class IOTListenerServer extends AbstractListenerServer {
    public IOTListenerServer(int port){
        super(port);
    }


    @Override
    public void addHandle(SocketChannel socketChannel) {

    }
}
