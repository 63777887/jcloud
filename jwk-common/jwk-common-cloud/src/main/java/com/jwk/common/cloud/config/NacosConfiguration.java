package com.jwk.common.cloud.config;

import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import java.lang.management.ManagementFactory;
import java.util.Set;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NacosConfiguration implements ApplicationRunner {

  private final static Logger log = LoggerFactory.getLogger(NacosConfiguration.class);

  private final NacosAutoServiceRegistration registration;

  @Value("${server.port}")
  Integer port;

  public NacosConfiguration(NacosAutoServiceRegistration registration) {
    this.registration = registration;
  }

  @Override
  public void run(ApplicationArguments args) {
    if (registration != null && port != null) {
      int tomcatPort = port;
      try {
        tomcatPort = new Integer(getTomcatPort());
      } catch (Exception e) {
        log.warn("获取tomcat外部端口失败");
      }
      log.info("Tomcat port:" + tomcatPort);
      registration.setPort(tomcatPort);
      registration.start();
    }
  }

  /**
   * 获取外部tomcat端口
   */
  public String getTomcatPort() throws Exception {
    Set<ObjectName> objectNames;
    MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
    objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
        Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
    if (!objectNames.iterator().hasNext()) {
      objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"), Query
          .match(Query.attr("protocol"),
              Query.value("org.apache.coyote.http11.Http11AprProtocol")));
    }
    return objectNames.iterator().next().getKeyProperty("port");
  }
}
