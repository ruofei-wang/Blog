package com.kkrepo.blog.service;

import com.kkrepo.blog.common.model.SplineChart;
import com.kkrepo.blog.domain.User;
import com.kkrepo.blog.domain.UserExample;
import com.kkrepo.blog.repository.UserMapper;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author WangRuofei
 * @create 2020-05-21 10:01 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Slf4j
@Service
public class UserService {

    @Resource
    private UserMapper mapper;

    public User findByName(String username) {
        UserExample example = new UserExample()
            .createCriteria()
            .andUsernameEqualTo(username)
            .example();
        return mapper.selectOneByExample(example);
    }

    public User queryById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    public int save(User link) {
        return mapper.insert(link);
    }

    public int update(User link) {
        return mapper.updateByPrimaryKey(link);
    }

    public int delete(Long id) {
        return mapper.deleteByPrimaryKey(id);
    }

    public long countAll() {
        return mapper.countByExample(new UserExample());
    }

    public List<Object[]> chart() {
        List<Object[]> chartList = new ArrayList<>();
        List<SplineChart> splineChartList = mapper.chart();
        for (SplineChart splineChart : splineChartList) {
            Object[] item = new Object[]{splineChart.getDayTime().getTime() - 8*3600000, splineChart.getNum()};
            chartList.add(item);
        }
        return chartList;
    }
}
