FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -q -DskipTests dependency:go-offline
COPY src ./src
RUN ./mvnw -q -DskipTests package

FROM eclipse-temurin:25-jre
WORKDIR /app
RUN mkdir -p /app/uploads
COPY --from=build /app/target/*.jar app.jar
ENV PORT=8080
ENV APP_UPLOAD_DIR=/app/uploads
ENV JAVA_TOOL_OPTIONS=-Djava.awt.headless=true
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
