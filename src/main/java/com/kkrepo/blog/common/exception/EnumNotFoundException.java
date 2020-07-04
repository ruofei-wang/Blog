package com.kkrepo.blog.common.exception;

/**
 * @author WangRuofei
 * @create 2020-05-22 12:23 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
public class EnumNotFoundException extends RuntimeException {

    public EnumNotFoundException() {
        super();
    }

    public EnumNotFoundException(String message) {
        super(message);
    }
}
