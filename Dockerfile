# 基础镜像
FROM eclipse-temurin:17
# 设置工作目录
WORKDIR /app
# 复制构建的JAR文件到容器中
COPY target/*.jar app.jar
# 声明服务端口
EXPOSE 8080
# 启动应用
ENTRYPOINT ["java", "-jar","app.jar"]