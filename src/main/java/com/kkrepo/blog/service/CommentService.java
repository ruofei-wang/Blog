package com.kkrepo.blog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kkrepo.blog.common.enums.CommentSortEnum;
import com.kkrepo.blog.common.enums.CommonStateEnum;
import com.kkrepo.blog.common.model.SplineChart;
import com.kkrepo.blog.domain.Comment;
import com.kkrepo.blog.domain.Comment.Column;
import com.kkrepo.blog.domain.CommentExample;
import com.kkrepo.blog.repository.CommentMapper;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author WangRuofei
 * @create 2020-05-21 9:56 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Slf4j
@Service
public class CommentService {

    @Resource
    private CommentMapper mapper;


    public List<Comment> queryLatestArticles() {
        return mapper.queryLatestArticles(CommonStateEnum.PUBLISHED.getCode(), 8);
    }

    public List<Comment> queryByArticleId(Long articleId, CommentSortEnum commentSortEnum) {
        CommentExample example = new CommentExample()
            .createCriteria()
            .andIf(articleId != null, criteria -> criteria.andArticleIdEqualTo(articleId))
            .andSortEqualTo(commentSortEnum.getCode())
            .andStateEqualTo(CommonStateEnum.PUBLISHED.getCode())
            .example()
            .orderBy(Column.id.desc());
        return mapper.selectByExampleWithBLOBs(example);
    }

    public int add(Comment comment) {
        return mapper.insertSelective(comment);
    }

    public long countAll() {
        return mapper.countByExample(new CommentExample());
    }

    public PageInfo<Comment> list(String name, String url, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        CommentExample example = new CommentExample()
            .createCriteria()
            .andIf(StringUtils.isNotBlank(name), x -> x.andNameEqualTo(name))
            .andIf(StringUtils.isNotBlank(url), x -> x.andUrlEqualTo(url))
            .example()
            .orderBy(Column.id.desc());
        List<Comment> commentList = mapper.selectByExampleWithBLOBs(example);
        return new PageInfo<>(commentList);
    }

    public Comment queryById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    public Comment queryByName(String name) {
        CommentExample example = new CommentExample()
            .createCriteria()
            .andNameEqualTo(name)
            .example();
        return mapper.selectOneByExample(example);
    }

    public int update(Comment comment) {
        return mapper.updateByPrimaryKey(comment);
    }

    public int delete(Long id) {
        return mapper.deleteByPrimaryKey(id);
    }

    public List<Object[]> chart() {
        List<Object[]> chartList = new ArrayList<>();
        List<SplineChart> splineChartList = mapper.chart();
        for (SplineChart splineChart : splineChartList) {
            Object[] item = new Object[]{splineChart.getDayTime().getTime() - 8*3600000 , splineChart.getNum()};
            chartList.add(item);
        }
        return chartList;
    }
}
