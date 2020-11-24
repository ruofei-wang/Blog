package com.kkrepo.blog.task;

import com.kkrepo.blog.common.Constant;
import com.kkrepo.blog.common.enums.CommonStateEnum;
import com.kkrepo.blog.document.BlogDocument;
import com.kkrepo.blog.domain.Article;
import com.kkrepo.blog.domain.Category;
import com.kkrepo.blog.domain.Tag;
import com.kkrepo.blog.service.CategoryService;
import com.kkrepo.blog.service.CommentService;
import com.kkrepo.blog.service.EsBlogService;
import com.kkrepo.blog.service.TagService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 异步 ES Document 更新task
 * @author WangRuofei
 * @date 2020-11-23 8:46 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Component
public class AsyncEsDocumentTask {

    @Autowired
    private EsBlogService esBlogService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CommentService commentService;

    @Async
    public void updateBlogDocumentByArticle(Article article) {
        if (CommonStateEnum.PUBLISHED.getCode().equals(article.getState())) {
            esBlogService.upsertBlogDocument(coverToDocument(article));
        }
    }

    @Async
    public void deleteBlogDocumentByArticleId(String articleId) {
        esBlogService.deleteBlogDocument(articleId);
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
