package com.jwk.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration(proxyBeanMethods = false)
public class MybatisPlusConfiguration extends WebMvcConfigurerAdapter {

  // 最新版

  /**
   * <p>
   * 查询 : 根据state状态查询用户列表，分页显示
   * </p>
   *
   * @param page  分页对象,xml中可以从里面进行取值,传递参数 Page 即自动分页,必须放在第一位(你可以继承Page实现自己的分页对象)
   * @param state 状态
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
