package com.kkrepo.blog.support;

import com.kkrepo.blog.common.exception.ErrorCode;
import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public class ErrorCodeAspectHandle {

    public static void handle(R response, HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.ofCodeNotException(response.getCode());
        if (errorCode != null) {
            response.setMsg(errorCode.getMsg());
        }
    }
}
