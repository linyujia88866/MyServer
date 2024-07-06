package cn.aqjyxt.Interceptor;

import cn.aqjyxt.bean.JWTUtils;
import cn.aqjyxt.bean.Returnben;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JWTInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(request.getRequestURI());
        Returnben returnben = new Returnben();
        Cookie[] cookies = request.getCookies();
        String token="";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    // 这里可以对token进行处理，例如验证
                    System.out.println("Token from Cookie: " + token);
                }
            }
        }
        try {
            //验证令牌
            DecodedJWT verify = JWTUtils.verify(token);
            return true;
        } catch (SignatureVerificationException e){
            e.printStackTrace();
            returnben.setMsg("无效签名");
            returnben.setSuccess("10001");
        } catch (TokenExpiredException e){
            e.printStackTrace();
            returnben.setMsg("token过期");
            returnben.setSuccess("10002");
        } catch (AlgorithmMismatchException e){
            e.printStackTrace();
            //token算法不一致
            returnben.setMsg("无效签名");
            returnben.setSuccess("10001");
        } catch (Exception e){
            e.printStackTrace();
            returnben.setMsg("token无效");
            returnben.setSuccess("10003");
        }
        //将map转为json
        String json = new ObjectMapper().writeValueAsString(returnben);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 进行清理工作，不要修改response状态
    }

}