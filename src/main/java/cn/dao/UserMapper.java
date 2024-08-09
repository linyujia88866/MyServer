package cn.dao;

import cn.entity.User;
import cn.vo.UserVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    //查询
    @Select("select * from users where username = #{uname}")
    User findByUser(String uname);

    @Select("select * from users where userId = #{id}")
    User findById(String id);
    //新增
    @Insert("insert into users(userId, username, password, authority)" +"values  (#{id},#{uname},#{psw},#{authority})")
    void add(String id, String uname, String psw, int authority);

    @Select("select userId, username, authority from users")
    List<UserVo> getAllUsers();
}