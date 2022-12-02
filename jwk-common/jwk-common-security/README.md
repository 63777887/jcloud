# common-security
- spring authorization server 封装与增强，该框架只是基础包，需引入此包，配置uaa授权服务和资源服务
- 完善的oauth2权限认证管理，默认提供了授权码模式、客户端模式、密码模式
- 通过模版简单扩展即可实现自定义鉴权方式
- 提供``@EnableJwkResourceServer``注解简单实现资源服务，授权服务因需要一些定制项，用户需自己实现，可以参考 [jcloud-uaa](https://gitee.com/musi1996/jcloud/tree/master/jcloud-uaa) 服务
- 提供了免鉴权注解``@Inner``，支持微服务间免鉴权和完全免鉴权两种免鉴权模式
- 提供了简单的权限控制方案，只需设置 ``@PreAuthorize("@pms.hasPermission())``即可实现鉴权管理
- 提供了token的redis存储，实现多节点下token的管理同步
- 为保证服务解耦，将用户查询抽出，不依赖数据库，因此引入该框架还需 [jcloud-upms](https://gitee.com/musi1996/jcloud/tree/master/jcloud-upms) 服务，或自己实现认证逻辑，所需的实现接口在jwk-api中

## 依赖引用
### maven
```xml
<dependency>
  <groupId>com.jwk</groupId>
  <artifactId>jwk-common-security</artifactId>
  <version>${version}</version>
</dependency>
```

## 使用文档

### 1. 授权服务
- 需提供security登录页面和授权码授权页面和对应的接口，并配置AuthorizationServer配置和Security配置，详情请参考 [jcloud-uaa](https://gitee.com/musi1996/jcloud/tree/master/jcloud-uaa) 服务
- 需提供用户和授权客户端对应的查询接口，详情请参考 [jcloud-upms](https://gitee.com/musi1996/jcloud/tree/master/jcloud-upms) 服务
### 2. 资源服务
需要鉴权的服务加上``@EnableJwkResourceServer``注解即可开启权限校验服务
```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableJwkFeignClients
@EnableJwkResourceServer
@MapperScan("com.jwk.upms.web.dao")
@EnableJwkCaching
public class UpmsApplication {

  private static final Logger LOGGER = LoggerFactory.getLogger(UpmsApplication.class);

  public static void main(String[] args) {
    ConfigurableApplicationContext applicationContext = SpringApplication.run(UpmsApplication.class, args);
    ConfigurableEnvironment environment = applicationContext.getEnvironment();
    String port = environment.getProperty("server.port");
    LOGGER.info("接口聚合文档地址：{}{}{}{}", "http://127.0.0.1:", port, "/", "doc.html");
  }
}
```
### 3. 接口免鉴权
默认资源服务器所有的资源都需要用户登录权限，如不需要鉴权，提供了注解和配置文件两种方式增加免鉴权
#### 3.1 配置文件免鉴权
```yml

jwk:
  auth: 
    # 免鉴权URL
    noauth-url: /admin/**,/inner/**,/login,/oauth/**,/api,/jwk/**,/actuator/**
```
#### 3.1 免鉴权注解``@Inner``
```java
public @interface Inner {

  /**
   * false: 不需要鉴权
   * true: 携带内部标识的不需要鉴权
   * @return
   */
  boolean needFrom() default true;

}
```
```java
	/**
	 * 根据clientId查找客户端
	 */
	@GetMapping(value = "/getClientDetailsById/{clientId}")
	@Inner
	public RestResponse getClientDetailsById(@PathVariable String clientId) {

		return RestResponse.success(sysOauthClientService.getOne(
				Wrappers.<SysOauthClient>lambdaQuery().eq(SysOauthClient::getClientId, clientId), false));
	}
```
### 4. 权限管理
- 默认所有的服务都不带权限校验，即只校验用户身份，不校验用户权限
- 如需加上权限校验，只需在接口加上@PreAuthorize("@pms.hasPermission())
```java
	/**
	 * 用户列表
	 */
	@GetMapping(value = "/list")
	@PreAuthorize("@pms.hasPermission()")
	public RestResponse list() {

		return RestResponse.success(sysUserService.list());
	}
```
### 5. 认证模式扩展
实现com.jwk.common.security.support.base下的三个抽象类，即可实现扩展新的认证类型
可参考com.jwk.common.security.support.grant.password 和 [jcloud-upms](https://gitee.com/musi1996/jcloud/tree/master/jcloud-uaa) 服务下的com.jwk.uaa.grant