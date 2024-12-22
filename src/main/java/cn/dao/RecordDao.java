package cn.dao;

import cn.entity.Record;
import cn.vo.RecordVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface RecordDao extends BaseMapper<Record> {
    //查询
    @Select("SELECT r.id, r.spend, r.comment,r.date,c.name FROM record r JOIN category c ON r.cid = c.id  WHERE c.username = #{username}")
    List<RecordVo> getAllRecord(String username);

    //查询
    @Select("SELECT r.id, r.spend, r.comment,r.date,c.name FROM record r JOIN category c ON r.cid = c.id  " +
            "WHERE c.username = #{username} and DATE_FORMAT(r.date, '%Y-%m') = #{date};")
    List<RecordVo> getAllRecordInMonth(String username, String date);
}

