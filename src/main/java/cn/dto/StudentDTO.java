package cn.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: 1399543132@qq.com
 * @Date: 2024/01/21/20:16
 * @Description: 学生表实体类
 */
@Data
public class StudentDTO {

    /**
     * 学号
     */
    @TableId
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 教室id
     */
    private Long classId;

    /**
     * 行位置
     */
    private String positionRow;

    /**
     * 列位置
     */
    private String positionColumn;

    /**
     * 班主任id
     */
    private Long teacherId;

}

