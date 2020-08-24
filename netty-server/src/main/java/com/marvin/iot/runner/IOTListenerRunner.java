 package com.marvin.iot.runner;

import com.marvin.iot.config.NettyConfig;
import com.marvin.iot.netty.IOTListenerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

 /**
  * IOT 服务启动器
  *
  * @author marvinwjs
  */
 @Component
public class IOTListenerRunner implements CommandLineRunner {
    private final static Logger LOGGER = LoggerFactory.getLogger(IOTListenerRunner.class);
    @Autowired
    private NettyConfig nettyConfig;
    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("start netty server");

        IOTListenerServer server = new IOTListenerServer(nettyConfig.getPort());
        server.setBossThreadNum(nettyConfig.getBossThreadNum());
        server.setWorkerThreadNum(nettyConfig.getWorkerThreadNum());
        server.startServer();
    }
}
