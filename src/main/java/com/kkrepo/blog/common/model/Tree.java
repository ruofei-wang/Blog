package com.kkrepo.blog.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author WangRuofei
 * @create 2020-05-25 10:04 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Data
public class Tree<T> {

    /**
     * 节点ID
     */
    private Long id;

    /**
     * 父节点ID
     */
    private Long pId;

    /**
     * 文章ID
     */
    private Long aId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论人
     */
    private String name;

    /**
     * 给谁评论
     */
    private String target;

    /**
     * 设备
     */
    private String device;

    /**
     * URL 地址
     */
    private String url;

    /**
     * 评论时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 评论分类
     */
    private Long sort;

    /**
     * 所有子级回复、评论列表
     */
    private List<Tree<T>> children = new ArrayList<>();
}
