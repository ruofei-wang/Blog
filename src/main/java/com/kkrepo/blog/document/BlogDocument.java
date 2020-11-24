package com.kkrepo.blog.document;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author WangRuofei
 * @date 2020-11-22 3:46 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogDocument {

    private String id;

    private String title;

    private String description;

    private String author;

    private List<String> tags;

    private String category;

    private String content;

    private int pv;

    private long comments;

    private Long createDate;
}
