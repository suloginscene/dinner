FROM openjdk:11-jre

ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY target/paper-*.jar paper.jar

ENTRYPOINT ["java", "-jar", "paper.jar"]
