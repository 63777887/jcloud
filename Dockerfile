#FROM azul/zulu-openjdk-alpine:8-jre
FROM moxm/java:1.8-full
LABEL maintainer="jiwk <63777887@qq.com>"
ARG JAR_FILE
ARG SW_IP=192.168.0.104:11800
ARG PORT
ARG OPTS=""

WORKDIR /${JAR_FILE}


COPY ./skywalking-agent/ lib/skywalking-agent

COPY ./${JAR_FILE}/target/${JAR_FILE}.jar app.jar

EXPOSE ${PORT}

# 配置字体和时区 docker 镜像中的时区非国内时区，更改为上海时区  RUN书写时的换行符是\
RUN echo "http://mirrors.aliyun.com/alpine/v3.6/main" > /etc/apk/repositories \
    && echo "http://mirrors.aliyun.com/alpine/v3.6/community" >> /etc/apk/repositories \
    && apk update upgrade \
    && apk add --no-cache procps unzip curl bash tzdata \
    && apk add ttf-dejavu \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone
# 功能为设置环境变量
# 普通参数，如指定启动环境等
ENV PARAMS=""
# JVM参数
#ENV JAVA_OPTS="-javaagent:/${JAR_FILE}/lib/skywalking-agent/skywalking-agent.jar -Dskywalking.agent.service_name=${JAR_FILE} -Dskywalking.collector.backend_service=${SW_IP}"
ENV JAVA_OPTS=${OPTS}
ENV SERVER_PORT=${PORT}
# 容器执行的命令
#ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar $PARAMS"]
#ENTRYPOINT sleep 30; echo java $JAVA_OPTS -Dserver.port=$PORT -jar app.jar $PARAMS; java $JAVA_OPTS -Dserver.port= $PORT -jar app.jar $PARAMS
CMD sleep 30; echo java $JAVA_OPTS -Dserver.port=${SERVER_PORT} -jar app.jar $PARAMS; java $JAVA_OPTS -Dserver.port=${SERVER_PORT} -jar app.jar $PARAMS