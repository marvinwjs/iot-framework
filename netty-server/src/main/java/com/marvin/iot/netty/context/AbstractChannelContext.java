package com.marvin.iot.netty.context;

import com.marvin.iot.channel.ChannelInfo;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author marvinwjs
 */
public abstract class AbstractChannelContext {
    private static ConcurrentHashMap<String,ChannelInfo> channelMap = new ConcurrentHashMap(16);

    public static ChannelInfo getChannelInfo(String iot){
        return channelMap.get(iot);
    }

    public static ChannelInfo addChannelInfo(ChannelInfo channelInfo){
        return channelMap.put(channelInfo.getIot(),channelInfo);
    }

    public static ChannelInfo rmChannelInfo(String iot){
        return channelMap.remove(iot);
    }
}
