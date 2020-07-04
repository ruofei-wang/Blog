package com.kkrepo.blog.controller.admin;

import com.kkrepo.blog.common.annotation.LogAnnotation;
import com.kkrepo.blog.domain.User;
import com.kkrepo.blog.service.UserService;
import com.kkrepo.blog.support.R;
import com.kkrepo.blog.support.ResultWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WangRuofei
 * @create 2020-06-27 10:02 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@RestController
@RequestMapping("/api/admin/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public R getInfo() {
        return ResultWrap.ok(getCurrentUser());
    }

    @GetMapping("/chart")
    public R chart() {
        return ResultWrap.ok(userService.chart());
    }

    @GetMapping("/{id}")
    public R findById(
        @PathVariable("id") Long id
    ) {
        return ResultWrap.ok(userService.queryById(id));
    }

    @PostMapping
    @LogAnnotation("新增用户")
    public R save(
        @RequestBody User user
    ) {
        User userDb = userService.findByName(user.getUsername());
        if (userDb != null) {
            return ResultWrap.ok(userDb.getId());
        }
        return ResultWrap.ok(userService.save(user));
    }

    @PutMapping
    @LogAnnotation("更新用户")
    public R update(
        @RequestBody User user
    ) {
        return ResultWrap.ok(userService.update(user));
    }

    @DeleteMapping("{id}")
    @LogAnnotation("删除用户")
    public R delete(
        @PathVariable Long id
    ) {
        return ResultWrap.ok(userService.delete(id));
    }
}
