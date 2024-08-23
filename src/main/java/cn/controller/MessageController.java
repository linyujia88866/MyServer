package cn.controller;

import cn.entity.Message;
import cn.result.Result;
import cn.service.MySocketService;
import cn.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static cn.utils.requestUtils.getTokenFromRequest;

@RestController
@Slf4j
@RequestMapping("/api/message")
public class MessageController {
    //注入userService
    @Autowired
    private MySocketService mySocketService;
    @GetMapping("")
    @ResponseBody
    public Result getAllNotRead (HttpServletRequest request) {
        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
        List<Message> res =  mySocketService.getAllNotReadNotice(username);
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
    public Result changeMsgStatus (HttpServletRequest request,@PathVariable long msgId) {
        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
        long res =  mySocketService.changeMsgStatus(username, msgId);
        if(res==0){
            return Result.error(70001, "你不是该消息的接收者，无法修改状态");
        }
        return Result.success(res);
    }
}
