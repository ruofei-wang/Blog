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

    public PageInfo<Log> list(Log log, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        LogExample example;
        if (log == null) {
            example = new LogExample().orderBy(Column.id.desc());
        } else {
            example = new LogExample()
                .createCriteria()
                .andIf(StringUtils.isNotBlank(log.getUsername()), x -> x.andUsernameEqualTo(log.getUsername()))
                .andIf(StringUtils.isNotBlank(log.getOperation()), x -> x.andOperationEqualTo(log.getOperation()))
                .example()
                .orderBy(Column.id.desc());
        }
        List<Log> logList = mapper.selectByExample(example);
        return new PageInfo<>(logList);
    }
}
