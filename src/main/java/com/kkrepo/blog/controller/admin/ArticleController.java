package com.kkrepo.blog.controller.admin;

import com.kkrepo.blog.common.annotation.LogAnnotation;
import com.kkrepo.blog.common.model.ArchivesWithArticle;
import com.kkrepo.blog.common.model.ArticleModel;
import com.kkrepo.blog.domain.Article;
import com.kkrepo.blog.domain.Category;
import com.kkrepo.blog.domain.Tag;
import com.kkrepo.blog.manager.ArticleManager;
import com.kkrepo.blog.service.CategoryService;
import com.kkrepo.blog.service.TagService;
import com.kkrepo.blog.support.R;
import com.kkrepo.blog.support.ResultWrap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WangRuofei
 * @create 2020-05-27 5:18 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@RestController
@RequestMapping("/api/admin/article")
public class ArticleController extends BaseController {

    @Autowired
    private ArticleManager articleManager;

    @GetMapping("/count")
    public R count() {
        return ResultWrap.ok(articleManager.countAll());
    }

    @GetMapping("/list")
    public R list(
        @RequestBody(required = false) Article article,
        @RequestParam(name = "pageNum", required = false) Integer pageNum,
        @RequestParam(name = "pageSize", required = false) Integer pageSize
    ) {
        return ResultWrap.ok(articleManager.list(article, pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public R<ArticleModel> queryById(
        @PathVariable Long id
    ) {
        return ResultWrap.ok(articleManager.queryById(id));
    }

    @GetMapping("/queryByCategory/{categoryId}")
    public R queryByCategory(
        @PathVariable Long categoryId
    ) {
        return ResultWrap.ok(articleManager.queryByCategory(categoryId, null));
    }

    @GetMapping("/queryByTag/{tagId}")
    public R queryByTag(
        @PathVariable Long tagId
    ) {
        return ResultWrap.ok(articleManager.queryByTag(tagId, null));
    }

    @PostMapping
    @LogAnnotation("新增博文")
    public R save(
        @RequestBody ArticleModel articleModel
    ) {
        return ResultWrap.ok(articleManager.add(articleModel, getCurrentUser()));
    }

    @PutMapping
    @LogAnnotation("更新博文")
    public R update(
        @RequestBody ArticleModel articleModel
    ) {
        return ResultWrap.ok(articleManager.update(articleModel, getCurrentUser()));
    }

    @DeleteMapping("/{id}")
    @LogAnnotation("删除博文")
    public R delete(
        @PathVariable Long id
    ) {
        return ResultWrap.ok(articleManager.delete(id));
    }

    @GetMapping("/publish/{id}")
    @LogAnnotation("发布博文")
    public R publish(
        @PathVariable Long id
    ) {
        return ResultWrap.ok(articleManager.publish(id));
    }

}
