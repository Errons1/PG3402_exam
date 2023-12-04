FROM maven:3.9-amazoncorretto-17 as builder
WORKDIR /app
COPY pom.xml pom.xml 
COPY src/ src/
COPY start.sh start.sh
RUN mvn package

FROM amazoncorretto:17.0.9-alpine3.18
COPY --from=builder /app/target/*.jar /app/application.jar
COPY --from=builder /app/start.sh /app/start.sh
RUN apk add bash
RUN chmod +x /app/start.sh
ENTRYPOINT ["/app/start.sh"]
CMD ["java","-jar", "-Dspring.profiles.active=docker", "/app/application.jar"]