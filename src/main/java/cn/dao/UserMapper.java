package cn.dao;

import cn.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    //查询
    @Select("select * from users where username = #{uname}")
    User findByUser(String uname);

    @Select("select * from users where user_id = #{id}")
    User findById(String id);
    //新增
    @Insert("insert into users(user_id, username, password, authority)" +"values  (#{id},#{uname},#{psw},#{authority})")
    void add(String id, String uname, String psw, int authority);
}