FROM openjdk:11-jre

RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

COPY target/paper-*.jar paper.jar
COPY entrypoint.sh /entrypoint.sh
ENTRYPOINT ["/bin/bash", "/entrypoint.sh"]
