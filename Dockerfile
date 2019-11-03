FROM debian:9

RUN echo \
    deb http://mirrors.aliyun.com/debian/ stretch main non-free contrib\
    deb-src http://mirrors.aliyun.com/debian/ stretch main non-free contrib\
    deb http://mirrors.aliyun.com/debian-security stretch/updates main\
    deb-src http://mirrors.aliyun.com/debian-security stretch/updates main\
    deb http://mirrors.aliyun.com/debian/ stretch-updates main non-free contrib\
    deb-src http://mirrors.aliyun.com/debian/ stretch-updates main non-free contrib\
    deb http://mirrors.aliyun.com/debian/ stretch-backports main non-free contrib\
    deb-src http://mirrors.aliyun.com/debian/ stretch-backports main non-free contrib\
    > /etc/apt/sources.list
# 更新源
RUN     apt-get update && apt-get -y upgrade

# 设置编码
ENV     LANG C.UTF-8
#设置时区
RUN     ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' > /etc/timezone

# 安装软件
RUN     apt-get -y install dos2unix net-tools procps

# 创建软件文件夹
RUN     mkdir -p /usr/soft/java /usr/soft/maven /usr/project/spring-boot-web /home/project
# 添加本地软件包到指定文件夹（会自动解压，软件压缩包必须放在docker同级目录）
ADD     jdk-8u221-linux-x64.tar.gz /usr/soft/java/
ADD     apache-maven-3.6.1-bin.tar.gz /usr/soft/maven/

# 把当前项目添加到指定目录
ADD     . /usr/project/spring-boot-web

# 配置环境变量
# java
ENV     JAVA_HOME=/usr/soft/java/jdk1.8.0_221
ENV     JRE_HOME=$JAVA_HOME/jre
ENV     CLASSPATH=.:$CLASSPATH:$JAVA_HOME/lib:$JRE_HOME/lib
ENV     PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin
# maven
ENV     MAVEN_HOME=/usr/soft/maven/apache-maven-3.6.1
ENV     MAVEN_OPTS="-Xms256m -Xmx512m"
ENV     PATH=${MAVEN_HOME}/bin:$PATH

# 授予可执行权限
RUN     chmod a+x  /usr/soft/maven/apache-maven-3.6.1/bin/mvn

# 修改*.sh编码
RUN     dos2unix /usr/project/spring-boot-web/src/main/resources/assembly/bin/*.sh

# 编译代码
RUN     cd /usr/project/spring-boot-web && mvn clean package

# 监听端口
EXPOSE  8888

# 启动服务
CMD     echo "The application will start " \
        && mv /usr/project/spring-boot-web/target/spring-boot-web-assembly.tar.gz /home/project/spring-boot-web-assembly.tar.gz \
        && cd /home/project/ \
        && tar -zxvf spring-boot-web-assembly.tar.gz \
        && cd spring-boot-web/bin \
        && sh docker-start.sh