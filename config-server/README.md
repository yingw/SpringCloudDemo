# 配置中心：Spring Cloud Config

官方文档：http://cloud.spring.io/spring-cloud-static/Finchley.SR1/single/spring-cloud.html#_spring_cloud_config

## 配置中心服务端

首先新建一个独立的项目作为配置中心服务端程序：config-server，只选择一个 `Config Server` 依赖就够了。

**pom.xml**
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
```

启用 ConfigServer
```java
@EnableConfigServer
```

配置项 **application.properties**
1. 文件仓库配置方式
```properties
server.port=8888
spring.profiles.active=native
spring.cloud.config.server.native.search-locations=file:///D:/Projects/spring-cloud-finchley-demo/config-repo
```
2. 或者 Git 远程仓库配置方式
```properties
server.port=8888
spring.cloud.config.server.git.uri=http://git.wilmar.cn/YinGuowei/wilmar-microservice-demo.git
spring.cloud.config.server.git.search-paths=config-repo
```

### 说明：仓库 location 的配置方式

有多个方式来配置仓库位置：
- 本地仓库
- Git 仓库
- SVN 仓库等

使用本地仓库方式一定要设置 `spring.profiles.active=native`，再配置路径

本地仓库的例子：
```properties
spring.cloud.config.server.git.uri=file:///C:/Users/yingu/cloud-config-repo
spring.cloud.config.server.git.uri=file://${user.home}/cloud-config-repo 
```
注意 Windows 下需要用三个 / 来设定盘符路径。 `$[HOME]` `${user.home}`  是 Mac、Linux 下环境变量

Git 仓库的配置方式：
```properties
spring.cloud.config.server.git.uri=${HOME}/bootiful-microservices-config
spring.cloud.config.server.git.uri=https://git.oschina.net/yinguowei/cloud-config-repo.git
```

读取配置的多个格式：
```
/{application}/{profile}[/{label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties
```

例如

- http://localhost:8888/eureka-server/default
- http://localhost:8888/eureka-server/default/master
- http://localhost:8888/eureka-server-default.yml
- http://localhost:8888/master/eureka-server-default.properties

### 多 profile 配置

项目通常分 dev, test, prod 等 profile，用来区分不同环境下的配置文件，这种情况配置中心应该是多个。

还有一种用于多节点高可用系统的多个实例，每个实例设置一个 profile 来往注册中心获取不同的参数，如 node1, node2，配置文件就可以这样设置

- demo-service.properties 每个 demo-service 实例都会读到
- demo-service-node1.properties 启动时加上 --spring.profiles.active=node1，可以读到
- demo-service-node2.properties node2 节点读到

## 客户端使用

依赖，增加 `Config Client` 即可，**pom.xml**
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

配置，要用文件名 **bootstrap.properties**，区别于默认的 application.properties，bootstrap 的内容会在系统加载属性前 load 并加载配置中心内的配置
```properties
spring.application.name=demo-service
spring.cloud.config.uri=http://localhost:8888
```

在配置仓库中新建个和应用名，配置 `spring.application.name` 相同的配置文件，如 **demo-service.properties**，再把一些由配置中心集中配置的属性（如端口）放到配置中心去
```properties
server.port=${PORT:8000}
```

>配置分离的原则：服务内参数、外部地址可以放在项目内 application.properties，整个微服务架构内统一管理方便的配置项，如注册中心地址、设置、用户中心地址、安全规则等，放在配置中心 properties

### 刷新配置
由于 2.0 开始没有开放所有管理端点，还需要开放管理端点 **/actuator/refresh**
```properties
management.endpoints.web.exposure.include=*
```
或者
```properties
management.endpoints.web.exposure.include=health, info, refresh
```

在设置可刷新配置的类上声明
```java
@RefreshScope
```

修改了配置中心的配置后，测试提交
```
curl -X POST -d {} -H "Content-Type: application/json" http://localhost:9999/actuator/refresh
```
或者简单点
```
curl -X POST http://localhost:9999/actuator/refresh
```

TODO: 通过 Spring Cloud Bus 将配置更新同步回应用

## 配置中心服务化

在后面注册中心（如 Eureka）启动后，还可以把 Config Server 也注册到注册中心上

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

**application.properties**
```
spring.application.name=config-server
eureka.client.service-url.default-zone=http://localhost:8761/eureka
```
就可以了，但是会在启动时有互相依赖而有个超时报错，启动一会后就好了


### Consul

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-consul-discovery-client</artifactId>
</dependency>
```

```properties
spring.cloud.consul.host=127.0.0.1
spring.cloud.consul.port=8500
spring.cloud.consul.discovery.prefer-ip-address=true
spring.cloud.consul.discovery.ip-address=127.0.0.1
```

>如果用了虚拟机 Consul 还要打通网络，改用 hostname 注册，且在虚拟机中加 host `echo '10.0.75.1 SHH-IT-CB002CL' >> /etc/hosts`
```
spring.cloud.consul.discovery.prefer-ip-address=false
spring.cloud.consul.discovery.hostname=SHH-IT-CB002CL
```


### 客户端配置

配置中心服务化后，客户端只需要设置连接注册中心的地址，及配置中心的 ServiceId，就可以连到配置中心，而不需要显式配置配置中心的地址

**application.properties**
```properties
spring.cloud.config.discovery.enabled=true
spring.cloud.config.discovery.service-id=config-server
```

## 注册中心高可用设置

配置中心 Config Server 的高可用分两种情况：

1. 一种没有将 Config Server 注册到注册中心上，设置一个负载均衡器连接两个 Config Server 就好了。
2. 另一种将 Config Server 注册到注册中心上了，多个节点都注册就好了，客户端连接的时候可以连接多个配置中心地址
```
spring.cloud.config.uri=http://localhost:8888,http://localhost:8889
```

## JDBC 后端

Spring Cloud Config 除了能将配置文件存储在本地文件、Git 仓库以外，还可以将配置存储在数据库（关系型）中

官方文档：http://cloud.spring.io/spring-cloud-static/Finchley.SR1/single/spring-cloud.html#_jdbc_backend

依赖

- Actuator
- Config Server
- JDBC
- SQL Server

添加依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```

需要创建表 `PROPERTIES`，字段：`APPLICATION`, `PROFILE`, `LABEL`, `KEY`, `VALUE`


在数据库创建表 `properties`

创建数据库初始化脚本文件 **resources\db\schema.sql**
 
MySQL
```sql
CREATE TABLE `properties` (
  `id` int(11) NOT NULL,
  `key` varchar(50) NOT NULL,
  `value` varchar(500) NOT NULL,
  `application` varchar(50) NOT NULL,
  `profile` varchar(50) NOT NULL,
  `label` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

SQL Server
```sql
CREATE TABLE NHS_CONFIG.dbo.properties (
  id int NOT NULL,
  [key] varchar(50) NOT NULL,
  value varchar(500) NOT NULL,
  application varchar(50) NOT NULL,
  profile varchar(50) NOT NULL,
  label varchar(50) NOT NULL,
  PRIMARY KEY (id)
)
```

初始数据 **resources\db\data.sql**

MySQL

```sql
INSERT INTO `properties` (id, key, value, application, profile, label) VALUES(1, 'server.port', '${PORT:8761}', 'eureka-server', 'default', 'master');
INSERT INTO `properties` (id, key, value, application, profile, label) VALUES(1, 'eureka.instance.prefer-ip-address', 'true', 'eureka-server', 'default', 'master');
```

SQL Server

```sql
INSERT INTO properties (id, [KEY], value, application, profile, label) VALUES(1, 'server.port', '${PORT:8761}', 'eureka-server', 'default', 'master');
INSERT INTO properties (id, [KEY], value, application, profile, label) VALUES(2, 'eureka.instance.prefer-ip-address', 'true', 'eureka-server', 'default', 'master');

```

设置 application.properties 数据源等

MySQL

```properties
spring.profiles.active=jdbc

spring.datasource.username=root
spring.datasource.password=test1234
spring.datasource.url=jdbc:mysql://localhost:3306/config_repo?useUnicode=true&characterEncoding=UTF-8&verifyServerCertificate=false&useSSL=false
#spring.datasource.schema=classpath:db/schema.sql
#spring.datasource.data=classpath:db/data.sql
spring.datasource.initialization-mode=never

spring.cloud.config.server.jdbc.sql=SELECT `KEY`, `VALUE` from PROPERTIES where APPLICATION=? and PROFILE=? and LABEL=?
```

SQL Server
```properties
spring.datasource.type=com.microsoft.sqlserver.jdbc.SQLServerDataSource
spring.datasource.url=jdbc:sqlserver://10.229.255.77:1433;DatabaseName=NHS_CONFIG
spring.datasource.username=sa
spring.datasource.password=Test1234
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

spring.cloud.config.server.jdbc.sql=SELECT [KEY], VALUE from PROPERTIES where APPLICATION=? and PROFILE=? and LABEL=?
```


>注意：并不建议用 flaway 或者 jpa `ddl-auto=update` 自动创建表和数据，建议手工在数据库创建并设置 `ddl-auto=none` 和 `spring.datasource.initialization-mode=never`。
>但可以用 flyway 来管理数据版本。

>要注意的是上面的 config.server.jdbc.sql 是默认值，但是 KEY 是 MySQL 关键字，需要加上反引号

重新启动后访问：http://localhost:8888/eureka-server/default/ 就可以得到新的属性了

### 设置 Property 对象来 mapping 管理

还可以通过 JPA 设置个对象和表对应做以后的配置管理

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

```properties
spring.jpa.database=mysql
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
```

从数据库中读取配置并控制台打印
```java
@Data
@NoArgsConstructor
@Entity
@Table(name = "properties")
class Property {

    @Id
    Long id;
    String key;
    String value;
    String application;
    String profile;
    String label;
}

interface PropertyRepository extends JpaRepository<Property, Long> {
}

@Component
class PropertyCLI implements CommandLineRunner {

    private final PropertyRepository propertyRepository;

    PropertyCLI(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public void run(String... args) {
        propertyRepository.findAll().forEach(System.out::println);
    }
}
```
