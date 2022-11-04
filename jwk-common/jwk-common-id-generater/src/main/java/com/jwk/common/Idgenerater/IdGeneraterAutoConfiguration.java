package com.jwk.common.Idgenerater;

import com.jwk.common.Idgenerater.manager.IdGeneratorManage;
import com.jwk.common.Idgenerater.manager.impl.RedisGeneratorManage;
import com.jwk.common.Idgenerater.service.IdGeneratorService;
import com.jwk.common.Idgenerater.service.impl.IdGeneratorServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jiwk
 * @version 0.1.3
 * <p>
 * 自动注入
 * @date 2022/11/2
 */
@Configuration
public class IdGeneraterAutoConfiguration {

  @Bean
  public IdGeneratorService idGeneratorService(IdGeneratorManage idGeneratorManage){
    IdGeneratorServiceImpl idGeneratorService = new IdGeneratorServiceImpl();
    return idGeneratorService;
  }

  @Bean
  @ConditionalOnMissingBean
  public IdGeneratorManage idGeneratorManage(){
    RedisGeneratorManage manage = new RedisGeneratorManage();
    return manage;
  }

}
