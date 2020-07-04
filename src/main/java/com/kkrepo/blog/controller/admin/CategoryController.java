package com.kkrepo.blog.controller.admin;

import com.kkrepo.blog.common.annotation.LogAnnotation;
import com.kkrepo.blog.domain.Article;
import com.kkrepo.blog.domain.Category;
import com.kkrepo.blog.manager.ArticleManager;
import com.kkrepo.blog.service.CategoryService;
import com.kkrepo.blog.support.R;
import com.kkrepo.blog.support.ResultWrap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * @create 2020-05-30 9:21 上午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@RestController
@RequestMapping("/api/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/findAll")
    public R findAll() {
        return ResultWrap.ok(categoryService.findAll());
    }

//    @GetMapping("/findArticleCountForCategory")
//    public R findArticleCountForCategory() {
//        Map<String, Object> map = new HashMap<>();
//        List<Category> categoryList = categoryService.findAll();
//        List<Article> articleList = articleManager.queryAll();
//        Map<String, List<Article>> articleGroupByMap = articleList.stream().collect(Collectors.groupingBy(Article::getCategory));
//        categoryList.forEach(x -> {
//            List<Article> groupByArticleList = articleGroupByMap.get(x.getId());
//            int size = groupByArticleList == null ? 0 : groupByArticleList.size();
//            map.put(x.getName(), size);
//        });
//        return ResultWrap.ok(map);
//    }

    @GetMapping("/list")
    public R list(
        Category category,
        @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
        @RequestParam(name = "pageSize", required = false, defaultValue = "100") Integer pageSize
    ) {
        return ResultWrap.ok(categoryService.list(category, pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public R findById(
        @PathVariable("id") Long id
    ) {
        return ResultWrap.ok(categoryService.queryById(id));
    }

    @PostMapping
    @LogAnnotation("新增分类")
    public R save(
        @RequestBody Category category
    ) {
        Category categoryDb = categoryService.queryByName(category.getName());
        if (categoryDb != null) {
            return ResultWrap.ok(categoryDb.getId());
        }
        return ResultWrap.ok(categoryService.save(category));
    }

    @PutMapping
    @LogAnnotation("更新分类")
    public R update(
        @RequestBody Category category
    ) {
        return ResultWrap.ok(categoryService.update(category));
    }

    @DeleteMapping("{id}")
    @LogAnnotation("删除分类")
    public R delete(
        @PathVariable Long id
    ) {
        return ResultWrap.ok(categoryService.delete(id));
    }

}
