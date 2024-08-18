package cn.service;

import cn.bo.CheckSocketTokenBo;
import cn.utils.JWTUtils;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class CheckSocketToken {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    public static RedisTemplate<String, String> redis;
    @PostConstruct
    public void getRedisTemplate() {
        redis = this.redisTemplate;
    }
    public CheckSocketTokenBo checkToken(String token ){
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        CheckSocketTokenBo checkSocketTokenBo = new CheckSocketTokenBo();
        String reason;
        try {
            String userId = JWTUtils.parseJWT(token);
            Set<String> test = redis.keys("*");
            assert test != null;
            String tokenExist = redis.opsForValue().get(userId);
            if(!Objects.equals(tokenExist, token)){
                reason ="被校验的token与redis中存储的token不一致";
                checkSocketTokenBo.setReason(reason);
                checkSocketTokenBo.setPass(false);

            }
            checkSocketTokenBo.setPass(true);
            checkSocketTokenBo.setReason("成功了不需要了理由");
        } catch (SignatureVerificationException e){
            reason = "无效签名";
            checkSocketTokenBo.setReason(reason);
            checkSocketTokenBo.setPass(false);
        } catch (TokenExpiredException e){
            reason = "token过期";
            checkSocketTokenBo.setReason(reason);
            checkSocketTokenBo.setPass(false);
        } catch (AlgorithmMismatchException e){
            reason = "无效签名";
            checkSocketTokenBo.setReason(reason);
            checkSocketTokenBo.setPass(false);
            //token算法不一致
        } catch (JWTDecodeException jwtDecodeException){
            checkSocketTokenBo.setReason("Token格式错误");
            checkSocketTokenBo.setPass(false);
        }

        return checkSocketTokenBo;
    }
}
