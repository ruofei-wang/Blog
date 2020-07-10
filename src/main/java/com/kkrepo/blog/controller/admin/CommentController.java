package com.kkrepo.blog.controller.admin;

import com.kkrepo.blog.common.annotation.LogAnnotation;
import com.kkrepo.blog.domain.Comment;
import com.kkrepo.blog.service.CommentService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WangRuofei
 * @create 2020-06-24 7:39 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@RestController
@RequestMapping("/api/admin/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/list")
    public R list(
        @RequestParam(name = "name", required = false) String name,
        @RequestParam(name = "url", required = false) String url,
        @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
        @RequestParam(name = "pageSize", required = false, defaultValue = "100") Integer pageSize
    ) {
        return ResultWrap.ok(commentService.list(name, url, pageNum, pageSize));
    }

    @GetMapping("/chart")
    public R chart() {
        return ResultWrap.ok(commentService.chart());
    }

    @GetMapping("/{id}")
    public R findById(
        @PathVariable("id") Long id
    ) {
        return ResultWrap.ok(commentService.queryById(id));
    }

    @PostMapping
    @LogAnnotation("新增友链")
    public R save(
        @RequestBody Comment comment
    ) {
        Comment commentDb = commentService.queryByName(comment.getName());
        if (commentDb != null) {
            return ResultWrap.ok(commentDb.getId());
        }
        return ResultWrap.ok(commentService.add(comment));
    }

    @PutMapping
    @LogAnnotation("更新友链")
    public R update(
        @RequestBody Comment comment
    ) {
        return ResultWrap.ok(commentService.update(comment));
    }

    @DeleteMapping("{id}")
    @LogAnnotation("删除友链")
    public R delete(
        @PathVariable Long id
    ) {
        return ResultWrap.ok(commentService.delete(id));
    }
}
