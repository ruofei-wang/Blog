package com.kkrepo.blog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kkrepo.blog.domain.Log;
import com.kkrepo.blog.domain.Log.Column;
import com.kkrepo.blog.domain.LogExample;
import com.kkrepo.blog.repository.LogMapper;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author WangRuofei
 * @create 2020-05-21 9:58 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Slf4j
@Service
public class LogService {

    @Resource
    private LogMapper mapper;


    public int add(Log log) {
        return mapper.insert(log);
    }

    public PageInfo<Log> list(String username, String operation, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        LogExample example = new LogExample()
            .createCriteria()
            .andIf(StringUtils.isNotBlank(username), x -> x.andUsernameEqualTo(username))
            .andIf(StringUtils.isNotBlank(operation), x -> x.andOperationEqualTo(operation))
            .example()
            .orderBy(Column.id.desc());
        List<Log> logList = mapper.selectByExample(example);
        return new PageInfo<>(logList);
    }
}
