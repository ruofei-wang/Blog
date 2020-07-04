package com.kkrepo.blog.manager;

import com.github.pagehelper.PageInfo;
import com.kkrepo.blog.common.Constant;
import com.kkrepo.blog.common.enums.CommentSortEnum;
import com.kkrepo.blog.common.model.Tree;
import com.kkrepo.blog.common.util.TreeUtil;
import com.kkrepo.blog.domain.Comment;
import com.kkrepo.blog.service.CommentService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * @author WangRuofei
 * @create 2020-05-25 10:11 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Slf4j
@Service
public class CommentManager {

    @Autowired
    private CommentService commentService;

    public void buidComments(Long articleId, int pageNum, CommentSortEnum commentSortEnum, Model model) {
        PageInfo<Tree<Comment>> pageInfo = new PageInfo<>();
        List<Comment> commentList = commentService.queryByArticleId(articleId, commentSortEnum);
        List<Tree<Comment>> trees = new ArrayList<>();
        commentList.forEach(c -> {
            Tree<Comment> tree = new Tree<>();
            tree.setId(c.getId());
            tree.setPId(c.getPId());
            tree.setAId(c.getArticleId());
            tree.setContent(c.getContent());
            tree.setName(c.getName());
            tree.setTarget(c.getCName());
            tree.setUrl(c.getUrl());
            tree.setDevice(c.getDevice());
            tree.setCreateTime(c.getCreateTime());
            tree.setSort(c.getSort());
            trees.add(tree);
        });
        List<Tree<Comment>> treeList = TreeUtil.build(trees);
        int total = treeList.size();
        if (treeList.size() == 0) {
            pageInfo.setList(treeList);
        } else {
            int start = (pageNum - 1) * Constant.COMMENT_PAGE_LIMIT;
            int end = pageNum * Constant.COMMENT_PAGE_LIMIT;
            if (end > total) {
                end = total;
            }
            pageInfo.setList(treeList.subList(start, end));
        }
        pageInfo.setTotal(commentList.size());
        pageInfo.setPageNum(pageNum);
        pageInfo.setPages((int) Math.ceil((double) treeList.size() / (double) Constant.COMMENT_PAGE_LIMIT));
        model.addAttribute(Constant.COMMENTS_MODEL, pageInfo);
    }

    public long countAll() {
        return commentService.countAll();
    }
}
