package cn.controller;

import cn.utils.JWTUtils;
import cn.entity.ReturnEntity;
import cn.dto.UserDto;
import cn.entity.User4Token;
import cn.entity.User;
import cn.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static cn.utils.requestUtils.getTokenFromRequest;

@RestController
@Slf4j
@RequestMapping(value = "/api")
public class MyController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public ReturnEntity login(HttpServletResponse response, @RequestBody UserDto userDto ) {
        ReturnEntity returnEntity = new ReturnEntity();
        String user = userDto.getUser();
        String password = userDto.getPassword();
        User userFromDataBase;
        try {
            userFromDataBase = userService.findByUser(user);
            if(!Objects.equals(password, userFromDataBase.getPassword())){
                returnEntity.setMsg("账号或密码错误");
                returnEntity.setSuccess("500");
                return returnEntity;
            }
        } catch (Exception e) {
            returnEntity.setMsg("账号或密码错误");
            returnEntity.setSuccess("500");
            return returnEntity;
        }

        User4Token User4Token = new User4Token();
        User4Token.setUser(user);
        User4Token.setPassword(password);
        String token = JWTUtils.getToken(User4Token);
        String auth = String.valueOf(userFromDataBase.getAuthority());
        returnEntity.setData(auth);
        returnEntity.setMsg("登录成功");
        returnEntity.setSuccess("200");
        // 存储Token到Redis，假设用户名作为key
        redisTemplate.opsForValue().set(user, token, 60, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(user + "_auth", auth, 60, TimeUnit.MINUTES);

        // 创建一个cookie
        Cookie myCookie = new Cookie("token", token);

        // 设置cookie的有效期，例如一个小时(3600秒)
        myCookie.setMaxAge(3600);

        // 设置cookie的路径，这样只有访问这个路径时才会发送cookie
        // 设置cookie到response中
        response.addCookie(myCookie);
        log.info("set token to cookies");
        return returnEntity;

    }

    @PostMapping("/verify")
    @ResponseBody
    public ReturnEntity verify(HttpServletRequest request, HttpSession session) {
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        String authority = redisTemplate.opsForValue().get(username + "_auth");

        ReturnEntity returnEntity = new ReturnEntity();
        returnEntity.setData(authority);
        returnEntity.setMsg("成功");
        returnEntity.setSuccess("200");
        return returnEntity;
    }

    @PostMapping("/logout")
    @ResponseBody
    public ReturnEntity logout(HttpServletRequest request) {
        ReturnEntity returnEntity = new ReturnEntity();
        String token=getTokenFromRequest(request);
        String userId = JWTUtils.parseJWT(token);
        try {
            redisTemplate.delete(userId);
            returnEntity.setData("success");
            returnEntity.setMsg("成功");
            returnEntity.setSuccess("200");
        }catch (Exception e){
            returnEntity.setData("failed");
            returnEntity.setMsg("登出失败");
            returnEntity.setSuccess("500");
        }

        return returnEntity;
    }
}