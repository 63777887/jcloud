package com.jwk.common.mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
public class MybatisPlusConfiguration implements WebMvcConfigurer {

  // 最新版

  /**
   * <p>
   * 查询 : 根据state状态查询用户列表，分页显示
   * </p>
   *
   * @return 分页对象
   */
//  IPage<User> selectPageVo(Page<?> page, Integer state);

  // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
  // paginationInterceptor.setOverflow(false);
  // 设置最大单页限制数量，默认 500 条，-1 不受限制
  // paginationInterceptor.setLimit(500);
  // 开启 count 的 join 优化,只针对部分 left join
  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    return interceptor;
  }


}
