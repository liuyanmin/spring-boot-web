<p align="center">
  每个人都可以基于此项目的基础上快速、高效的开发项目！
</p>

<p align="center">  
  <a href="https://github.com/spring-projects/spring-boot">
    <img alt="spring boot version" src="https://img.shields.io/badge/spring%20boot-2.1.8.RELEASE-brightgreen">
  </a>
  <a href="https://www.apache.org/licenses/LICENSE-2.0">
    <img alt="code style" src="https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square">
  </a>
</p>

### spring-boot-web是一套集成spring boot常用开发组件的后台快速开发框架
> spring-boot-web 是易于使用，功能丰富，满足企业开发的基本需求，可以基于此快速、高效的开发业务代码，是一套web开发非常好的脚手架。并且，项目架构非常简单清晰、容易上手，可以根据不同的需求定制化开发和修改。

> 前后端分离，spring-boot-web 专注于后端服务。

## 目标
> 每个人都可以成为高级开发工程师，可以独立、快速的搭建稳定的项目架构。

### 主要特征
#### v1.0
1. 集成spring boot 常用开发组件
2. 集成validation，通过注解校验请求参数
3. 集成lombok
4. 集成mybatis generator，快速生成domain和mapper数据库常用操作CRUD代码
5. 集成swagger2，自动生成在线api文档
6. 集成redis，封装了一套redis工具类，RedisTemplate和RedisService，可以实现查询redis没有数据时，自动从把数据库力中的数据写入redis操作，可以大大的提高开发效率，使代码变得更加整理
7. 集成druid连接池
8. 集成maven-assembly插件，进行不同环境的打包部署，并把配置文件提取到conf中，包含启动、停止、重启shell脚本
9. 集成docker部署

### 功能实现
#### v1.0
1. 实现角色+菜单+按钮权限控制，包括权限管理、菜单管理、角色管理、用户管理的CRUD
2. 使用JWT实现用户登录，支持账号单点登录和多端登录，前后台密码传输采用rsa加密，保证登录安全
3. 实现找回密码、修改邮箱等功能
4. 实现系统化参数配置管理
5. 日志管理，记录请求参数，提供查询操作
6. 提供服务健康状态检查接口
7. 实现拦截器可以配置，application.yml可以配置启用或禁用，包括：日志拦截器、jwt拦截器、权限认证拦截器
8. 后台可通过注解控制是否开启接口权限认证

### 项目环境 
组件 | 版本 |  备注
-|-|-
JDK | 1.8+ | JDK1.8及以上 |
MySQL | 5.7+ | 5.7及以上 |
Redis | 3.2+ |  |

### 技术选型 
技术 | 版本 |  备注
-|-|-
Spring Boot | 2.1.8.RELEASE | 最新发布稳定版 |
Spring Framework | 5.1.9.RELEASE | 最新发布稳定版 |
Mybatis | 3.5.0 | 持久层框架 |
Pagehelper | 1.2.12 | mybatis分页组件 |
Alibaba Druid | 1.1.20 | 数据源 |
Fastjson | 1.2.62 | JSON处理工具集（注意及时更新最新版，防止day0漏洞） |
swagger2 | 2.9.2 | api文档生成工具 |
commons-lang3 | 3.9 | 常用工具包 |
commons-io | 2.6 | IO工具包 |
jwt | 0.9.1 | json web token |
hutool-all | 4.6.10 | 常用工具集 |
lombok | 1.18.10 | 注解生成Java Bean等工具 |
mail | 1.4.7 | 邮件工具类 |

## 开始
### 克隆 spring-boot-web
```bash
git clone https://github.com/liuyanmin/spring-boot-web.git
```

### 创建数据库
```sql
create database `spring_boot_web` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```
执行项目resources目录下的schema.sql文件，创建表和初始化数据。

### 修改application-*.yml 配置文件
* application-dev: 开发环境配置文件
* application-test: 测试环境配置文件
* application-prod: 生产环境配置文件

修改数据库和redis的用户名和密码: 
```yaml
spring:
  # datasource config
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/spring_boot_web?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: 123456
  # redis config
  redis:
    host: 127.0.0.1
    port: 6379
    password: 123456
    db: 1
```

### 修改 application.yml 主配置文件
修改邮箱配置。在用户登录找回密码的时候需要通过发送邮件找回。
```yaml
spring-boot-web:
  mail:
    protocol: smtp
    host: smtp.163.com
    from: ***@163.com
    from-password: sdfa
    from-name: spring-boot-web
    message-type: text/html;charset=UTF-8
```

### 修改运行环境
修改 pom.xml 配置文件: 
```yaml
<!-- MAVEN打包选择运行环境， -->
  <!-- dev:开发环境  test:测试环境  prod:生产环境 -->
  <profiles>
    <profile>
      <id>dev</id>
      <properties>
        <profileActive>dev</profileActive>
      </properties>
      <activation>
        <activeByDefault>false</activeByDefault>
      </activation>
    </profile>
    <profile>
      <id>test</id>
      <properties>
        <profileActive>test</profileActive>
      </properties>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
    </profile>
    <profile>
      <id>prod</id>
      <properties>
        <profileActive>prod</profileActive>
      </properties>
      <activation>
        <activeByDefault>false</activeByDefault>
      </activation>
    </profile>
  </profiles>
```

### 修改 resources/mybatis-generator/generatorConfig.xml 配置文件
修改数据库连接:
```yaml
<!--数据库连接信息-->
<jdbcConnection driverClass="com.mysql.jdbc.Driver"
                connectionURL="jdbc:mysql://localhost:3306/spring_boot_web?useUnicode=true&amp;characterEncoding=UTF-8&amp;serverTimezone=UTC&amp;verifyServerCertificate=false&amp;useSSL=false"
                userId="root"
                password="***"/>
```

### 代码配置类: 
```text
com.lym.springoot.web
└── config
    ├── converter
    │   └── Json类型转换工具类
    ├── datasource
    │   └── DataSourceConfig.java
    ├── interceptor
    │   └── InterceptorConfig.java
    ├── json
    │   └── api json 序列化与反序列化配置类
    ├── properties
    │   └── yml配置映射
    ├── redis
    │     ├── RedisConfig.java
    │     ├── RedisService.java
    │   └── RedisTemplate.java
    ├── swagger
    │   └── Swagger2Config.java   
    ├── validator
    │   └── ValidatorConfig.java   
    └── MultipartConfig.java
```

### docker 部署
需要在项目的根目录下放jdk和maven的安装包
```text
jdk-8u221-linux-x64.tar.gz
apache-maven-3.6.1-bin.tar.gz
```