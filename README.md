<div align="center">

[![JCloud](https://img.shields.io/badge/JCloud-0.1.5-success.svg)]()
[![Spring Cloud Alibaba](https://img.shields.io/badge/Spring%20Cloud-2021-blue.svg)](https://github.com/alibaba/spring-cloud-alibaba)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7-blue.svg)](https://github.com/spring-projects/spring-boot)
![LICENSE](https://img.shields.io/github/license/63777887/jcloud)

</div>

## 系统说明

- 基于 Spring Cloud 2021 、Spring Boot 2.7、 Spring Authorization Server 0.3.1 的 RBAC **权限管理系统**
- 提供常见中间件的处理方案，如Redis，MQ，Prometheus等
- 提供对常见容器化支持 Docker、Kubernetes 支持


### 核心依赖

| 依赖                   | 版本         |
| ---------------------- |------------|
| Spring Boot            | 2.7.0      |
| Spring Cloud           | 2021.0.4   |
| Spring Cloud Alibaba   | 2021.0.4.0 |
| Spring Authorization Server | 0.3.1      |
| Mybatis Plus           | 3.4.1      |
| hutool                 | 5.8.9      |


### 模块结构
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
     ├── jwk-common-dynamic-datasource -- 动态数据源
     ├── jwk-common-id-generater -- ID生成方案
     ├── jwk-common-knife4j -- 接口文档
     ├── jwk-common-mybatis -- Mybatis Plus数据源包
     ├── jwk-common-prometheus -- 普罗米修斯监控
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
### 使用方式

请查看每个模块的使用说明
