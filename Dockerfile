FROM maven:3-amazoncorretto-21-debian as builder

WORKDIR /app

COPY pom.xml /app/pom.xml
COPY src /app/src

RUN mvn clean package -DskipTests

FROM openjdk:21-bullseye

WORKDIR /app

COPY --from=builder /app/target/ProcessingUnit-0.0.1.jar /app/ProcessingUnit-0.0.1.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "ProcessingUnit-0.0.1.jar"]
