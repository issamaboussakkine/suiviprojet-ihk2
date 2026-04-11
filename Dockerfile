FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN rm -rf src/test
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]