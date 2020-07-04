package com.kkrepo.blog.support;

import lombok.Data;

@Data
public class R<T> {
    /**
     * 成功时：code =  200
     */
    protected int code;

    protected String msg;

    protected T data;

    public R(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
