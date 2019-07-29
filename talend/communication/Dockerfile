#FROM openjdk:8-jdk-alpine
FROM 192.168.1.101:5005/oraclejdk8
VOLUME /tmp

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ADD target/business-model.jar app.jar


ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
