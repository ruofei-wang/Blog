FROM java:8

LABEL author="kkrepocom"\
    name="Blog"\
    version="1.0"\
    description="a blog program"

ENV PATH=.:$PATH
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone


WORKDIR /opt/blog

COPY ./target/Blog.jar /opt/blog/Blog.jar
COPY ./target/classes/application.yml /opt/blog/application.yml
COPY ./docker-entrypoint.sh /opt/blog/docker-entrypoint.sh
RUN chmod +x docker-entrypoint.sh

EXPOSE 8080
VOLUME /opt/blog/logs
VOLUME /opt/blog/data

ENTRYPOINT ["docker-entrypoint.sh"]
CMD [""]
