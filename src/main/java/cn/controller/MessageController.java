package cn.controller;

import cn.entity.Message;
import cn.enums.MessageStatus;
import cn.enums.MessageType;
import cn.result.Result;
import cn.service.MySocketService;
import cn.utils.JWTUtils;
import io.lettuce.core.dynamic.annotation.Param;
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
    public Result changeMsgStatus (HttpServletRequest request, @PathVariable long msgId,
                                   @RequestParam(value = "status") int status) {
        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
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
