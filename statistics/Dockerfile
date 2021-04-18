FROM openjdk:15-alpine
EXPOSE 8080
ARG JAR_FILE=target/statistics-1.0-SNAPSHOT.jar
ADD ${JAR_FILE} statistics.jar
ENTRYPOINT ["java","-jar","/statistics.jar"]