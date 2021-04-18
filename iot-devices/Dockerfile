FROM openjdk:15-alpine
ARG JAR_FILE=target/iot-devices-1.0-SNAPSHOT.jar
ADD ${JAR_FILE} iot-devices.jar
ENTRYPOINT ["java","-jar","/iot-devices.jar"]