package com.jwk.test.netty1;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TestServerChannel extends SimpleChannelInboundHandler<Long> {

  public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

  @Override
  public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    channelGroup.add(channel);
//    channelGroup.writeAndFlush(channel.remoteAddress()+"加入聊天室");
//    channelGroup.writeAndFlush(channel.remoteAddress()+"加入聊天室");
    System.out.println("连接注册成功:" + ctx.channel().remoteAddress());
  }

  @Override
  public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    channelGroup.remove(ctx.channel());
//    channelGroup.writeAndFlush(channel.remoteAddress()+"退出聊天室");
//    channelGroup.writeAndFlush(channel.remoteAddress()+"退出聊天室");
    System.out.println("连接注销成功:" + ctx.channel().remoteAddress());
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("活跃连接:" + ctx.channel().remoteAddress());
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("不活跃连接:" + ctx.channel().remoteAddress());
  }

  private static AtomicInteger sum = new AtomicInteger(0);

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, Long msg)
      throws Exception {
//    String message = msg;
    System.out.println(msg);
    sum.getAndIncrement();
//    Channel channel = channelHandlerContext.channel();
//
//    channelGroup.forEach(channelOther -> {
//      if (channelOther.equals(channel)){
////        channelOther.writeAndFlush("我: "+message);
//        channelOther.writeAndFlush("服务器时间" + LocalDateTime.now() + " 我: " + msg);
//      }else {
////        channelOther.writeAndFlush(channel.remoteAddress()+": "+message);
//        channelOther.writeAndFlush("服务器时间" + LocalDateTime.now() + " "+channel.remoteAddress()+": " + msg);
//      }
//    });
//    channelHandlerContext.channel().writeAndFlush(1L);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    System.out.println("消息获取完毕");
    System.out.println(sum.get());
  }

  /*
  当客户端的所有ChannelHandler中4s内没有write事件，则会触发userEventTriggered方法
   */
  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    super.userEventTriggered(ctx, evt);
  }


  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    //发生了异常后关闭连接，同时从channelgroup移除
    ctx.channel().close();
    channelGroup.remove(ctx.channel());
  }

}
