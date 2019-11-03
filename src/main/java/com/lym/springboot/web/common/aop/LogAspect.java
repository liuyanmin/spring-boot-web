package com.lym.springboot.web.common.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lym.springboot.web.common.api.ApiCode;
import com.lym.springboot.web.common.api.ApiResult;
import com.lym.springboot.web.config.properties.SpringBootWebProperties;
import com.lym.springboot.web.core.dao.LogDao;
import com.lym.springboot.web.core.domain.WebLog;
import com.lym.springboot.web.util.HttpServletRequestUtil;
import com.lym.springboot.web.util.RequestHolder;
import com.lym.springboot.web.util.UriMatcherUtil;
import lombok.extern.log4j.Log4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 打印响应日志
 *
 * Created by liuyanmin on 2019/9/30.
 */
@Log4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private LogDao logDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SpringBootWebProperties springBootWebProperties;

    // 是否开启响应日志
    @Value("${spring-boot-web.interceptor-config.log-config.response.enabled}")
    private Boolean responseEnabled;


    @Pointcut("execution(public * com.lym.springboot.web.core.controller.*.*(..))")
    public void webLog() {
    }


    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        HttpServletRequest request = HttpServletRequestUtil.getRequest();
        String uri = request.getRequestURI();

        // 判断是否开启日志
        if (!springBootWebProperties.getInterceptorConfig().getLogConfig().isEnabled() || !responseEnabled) {
            return;
        }

        String[] includePaths = springBootWebProperties.getInterceptorConfig().getLogConfig().getIncludePath();
        String[] excludePaths = springBootWebProperties.getInterceptorConfig().getLogConfig().getExcludePath();
        // 判断URI是否匹配
        if (!UriMatcherUtil.matcher(includePaths, uri) || UriMatcherUtil.matcher(excludePaths, uri)) {
            return;
        }

        if (ret == null) {
            return;
        }

        boolean status = false;
        String result = ret.toString();
        if (result.startsWith("{") && result.endsWith("}")) {
            try {
                if (ret instanceof ApiResult) {
                    ApiResult apiResult = (ApiResult) ret;
                    if (apiResult.getCode() == ApiCode.SUCCESS.getCode()) {
                        status = true;
                    }
                }
                result = objectMapper.writeValueAsString(ret);
            } catch (JsonProcessingException e) {
            }
        }
        log.info("RESPONSE : " + result + "\n");

        // 更新响应时间
        Map<String, Object> threadLocal = RequestHolder.THREAD_LOCAL.get();
        if (threadLocal == null || threadLocal.get("logId") == null || threadLocal.get("startTime") == null) {
            return;
        }

        Integer logId = (Integer) threadLocal.get("logId");
        Long startTime = (Long) threadLocal.get("startTime");
        Long times = System.currentTimeMillis() - startTime;

        WebLog webLog = new WebLog();
        webLog.setId(logId);
        webLog.setTimes(times.intValue());
        webLog.setStatus(status);
        logDao.updateById(webLog);
    }

}
