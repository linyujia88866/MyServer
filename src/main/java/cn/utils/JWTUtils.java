package cn.utils;

import cn.entity.User4Token;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;

/**
 * @author admin
 */
public class JWTUtils {

    //解析token的秘钥
    public static String password = "liyannoly";
    /**
     * 获取token
     * @param u user
     * @return token
     */
    public static String getToken(User4Token u) {
        Calendar instance = Calendar.getInstance();
        //默认令牌过期时间7天
        instance.add(Calendar.DATE, 3);

        //把用户id保存到subject变量，也可以使用.withClaim("userId", 123)
        JWTCreator.Builder builder = JWT.create();
//        builder.withSubject(u.getUser());
        builder.withClaim("userId", u.getUser());

        return builder.withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(password));
    }

    /**
     * 验证token合法性 成功返回token
     */
    public static DecodedJWT verify(String token){
        return JWT.require(Algorithm.HMAC256(password)).build().verify(token);
    }

    /**
     * 解析Jwt字符串
     *
     * @param token Jwt字符串
     * @return Claims 解析后的对象
     */
    public static String parseJWT(String token) {
        return JWT.require(Algorithm.HMAC256(password)).build().verify(token).getClaim("userId").asString();
    }
}