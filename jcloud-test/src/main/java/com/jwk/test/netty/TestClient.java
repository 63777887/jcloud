package com.jwk.test.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.Scanner;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
public class TestClient {

	private String ip = "127.0.0.1";

	private Integer port = 9999;

	// public TestClient(String ip, Integer port) {
	// this.ip = ip;
	// this.port = port;
	// }

	// @PostConstruct
	public void run() {
		NioEventLoopGroup workgroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		try {
			bootstrap.group(workgroup).channel(NioSocketChannel.class).handler(new TestClientChannelInitializer());

			ChannelFuture bind = bootstrap.connect(ip, port).sync();
			Channel channel = bind.channel();
			// Scanner scanner = new Scanner(System.in);
			// while (scanner.hasNextLine()) {
			// String msg = scanner.nextLine();
			// channel.writeAndFlush(msg);
			// }
		}
		catch (InterruptedException e) {
			log.error("连接失败");
			e.printStackTrace();
		}
		finally {
			workgroup.shutdownGracefully();
		}
	}

	// public static void main(String[] args) {
	// new TestClient("127.0.0.1", 9999).run();
	// }

}
