package com.kkrepo.blog.common.model;

import com.kkrepo.blog.domain.Category;
import com.kkrepo.blog.domain.Tag;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * @author WangRuofei
 * @create 2020-05-26 3:22 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Data
public class ArticleModel {

    private Long id;

    private String title;

    private String description;

    private String cover;

    private String author;

    private Integer type;

    private String origin;

    private String state;

    private Date publishTime;

    private Date createTime;

    private Date updateTime;

    private String content;

    private String contentMd;

    private Category category;

    private List<Tag> tags;
}
