CREATE TABLE tb_article
(
    id           bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '编号',
    title        varchar(400)           DEFAULT NULL COMMENT '标题',
    description  varchar(1024) NOT NULL DEFAULT '' COMMENT '文章描述',
    cover        varchar(400)           DEFAULT NULL COMMENT '封面图片',
    author       varchar(100)  NOT NULL COMMENT '作者',
    content      mediumtext COMMENT '内容',
    content_md   mediumtext COMMENT '内容-Markdown',
    type         int(11)       NOT NULL DEFAULT '0' COMMENT '类型， 0原创 1转载',
    origin       varchar(100)           DEFAULT NULL COMMENT '来源',
    state        varchar(100)  NOT NULL DEFAULT '' COMMENT '状态（0: 初始1:已发布 2:已删除）',
    publish_time datetime               DEFAULT NULL COMMENT '发布时间',
    create_time  timestamp     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  timestamp     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8 COMMENT ='文章表';

CREATE TABLE tb_category
(
    id          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
    name        varchar(100)        DEFAULT NULL COMMENT '分类名称',
    create_time timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8 COMMENT ='分类表';

CREATE TABLE tb_article_category
(
    id          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
    article_id  bigint(20) NOT NULL COMMENT '文章ID',
    category_id bigint(20) NOT NULL COMMENT '分类ID',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8 COMMENT ='文章&&分类关联表';

CREATE TABLE tb_tag
(
    id          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
    name        varchar(100)        DEFAULT NULL COMMENT '标签名称',
    create_time timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8 COMMENT ='标签表';

CREATE TABLE tb_article_tag
(
    id         bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
    article_id bigint(20) NOT NULL COMMENT '文章ID',
    tag_id     bigint(20) NOT NULL COMMENT '标签ID',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8 COMMENT ='文章&&标签关联表';

CREATE TABLE tb_comment
(
    id            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    p_id          bigint(20)          DEFAULT '0' COMMENT '父级ID，给哪个留言进行回复',
    c_id          bigint(20)          DEFAULT '0' COMMENT '子级ID，给哪个留言下的回复进行评论',
    article_title varchar(200)        DEFAULT NULL COMMENT '文章标题',
    article_id    bigint(20)          DEFAULT NULL COMMENT '文章ID',
    name          varchar(20)         DEFAULT NULL COMMENT '昵称',
    c_name        varchar(20)         DEFAULT NULL COMMENT '给谁留言',
    content       text COMMENT '留言内容',
    email         varchar(100)        DEFAULT NULL COMMENT '邮箱',
    url           varchar(200)        DEFAULT NULL COMMENT '网址',
    state         varchar(1) NOT NULL DEFAULT '0' COMMENT '状态（0:初始 1:已审核 2:已删除）',
    sort          bigint(20)          DEFAULT '0' COMMENT '分类：0:默认，文章详情页，1:友链页，2:关于页',
    ip            varchar(20)         DEFAULT NULL COMMENT 'IP地址',
    device        varchar(100)        DEFAULT NULL COMMENT '设备',
    address       varchar(100)        DEFAULT NULL COMMENT '地址',
    create_time   timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8 COMMENT ='评论表';

CREATE TABLE tb_link
(
    id   bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
    name varchar(100) DEFAULT NULL COMMENT '连接名称',
    url  varchar(200) DEFAULT NULL COMMENT '连接URL',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8 COMMENT ='友链表';

CREATE TABLE tb_log
(
    id          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
    username    varchar(20)         DEFAULT NULL COMMENT '操作用户',
    operation   varchar(20)         DEFAULT NULL COMMENT '操作描述',
    time        bigint(20)          DEFAULT NULL COMMENT '耗时(毫秒)',
    method      varchar(100)        DEFAULT NULL COMMENT '操作方法',
    params      varchar(255)        DEFAULT NULL COMMENT '操作参数',
    ip          varchar(20)         DEFAULT NULL COMMENT 'IP地址',
    location    varchar(20)         DEFAULT NULL COMMENT '操作地点',
    create_time timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8 COMMENT ='系统日志表';

CREATE TABLE tb_login_log
(
    id          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
    username    varchar(20)         DEFAULT NULL COMMENT '用户名',
    ip          varchar(20)         DEFAULT NULL COMMENT 'IP地址',
    location    varchar(255)        DEFAULT NULL COMMENT '登录地点',
    device      varchar(255)        DEFAULT NULL COMMENT '登录设备',
    create_time timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8;

CREATE TABLE tb_user
(
    id          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '编号',
    username    varchar(100) NOT NULL COMMENT '用户名',
    password    varchar(100) NOT NULL COMMENT '密码',
    avatar      varchar(200)          DEFAULT NULL COMMENT '头像',
    email       varchar(256)          DEFAULT NULL COMMENT '邮箱',
    phone       varchar(32)           DEFAULT NULL COMMENT '手机号',
    introduce   varchar(100)          DEFAULT NULL COMMENT '介绍',
    info        varchar(256)          DEFAULT NULL COMMENT '备注',
    create_time timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8 COMMENT ='用户表';
