package com.kkrepo.blog.support;

import com.kkrepo.blog.common.util.GetRequestJsonUtils;
import com.kkrepo.blog.common.util.JsonMapper;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
@ConditionalOnExpression("${api.log.enabled:true}")
public class APILogAspect {

    @Around("execution(* com.kkrepo.blog.controller.*.*(..)) || execution(* com.kkrepo.blog.controller.admin.*.*(..))")
    private Object controllerAspect(ProceedingJoinPoint pjp) throws Throwable {
        long nanoTime = System.nanoTime();
        HttpServletRequest request = getRequest();
        String uri = request != null ? request.getRequestURI() : "";
//        String body = GetRequestJsonUtils.getRequestJsonString(request);
        log.info("nanoTime:{}|url:{}|start|request: {}", nanoTime, uri, getParamsMap(request));

        Object result = pjp.proceed();
        if(result instanceof R) {
            R response = (R) result;
            ErrorCodeAspectHandle.handle(response, request);
            log.info("nanoTime:{}|url:{}|end|response: {}", nanoTime, uri, JsonMapper.nonEmptyMapper().toJson(response));
            return response;
        }
        return result;
    }

    private HttpServletRequest getRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        return ra == null ? null : ((ServletRequestAttributes) ra).getRequest();
    }

    private Map<String, String> getParamsMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        if (request == null || request.getParameterMap() == null) {
            return map;
        }
        Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.forEach((key, values) -> {
            String value = values == null ? "" : String.join(",", values);
            map.put(key, value);
        });
        return map;
    }

}
