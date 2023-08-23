FROM openjdk:8-jre

COPY distributions/%%{{ModuleName}}%%.zip /tmp
RUN unzip /tmp/%%{{ModuleName}}%%.zip -d /app

WORKDIR /app/%%{{ModuleName}}%%
ENTRYPOINT /app/%%{{ModuleName}}%%/bin/%%{{ModuleName}}%%
