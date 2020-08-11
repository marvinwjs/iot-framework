package com.marvin.iot.netty.handler;

import com.marvin.iot.channel.ChannelInfo;
import com.marvin.iot.netty.context.ChannelContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * channel入站管理器
 * @author marvinwjs
 */
public class ChannelManagerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelManagerHandler.class);
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("channel registered! ==>{}", ctx.channel());
        ChannelId channelId = ctx.channel().id();
        ChannelContext.addChannelInfo(ChannelInfo.build(channelId.toString(),ctx.channel()));
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("channel unregistered! ==>{}", ctx.channel());
        ChannelId channelId = ctx.channel().id();
        LOGGER.info("remove channel: {}", channelId);
        ctx.fireChannelUnregistered();
    }
}
