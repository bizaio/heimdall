FROM openjdk:11-jre-slim
VOLUME /tmp
VOLUME /opt/dataright/localdb
EXPOSE 9103
ARG JAR_FILE
ADD target/${JAR_FILE} /opt/dataright/heimdall-auth.jar
ENTRYPOINT ["java","-jar","/opt/dataright/heimdall-auth.jar"]
