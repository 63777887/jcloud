import com.jwk.common.Idgenerater.IdGeneraterAutoConfiguration;
import com.jwk.common.Idgenerater.exception.IdGeneratorException;
import com.jwk.common.Idgenerater.service.impl.IdGeneratorServiceImpl;
import com.jwk.common.redis.JwkRedisAutoConfiguration;
import com.jwk.common.redis.exception.RedisException;
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
@SpringBootTest(classes = {IdGeneraterAutoConfiguration.class, JwkRedisAutoConfiguration.class,
    RedisAutoConfiguration.class},
    properties = "")
public class IdGeneratorTest {

  @Resource
  IdGeneratorServiceImpl idGeneratorService;

  @Test
  void testRedisTemplate() throws IdGeneratorException, RedisException {
//    idGeneratorService.setIdGeneratorManage(idGeneratorManage);
    idGeneratorService.getId(1);
  }

}
