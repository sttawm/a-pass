FROM hseeberger/scala-sbt:graalvm-ce-21.3.0-java11_1.6.2_2.13.8 as build-image

RUN microdnf install unzip

COPY server /server
WORKDIR /server

# build the dist artifact
RUN sbt dist

# organize the distribution
RUN set -x && \
    unzip -d svc /server/target/universal/server-*.zip && \
    mv svc/*/* svc/ && \
    rm svc/bin/*.bat && \
    mv svc/bin/server* svc/bin/start

# prod deployment image
FROM openjdk:11-buster

# just pull what we need from stage 1 of the build
COPY --from=build-image /server/svc /svc
EXPOSE 9000

# RUN main concern. By doing this way, it should use shell to expand env vars
CMD set -x && \
    /svc/bin/start -Dconfig.resource=$APP_CONF_FILE -Dlogger.resource=logback.xml -Dplay.http.secret.key=$APP_SECRET_KEY
