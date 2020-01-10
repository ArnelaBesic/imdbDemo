FROM openjdk:8
ENV DOWNLOAD http://www.h2database.com/h2-2019-10-14.zip
ENV DATA_DIR /opt/h2-data

RUN mkdir -p ${DATA_DIR} \
    && curl ${DOWNLOAD} -o h2.zip \
    && unzip h2.zip -d /opt/ \
    && rm h2.zip

COPY h2.server.properties /root/.h2.server.properties

EXPOSE 8082 9092

WORKDIR /opt/h2-data

CMD java -cp /opt/h2/bin/h2*.jar org.h2.tools.Server \
 	-web -webAllowOthers -webPort 8082 \
 	-tcp -tcpAllowOthers -tcpPort 9092 \
 	-baseDir ${DATA_DIR} ${H2_OPTIONS}
