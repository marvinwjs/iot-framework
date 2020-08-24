package com.marvin.iot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * netty config
 *
 * @author marvinwjs
 */
@Configuration
@ConfigurationProperties(prefix = "application.netty")
public class NettyConfig {
    private int port;
    private int bossThreadNum;
    private int workerThreadNum;

    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }

    public int getBossThreadNum() {
        return bossThreadNum;
    }

    public void setBossThreadNum(int bossThreadNum) {
        this.bossThreadNum = bossThreadNum;
    }

    public int getWorkerThreadNum() {
        return workerThreadNum;
    }

    public void setWorkerThreadNum(int workerThreadNum) {
        this.workerThreadNum = workerThreadNum;
    }
}
