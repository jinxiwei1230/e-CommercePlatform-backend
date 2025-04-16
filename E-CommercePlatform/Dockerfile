# 构建阶段（使用你本地的 maven:3.8.6-eclipse-temurin-17）
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
# RUN mvn dependency:go-offline -B    # -B 表示批处理模式，减少日志输出
COPY src ./src
RUN mvn package -DskipTests         # 打包时跳过测试

# 使mysql支持中文
ENV LANG=C.UTF-8

# 运行阶段（使用你本地的 eclipse-temurin:17-jre）
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/E-CommercePlatform-*.jar app.jar
EXPOSE 8080
# ENTRYPOINT ["java", "-jar", "app.jar"]