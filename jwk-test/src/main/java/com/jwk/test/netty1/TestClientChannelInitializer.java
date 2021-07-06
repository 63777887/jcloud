package com.jwk.test.netty1;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class TestClientChannelInitializer extends ChannelInitializer<SocketChannel> {


  @Override
  protected void initChannel(SocketChannel channel) throws Exception {
    ChannelPipeline pipeline = channel.pipeline();
//    pipeline.addLast(new LoggingHandler());
//    //向pipeline加入解码器
//    pipeline.addLast("decoder", new StringDecoder());
//    //向pipeline加入编码器
//    pipeline.addLast("encoder", new StringEncoder());
    //添加解压缩Handler
//    pipeline.addLast("decompressor", new HttpContentDecompressor());

    //加入一个出站的handler 对数据进行一个编码
    pipeline.addLast(new MyLongToByteEncoder());

    //这时一个入站的解码器(入站handler )
    //pipeline.addLast(new MyByteToLongDecoder());
    pipeline.addLast(new MyByteToLongDecoder2());
    pipeline.addLast(new TestClientChannel());
  }
}
