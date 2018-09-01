
https://github.com/codecentric/spring-boot-admin

[文档首页](http://codecentric.github.io/spring-boot-admin/current/)

目前版本：2.0.2

配置中心、注册中心

```java
@EnableAdminServer
```
## 日志文件

```properties
#logging.file=/var/log/sample-boot-application.log
logging.file=sample-boot-application.log
logging.path=d:/
```

## Clustering

需要缓存 Hazelcast 支持
```xml
<dependency>
    <groupId>com.hazelcast</groupId>
    <artifactId>hazelcast</artifactId>
</dependency>
```
HazelcastConfig:
```java
@Bean
public Config hazelcastConfig() {
    MapConfig mapConfig = new MapConfig("spring-boot-admin-event-store").setInMemoryFormat(InMemoryFormat.OBJECT)
                                                                        .setBackupCount(1)
                                                                        .setEvictionPolicy(EvictionPolicy.NONE);
    return new Config().setProperty("hazelcast.jmx", "true").addMapConfig(mapConfig);
}
```

## 邮件通知

[文档](http://codecentric.github.io/spring-boot-admin/current/#mail-notifications)

pom.xml
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

application.properties
spring.mail.host=smtp.example.com # 邮件服务的 smtp 地址，其他和 mail starter 一致
spring.mail.username
spring.mail.password

spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true

spring.boot.admin.notify.mail.to= 逗号分隔
spring.boot.admin.notify.mail.cc
spring.boot.admin.notify.mail.from
spring.boot.admin.notify.mail.additional-properties

## 客户端

客户端如果是微服务架构和 sba 一起在注册中心，只需要开放管理端点，pom 依赖也可以不用

management.endpoints.web.exposure.include=*

pom.xml （可以不用）
```xml
<!-- SBA -->
<dependency>
    <groupId>de.codecentric</groupId>
    <artifactId>spring-boot-admin-starter-client</artifactId>
</dependency>
```

### 客户端多实例

客户端多实例还有点问题，只会认出一个，重启后认出多个 UNKNOWN 状态，或者 RESTRICT 状态实例