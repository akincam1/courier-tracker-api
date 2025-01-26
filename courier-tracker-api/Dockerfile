FROM openjdk:21-jdk-slim as build

WORKDIR /app

RUN apt-get update -qq && apt-get install -y maven

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/infra/target/infra*.jar /infra.jar

ENV TZ Europe/Istanbul

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/infra.jar"]