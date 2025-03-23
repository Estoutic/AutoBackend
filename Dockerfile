FROM maven:3.9.0-amazoncorretto-17 AS build
COPY src /home/projects/auto_backend/src
COPY pom.xml /home/projects/auto_backend/
WORKDIR /home/projects/auto_backend/
RUN mvn dependency:resolve
RUN mvn clean package -DskipTests -DskipAspectJ=true

FROM amazoncorretto:17-alpine
COPY --from=build /home/projects/auto_backend/target/*.jar /usr/local/lib/auto_backend.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "/usr/local/lib/auto_backend.jar"]