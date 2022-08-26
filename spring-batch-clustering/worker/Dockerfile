FROM openjdk:11-slim-buster

RUN mkdir -p /app/config

ENV JAVA_OPTS="-DPROTOTYPING=false"
ENV JAR_NAME=""

COPY ./target/${JAR_NAME} /app
COPY ./src/main/resources/application-docker.yml /app/config
COPY ./src/main/resources/logback.xml /app/config

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Dlogging.config=classpath:file:///app/config/logback.xml -Dspring.config.location=file:///app/config/application-docker.yml -jar /app/${JAR_NAME}"]