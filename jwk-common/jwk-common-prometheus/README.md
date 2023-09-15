# common-prometheus
- Spring cloud 对接 Prometheus ，默认支持 http，提供了openfeign的支持（详情参照jwk-common-cloud），支持原生的端点（actuator）配置。默认开放了所有的端点
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
@Data
@ConfigurationProperties(prefix = "jwk.prometheus")
public class JwkPrometheusProperties {

  protected final static Logger logger = LoggerFactory.getLogger(JwkPrometheusProperties.class);

  private boolean enabled;

  /**
   * 服务名
   */
  private String application;

  /**
   * 服务名
   */
  private String namespace = "jcloud";

  /**
   * 注册类型
   */
  private String registryMode = JwkPrometheusConstants.DEFAULT_REGISTER_MODE;
}
```
示例：
#### 1.1 设置配置文件
```yaml
jwk:
  prometheus:
    enabled: true
    namespace: jcloud
  zookeeper:
    address: 127.0.0.1:2181
```
http访问对应的项目的/jcloud/prometheus路径( jcloud为namespace，默认为jcloud )

### 2. 自定义节点注册
#### 2.1 实现对应的RegistryService，注册覆盖默认的bean
#### 2.2 配置文件注册方式改为新加的mode支持
```yaml
jwk:
  prometheus:
    registry-mode: xxx
```
