package cn.controller;

import cn.entity.Message;
import cn.entity.User;
import cn.enums.MessageStatus;
import cn.result.Result;
import cn.service.MySocketService;
import cn.service.UserService;
import cn.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

import static cn.utils.requestUtils.getTokenFromRequest;

@RestController
@Slf4j
@RequestMapping("/api/message")
public class MessageController {
    //注入userService
    @Autowired
    private MySocketService mySocketService;
    @Autowired
    private UserService userService;

    @GetMapping("")
    @ResponseBody
    public Result getAllNotRead (HttpServletRequest request) {
        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
        List<Message> res =  mySocketService.getAllNotReadNotice(username);
        return Result.success(res);
    }

    @GetMapping("/one/{msgId}")
    @ResponseBody
    public Result getOneMsg (HttpServletRequest request, @PathVariable Long msgId) {
        log.info(String.valueOf(msgId));

        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
        log.info(username);
        Message res =  mySocketService.getOneMsg(msgId);
        log.info(String.valueOf(res));
        if(!Objects.equals(res.getReceiver(), username) && !Objects.equals(res.getSender(), username)){
            return Result.error(70004, "你没有权限查询该消息");
        }
        return Result.success(res);
    }

    @GetMapping("/send")
    @ResponseBody
    public Result getAllMySend (HttpServletRequest request) {
        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
        List<Message> res =  mySocketService.getAllMySend(username);
        return Result.success(res);
    }

    @GetMapping("/count")
    @ResponseBody
    public Result countAllNotRead (HttpServletRequest request) {
        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
        long res =  mySocketService.countAllNotReadNotice(username);
        log.info("共找到{}条消息", res);
        return Result.success(res);
    }

    @PutMapping("/{msgId}")
    @ResponseBody
    public Result changeMsgStatus (HttpServletRequest request, @PathVariable long msgId,
                                   @RequestParam(value = "status") int status) {
        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
        if(status == 2 || status ==3){
            User userFromDataBase = userService.findByUser(username);
            if(!Objects.equals(userFromDataBase.getAuthority(), 0)){
                return Result.error(40001, "您不是超级管理员！");
            }
        }
        long res =  mySocketService.changeMsgStatus(username, msgId, status);
        if(res==0){
            return Result.error(70001, "你不是该消息的接收者，无法修改状态");
        }
        if(res==2){
            return Result.error(70002, "你无法将该消息设置为状态"+
                    Objects.requireNonNull(MessageStatus.fromCode(status)).getValue() );
        }
        if(res==3){
            return Result.error(70003, "该消息状态已经无法更改" );
        }

        return Result.success(res);
    }
}
