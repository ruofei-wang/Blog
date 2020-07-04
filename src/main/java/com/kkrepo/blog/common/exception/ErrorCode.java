package com.kkrepo.blog.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author WangRuofei
 * @create 2020-05-22 12:21 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    LOGIN_ERROR(500, "用户名或密码错误"),
    PARAM_ERROR(401, "参数错误"),
    USER_ERROR(500, "获取用户信息失败"),
    LOGOUT_ERROR(500, "退出失败"),
    SYSTEM_ERROR(500, "系统内部错误"),
    COMMON_ERROR(500, "系统内部错误"),
    FILE_ERROR(400, "上传的文件为空"),
    NOT_EXIST_ERROR(404, "对象不存在"),
    COMMON_SUCCESS(200, "成功"),
    RESET_SUCCESS(200, "重置密码成功");


    private int code;
    @Setter
    private String msg;

    public static ErrorCode ofCode(int code) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (code == errorCode.getCode()) {
                return errorCode;
            }
        }
        throw new EnumNotFoundException();
    }

    public static ErrorCode ofCodeNotException(int code) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (code == errorCode.getCode()) {
                return errorCode;
            }
        }
        return null;
    }
}
