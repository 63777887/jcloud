package com.jwk.test.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestServer {

	private final Integer port = 7000;

	// public TestServer(Integer port) {
	// this.port=port;
	// }

	@PostConstruct
	public void run() {
		NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
		NioEventLoopGroup workGroup = new NioEventLoopGroup();

		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
				// 链接数量
				.option(ChannelOption.SO_BACKLOG, 128)
				// 保持长链接
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
				.childHandler(new TestServerChannelInitializer());

		ChannelFuture bind = serverBootstrap.bind(port);
		if (bind.isSuccess()) {
			log.info("启动 Netty 成功");
		}

		// 监听关闭
		// channelFuture.channel().closeFuture().sync();

	}

	// public static void main(String[] args) {
	// new TestServer(9999).run();
	//// new TestServer(8888).run();
	// }

}
