package cn.service;

import cn.entity.User;

public interface UserService {
    //根据用户名查询数据库
    User findByUser(String uname);
    User findById(String id);
    //注册  将用户名和密码添加到数据库中
    void register(String id, String uname, String psw);
}