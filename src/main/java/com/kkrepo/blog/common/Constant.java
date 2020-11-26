package com.kkrepo.blog.common;

import java.util.regex.Pattern;

/**
 * 常量类
 * @author WangRuofei
 * @create 2020-05-21 10:13 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
public interface Constant {

    /**
     * 博客前台默认每页显示多少条博文
     */
    int DEFAULT_PAGE_LIMIT = 12;

    /**
     * Footer模块最新文章的Model对象Key值
     */
    String RECENT_POSTS = "RecentPosts";

    /**
     * Footer模块最新评论的Model对象Key值
     */
    String RECENT_COMMENTS = "RecentComments";

    /**
     * Index页面Model对象Key值
     */
    String INDEX_MODEL = "list";

    /**
     * 文章详情页面Model对象Key值
     */
    String ARTICLE_MODEL = "article";

    /**
     * 文章详情页评论数据Model对象Key值
     */
    String COMMENTS_MODEL = "comments";

    /**
     * Archives页面Model对象Key值
     */
    String ARCHIVES_MODEL = "list";

    /**
     * Links页面Model对象Key值
     */
    String LINKS_MODEL = "list";

    /**
     * Articles页评论分类
     */
    int COMMENT_SORT_ARTICLE = 0;

    /**
     * 默认每页显示多少条评论数据
     */
    int COMMENT_PAGE_LIMIT = 8;

    /**
     * Links页评论分类
     */
    int COMMENT_SORT_LINKS = 1;

    /**
     * About页评论分类
     */
    int COMMENT_SORT_ABOUT = 2;

    String DEFAULT_CATEGORY = "默认分类";

    String INDEX_PAGEINFO = "pageInfo";

    int ARTICLE_OUTLINE_LIMIT = 50;

    /**
     * User-Agent
     */
    String USER_AGENT = "User-Agent";

    /**
     * 默认索引名称
     */
    String ELASTIC_SEARCH_INDEX = "blog";

    /**
     * 定义HTML标签及空格换行等字符的正则表达式
     */
    String REGEX_HTML="<[^>]+>|\\s*|\t|\r|\n";
    Pattern htmlRegex = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);

    /**
     * 搜索页面默认每页展示数量
     */
    int DEFAULT_SEARCH_PAGE_LIMIT = 10;
}
