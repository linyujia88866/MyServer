package cn.dao;


import cn.entity.Category;

import cn.entity.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface CategoryDao extends BaseMapper<Category> {

}

