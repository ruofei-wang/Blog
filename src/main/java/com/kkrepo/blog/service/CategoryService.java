package com.kkrepo.blog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kkrepo.blog.domain.Category;
import com.kkrepo.blog.domain.Category.Column;
import com.kkrepo.blog.domain.CategoryExample;
import com.kkrepo.blog.repository.CategoryMapper;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author WangRuofei
 * @create 2020-05-21 9:54 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Slf4j
@Service
public class CategoryService {

    @Resource
    private CategoryMapper mapper;

    public Category queryById(long id) {
        return mapper.selectByPrimaryKey(id);
    }

    public int deleteArticleCategoryByArticleId(long articleId) {
        return mapper.deleteArticleCategoryByArticleId(articleId);
    }

    public int addArticleCategory(long articleId, long catogoryId) {
        return mapper.addArticleCategory(articleId, catogoryId);
    }

    public List<Category> findAll() {
        return mapper.selectByExample(new CategoryExample());
    }

    public PageInfo<Category> list(Category category, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        CategoryExample example;
        if (category == null) {
            example = new CategoryExample().orderBy(Column.id.desc());
        } else {
            String name = category.getName();
            example = new CategoryExample()
                .createCriteria()
                .andIf(StringUtils.isNotBlank(name), criteria -> criteria.andNameLike("%" + name + "%"))
                .example()
                .orderBy(Column.id.desc());
        }
        List<Category> categoryList = mapper.selectByExample(example);
        return new PageInfo<>(categoryList);
    }

    public int save(Category category) {
        return mapper.insert(category);
    }

    public Category queryByName(String name) {
        CategoryExample example = new CategoryExample()
            .createCriteria()
            .andNameLike(name)
            .example();
        return mapper.selectOneByExample(example);
    }

    public int update(Category category) {
        return mapper.updateByPrimaryKey(category);
    }

    public int delete(Long id) {
        mapper.deleteArticleCategoryByCategoryId(id);
        return mapper.deleteByPrimaryKey(id);
    }

    public Category queryByArticle(Long articleId) {
        return mapper.queryByArticle(articleId);
    }
}
