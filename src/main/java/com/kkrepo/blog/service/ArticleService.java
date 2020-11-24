package com.kkrepo.blog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kkrepo.blog.common.enums.CommonStateEnum;
import com.kkrepo.blog.domain.Article;
import com.kkrepo.blog.domain.Article.Column;
import com.kkrepo.blog.domain.ArticleExample;
import com.kkrepo.blog.repository.ArticleMapper;
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
public class ArticleService {

    @Resource
    private ArticleMapper mapper;

    public PageInfo<Article> queryByPage(int pageNum, int pageSize, CommonStateEnum commonStateEnum) {
        PageHelper.startPage(pageNum, pageSize);
        ArticleExample example = new ArticleExample()
            .createCriteria()
            .andStateEqualTo(commonStateEnum.getCode())
            .example()
            .orderBy(Column.id.desc());
        Column[] columns = new Column[]{
            Column.id,
            Column.title,
            Column.cover,
            Column.description,
        };
        List<Article> articleList = mapper.selectByExampleSelective(example, columns);
        return new PageInfo<>(articleList);
    }

    public List<Article> queryLatestArticles() {
        return mapper.selectLatestIdAndName(CommonStateEnum.PUBLISHED.getCode(), 8);
    }

    public Article queryById(long id) {
        return mapper.selectByPrimaryKey(id);
    }

    public List<String> queryArchivesDates(CommonStateEnum commonStateEnum) {
        return mapper.queryArchivesDates(commonStateEnum.getCode());
    }

    public List<Article> queryArchivesByDate(String publishTime, CommonStateEnum commonStateEnum) {
        return mapper.queryArchivesByDate(publishTime, commonStateEnum.getCode());
    }

    public long countAll() {
        return mapper.countByExample(new ArticleExample());
    }

    public PageInfo<Article> list(String title, String author, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        ArticleExample example = new ArticleExample()
            .createCriteria()
            .andIf(StringUtils.isNotBlank(title), criteria -> criteria.andTitleLike("%" + title + "%"))
            .andIf(StringUtils.isNotBlank(author), criteria -> criteria.andAuthorLike("%" + author + "%"))
            .example()
            .orderBy(Column.id.desc());
        List<Article> articleList = mapper.selectByExample(example);
        return new PageInfo<>(articleList);
    }

    public int insert(Article article) {
        return mapper.insert(article);
    }

    public int updateWithBLOBs(Article article) {
        return mapper.updateByPrimaryKeyWithBLOBs(article);
    }

    public int update(Article article) {
        return mapper.updateByPrimaryKeySelective(article);
    }

    public List<Article> queryByCategory(long categoryId) {
        return mapper.selectByCategory(categoryId);
    }

    public List<Article> queryByCategory(long categoryId, CommonStateEnum commonStateEnum) {
        return mapper.selectByCategoryAndState(categoryId,0, 1000, commonStateEnum.getCode());
    }

    public List<Article> queryByTag(long tagId) {
        return mapper.selectByTag(tagId);
    }

    public List<Article> queryByTag(long tagId, CommonStateEnum commonStateEnum) {
        return mapper.selectByTagAndState(tagId, 0, 1000, commonStateEnum.getCode());
    }

    public List<Article> queryAll() {
        return mapper.selectByExample(new ArticleExample().orderBy(Column.id.desc()));
    }

    public List<Article> queryAllWithBLOBs() {
        return mapper.selectByExampleWithBLOBs(new ArticleExample().orderBy(Column.id.desc()));
    }

    public PageInfo<Article> queryPageByCategory(int categoryId, int pageNum, int pageSize, CommonStateEnum commonStateEnum) {
        int total = mapper.countByCategoryAndState(categoryId, commonStateEnum.getCode());
        int offset = (pageNum - 1)*pageSize;
        List<Article> articleList = mapper.selectByCategoryAndState(categoryId, offset, pageSize, commonStateEnum.getCode());
        PageInfo pageInfo = new PageInfo(articleList);
        pageInfo.setPageSize(pageSize);
        pageInfo.setPageNum(pageNum);
        pageInfo.setTotal(total);
        pageInfo.setPages((int) Math.ceil((double) total/pageSize));
        return pageInfo;
    }

    public PageInfo<Article> queryPageByTag(int tagId, int pageNum, int pageSize, CommonStateEnum commonStateEnum) {
        int total = mapper.countByTagAndState(tagId, commonStateEnum.getCode());
        int offset = (pageNum - 1)*pageSize;
        List<Article> articleList = mapper.selectByTagAndState(tagId, offset, pageSize, commonStateEnum.getCode());
        PageInfo pageInfo = new PageInfo(articleList);
        pageInfo.setPageSize(pageSize);
        pageInfo.setPageNum(pageNum);
        pageInfo.setTotal(total);
        pageInfo.setPages((int) Math.ceil((double) total/pageSize));
        return pageInfo;
    }

    public PageInfo<Article> queryPageByAuthor(String author, int pageNum, int pageSize, CommonStateEnum commonStateEnum) {
        PageHelper.startPage(pageNum, pageSize);
        ArticleExample example = new ArticleExample()
            .createCriteria()
            .andAuthorEqualTo(author)
            .andStateEqualTo(commonStateEnum.getCode())
            .example()
            .orderBy(Column.id.desc());
        Column[] columns = new Column[]{
            Column.id,
            Column.title,
            Column.cover,
            Column.description,
        };
        List<Article> articleList = mapper.selectByExampleSelective(example, columns);
        return new PageInfo<>(articleList);
    }
}
