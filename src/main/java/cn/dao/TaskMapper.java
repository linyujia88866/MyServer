package cn.dao;


import cn.entity.Task;
import cn.vo.TaskVo;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface TaskMapper {

    //查询
    @Select("select title, content from tasks where taskId = #{taskId} and username = #{username} ")
    Task findById(String taskId, String username);

    @Select("select taskId, title, createdAt from tasks where username = #{username} ORDER BY createdAt DESC")
    List<TaskVo> getAllTasks(String username);
    //新增
    @Insert("insert into tasks(taskId, title, createdAt, content, username)" +"values  (#{taskId},#{title},#{createdAt},#{content},#{username})")
    void add(String taskId, String title, Timestamp createdAt, String content, String username);

    @Update("UPDATE tasks SET title = #{title}, createdAt = #{createdAt}, content= #{content} WHERE taskId = #{taskId} and username = #{username}")
    int update(String taskId, String title, Timestamp createdAt, String content, String username);

    @Delete("DELETE  FROM  tasks  WHERE taskId = #{taskId} and username = #{username}")
    int delete(String taskId, String username);
}
