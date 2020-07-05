FROM java:8

LABEL author="kkrepocom"\
    name="Blog"\
    version="1.0"\
    description="a blog program"

ENV PATH=.:$PATH
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone


WORKDIR /apt/blog

COPY ./target/Blog.jar /apt/blog/Blog.jar
COPY ./target/classes/application.yml /apt/blog/application.yml
COPY ./docker-entrypoint.sh /apt/blog/docker-entrypoint.sh

EXPOSE 8080
VOLUME /opt/blog

ENTRYPOINT ["docker-entrypoint.sh"]