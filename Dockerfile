FROM openjdk:17

LABEL cogent.cogent-admin.image.authors="keboom"

COPY . /opt/mobicaster

WORKDIR /opt/mobicaster

# 时区设置
RUN echo "Asia/shanghai" > /etc/timezone

EXPOSE 8082

ENTRYPOINT exec java -jar MobicasterServer-0.0.1-SNAPSHOT.jar