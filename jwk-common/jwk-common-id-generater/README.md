# common-id-generater
- 提供了分布式ID生成器，默认使用redis的自增序列生成，保证全局趋势递增，由于redis的极端情况的不可靠性，无法保证完全单调递增
- 支持自定义ID生成策略


## 依赖引用
### maven
```xml
<dependency>
  <groupId>com.jwk</groupId>
  <artifactId>jwk-common-id-generater</artifactId>
  <version>${version}</version>
</dependency>
```

## 使用文档

### 1. 获取ID
示例：
#### 1.1 设置配置文件
```
jwk.redis.host=127.0.0.1
jwk.redis.port=6379
```
```java
@ConfigurationProperties(prefix = "jwk.id.gen")
public class IdGeneraterProperties {


	/**
	 * 普通槽总数[0,16384)
	 */
	public int slotCount = 16384;

	/**
	 * 普通槽位的初始化值，产生的第一个id：初始值+1
	 */
	public long slotInitialValue = 1;

	/**
	 * 批量获取id时最大size
	 */
	public int maxSize = 1000;

	/**
	 * 分布式锁前缀名
	 */
	public String lockPrefix = "IDGEN_LOCK";

	/**
	 * IDGenKey前缀
	 */
	public String keyPrefix = "IDGEN";
}

```
#### 1.2 调用方法获取ID
```java
  @Autowired
  IdGeneratorService idGeneratorService;

  @GetMapping("/test")
  public Long test(Integer key) throws IdGeneratorException, RedisException {
    return idGeneratorService.getId(key);
  }

```

### 2. 自定义ID生成方式
#### 2.1 实现对应的IdGeneratorManage，注册覆盖默认的bean
