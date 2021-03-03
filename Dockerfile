FROM openjdk:14-alpine

ENV WORK_DIR /usr/telestion

EXPOSE 8080

COPY conf/config.json $WORK_DIR/config.json
COPY build/distributions/RocketSound-*.tar $WORK_DIR/

WORKDIR $WORK_DIR
ENTRYPOINT ["sh", "-c"]
CMD ["tar -xf *.tar && cd RocketSound-* && mkdir conf && cp ../config.json conf/config.json && ./bin/RocketSound"]


FROM alpine:3.9

RUN echo 'https://dl-cdn.alpinelinux.org/alpine/v3.9/main' >> /etc/apk/repositiories
RUN echo 'https://dl-cdn.alpinelinux.org/alpine/v3.9/community' >> /etc/apk/repositiories

RUN apk update
RUN apk add mongodb

VOLUME [ "/data/db" ]
WORKDIR /data

EXPOSE 27017

# Further configuration at mongodb startup
CMD [ "mongod" ]

# go to the mongo terminal with: sudo docker exec -it <container_id> sh
# /data # mongo
# > show dbs