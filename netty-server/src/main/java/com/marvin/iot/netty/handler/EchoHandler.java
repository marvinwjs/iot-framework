package com.marvin.iot.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelInboundHandlerAdapter.class);
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("new channel coming! ----> {}", ctx.channel());
        ChannelId channelId = ctx.channel().id();
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("channel out! ----> {}", ctx.channel());
        ChannelId channelId = ctx.channel().id();
        LOGGER.info("remove channel: {}", channelId);
        ctx.fireChannelUnregistered();
    }
}
