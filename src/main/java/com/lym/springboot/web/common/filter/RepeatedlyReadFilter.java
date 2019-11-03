package com.lym.springboot.web.common.filter;

import com.lym.springboot.web.util.io.BodyReaderHttpServletRequestWrapper;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 包装POST请求体输入流，只包装POST请求
 * Created by liuyanmin on 2019/9/30.
 */
@Component
public class RepeatedlyReadFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            // 遇到post方法才对request进行包装
            String methodType = httpRequest.getMethod();
            if ("POST".equals(methodType)) {
                requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) request);
            }
        }
        if (requestWrapper == null) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, response);
        }
    }

    @Override
    public void destroy() {
    }

}
