<div align="center">

[![JCloud](https://img.shields.io/badge/JCloud-0.1.5-success.svg)]()
[![Spring Cloud Alibaba](https://img.shields.io/badge/Spring%20Cloud-2021-blue.svg)](https://github.com/alibaba/spring-cloud-alibaba)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7-blue.svg)](https://github.com/spring-projects/spring-boot)
![LICENSE](https://img.shields.io/github/license/63777887/jcloud)

</div>

## 系统说明

- 基于 Spring Cloud 、Spring Boot、 Spring Authorization Server 的 RBAC **权限管理系统**
- 提供常见中间件的处理方案，如Redis，MQ，Prometheus等
- 提供动态的前后端权限控制，后端精确到接口，前端精确到按钮
- 提供如多级缓存，全局ID生成，服务监控等常见问题方案
- 提供对常见容器化支持 Docker、Kubernetes 支持

## 分支导航

### github
jcloud: https://github.com/63777887/jcloud   
jcloud-ui: https://github.com/63777887/jcloud-ui


### gitee
jcloud: https://gitee.com/musi1996/jcloud   
jcloud-ui: https://gitee.com/musi1996/jcloud-ui   

## 核心依赖

| 依赖                   | 版本         |
| ---------------------- |------------|
| Spring Boot            | 2.7.0      |
| Spring Cloud           | 2021.0.4   |
| Spring Cloud Alibaba   | 2021.0.4.0 |
| Spring Authorization Server | 0.3.1      |
| Mybatis Plus           | 3.4.1      |
| hutool                 | 5.8.9      |


## 模块结构
```lua
jCloud
├── jwk-uaa -- 授权服务提供
├── jwk-upms -- 用户管理
├── jcloud-upms-base -- 公共实体
└── jwk-common -- 系统公共模块
     ├── jwk-common-bom -- 版本管理
     ├── jwk-common-canal -- Canal组件
     ├── jwk-common-canal-rocketmq -- RocketMQ同步Canal
     ├── jwk-common-cloud -- Spring Cloud Alibaba
     ├── jwk-common-core -- 公共工具类核心包
     ├── jwk-common-log -- 日志插件
     ├── jwk-common-dynamic-datasource -- 动态数据源
     ├── jwk-common-id-generater -- ID生成方案
     ├── jwk-common-knife4j -- 接口文档
     ├── jwk-common-mybatis -- Mybatis Plus数据源包
     ├── jwk-common-prometheus -- 普罗米修斯监控
     ├── jwk-common-zookeeper -- zk插件
     ├── jwk-common-rabbitmq -- RabbitMQ整合
     ├── jwk-common-redis -- redis和多级缓存
     ├── jwk-common-rocketmq -- RocektMQ整合
     ├── jwk-common-seata -- 分布式事务
     ├── jwk-common-security -- Security和Oauth基础
     └── jwk-common-sharding-jdbc -- 分库分表
├── jwk-knife4j -- 接口文档管理
├── jwk-admin -- Admin监控
└── jwk-gateway -- Spring Cloud Gateway网关
```
## 使用方式

请查看每个模块的使用说明

## 界面

### 登录界面
![jcloud-login-page.png](docs%2Fimages%2Freadme%2Fjcloud-login-page.png)

### 登录验证
![jcloud-verify.png](docs%2Fimages%2Freadme%2Fjcloud-verify.png)

### 首页
![jcloud-home.png](docs%2Fimages%2Freadme%2Fjcloud-home.png)
![jcloud-user-setting.png](docs%2Fimages%2Freadme%2Fjcloud-user-setting.png)
![jcloud-theme.png](docs%2Fimages%2Freadme%2Fjcloud-theme.png)
![jcloud-theme-change.png](docs%2Fimages%2Freadme%2Fjcloud-theme-change.png)
![jcloud-theme-dark.png](docs%2Fimages%2Freadme%2Fjcloud-theme-dark.png)
![jcloud-theme-grey.png](docs%2Fimages%2Freadme%2Fjcloud-theme-grey.png)

### 用户管理
![jcloud-user.png](docs%2Fimages%2Freadme%2Fjcloud-user.png)
![jcloud-user-import.png](docs%2Fimages%2Freadme%2Fjcloud-user-import.png)
![jcloud-user-export.png](docs%2Fimages%2Freadme%2Fjcloud-user-export.png)

### 权限管理
![jcloud-role.png](docs%2Fimages%2Freadme%2Fjcloud-role.png)
![jcloud-role-setting.png](docs%2Fimages%2Freadme%2Fjcloud-role-setting.png)

### 菜单管理
![jcloud-menu.png](docs%2Fimages%2Freadme%2Fjcloud-menu.png)
![jcloud-menu-add.png](docs%2Fimages%2Freadme%2Fjcloud-menu-add.png)

### 系统设置
![jcloud-system-redisMonitor.png](docs%2Fimages%2Freadme%2Fjcloud-system-redisMonitor.png)