FROM java:8-jre
MAINTAINER caimingkai

#修改时区为中国
RUN rm -f /etc/localtime
RUN ln -s /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

ADD ./target/fw-gateway-1.0-SNAPSHOT.jar /app/
CMD ["java", "-Xmx150m", "-jar", "/app/fw-gateway-1.0-SNAPSHOT.jar"]

EXPOSE 8080