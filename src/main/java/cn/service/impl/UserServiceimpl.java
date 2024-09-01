package cn.service.impl;

import cn.dao.UserMapper;
import cn.entity.User;
import cn.service.UserService;
import cn.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceimpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public User findByUser(String uname) {
        return userMapper.findByUser(uname);
    }
    @Override
    public User findById(String id) {
        return userMapper.findById(id);
    }

    @Override
    public int deleteById(String id) {
        return userMapper.deleteById(id);
    }

    @Override
    public int freezeById(String id) {
        return userMapper.freezeUser(id);
    }

    @Override
    public int unFreezeById(String id) {
        return userMapper.unFreezeUser(id);
    }
    @Override
    public int expansionById(String id, long size) {
        return userMapper.expansionUser(id, size);
    }

    @Override
    public void register(String id, String uname, String psw) {
        int defaultAuthority = 10;
        userMapper.add(id, uname, psw, defaultAuthority);
    }

    @Override
    public void createByAdmin(String id, String uname, String psw) {
        int defaultAuthority = 10;
        userMapper.add(id, uname, psw, defaultAuthority);
    }

    @Override
    public List<UserVo> getAllUsers() {
        return userMapper.getAllUsers();
    }
}