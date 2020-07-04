package com.kkrepo.blog.common.model;

import com.kkrepo.blog.domain.Article;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * @author WangRuofei
 * @create 2020-05-26 10:41 上午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Data
@Builder
public class ArchivesWithArticle {

    private String date;

    private List<Article> articles;
}
