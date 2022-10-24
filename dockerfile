FROM adoptopenjdk/openjdk11:alpine

ARG JAR_FILE=build/libs/mapnote-server-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]

# MAC IMAGE : docker build --platform linux/amd64 -t docker-test . 
# WINDOW IMAGE : docker build -t docker-test .