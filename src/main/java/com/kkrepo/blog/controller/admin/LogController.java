package com.kkrepo.blog.controller.admin;

import com.kkrepo.blog.domain.Log;
import com.kkrepo.blog.service.LogService;
import com.kkrepo.blog.support.R;
import com.kkrepo.blog.support.ResultWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WangRuofei
 * @create 2020-06-24 7:55 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@RestController
@RequestMapping("/api/admin/log")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping("/list")
    public R list(
        Log log,
        @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
        @RequestParam(name = "pageSize", required = false, defaultValue = "100") Integer pageSize
    ) {
        return ResultWrap.ok(logService.list(log, pageNum, pageSize));
    }
}
