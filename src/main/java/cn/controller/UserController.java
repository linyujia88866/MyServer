package cn.controller;

import cn.entity.User;
import cn.result.Result;
import cn.service.UserService;
import cn.utils.JWTUtils;
import cn.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

import static cn.utils.TestCard.isValidCardId;
import static cn.utils.requestUtils.getTokenFromRequest;

@RestController
@RequestMapping("/api/user")
@CrossOrigin //解决跨域问题
@Slf4j
public class UserController {
    //注入userService
    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     * @param user 包含用户名和密码的用户对象，通过RequestBody接收前端传来的JSON数据
     * @return 返回注册结果，如果用户名不存在，则返回注册成功结果；否则返回错误信息
     * 对参数没有进行验证，仅用于演示
     */
    @PostMapping("/register")
    public Result register (@RequestBody User user) {
        String uname = user.getUsername();
        String id = user.getId();
        boolean isValid;
        try {
            isValid=isValidCardId(String.valueOf(id));
        }catch (Exception e){
            log.info(e.getMessage());
            return Result.error(333, "身份证号码校验失败！");
        }
        if(!isValid){
            return Result.error(333, "身份证号码校验失败！");
        }
        String psw = user.getPassword();
        User byId =  userService.findById(id);
        User byuser =  userService.findByUser(uname);
        //查询数据库判断该用户是否存在
        if (byId != null){
            return Result.error(333, "身份证号码已被注册！");
        } else if(byuser!=null){
            //不存在则注册成功，将用户名和密码添加到数据库中
            return Result.error(333, "用户名已存在！");
        }else {
            userService.register(String.valueOf(id), uname,psw);
            return Result.success("注册成功！");
        }

    }


    @PostMapping("/create")
    public Result create (HttpServletRequest request, @RequestBody User user) {
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        User userFromDataBase = userService.findByUser(username);
        if(!Objects.equals(userFromDataBase.getAuthority(), 0)){
            return Result.error(500, "您不是超级管理员！");
        }
        String uname = user.getUsername();
        String psw = user.getPassword();
        User byuser =  userService.findByUser(uname);
        String id = "test_" + uname;
        if(byuser!=null){
            //不存在则注册成功，将用户名和密码添加到数据库中
            return Result.error(333, "用户名已存在！");
        }else {
            userService.register(id, uname,psw);
            return Result.success("注册成功！");
        }
    }
    @GetMapping("/all")
    public Result users(HttpServletRequest request){
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        User userFromDataBase = userService.findByUser(username);
        if(!Objects.equals(userFromDataBase.getAuthority(), 0)){
            return Result.error(500, "您不是超级管理员！");
        }
        List<UserVo> res = userService.getAllUsers();
        return Result.success(res);
    }

    @GetMapping("/info")
    public Result userInfo(HttpServletRequest request, @RequestBody User user){
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        User userFromDataBase = userService.findByUser(username);
        if(!Objects.equals(userFromDataBase.getAuthority(), 0)){
            return Result.error(500, "您不是超级管理员！");
        }
        User res = userService.findByUser(user.getUsername());
        return Result.success(res);
    }
}