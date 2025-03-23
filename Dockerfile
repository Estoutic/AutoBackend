FROM maven:3.9.0-amazoncorretto-17 AS build
COPY src /home/projects/auto_backend/src
COPY pom.xml /home/projects/auto_backend/
WORKDIR /home/projects/auto_backend/

# Сборка без тестов
RUN mvn clean package -DskipTests -Dmaven.test.skip=true

FROM amazoncorretto:17-alpine
# Копируем собранный jar
COPY --from=build /home/projects/auto_backend/target/*.jar /usr/local/lib/app.jar

# Запускаем с указанием правильного пути к классу
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "/usr/local/lib/app.jar"]