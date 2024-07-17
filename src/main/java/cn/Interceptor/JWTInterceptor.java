package cn.Interceptor;

import cn.utils.JWTUtils;
import cn.entity.ReturnEntity;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static cn.utils.requestUtils.getTokenFromRequest;

@Slf4j
@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        log.info("request url is {}", request.getRequestURI());
        ReturnEntity returnEntity = new ReturnEntity();
        String token = getTokenFromRequest(request);
        try {
            String userId = JWTUtils.parseJWT(token);
            String tokenStored = redisTemplate.opsForValue().get(userId);

            if(!Objects.equals(tokenStored, token)){
                throw new Exception("被校验的token与redis中存储的token不一致");
            }
            redisTemplate.expire(userId, 30, TimeUnit.MINUTES);
            redisTemplate.expire(userId + "_auth", 30, TimeUnit.MINUTES);
            return true;
        } catch (SignatureVerificationException e){
            log.info("SignatureVerificationException: {}", e.getMessage());
            returnEntity.setMsg("无效签名");
            returnEntity.setSuccess("10001");
        } catch (TokenExpiredException e){
            log.info("TokenExpiredException: {}", e.getMessage());
            returnEntity.setMsg("token过期");
            returnEntity.setSuccess("10002");
        } catch (AlgorithmMismatchException e){
            log.info("AlgorithmMismatchException: {}", e.getMessage());
            //token算法不一致
            returnEntity.setMsg("无效签名");
            returnEntity.setSuccess("10001");
        } catch (Exception e){
            log.info("Other exception: {}", e.getMessage());
            returnEntity.setMsg("token无效");
            returnEntity.setSuccess("10003");
        }
        //将map转为json
        String json = new ObjectMapper().writeValueAsString(returnEntity);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        // 进行清理工作，不要修改response状态
    }

}