package cn.how2j.springboot.web;

import cn.aqjyxt.bean.JWTUtils;
import cn.aqjyxt.bean.Returnben;
import cn.aqjyxt.entity.UserDto;
import cn.aqjyxt.entity.aqjyxt_user;
import cn.entity.User;
import cn.hutool.core.lang.Assert;
import cn.result.Result;
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

import static cn.aqjyxt.utils.requestUtils.getTokenFromRequest;

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
    public Returnben login(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestBody UserDto userDto ) {
        Returnben returnben = new Returnben();
        String user = userDto.getUser();
        String password = userDto.getPassword();
        try {
            User userFromDataBase = userService.findByUser(user);
            if(!Objects.equals(password, userFromDataBase.getPassword())){
                returnben.setMsg("账号或密码错误");
                returnben.setSuccess("500");
                return returnben;
            }
        } catch (Exception e) {
            returnben.setMsg("账号或密码错误");
            returnben.setSuccess("500");
            return returnben;
        }

        aqjyxt_user aqjyxt_user = new aqjyxt_user();
        aqjyxt_user.setUser(user);
        aqjyxt_user.setPassword(password);
        String token = JWTUtils.getToken(aqjyxt_user);
        returnben.setData("The token is already set in Cookies");
        returnben.setMsg("登录成功");
        returnben.setSuccess("200");
        // 存储Token到Redis，假设用户名作为key
        redisTemplate.opsForValue().set(user, token, 30, TimeUnit.MINUTES);


        // 创建一个cookie
        Cookie myCookie = new Cookie("token", token);

        // 设置cookie的有效期，例如一个小时(3600秒)
        myCookie.setMaxAge(3600);

        // 设置cookie的路径，这样只有访问这个路径时才会发送cookie
//        myCookie.setPath("/");
        // 设置cookie到response中
        response.addCookie(myCookie);
        log.info("设置token到cookie中");
        return returnben;

    }

    @PostMapping("/verify")
    @ResponseBody
    public Returnben verify(HttpServletRequest request, HttpSession session) {
        Returnben returnben = new Returnben();
        returnben.setData("success");
        returnben.setMsg("成功");
        returnben.setSuccess("200");
        return returnben;
    }

    @PostMapping("/logout")
    @ResponseBody
    public Returnben logout(HttpServletRequest request, HttpSession session) {
        Returnben returnben = new Returnben();
        String token=getTokenFromRequest(request);
        String userId = JWTUtils.parseJWT(token);
        try {
            redisTemplate.delete(userId);
            returnben.setData("success");
            returnben.setMsg("成功");
            returnben.setSuccess("200");
        }catch (Exception e){
            returnben.setData("failed");
            returnben.setMsg("登出失败");
            returnben.setSuccess("500");
        }

        return returnben;
    }

    @PostMapping("/postHello")
    public String postHello1(@RequestParam("name") String name,
                             @RequestParam("age") Integer age) {
        return "name：" + name + "\nage：" + age;
    }
}