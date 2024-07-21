# 基于官方的Java 1.8镜像
FROM openjdk:8-jdk-alpine

# 在容器内部创建一个目录存放JAR包
ARG JAR_FILE=target/product-service-0.0.1-SNAPSHOT.jar

# 复制JAR包到镜像中
COPY ${JAR_FILE} app.jar

# 暴露容器内的端口给外部访问
EXPOSE 9802

# 运行Java应用程序
ENTRYPOINT ["java", "-jar", "/app.jar"]