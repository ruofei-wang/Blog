package com.kkrepo.blog.support;

import com.kkrepo.blog.common.exception.BaseException;
import com.kkrepo.blog.common.exception.ErrorCode;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * @author WangRuofei
 * @create 2020-05-26 10:28 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
public class ResultWrap {

    public static final int SUCCESS_CODE = 200;

    public static final String DATA_ITEMS = "items";

    private ResultWrap() {
    }

    public static <T> R<T> ok(T data) {
        return new R<>(SUCCESS_CODE, StringUtils.EMPTY, data);
    }

    public static <T> R<T> ok(int code, T data) {
        return new R<>(code, StringUtils.EMPTY, data);
    }

    public static <T> R<List<T>> ok(List<T> data) {
        return new R(SUCCESS_CODE, StringUtils.EMPTY, data);
    }

    public static R error(BaseException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return new R<>(errorCode.getCode(), errorCode.getMsg());
    }

    public static R error(ErrorCode errorCode) {
        return new R<>(errorCode.getCode(), errorCode.getMsg());
    }

    public static R error(int code, String msg) {
        return new R(code, msg);
    }

}
