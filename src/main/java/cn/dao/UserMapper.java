package cn.dao;

import cn.entity.User;
import cn.vo.UserVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    //查询
    @Select("select * from users where username = #{uname}")
    User findByUser(String uname);

    @Select("select * from users where userId = #{id}")
    User findById(String id);

    @Update("UPDATE users SET status=1 where userId = #{id}")
    int freezeUser(String id);

    @Update("UPDATE users SET status=0 where userId = #{id}")
    int unFreezeUser(String id);

    @Delete("DELETE FROM  users where userId = #{id}")
    int deleteById(String id);
    //新增
    @Insert("insert into users(userId, username, password, authority)" +"values  (#{id},#{uname},#{psw},#{authority})")
    void add(String id, String uname, String psw, int authority);

    @Select("select userId, username, authority, status from users")
    List<UserVo> getAllUsers();
}