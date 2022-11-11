# common-cloud
- 提供了`Spring Cloud Alibaba`的 Nacos、 Sentinel依赖，对服务间调用异常和限流异常做了统一封装


## 依赖引用
### maven
```xml
<dependency>
  <groupId>com.jwk</groupId>
  <artifactId>jwk-common-cloud</artifactId>
  <version>${version}</version>
</dependency>
```

## 使用文档

### 示例：
####  需要开启新的Feign注解
```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableJwkFeignClients
public class TestApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}

}
```