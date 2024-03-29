package com.jwk.common.shardingjdbc;

import com.jwk.common.shardingjdbc.entity.User;
import com.jwk.common.shardingjdbc.service.UserService;
import java.util.Hashtable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

//@SpringBootTest(classes = ShardingJdbcApplication.class)
class ShardingJdbcTests {

	@Autowired
	UserService userService;

	@Test
	void contextLoads() {
	}

	@Test
	void addUser() {
		for (int i = 0; i < 10; i++) {
			User user = new User();
			// user.setId((long) i);
			user.setAge(i + 10);
			user.setName("用户" + i);
			userService.save(user);
		}
	}

	@Test
	void getUsers() {
		new ThreadPoolExecutor(2, 20, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000),
				Executors.defaultThreadFactory(), new AbortPolicy());
		Executors.newFixedThreadPool(10);
		new ConcurrentHashMap<>();
		new Hashtable<>();
		new CountDownLatch(-1);
	}

}
