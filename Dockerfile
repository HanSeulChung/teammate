FROM openjdk:17-alpine

WORKDIR /usr/src/app

ARG JAR_PATH=./backend/build/libs

COPY ${JAR_PATH}/backend-0.0.1-SNAPSHOT.jar backend-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar","-Dspring.profiles.active=prod","-Dcom.amazonaws.sdk.disableEc2Metadata=true", "backend-0.0.1-SNAPSHOT.jar"]