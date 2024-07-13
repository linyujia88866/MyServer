package cn.dao;


import cn.entity.Task;
import cn.vo.TaskVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface TaskMapper {

    //查询
    @Select("select * from tasks where taskId = #{taskId}")
    Task findById(String id);

    @Select("select taskId, title, createdAt from tasks")
    List<TaskVo> getAllTasks();
    //新增
    @Insert("insert into tasks(taskId, title, createdAt, content)" +"values  (#{taskId},#{title},#{createdAt},#{content})")
    void add(String taskId, String title, Timestamp createdAt, String content);

    @Update("UPDATE tasks SET title = #{title}, createdAt = #{createdAt}, content= #{content} WHERE taskId = #{taskId}")
    int update(String taskId, String title, Timestamp createdAt, String content);
}
