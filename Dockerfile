FROM reg.enncloud.cn/iot/java:8-jre-alpine
MAINTAINER Li Xiang lixiangk@enncloud.cn

# copy application
ARG JAR_FILE
ADD ${JAR_FILE} /data/bin/app.jar

# set start command
ENTRYPOINT ["java",\
# set urandom
"-Djava.security.egd=file:/dev/./urandom",\
# set timezone
"-Duser.timezone=Asia/Shanghai",\
"-jar","/data/bin/app.jar"\
# set config path
#"--spring.config.location=/data/iot/conf/application.properties"
]
