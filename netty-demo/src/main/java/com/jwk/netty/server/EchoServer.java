package com.jwk.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class EchoServer {

  private final int port;

  public EchoServer(int port) {
    this.port = port;
  }

  //启动服务器应先创建一个ServerBootstrap对象，因为使用NIO，所以指定NioEventLoopGroup来接受和
  //处理新连接，指定通道类型为NioServerSocketChannel，设置InetSocketAddress让服务器监听某个端口已等待客户端连接。
  // 接下来，调用childHandler放来指定连接后调用的ChannelHandler，这个方法传ChannelInitializer类型的参数，ChannelInitializer是个抽象类，所
  //以需要实现initChannel方法，这个方法就是用来设置ChannelHandler。
  // 最后绑定服务器等待直到绑定完成，调用sync()方法会阻塞直到服务器完成绑定，然后服务器等待通道关闭，因为使用sync()，所以关闭操作也
  //会被阻塞。现在你可以关闭EventLoopGroup和释放所有资源，包括创建的线程。
  public void start() throws Exception {
    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    EventLoopGroup workGroup = new NioEventLoopGroup();
    try {
      //create ServerBootstrap instance
      ServerBootstrap b = new ServerBootstrap();
      System.out.println(
          EchoServer.class.getName() + "started and listen on " );
      //Specifies NIO transport, local socket address
      //Adds handler to channel pipeline
      b.group(bossGroup,workGroup).channel(NioServerSocketChannel.class).localAddress(port)
          .option(ChannelOption.SO_BACKLOG, 128)
          .childOption(ChannelOption.SO_KEEPALIVE, true)
          .childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
              ChannelPipeline pipeline = ch.pipeline();
              //向pipeline加入解码器
              pipeline.addLast("decoder", new StringDecoder());
              //向pipeline加入编码器
              pipeline.addLast("encoder", new StringEncoder());
              pipeline.addLast(new EchoServerHandler());
            }
          });
      //Binds server, waits for server to close, and releases resources
      ChannelFuture f = b.bind().sync();
      System.out.println(
          EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
      f.channel().closeFuture().sync();
    } finally {
      bossGroup.shutdownGracefully();
      workGroup.shutdownGracefully();
    }
  }

  public static void main(String[] args) throws Exception {
    new EchoServer(65535).start();
  }
}
