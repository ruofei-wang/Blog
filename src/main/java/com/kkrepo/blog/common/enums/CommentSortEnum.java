package com.kkrepo.blog.common.enums;

import lombok.Getter;

/**
 * @author WangRuofei
 * @create 2020-05-25 4:29 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
public enum CommentSortEnum {

    /**
     * 文件评论
     */
    DEFAULT(0L),
    /**
     * 友链页评论
     */
    FRIENDURL(1L),
    /**
     * 关于页评论
     */
    ABOUT(2L);

    @Getter
    private long code;

    CommentSortEnum(long code) {
        this.code = code;
    }

}
