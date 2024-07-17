package cn.service;


import cn.dao.TaskMapper;
import cn.entity.Task;
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

    public Task findById(String id, String username) {
        return taskMapper.findById(id, username);
    }

    public List<TaskVo> getAllTasks(String username) {
        return taskMapper.getAllTasks(username);
    }

    public void saveTask(String taskId, String title, Timestamp createdAt, String content, String username) {
        taskMapper.add(taskId, title,createdAt,content, username);
    }
    public int updateTask(String taskId, String title, Timestamp createdAt, String content, String username) {
        return taskMapper.update(taskId, title,createdAt,content, username);
    }
}
