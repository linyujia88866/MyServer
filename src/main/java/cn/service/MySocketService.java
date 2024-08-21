package cn.service;

import cn.entity.Message;
import cn.entity.MessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MySocketService {


    @Autowired
    private MessageMapper messageMapper;

    public void save(Message m){
        messageMapper.save(m);
    }


    public void update(int status,  long messageId){
        messageMapper.updateStatusById(status, messageId);
    }

    public List<Message> getAllNotRead(String username) {
//        Message user = new Message();
//        log.info(username);
//        user.setReceiver(username);
//        user.setStatus(0);
        // 创建一个ExampleMatcher，用于匹配所有字段，包括null字段
//        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues()
//                .withMatcher("receiver", match -> match.ignoreCase(false));
        // 创建一个Example对象
//        Example<Message> example = Example.of(user);
        return messageMapper.getAllNotRead(username);
    }


    public List<Message> getAllNotReadNotice(String username) {
//        Message user = new Message();
//        log.info(username);
//        user.setReceiver(username);
//        user.setStatus(0);
        // 创建一个ExampleMatcher，用于匹配所有字段，包括null字段
//        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues()
//                .withMatcher("receiver", match -> match.ignoreCase(false));
        // 创建一个Example对象
//        Example<Message> example = Example.of(user);
        return messageMapper.getAllNotReadNotice(username);
    }

    public long countAllNotRead(String username){
        return messageMapper.countAllNotRead(username);

    }

    public long countAllNotReadNotice(String username){
        return messageMapper.countAllNotReadNotice(username);

    }
}
