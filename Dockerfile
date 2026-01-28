FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY .mvn ./.mvn
COPY mvnw mvnw
COPY pom.xml .
COPY src ./src
COPY resources/mails src/main/resources/mails/
COPY resources/view src/main/resources/view/
COPY resources/static src/main/resources/static/
COPY resources/robots.txt src/main/resources/
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN apk add --no-cache wget
COPY --from=builder /app/target/jira-1.0.jar app.jar
COPY --from=builder /app/src/main/resources/mails ./resources/mails/
COPY --from=builder /app/src/main/resources/view ./resources/view/
COPY --from=builder /app/src/main/resources/static ./resources/static/
COPY --from=builder /app/src/main/resources/robots.txt ./resources/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]