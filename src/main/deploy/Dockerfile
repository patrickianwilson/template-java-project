FROM openjdk:8-jre

COPY distributions/template-java-project.zip /tmp
RUN unzip /tmp/template-java-project.zip -d /app

WORKDIR /app/template-java-project
ENTRYPOINT /app/template-java-project/bin/template-java-project
