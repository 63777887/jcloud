package com.jwk.test.netty1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;

//@Component
@Slf4j
public class TestClient {

	private String ip = "127.0.0.1";

	private Integer port = 9999;

	public TestClient(String ip, Integer port) {
		this.ip = ip;
		this.port = port;
	}

	// @PostConstruct
	public void run() {
		NioEventLoopGroup workgroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		try {
			bootstrap.group(workgroup).channel(NioSocketChannel.class).handler(new TestClientChannelInitializer());

			ChannelFuture bind = bootstrap.connect(ip, port).sync();
			Channel channel = bind.channel();

			for (int i = 0; i < 100; i++) {
				channel.writeAndFlush(i);
			}
			// Scanner scanner = new Scanner(System.in);
			// while (scanner.hasNextLine()) {
			// String msg = scanner.nextLine();
			// channel.writeAndFlush(msg);
			// }
			// bind.channel().closeFuture().sync();
		}
		catch (InterruptedException e) {
			log.error("连接失败");
			e.printStackTrace();
		}
		finally {
			workgroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) {

		ExecutorService executorService = Executors.newFixedThreadPool(30);
		for (int i = 0; i < 10000; i++) {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					new TestClient("127.0.0.1", 9999).run();
				}
			});

		}
	}

}
