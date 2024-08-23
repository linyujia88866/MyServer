package cn.service;

import cn.entity.Message;
import cn.entity.MessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
public class MySocketService {
    @Autowired
    private MessageMapper messageMapper;

    public List<Message> getAllNotReadNotice(String username) {
        return messageMapper.getAllNotReadNotice(username);
    }

    public long changeMsgStatus(String username, long msgId){
        Message msg = messageMapper.getMsgById(msgId);
        if(Objects.equals(msg.getReceiver(), username)){
            messageMapper.updateStatusById(1, msgId);
            return 1;
        }else {
            return 0;
        }
    }

    public long countAllNotReadNotice(String username){
        return messageMapper.countAllNotReadNotice(username);

    }
}
