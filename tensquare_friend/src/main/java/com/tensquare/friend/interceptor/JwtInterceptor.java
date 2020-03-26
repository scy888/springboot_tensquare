package com.tensquare.friend.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 请求头中的authorization字符串,鉴权判断
        //System.out.println("进入了JwtInterceptor...");
        String authorization = request.getHeader("Authorization");
        if(!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer ")){
            String token = authorization.substring(7);
            // 解析token
            Claims claims = null;
            try {
                claims = jwtUtil.parseJWT(token);

                // 判断不同的角色设置不同的key给user/admin controller使用
                String roles = (String)claims.get("roles");
                if("admin".equals(roles)){
                    // AdminController
                    request.setAttribute("admin_claims", claims);
                }else if("user".equals(roles)){
                    // UserController
                    request.setAttribute("user_claims", claims);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}
