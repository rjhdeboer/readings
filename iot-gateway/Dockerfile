FROM openjdk:15-alpine
EXPOSE 8080
ARG JAR_FILE=target/iot-gateway-1.0-SNAPSHOT.jar
ADD ${JAR_FILE} iot-gateway.jar
ENTRYPOINT ["java","-jar","/iot-gateway.jar"]