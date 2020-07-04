package com.kkrepo.blog.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author WangRuofei
 * @create 2020-06-27 9:23 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QiNiuEntity {

    /**
     * 对象KEY
     */
    private String key;

    /**
     * 对象名称
     */
    private String name;

    /**
     * 对象类型
     */
    private String type;

    /**
     * 对象大小
     */
    private long size;

    /**
     * 对象链接
     */
    private String url;
}
