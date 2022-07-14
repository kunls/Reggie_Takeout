package com.lxk.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.lxk.reggie.common.BaseContext;
import com.lxk.reggie.common.R;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
        if (check(urls, requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getSession().getAttribute("employee") != null) {
            long empId = (long) request.getSession().getAttribute("employee");
            BaseContext.set(empId);
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getSession().getAttribute("user") != null) {
            long userId = (long) request.getSession().getAttribute("user");
            BaseContext.set(userId);
            filterChain.doFilter(request, response);
            return;
        }
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            if (antPathMatcher.match(url, requestURI)) {
                return true;
            }
        }
        return false;
    }
}
