package com.kkrepo.blog.controller.admin;

import com.kkrepo.blog.common.annotation.LogAnnotation;
import com.kkrepo.blog.domain.Tag;
import com.kkrepo.blog.service.TagService;
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
 * @create 2020-06-24 7:40 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@RestController
@RequestMapping("/api/admin/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/findAll")
    public R findAll() {
        return ResultWrap.ok(tagService.findAll());
    }

    @GetMapping("/list")
    public R list(
        Tag tag,
        @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
        @RequestParam(name = "pageSize", required = false, defaultValue = "100") Integer pageSize
    ) {
        return ResultWrap.ok(tagService.list(tag, pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public R findById(
        @PathVariable("id") Long id
    ) {
        return ResultWrap.ok(tagService.queryById(id));
    }

    @PostMapping
    @LogAnnotation("新增标签")
    public R save(
        @RequestBody Tag tag
    ) {
        Tag tagDb = tagService.queryByName(tag.getName());
        if (tagDb != null) {
            return ResultWrap.ok(tagDb.getId());
        }
        return ResultWrap.ok(tagService.save(tag));
    }

    @PutMapping
    @LogAnnotation("更新标签")
    public R update(
        @RequestBody Tag tag
    ) {
        return ResultWrap.ok(tagService.update(tag));
    }

    @DeleteMapping("{id}")
    @LogAnnotation("删除标签")
    public R delete(
        @PathVariable Long id
    ) {
        return ResultWrap.ok(tagService.delete(id));
    }
}
