package com.jwk.test.netty1;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class TestServerChannelInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		ChannelPipeline pipeline = channel.pipeline();
		// pipeline.addLast(new LoggingHandler());
		// //向pipeline加入解码器
		// pipeline.addLast("decoder", new StringDecoder());
		// //向pipeline加入编码器
		// pipeline.addLast("encoder", new StringEncoder());

		// //websocket 基于http协议，所需要的http 编解码器
		// pipeline.addLast(new HttpServerCodec());
		// //在http上有一些数据流产生，有大有小，我们对其进行处理，既然如此，我们需要使用netty 对下数据流写
		// 提供支持，这个类叫：ChunkedWriteHandler
		// pipeline.addLast(new ChunkedWriteHandler());
		// //对httpMessage 进行聚合处理，聚合成request或 response
		// pipeline.addLast(new HttpObjectAggregator(1024*64));

		// //添加解压缩Handler
		// pipeline.addLast("decompressor", new HttpContentDecompressor());
		// //添加解压缩Handler
		// pipeline.addLast("decompressor", new HttpContentDecompressor());

		/**
		 * 本handler 会帮你处理一些繁重复杂的事情 会帮你处理握手动作：handshaking（close、ping、pong） ping+pong = 心跳
		 * 对于websocket 来讲，都是以frams 进行传输的，不同的数据类型对应的frams 也不同
		 */
		// pipeline.addLast(new WebSocketServerProtocolHandler("/hello2"));

		// 加入一个出站的handler 对数据进行一个编码
		pipeline.addLast(new MyLongToByteEncoder());

		// 这时一个入站的解码器(入站handler )
		// pipeline.addLast(new MyByteToLongDecoder());
		pipeline.addLast(new MyByteToLongDecoder2());
		pipeline.addLast(new TestServerChannel());
	}

}
