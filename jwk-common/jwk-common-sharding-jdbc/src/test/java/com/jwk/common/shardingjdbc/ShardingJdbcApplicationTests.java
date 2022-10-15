package com.jwk.common.shardingjdbc;

import com.jwk.common.shardingjdbc.entity.User;
import com.jwk.common.shardingjdbc.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ShardingJdbcApplication.class)
class ShardingJdbcApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	UserService userService;

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
		userService.list().forEach(System.out::println);
		// System.out.println(userService.lambdaQuery().eq(User::getId,
		// 1442767333306155009L).list());
		// System.out.println(userService.lambdaQuery().between(User::getId,1442773507774656513L,
		// 1442773511704719361L).list());
		// System.out.println(userService.lambdaQuery().between(User::getId,1442773507774656513L,
		// 1442773511704719361L).eq(User::getAge,19L).list());
	}

}
