FROM openjdk:8-jdk-alpine

# https://github.com/docker-library/openjdk/issues/76
RUN apk add --no-cache tini

# Tini is now available at /sbin/tini
ENTRYPOINT ["/sbin/tini", "--"]

# copy arthas
COPY --from=hengyunabc/arthas:latest /opt/arthas /opt/arthas

COPY attach.sh /tmp/attach.sh

# CMD ["/tmp/attach.sh"]

# jps | grep arthas-demo | awk '{print $1}'