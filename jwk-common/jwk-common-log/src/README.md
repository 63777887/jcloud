# common-log
- log日志封装，增加日志监听事件

## 依赖引用
### maven
```xml
<dependency>
  <groupId>com.jwk</groupId>
  <artifactId>jwk-common-log</artifactId>
  <version>${version}</version>
</dependency>
```

## 使用文档

#### 登陆成功事件


```java
import com.jwk.common.log.utils.SysLogUtils;

SysLogUtils.pushLoginSuccessLog(userId,clientId);
```
在LogTypeE增加日志事件类型

