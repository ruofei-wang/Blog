package com.kkrepo.blog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kkrepo.blog.domain.Tag;
import com.kkrepo.blog.domain.Tag.Column;
import com.kkrepo.blog.domain.TagExample;
import com.kkrepo.blog.repository.TagMapper;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author WangRuofei
 * @create 2020-05-21 10:00 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Slf4j
@Service
public class TagService {

    @Resource
    private TagMapper mapper;

    public List<Tag> queryByArticleId(long articleId) {
        return mapper.queryByArticleId(articleId);
    }

    public int deleteArticleTagByArticleId(long articleId) {
        return mapper.deleteArticleTagByArticleId(articleId);
    }

    public int addArticleTag(long articleId, long tagId) {
        return mapper.addArticleCategory(articleId, tagId);
    }

    public PageInfo<Tag> list(Tag tag, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        TagExample example;
        if (tag == null) {
            example = new TagExample().orderBy(Column.id.desc());
        } else {
            example = new TagExample()
                .createCriteria()
                .andIf(StringUtils.isNotBlank(tag.getName()), x -> x.andNameEqualTo(tag.getName()))
                .example()
                .orderBy(Column.id.desc());
        }
        List<Tag> tagList = mapper.selectByExample(example);
        return new PageInfo<>(tagList);
    }

    public Tag queryById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    public Tag queryByName(String name) {
        TagExample example = new TagExample()
            .createCriteria()
            .andNameEqualTo(name)
            .example();
        return mapper.selectOneByExample(example);
    }

    public int save(Tag tag) {
        return mapper.insert(tag);
    }

    public int update(Tag tag) {
        return mapper.updateByPrimaryKey(tag);
    }

    public int delete(Long id) {
        mapper.deleteArticleTagByTagId(id);
        return mapper.deleteByPrimaryKey(id);
    }

    public List<Tag> findAll() {
        return mapper.selectByExample(new TagExample());
    }

    public long countAll() {
        return mapper.countByExample(new TagExample());
    }
}
