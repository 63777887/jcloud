package com.jwk.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {

  private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @Override
  public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    channelGroup.add(ctx.channel());
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    System.out.println(msg);
    //获取到当前channel
    Channel channel = ctx.channel();
    //这时我们遍历channelGroup, 根据不同的情况，回送不同的消息

    channelGroup.forEach(ch -> {
      if (channel != ch) { //不是当前的channel,转发消息
        ch.writeAndFlush("[客户]" + channel.remoteAddress() + " 发送了消息" + msg + "\n");
      } else {//回显自己发送的消息给自己
        ch.writeAndFlush("[自己]发送了消息" + msg + "\n");
      }
    });
  }

//  @Override
//  protected void channelRead0(ChannelHandlerContext ctx, String msg)
//      throws Exception {
//    System.out.println(msg);
//    //获取到当前channel
//    Channel channel = ctx.channel();
//    //这时我们遍历channelGroup, 根据不同的情况，回送不同的消息
//
//    channelGroup.forEach(ch -> {
//      if(channel != ch) { //不是当前的channel,转发消息
//        ch.writeAndFlush("[客户]" + channel.remoteAddress() + " 发送了消息" + msg + "\n");
//      }else {//回显自己发送的消息给自己
//        ch.writeAndFlush("[自己]发送了消息" + msg + "\n");
//      }
//    });
//  }
//
//  @Override
//  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
//  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }

}
