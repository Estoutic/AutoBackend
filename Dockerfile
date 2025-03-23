FROM maven:3.9.0-amazoncorretto-17 AS build
COPY src /home/projects/auto_backend/src
COPY pom.xml /home/projects/auto_backend/
WORKDIR /home/projects/auto_backend/

# Явно запускаем Maven с нужными параметрами
RUN mvn clean package -DskipTests -Dmaven.test.skip=true

# Проверим содержимое JAR-файла для диагностики
RUN ls -la target/
RUN jar tvf target/autoBackend-0.0.1-SNAPSHOT.jar | grep META-INF/MANIFEST.MF

FROM amazoncorretto:17-alpine
# Копируем конкретный jar-файл с правильным именем
COPY --from=build /home/projects/auto_backend/target/autoBackend-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "/usr/local/lib/app.jar"]