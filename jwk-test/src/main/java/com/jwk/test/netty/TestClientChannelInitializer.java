package com.jwk.test.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class TestClientChannelInitializer extends ChannelInitializer<SocketChannel> {


  @Override
  protected void initChannel(SocketChannel channel) throws Exception {
    ChannelPipeline pipeline = channel.pipeline();
//    pipeline.addLast(new LoggingHandler());
    //向pipeline加入解码器
    pipeline.addLast("decoder", new StringDecoder());
    //向pipeline加入编码器
    pipeline.addLast("encoder", new StringEncoder());
    //添加解压缩Handler
    pipeline.addLast("decompressor", new HttpContentDecompressor());
    pipeline.addLast(new TestClientChannel());
  }
}
