package com.jwk.test.utils;

import com.jwk.test.service.ExcelExportService;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * 实现动态绑定接口实现类注册器
 * </p>
 */
@Service("register")
public class Register implements InitializingBean, ApplicationContextAware {

  private Logger logger = LoggerFactory.getLogger(Register.class);
  private Map<String, ExcelExportService> exportServiceImpMap = new HashMap<String, ExcelExportService>();

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  /**
   * 注册接口StorageType的所有实现的bean， 可以按照自己的规则放入 注册中心 serviceImpMap里
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    Map<String, ExcelExportService> beanMap = applicationContext
        .getBeansOfType(ExcelExportService.class);
    String name = null;
    for (ExcelExportService excelExportService : beanMap.values()) {
      name = excelExportService.getClass().getSimpleName();
      logger.info("exportService对应的服务key:{}", name);
      // 将类名，作为 key,
      exportServiceImpMap.put(name, excelExportService);
    }
  }

  /**
   * 获取实现类
   *
   * @param name 实现类名
   * @return 实现类实例
   */
  public ExcelExportService getExportService(String name) {
    final ExcelExportService[] excelExportService = {null};
    exportServiceImpMap.forEach((s, service) -> {
      if (s.contains(name)) {
        excelExportService[0] = service;
      }
    });
    return excelExportService[0];
  }
}
