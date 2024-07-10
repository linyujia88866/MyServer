package cn.service.impl;

import cn.dao.UserMapper;
import cn.entity.User;
import cn.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceimpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUser(String uname) {
        User u = userMapper.findByUser(uname);
        return u;
    }
    @Override
    public User findById(String id) {
        return userMapper.findById(id);
    }
    @Override
    public void register(String id, String uname, String psw) {
        userMapper.add(id, uname,psw);
    }
}