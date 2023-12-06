# 变更记录

### v0.1.6 - 2023-11-16
- :sparkles: [jwk-common-redis](jwk-common/jwk-common-redis) 新增注解和方法形式的分布式锁组件，简化加锁逻辑
- :sparkles: [jcloud-uaa](jcloud-uaa) 增加前端所需的登陆，用户，权限，菜单等模块接口

### v0.1.5 - 2023-9-15
- :sparkles: 模块拆分，独立出zk模块
- :sparkles: 普罗米修斯增加了开关控制
- :recycle: 普罗米修斯监控feign接口的功能转移到了cloud模块
  
### v0.1.4 - 2022-12-2
- :sparkles: [jwk-common-security](jwk-common/jwk-common-security) 由oauth2改成spring authorization server授权模式
- :sparkles: [jcloud-uaa](jcloud-uaa)  扩展了密码授权模式，手机号授权模式和邮箱授权模式
- :memo: 完善 [jwk-common-security](jwk-common/jwk-common-security) 使用文档。
- :card_file_box: 更新数据库表结构和数据


### v0.1.3 - 2022-11-10
- :sparkles: [jwk-common-redis](jwk-common/jwk-common-redis) 增加redis接口限流，redis key过期监听
- :sparkles: [jwk-common-prometheus](jwk-common/jwk-common-prometheus) 增加对http请求的监控
- :bug: 新增cacheServerId标识同步本地缓存时，不去删除同步发起者的缓存
- :zap: log输出的判断
- :memo: 完善 [jwk-common](jwk-common) 使用文档。
