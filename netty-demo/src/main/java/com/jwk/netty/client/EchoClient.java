package com.jwk.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class EchoClient {

  private final String host;
  private final int port;

  public EchoClient(String host, int port) {
    this.host = host;
    this.port = port;
  }

  //创建启动一个客户端包含下面几步：
  //创建Bootstrap对象用来引导启动客户端
  //创建EventLoopGroup对象并设置到Bootstrap中，EventLoopGroup可以理解为是一个线程池，这个线程池用来处理连接、接受数据、发送数据
  //创建InetSocketAddress并设置到Bootstrap中，InetSocketAddress是指定连接的服务器地址
  //添加一个ChannelHandler，客户端成功连接服务器后就会被执行
  //调用Bootstrap.connect()来连接服务器
  //最后关闭EventLoopGroup来释放资源
  public void start() throws Exception {
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap();
      b.group(group).channel(NioSocketChannel.class)
          .remoteAddress(new InetSocketAddress(host, port))
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
              //得到pipeline
              ChannelPipeline pipeline = ch.pipeline();
              //加入相关handler
              pipeline.addLast("decoder", new StringDecoder());
              pipeline.addLast("encoder", new StringEncoder());
              pipeline.addLast(new EchoClientHandler());
            }
          });
      ChannelFuture f = b.connect().sync();
      Channel channel = f.channel();
      Scanner scanner = new Scanner(System.in);
      while (scanner.hasNextLine()) {
        String msg = scanner.nextLine();
        //通过channel 发送到服务器端
        channel.writeAndFlush(msg + "\r\n");
      }
//      f.channel().closeFuture().sync();
    } finally {
      group.shutdownGracefully();
    }
  }

  public static void main(String[] args) throws Exception {
    new EchoClient("localhost", 65535).start();
  }

}
