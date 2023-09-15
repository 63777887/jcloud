# common-zookeeper
- 对zk的简单引用


## 依赖引用
### maven
```xml
<dependency>
  <groupId>com.jwk</groupId>
  <artifactId>jwk-common-zookeeper</artifactId>
  <version>${version}</version>
</dependency>
```

## 使用文档

#### 配置文件
```java
@Data
@ConfigurationProperties(prefix = "jwk.zookeeper")
public class JwkZookeeperProperties {

  private boolean enabled;

  /**
   * 优先级: address > addressEnv
   */
  private String address = System.getenv("ZOOKEEPER_URL");

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
#### 设置配置文件
```yaml
jwk:
  zookeeper:
    enabled: true
    address: 127.0.0.1:2181
```
