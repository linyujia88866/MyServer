package cn.service;

import cn.entity.User;
import cn.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {
    //根据用户名查询数据库
    User findByUser(String uname);
    User findById(String id);
    int deleteById(String id);
    int freezeById(String id);
    int unFreezeById(String id);

    int expansionById(String id, long size);

    //注册  将用户名和密码添加到数据库中
    void register(String id, String uname, String psw);

    void createByAdmin(String id, String uname, String psw);

    List<UserVo> getAllUsers();
}