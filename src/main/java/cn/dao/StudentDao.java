package cn.dao;


import cn.entity.StudentEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: 1399543132@qq.com
 * @Date: 2024/01/21/20:44
 * @Description:
 */

@Mapper
public interface StudentDao extends BaseMapper<StudentEntity> {
}

