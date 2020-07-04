package com.kkrepo.blog.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kkrepo.blog.common.annotation.LogAnnotation;
import com.kkrepo.blog.common.util.AddressUtil;
import com.kkrepo.blog.common.util.HttpContextUtil;
import com.kkrepo.blog.common.util.IPUtil;
import com.kkrepo.blog.common.util.JsonMapper;
import com.kkrepo.blog.domain.Log;
import com.kkrepo.blog.domain.User;
import com.kkrepo.blog.service.LogService;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 后端操作日志入库
 * @author WangRuofei
 * @create 2020-05-21 10:30 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Aspect
@Component
public class LogAspect {

    @Autowired
    private LogService logService;

    @Pointcut("@annotation(com.kkrepo.blog.common.annotation.LogAnnotation)")
    public void pointcut() {}

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user != null) {
            Log log = new Log();
            long beginTime = System.currentTimeMillis();
            // 获取reqeust 请求
            HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
            // 获取IP
            String ip = IPUtil.getIpAddr(request);
            log.setIp(ip);
            long interval = System.currentTimeMillis() - beginTime;
            log.setTime(interval);
            MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
            Method method = methodSignature.getMethod();
            LogAnnotation annotation = method.getAnnotation(LogAnnotation.class);
            if (annotation != null) {
                log.setOperation(annotation.value());
            }
            String className = proceedingJoinPoint.getTarget().getClass().getName();
            String methodName = methodSignature.getName();
            log.setMethod(className + "." + methodName + "()");
            Object[] args = proceedingJoinPoint.getArgs();
            LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
            String[] parameterNames = discoverer.getParameterNames(method);
            if (args != null && parameterNames != null) {
                StringBuilder params = new StringBuilder();
                params = handleParams(params, args, Arrays.asList(parameterNames));
                String str = params.toString();
                if (str.length() > 100) {
                    str = str.substring(0, 80) + "...";
                    log.setParams(str);
                }
            }
            log.setCreateTime(new Date());
            log.setLocation(AddressUtil.getAddress(log.getIp()));
            log.setUsername(user.getUsername());
            logService.add(log);
        }
        return result;
    }

    private StringBuilder handleParams(StringBuilder params, Object[] args, List<String> paramNames) throws JsonProcessingException {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Map) {
                Set set = ((Map) args[i]).keySet();
                List list = new ArrayList();
                List paramList = new ArrayList();
                for (Object key : set) {
                    list.add(((Map) args[i]).get(key));
                    paramList.add(key);
                }
                return handleParams(params, list.toArray(), paramList);
            } else {
                if (args[i] instanceof Serializable) {
                    Class<?> clazz = args[i].getClass();
                    try {
                        clazz.getDeclaredMethod("toString", new Class[]{null});
                        //如果不抛出NoSuchMethodException异常，则存在ToString方法
                        params.append(" ").append(paramNames.get(i)).append(JsonMapper.toJsonNotNull(args[i]));
                    } catch (NoSuchMethodException e) {
                        params.append(" ").append(paramNames.get(i)).append(JsonMapper.toJsonNotNull(args[i].toString()));
                    }
                } else if (args[i] instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) args[i];
                    params.append(" ").append(paramNames.get(i)).append(": ").append(file.getName());
                } else {
                    params.append(" ").append(paramNames.get(i)).append(": ").append(args[i]);
                }
            }
        }
        return params;
    }
}
