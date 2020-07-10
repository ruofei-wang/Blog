package com.kkrepo.blog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kkrepo.blog.domain.Link;
import com.kkrepo.blog.domain.Link.Column;
import com.kkrepo.blog.domain.LinkExample;
import com.kkrepo.blog.repository.LinkMapper;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author WangRuofei
 * @create 2020-05-21 9:57 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Slf4j
@Service
public class LinkService {

    @Resource
    private LinkMapper mapper;

    public List<Link> queryAll() {
        return mapper.selectByExample(new LinkExample());
    }

    public PageInfo<Link> list(String name, String url, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        LinkExample example = new LinkExample()
            .createCriteria()
            .andIf(StringUtils.isNotBlank(name), x -> x.andNameEqualTo(name))
            .andIf(StringUtils.isNotBlank(url), x -> x.andUrlEqualTo(url))
            .example()
            .orderBy(Column.id.desc());
        List<Link> linkList = mapper.selectByExample(example);
        return new PageInfo<>(linkList);
    }

    public Link queryById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    public Link queryByName(String name) {
        LinkExample example = new LinkExample()
            .createCriteria()
            .andNameEqualTo(name)
            .example();
        return mapper.selectOneByExample(example);
    }

    public int save(Link link) {
        return mapper.insert(link);
    }

    public int update(Link link) {
        return mapper.updateByPrimaryKey(link);
    }

    public int delete(Long id) {
        return mapper.deleteByPrimaryKey(id);
    }
}
