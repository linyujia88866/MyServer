package cn.controller;

import cn.aqjyxt.bean.JWTUtils;
import cn.entity.User;
import cn.result.Result;
import cn.service.UserService;
import cn.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

import static cn.aqjyxt.utils.requestUtils.getTokenFromRequest;
import static cn.utils.TestCard.isValidCardId;

@RestController
@RequestMapping("/api/user")
@CrossOrigin //解决跨域问题
@Slf4j
public class UserController {
    //注入userService
    @Autowired
    private UserService userService;


    @Autowired
    private RedisTemplate redisTemplate;

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


    /**
     * 用户登录接口
     * @param user 包含用户名和密码的用户对象，通过RequestBody接收前端传来的JSON数据
     * @return 返回登录结果，如果用户名正确且密码匹配，则返回登录成功结果；否则返回错误信息
     * 此登陆接口没有涉及对密码的加密，因此仅用于演示，在实际应用中应使用加密技术保护密码
     * 也没有设计token，因此登录成功后，用户无法退出，需要手动关闭浏览器或重新打开浏览器
     */
    @PostMapping("/login")
    public Result log(@RequestBody User user){
        // 提取用户名和密码
        String uname = user.getUsername();
        String pword = user.getPassword();
        // 根据用户名查找用户
        User byUser = userService.findByUser(uname);
        if (byUser == null){
            // 如果用户名不存在，则返回用户名错误信息
            return Result.error(444,"用户名错误");
        }
        if (pword.equals(byUser.getPassword())){
            // // 如果密码匹配，则登录成功
            return Result.success();
        }
        // 如果密码不匹配，则返回密码错误信息
        return Result.error(444,"密码错误！");
    }


    @GetMapping("/auth")
    public Result check(){

        return Result.success();
    }
}