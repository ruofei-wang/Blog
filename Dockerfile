FROM java:8

LABEL author="kkrepocom"\
    name="Blog"\
    version="1.0"\
    description="a blog program"

ENV PATH=.:$PATH
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone


WORKDIR /blog

COPY ./target/Blog.jar /blog/Blog.jar
COPY ./target/classes/application.yml /blog/application.yml
COPY ./docker-entrypoint.sh /blog/docker-entrypoint.sh
RUN chmod +x docker-entrypoint.sh

EXPOSE 8080
VOLUME /blog/logs
VOLUME /blog/data

ENTRYPOINT ["docker-entrypoint.sh"]
