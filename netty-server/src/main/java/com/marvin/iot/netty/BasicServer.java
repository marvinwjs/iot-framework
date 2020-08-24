package com.marvin.iot.netty;

/**
 *
 * @author marvinwjs
 */
public abstract class BasicServer {
    protected boolean isRunning;

    /**
     * server is running
     *
     * @return
     */
    public boolean isRunning(){
        return this.isRunning;
    }
}
