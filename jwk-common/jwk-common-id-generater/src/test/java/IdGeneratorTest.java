import com.jwk.common.idgenerater.IdGeneraterAutoConfiguration;
import com.jwk.common.idgenerater.service.impl.IdGeneratorServiceImpl;
import com.jwk.common.redis.JwkRedisAutoConfiguration;
import com.jwk.common.redis.annotation.JwkRateLimiter;
import com.jwk.common.redis.annotation.JwkRedisLock;

import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * Mock测试
 * @date 2022/11/3
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest(
		classes = { IdGeneraterAutoConfiguration.class, JwkRedisAutoConfiguration.class, RedisAutoConfiguration.class })
public class IdGeneratorTest {

	@Resource
	IdGeneratorServiceImpl idGeneratorService;

	@Test
	@JwkRedisLock
	@JwkRateLimiter(value = "test")
	void testRedisTemplate() throws Throwable {
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 1; i++) {
			idGeneratorService.getId(1);
		}
		long endTime = System.currentTimeMillis();
		System.out.println(endTime - startTime);
	}

	// lock： 20785 100000
	// unlock： 3426 100000

}
