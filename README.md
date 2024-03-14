## 项目介绍

主要分为三大部分

1. 前端（Vue+TS）
2. 后端（SpringBoot + MyBatis + Spring）
3. 代码沙箱API（Docker + Docker java）

当前项目存在的缺陷

1. 用户提交代码后，提交数和通过数没有发生改变
   解决方案：在判题模块最后修改数据的时候，添加通过数和提交数的修改
2. 因为代码沙箱使用的是模版方法模式，一旦编译错误或者运行错误，就会抛出异常，无法获取到错误响应的数据
   解决方案：不要使用`try...catch...` ，而是将异常抛出，在最后总体的方法当中进行异常的捕获
   更好的解决方案：使用自定义的异常系统

## 上线

本项目在上线的时候，采用了微服务的结构，将单体代码结构，改造成了微服务框架

使用到的技术：

1. nacos
2. redis
3. rabbitmq
4. Gateway

流程：

1. 先将微服务项目通过`Docker Compose`进行部署
2. 通过访问`gateway服务`的接口文档，进行测试
3. 在本机中运行前端项目，将`axios`的请求地址，改为服务器的地址
4. 进行测试前端代码是否能够和后端代码互相通信
5. 将代码沙箱服务部署，通过本机的测试类，直接调用代码沙箱API

遇到的问题：

1. `mysql容器`部署之后，一直掉
   查看日志文件，发现是大小写设置的问题
   需要在`docker配置文件`当中这样配置`mysql`

   ```yaml
   services: 
   	mysql:
       image: mysql # 使用的镜像
       container_name: yuoj-mysql # 启动的实例名称
       environment:
         MYSQL_ROOT_PASSWORD: tz020515.. # root 用户密码
         TZ: Asia/Shanghai
       ports:
         - "3306:3306" # 端口映射
       volumes:
         - ./.mysql-data:/var/lib/mysql # 将数据目录挂载到本地目录以进行持久化
         - ./mysql-init:/docker-entrypoint-initdb.d # 启动脚本
         - /home/mysql/conf/my.cnf:/etc/mysql/my.cnf
         - /home/mysql/data2:/var/lib/mysql
       command:
         --lower-case-table-names=1
       restart: always # 崩溃后自动重启
       networks:
         - mynetwork # 指定网络
   ```

2. 使用的是内存为`4G`的服务器，运行后端微服务的时候，老是掉
   解决：设置运行`jar包`的堆栈参数（在各个服务中的`DockerFile`当中设置）
   通过参数`-Xmx512m` `-Xms512m` 

   ```dockerfile
   # 基础镜像
   FROM openjdk:8-jdk-alpine
   
   # 指定工作目录
   WORKDIR /app
   
   # 将 jar 包添加到工作目录，比如 target/yuoj-backend-user-service-0.0.1-SNAPSHOT.jar
   ADD target/yuoj-backend-judge-service-0.0.1-SNAPSHOT.jar .
   
   # 暴露端口
   EXPOSE 8104
   
   # 启动命令
   ENTRYPOINT ["java","-Xmx512m","-Xms512m","-jar","/app/yuoj-backend-judge-service-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]
   ```

3. 微服务访问代码沙箱API，在执行Docker相关命令时，报错：`permission denied` 
   解决：公开docker

   ```sh
   sudo chmod 666 /var/run/docker.sock
   ```

4. 开始的时候，打算将前端项目和后端微服务，分别部署在两个不同的服务器的，发现登录无法实现了
   原因：使用的`Session`存储用户的登录信息，然后将这个`Session`保存到`Cookie`当中，`Cookie`是被保存到浏览器缓存当中的，两个服务器都不同，就会导致取不到同一个`Cookie`
   
5. 微服务项目部署之后，访问接口文档报错：`knife4j异常` 
   解决：重新启动`backend-gateway`服务即可


