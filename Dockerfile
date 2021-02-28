FROM openjdk:14-alpine

ENV WORK_DIR /usr/telestion

EXPOSE 8080

COPY build/distributions/RocketSound-*.tar $WORK_DIR/

WORKDIR $WORK_DIR
ENTRYPOINT ["sh", "-c"]
CMD ["tar -xf *.tar && cd RocketSound-* && ./bin/RocketSound"]
