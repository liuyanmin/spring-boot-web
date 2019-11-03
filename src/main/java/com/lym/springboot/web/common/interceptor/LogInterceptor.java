package com.lym.springboot.web.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lym.springboot.web.common.constant.CommonConstant;
import com.lym.springboot.web.core.dao.LogDao;
import com.lym.springboot.web.core.domain.WebLog;
import com.lym.springboot.web.util.IpUtil;
import com.lym.springboot.web.util.RequestHolder;
import com.lym.springboot.web.util.io.HttpHelper;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 请求日志拦截器
 * Created by liuyanmin on 2019/9/30.
 */
@Log4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    @Autowired
    private LogDao logDao;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 不拦截OPTIONS请求
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        try {
            // 请求url
            String requestURL = request.getRequestURL().toString();
            // url请求参数
            String queryString = request.getQueryString();

            if (!StringUtils.isEmpty(queryString)) {
                requestURL = requestURL + "?" + queryString;
            }

            // 获取body请求数据
            String body = HttpHelper.getBodyString(request);
            if (StringUtils.isNotEmpty(body) && body.startsWith("{") && body.endsWith("}")) {
                JSONObject jsonObject = JSONObject.parseObject(body);
                body = jsonObject.toJSONString();
            }

            String ip = IpUtil.getRequestIp(request);
            String adminId = request.getAttribute(CommonConstant.ADMIN_ID) == null ? "" : request.getAttribute(CommonConstant.ADMIN_ID).toString();
            String method = request.getMethod();

            Map<String, Object> requestMap = new LinkedHashMap<>();
            requestMap.put("adminId", adminId);
            requestMap.put("ip", ip);
            requestMap.put("method", method);
            requestMap.put("url", requestURL);
            requestMap.put("args", body);
            log.info("REQUEST : " + objectMapper.writeValueAsString(requestMap));

            // 保存日志
            WebLog webLog = new WebLog();
            webLog.setIp(ip);
            webLog.setUserId(StringUtils.isNotEmpty(adminId) ? Integer.valueOf(adminId) : null);
            webLog.setUrl(requestURL);
            webLog.setMethod(method);
            webLog.setParams(body);
            logDao.insert(webLog);

            // 数据存入ThreadLocal
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("logId", webLog.getId());
            paramsMap.put("startTime", System.currentTimeMillis());
            RequestHolder.THREAD_LOCAL.set(paramsMap);
        } catch (Exception e) {
            log.error("打印请求日志异常: ", e);
        }
        return true;
    }

}
