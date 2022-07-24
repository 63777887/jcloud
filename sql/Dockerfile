FROM mysql/mysql-server:8.0.29

MAINTAINER jiwk(63777887@qq.com)

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ./jcloud.sql /docker-entrypoint-initdb.d/

