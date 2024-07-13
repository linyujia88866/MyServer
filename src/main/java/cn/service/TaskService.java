package cn.service;


import cn.dao.TaskMapper;
import cn.entity.Task;
import cn.entity.User;
import cn.vo.TaskVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@Slf4j
public class TaskService {

    @Autowired
    private TaskMapper taskMapper;


    public Task findById(String id) {
        return taskMapper.findById(id);
    }

    public List<TaskVo> getAllTasks() {
        return taskMapper.getAllTasks();
    }

    public void saveTask(String taskId, String title, Timestamp createdAt, String content) {
        taskMapper.add(taskId, title,createdAt,content);
    }


    public int updateTask(String taskId, String title, Timestamp createdAt, String content) {
        return taskMapper.update(taskId, title,createdAt,content);
    }
}
