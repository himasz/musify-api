FROM openjdk:11-jre-slim

ARG environment
ENV ENVIRONMENT="$environment"

COPY target/sample-api.jar /sample.jar

CMD java -jar /sample.jar
