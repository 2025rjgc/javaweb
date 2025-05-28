//package com.example.demo.filter;
//
//import io.jsonwebtoken.Claims;
//import lombok.extern.slf4j.Slf4j;
//
//import javax.servlet.*;
//import javax.servlet.ServletRequest;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import com.example.demo.utils.JWTUtils;
//
//@Slf4j
//@WebFilter(urlPatterns = "/*") //拦截请求
//public class TokenFilter implements Filter {
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        //获取请求路径
//        String path = request.getRequestURI();
//        //判断是否是登录请求，包含/login，则放行
//        if (path.contains("/login")){
//            log.info("登录请求，放行");
//            filterChain.doFilter(request,response);//放行
//            return;
//        }
//        //获取请求头中的token
//        String token = request.getHeader("token");
//        //判断token是否为空，为空则返回401
//        if (token == null || token.isEmpty()){
//            log.info("令牌为空，返回401");
//            response.setStatus(401);
//            return;
//        }
//        //token存在，校验token是否合法，不合法则返回401
//        try {
//            Claims claims = JWTUtils.verifyJWT(token);
//            Integer empid =Integer.valueOf(claims.get("id").toString());
//            log.info("当前ID为：{}",empid);
//        }catch (Exception e){
//            log.info("令牌非法，返回401");
//            response.setStatus(401);
//            return;
//        }
//        //token合法，放行
//        log.info("令牌合法，放行");
//        filterChain.doFilter(request,response);
//    }
//}
