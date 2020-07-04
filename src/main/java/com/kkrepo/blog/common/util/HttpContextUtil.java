package com.kkrepo.blog.common.util;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author WangRuofei
 * @create 2020-05-21 10:46 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
public class HttpContextUtil {

    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

}
