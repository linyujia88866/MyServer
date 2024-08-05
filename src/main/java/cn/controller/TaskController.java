package cn.controller;


import cn.utils.JWTUtils;
import cn.dto.TaskDto;
import cn.entity.Task;
import cn.result.Result;
import cn.service.TaskService;
import cn.utils.UuidUtil;
import cn.vo.TaskVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;

import static cn.utils.requestUtils.getTokenFromRequest;

@RestController
@Slf4j
@RequestMapping("/api/task")
public class TaskController {

    //注入userService
    @Autowired
    private TaskService taskService;

    /**
     * 用户注册接口
     * @param taskdto 包含用户名和密码的用户对象，通过RequestBody接收前端传来的JSON数据
     * @return 返回注册结果，如果用户名不存在，则返回注册成功结果；否则返回错误信息
     * 对参数没有进行验证，仅用于演示
     */
    @PostMapping("/save")
    @ResponseBody
    public Result save (HttpServletRequest request,@RequestBody TaskDto taskdto) {
        String taskId = UuidUtil.getUuid();
        String title = taskdto.getTitle();

        long currentTimeMillis = System.currentTimeMillis();

        // 将当前时间戳转换为java.sql.Timestamp
        Timestamp timestamp = new Timestamp(currentTimeMillis);
        String content = taskdto.getContent();
        log.info("保存任务{}成功！", taskId);
        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
        taskService.saveTask(taskId, title, timestamp, content, username);
        return Result.success(taskId);
    }

    @PostMapping("/update/{taskId}")
    @ResponseBody
    public Result update (HttpServletRequest request,@RequestBody TaskDto taskdto, @PathVariable String taskId) {
        String title = taskdto.getTitle();
        long currentTimeMillis = System.currentTimeMillis();
        // 将当前时间戳转换为java.sql.Timestamp
        Timestamp timestamp = new Timestamp(currentTimeMillis);
        String content = taskdto.getContent();
        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
        int res = taskService.updateTask(taskId, title, timestamp, content,username);
        log.info("保存任务{}成功！", taskId);
        return Result.success(res);
    }

    @PostMapping("/delete/{taskId}")
    @ResponseBody
    public Result delete (HttpServletRequest request, @PathVariable String taskId) {
        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
        int res = taskService.deleteTask(taskId,  username);
        log.info("保存任务{}成功！", taskId);
        return Result.success(res);
    }

    @GetMapping("/tasks")
    @ResponseBody
    public Result getAllTasks(HttpServletRequest request){
        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
        List<TaskVo> res =  taskService.getAllTasks(username);
        return Result.success(res);
    }


    @GetMapping("/get/{taskId}")
    @ResponseBody
    public Result getTaskById(HttpServletRequest request, @PathVariable("taskId") String taskId){
        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
        Task res =  taskService.findById(taskId, username);
        return Result.success(res);
    }
}
