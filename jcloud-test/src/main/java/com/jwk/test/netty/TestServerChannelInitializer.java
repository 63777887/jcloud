package com.jwk.test.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class TestServerChannelInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		ChannelPipeline pipeline = channel.pipeline();
		// pipeline.addLast(new LoggingHandler());
		// //向pipeline加入解码器
		// pipeline.addLast("decoder", new StringDecoder());
		// //向pipeline加入编码器
		// pipeline.addLast("encoder", new StringEncoder());

		// websocket 基于http协议，所需要的http 编解码器
		pipeline.addLast(new HttpServerCodec());
		// 在http上有一些数据流产生，有大有小，我们对其进行处理，既然如此，我们需要使用netty 对下数据流写
		// 提供支持，这个类叫：ChunkedWriteHandler
		pipeline.addLast(new ChunkedWriteHandler());
		// 对httpMessage 进行聚合处理，聚合成request或 response
		pipeline.addLast(new HttpObjectAggregator(1024 * 64));

		// //添加解压缩Handler
		// pipeline.addLast("decompressor", new HttpContentDecompressor());
		// //添加解压缩Handler
		// pipeline.addLast("decompressor", new HttpContentDecompressor());

		/**
		 * 本handler 会帮你处理一些繁重复杂的事情 会帮你处理握手动作：handshaking（close、ping、pong） ping+pong = 心跳
		 * 对于websocket 来讲，都是以frams 进行传输的，不同的数据类型对应的frams 也不同
		 */
		pipeline.addLast(new WebSocketServerProtocolHandler("/hello2"));
		pipeline.addLast(new TestServerChannel());
	}

}
