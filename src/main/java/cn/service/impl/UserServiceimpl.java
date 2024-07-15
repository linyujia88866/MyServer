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
        List<UserVo> res = userMapper.getAllUsers();
        log.info(res.toString());
        return res;
//        return null;
    }
}