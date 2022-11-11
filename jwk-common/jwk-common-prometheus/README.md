# common-prometheus
- Spring cloud 对接 Prometheus ，支持 openfeign 和 http，支持原生的端点（actuator）配置。默认开放了所有的端点
- 支持多种注册方式，默认提供zookeeper的节点注册方式


## 依赖引用
### maven
```xml
<dependency>
  <groupId>com.jwk</groupId>
  <artifactId>jwk-common-prometheus</artifactId>
  <version>${version}</version>
</dependency>
```

## 使用文档

### 1. 使用Prometheus监控
#### 配置文件
```java
@ConfigurationProperties(prefix = "jwk.prometheus")
public class JwkPrometheusProperties {

	protected final static Logger logger = LoggerFactory.getLogger(JwkPrometheusProperties.class);

	/**
	 * 服务名
	 */
	private String application;

	/**
	 * 注册类型
	 */
	private String registryMode = JwkPrometheusConstants.DEFAULT_REGISTER_MODE;

	@NestedConfigurationProperty
	private ZookeeperProperties zookeeper;

}


public class ZookeeperProperties {

  /**
   * 优先级: address > addressEnv
   */
  private String address = System.getenv("ZOOKEEPER_URL");

  /**
   * 服务注册namespace
   */
  private String namespace = System.getenv("PROMETHEUS_SERVICE_NAMESPACE");

  /**
   * session超时时间
   */
  private int sessionTimeout = 100 * 1000;

  /**
   * 连接超时时间
   */
  private int connectionTimeout = 50 * 1000;

  /**
   * 重试间隔
   */
  private int retryIntervalMs = 3 * 1000;

  /**
   * 重试次数
   */
  private int retryNumber = 5;

}

```
示例：
#### 1.1 设置配置文件
```yaml
jwk:
  prometheus:
    zookeeper:
      address: 127.0.0.1:2181
      namespace: /jwk/prometheus
    application: ${spring.application.name}
    registry-mode: zookeeper
```
http访问对应的项目的/jwk/prometheus路径

### 2. 自定义节点注册
#### 2.1 实现对应的RegistryService，注册覆盖默认的bean
#### 2.2 配置文件注册方式改为新加的mode支持
```yaml
jwk:
  prometheus:
    registry-mode: xxx
```