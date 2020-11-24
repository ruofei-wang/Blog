package com.kkrepo.blog.common.model;

import lombok.Data;

/**
 * @author WangRuofei
 * @date 2020-11-24 4:14 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Data
public class SearchModel {

    private String title;

    private String description;

    private String author;

    private String createDate;

    private int comments;

    private int pv;
}
