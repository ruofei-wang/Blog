package com.kkrepo.blog.configuration.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 允许 HttpServletRequest InputStream 被读取多次
 * @author WangRuofei
 * @create 2020-06-30 6:11 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
public class CustomHttpServletRequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        CustomHttpServletRequestWrapper requestWrapper = new CustomHttpServletRequestWrapper((HttpServletRequest) request);
        chain.doFilter(requestWrapper, response);
    }
}
