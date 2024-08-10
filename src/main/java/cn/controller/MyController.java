package cn.controller;

import cn.dto.UserDto;
import cn.entity.User;
import cn.entity.User4Token;
import cn.result.Result;
import cn.service.UserService;
import cn.utils.JWTUtils;
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
    public Result login(HttpServletResponse response, @RequestBody UserDto userDto ) {
        String user = userDto.getUser();
        String password = userDto.getPassword();
        User userFromDataBase;
        try {
            userFromDataBase = userService.findByUser(user);
            if(!Objects.equals(password, userFromDataBase.getPassword())){
                return Result.error(500,"账号或密码错误");
            }
        } catch (Exception e) {
            return Result.error(500,"账号或密码错误");
        }

        User4Token User4Token = new User4Token();
        User4Token.setUser(user);
        User4Token.setPassword(password);
        String token = JWTUtils.getToken(User4Token);
        String auth = String.valueOf(userFromDataBase.getAuthority());
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
        return Result.success("登录成功", auth);

    }

    @PostMapping("/verify")
    @ResponseBody
    public Result verify(HttpServletRequest request, HttpSession session) {
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        String authority = redisTemplate.opsForValue().get(username + "_auth");
        return Result.success(authority,"鉴权成功");
    }

    @PostMapping("/logout")
    @ResponseBody
    public Result logout(HttpServletRequest request) {
        String token=getTokenFromRequest(request);
        String userId = JWTUtils.parseJWT(token);
        try {
            redisTemplate.delete(userId);
            return Result.success("success", "登出成功");
        }catch (Exception e){
            return Result.error(500,"登出失败");
        }
    }
}