package com.kkrepo.blog.schedule;

import com.kkrepo.blog.common.Constant;
import com.kkrepo.blog.document.BlogDocument;
import com.kkrepo.blog.domain.Article;
import com.kkrepo.blog.domain.Category;
import com.kkrepo.blog.domain.Tag;
import com.kkrepo.blog.service.ArticleService;
import com.kkrepo.blog.service.CategoryService;
import com.kkrepo.blog.service.CommentService;
import com.kkrepo.blog.service.EsBlogService;
import com.kkrepo.blog.service.TagService;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时更新或者创建redis的定时任务
 * @author WangRuofei
 * @date 2020-11-22 7:52 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Slf4j
@Component
public class ElasticSearchUpsertSchedule {

    private static final int fixedDelay = 60 * 60 * 1000;

    @Autowired
    private EsBlogService esBlogService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CommentService commentService;

    @Scheduled(fixedDelay = fixedDelay)
    public void esSchedule() {
        log.info("ElasticSearchUpsertSchedule start....   time:{}", new Date());
        // 创建索引
        if (!esBlogService.existsIndex()) {
            if (esBlogService.createBlogIndex()) {
                List<Article> articleList = articleService.queryAllWithBLOBs();
                List<BlogDocument> blogDocumentList = articleList.stream().map(x -> coverToDocument(x)).collect(Collectors.toList());
                if (!esBlogService.bulkCreateBlogDocument(blogDocumentList)) {
                    log.error("ElasticSearchUpsertSchedule esBlogService.bulkCreateBlogDocument() error!");
                }
            } else {
                log.error("ElasticSearchUpsertSchedule esBlogService.createBlogIndex() error!");
            }
        } else {
            List<Article> articleList = articleService.queryAllWithBLOBs();
            List<BlogDocument> blogDocumentList = articleList.stream().map(x -> coverToDocument(x)).collect(Collectors.toList());
            if (!esBlogService.batchUpdateBlogDocument(blogDocumentList)) {
                log.error("ElasticSearchUpsertSchedule esBlogService.batchUpdateBlogDocument() error!");
            }
        }
    }

    /**
     * 将 article 封装成 BlogDocument 对象
     * @param x
     * @return
     */
    private BlogDocument coverToDocument(Article x) {
        Category category = categoryService.queryByArticle(x.getId());
        List<Tag> tags = tagService.queryByArticleId(x.getId());
        long comments = commentService.countByArticleId(x.getId());
        return BlogDocument.builder()
            .id(x.getId() + "")
            .title(x.getTitle())
            .author(x.getAuthor())
            .description(x.getDescription())
            .category(category.getName())
            .tags(tags.stream().map(t -> t.getName()).collect(Collectors.toList()))
            .content(Constant.htmlRegex.matcher(x.getContent()).replaceAll(""))
            .pv(x.getPv())
            .createDate(x.getCreateTime().getTime())
            .comments(comments)
            .build();
    }
}
