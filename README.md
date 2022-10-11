<div align="center">

[![JCloud](https://img.shields.io/badge/JCloud-0.1.1-success.svg)]()
[![Spring Cloud Alibaba](https://img.shields.io/badge/Spring%20Cloud-2021-blue.svg)](https://github.com/alibaba/spring-cloud-alibaba)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7-blue.svg)](https://github.com/spring-projects/spring-boot)
![LICENSE](https://img.shields.io/github/license/63777887/jcloud)

</div>

## 系统说明

- 基于 Spring Cloud 2021 、Spring Boot 2.7、 OAuth2 的 RBAC **权限管理系统**
- 常见中间件的处理方案
- 提供对常见容器化支持 Docker、Kubernetes 支持


### 核心依赖

| 依赖                   | 版本         |
| ---------------------- |------------|
| Spring Boot            | 2.7.0      |
| Spring Cloud           | 2021.0.2   |
| Spring Cloud Alibaba   | 2021.0.1.0 |
| Spring Security Oauth2 | 2.1.2      |
| Mybatis Plus           | 3.4.1      |
| hutool                 | 5.7.3      |


```lua
jCloud
├── jwk-uaa -- 授权服务提供
├── jwk-upms -- 用户管理
├── jwk-api -- feign接口模块
└── jwk-common -- 系统公共模块
     ├── jwk-common-bom -- 版本管理
     ├── jwk-common-core -- 公共工具类核心包
     ├── jwk-common-cloud -- Spring Cloud Alibaba
     ├── jwk-common-security -- Security和Oauth基础
     ├── jwk-common-mybatis -- Mybatis Plus数据源包
     ├── jwk-common-dynam-datasource -- 动态数据源
     ├── jwk-common-redis -- redis和多级缓存
     ├── jwk-common-seata -- 分布式事务
     ├── jwk-common-prometheus -- 普罗米修斯监控
     ├── jwk-common-knife4j -- 接口文档
     ├── jwk-common-rocketmq -- RocektMQ整合
     ├── jwk-common-rabbitmq -- RabbitMQ整合
     ├── jwk-common-canal -- Canal组件
     ├── jwk-common-canal-rocketmq -- RocketMQ同步Canal
     └── jwk-common-sharding-jdbc -- 分库分表
├── jwk-knife4j -- 接口文档管理
├── jwk-admin -- Admin监控
└── jwk-gateway -- Spring Cloud Gateway网关
```