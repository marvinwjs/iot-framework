package com.marvin.iot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author marvinwjs
 */
@Configuration
@ConfigurationProperties(prefix = "application.netty")
public class NettyConfig {
    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}