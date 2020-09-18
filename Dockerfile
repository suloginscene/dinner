FROM openjdk:11-jre
ENV LANG=ko_KR.UTF-8
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV GROUP=docker-user
ENV USER=docker-scene
RUN groupadd -r $GROUP && useradd -r -g $GROUP $USER
USER $USER
WORKDIR /home/$USER
ENV PROFILE=dev
COPY target/dinner-*.jar dinner.jar
ENTRYPOINT ["java", "-jar", "dinner.jar", "--spring.profiles.active=${PROFILE}"]
