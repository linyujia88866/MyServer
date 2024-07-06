package cn.how2j.springboot.web;

import cn.aqjyxt.bean.JWTUtils;
import cn.aqjyxt.bean.Returnben;
import cn.aqjyxt.entity.UserDto;
import cn.aqjyxt.entity.aqjyxt_user;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@RestController
public class MyController {

    @PostMapping("login")
    @ResponseBody
    public Returnben login(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestBody UserDto userDto ) {
        Returnben returnben = new Returnben();
        String user = userDto.getUser();
        String password = userDto.getPassword();
        System.out.println(user);
        if(!Objects.equals(user, "LiYan") || !Objects.equals(password, "9802")){
            returnben.setMsg("失败");
            returnben.setSuccess("500");
            return returnben;
        }
        aqjyxt_user aqjyxt_user = new aqjyxt_user();
        aqjyxt_user.setUser(user);
        aqjyxt_user.setPassword(password);
        String token = JWTUtils.getToken(aqjyxt_user);
        returnben.setData(token);
        returnben.setMsg("成功");
        returnben.setSuccess("200");

        // 创建一个cookie
        Cookie myCookie = new Cookie("token", token);

        // 设置cookie的有效期，例如一个小时(3600秒)
        myCookie.setMaxAge(3600);

        // 设置cookie的路径，这样只有访问这个路径时才会发送cookie
//        myCookie.setPath("/");

        // 设置cookie到response中
        response.addCookie(myCookie);
        return returnben;

    }

    @PostMapping("/verify")
    @ResponseBody
//    @ApiOperation("登录接口")
    public Returnben verify(HttpServletRequest request, HttpSession session) {
        Returnben returnben = new Returnben();
        returnben.setData("success");
        returnben.setMsg("成功");
        returnben.setSuccess("200");
        return returnben;
    }

    @PostMapping("/postHello")
    public String postHello1(@RequestParam("name") String name,
                             @RequestParam("age") Integer age) {
        return "name：" + name + "\nage：" + age;
    }
}