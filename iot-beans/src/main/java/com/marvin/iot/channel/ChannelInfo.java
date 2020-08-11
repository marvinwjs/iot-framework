package com.marvin.iot.channel;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;

/**
 * @author marvinwjs
 */
@Setter
@Getter
@ToString
public class ChannelInfo {
    private Channel channel;
    private String iot;
    private Date updateTime;

    public ChannelInfo(String iot,Channel channel){
       new ChannelInfo(iot,channel,new Date());
    }

    public ChannelInfo(String iot,Channel channel,Date updateTime){
        this.channel = channel;
        this.iot = iot;
        this.updateTime = updateTime;
    }

    public static ChannelInfo build(String iot,Channel channel){
        return new ChannelInfo(iot,channel);
    }
}
