FROM openjdk:11-jre

RUN echo "TZ='Asia/Seoul'; export TZ" >> .profile
RUN source ~/.profile

COPY target/paper-*.jar paper.jar
COPY entrypoint.sh /entrypoint.sh
ENTRYPOINT ["/bin/bash", "/entrypoint.sh"]
