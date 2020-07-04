package com.kkrepo.blog.support;

import com.google.common.collect.Maps;
import com.kkrepo.blog.common.exception.BaseException;
import com.kkrepo.blog.common.util.JsonMapper;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author WangRuofei
 * @create 2020-05-22 11:30 上午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@ControllerAdvice
public class ResultExceptionHandler extends ResponseEntityExceptionHandler {

    private JsonMapper jsonMapper = new JsonMapper();

    @ExceptionHandler(value = {RuntimeException.class})
    public final ResponseEntity handleException(BaseException exception, HttpServletRequest request) {
        logError(exception, request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        R result = new R(exception.getErrorCode().getCode(), exception.getErrorCode().getMsg());
        ErrorCodeAspectHandle.handle(result, request);
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    private void logError(Exception ex) {
        Map<String, String> map = Maps.newHashMap();
        map.put("message", ex.getMessage());
        logger.error(jsonMapper.toJson(map), ex);
    }

    private void logError(Exception ex, HttpServletRequest request) {
        Map<String, String> map = Maps.newHashMap();
        map.put("message", ex.getMessage());
        map.put("remote_addr", request.getRemoteAddr());
        map.put("path", request.getRequestURI());
        String queryString = JsonMapper.nonEmptyMapper().toJson(request.getParameterMap());
        map.put("params", queryString);
        logger.error(jsonMapper.toJson(map), ex);
    }
}
