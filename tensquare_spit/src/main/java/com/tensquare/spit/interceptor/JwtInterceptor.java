package com.tensquare.spit.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("经过了拦截器");
       /* //获取前端传过来的token
        String authorization = request.getHeader("Authorization");
        //我们约定好的格式:"Bearer "+token
        if (authorization==null||!authorization.startsWith("Bearer ")){
            throw new RuntimeException("权限不足,无法操作.....");
        }
        //提取token
        String token = authorization.substring(7);
        //解析token
        Claims claims = jwtUtil.parseJWT(token);
         if (claims==null){
             throw new RuntimeException("权限不足,无法操作.....");
         }
        String roles = (String) claims.get("roles");
         if ("admin".equals(roles)){
             request.setAttribute("admin", claims);
         }
         if ("user".equals(roles)){
             request.setAttribute("user", claims);
         }*/
       //上面的拦截方式是所有的用户操作都需要登录才能完成功能,权限在拦截器里判断
        String authorization = request.getHeader("Authorization");
        if (authorization!=null && authorization.startsWith("Bearer ") ){
            String token = authorization.substring(7);
            try {
                Claims claims = jwtUtil.parseJWT(token);
                String roles = (String) claims.get("roles");
                if ("admin".equals(roles)){
                  request.setAttribute("admin",claims );
                }
                else if ("user".equals(roles)){
                    request.setAttribute("user",claims );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
