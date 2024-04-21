#FROM openjdk:21 AS build
#
## Устанавливаем рабочий каталог
#WORKDIR /app
#
## Копируем файлы сборки проекта
#COPY build.gradle .
#COPY settings.gradle .
#COPY gradlew .
#COPY gradle ./gradle
#
## Копируем исходный код
#COPY src ./src
#
## Выполняем сборку проекта
#RUN ./gradlew build -x test --no-daemon
#
## Создаем образ для выполнения
#FROM openjdk:21
#
## Устанавливаем рабочий каталог
#WORKDIR /app
#
#
## Копирование собранного JAR из этапа сборки
#COPY --from=build /app/build/libs/MediaWiki-1.0-SNAPSHOT.jar mediawiki.jar
#
## Команда запуска приложения
#CMD ["java", "-jar", "mediawiki.jar"]

#FROM openjdk:21 AS build
#COPY build.gradle gradlew settings.gradle /
#COPY src src
#COPY gradle gradle
#RUN ./gradlew build
#
#FROM openjdk:21
#COPY --from=build /app/build/libs/MediaWiki-1.0-SNAPSHOT.jar mediawiki.jar
#CMD ["java", "-jar", "mediawiki.jar"]
FROM openjdk:21
ADD build/libs/*.jar app.jar
CMD [ "java", "-jar", "app.jar" ]