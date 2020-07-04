package com.kkrepo.blog.common.enums;

import lombok.Getter;

/**
 * @author WangRuofei
 * @create 2020-05-25 4:29 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
public enum CommonStateEnum {

    /**
     * 初始
     */
    INIT("0"),
    /**
     * 已发布
     */
    PUBLISHED("1"),
    /**
     * 已删除
     */
    DELETED("2");

    @Getter
    private String code;

    CommonStateEnum(String code) {
        this.code = code;
    }

}
