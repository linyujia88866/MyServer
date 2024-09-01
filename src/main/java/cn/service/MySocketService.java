package cn.service;

import cn.dao.UserMapper;
import cn.entity.Message;
import cn.entity.MessageMapper;
import cn.entity.User;
import cn.enums.MessageType;
import cn.hutool.json.JSONObject;
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

    @Autowired
    private UserMapper userMapper;

    public List<Message> getAllNotReadNotice(String username) {
        return messageMapper.getAllNotReadNotice(username);
    }

    public Message getOneMsg(Long msgId) {
        return messageMapper.getMsgById(msgId);
    }


    public List<Message> getAllMySend(String username) {
        return messageMapper.getAllMySend(username);
    }

    public long changeMsgStatus(String username, long msgId, Integer status){
        Message msg = messageMapper.getMsgById(msgId);
        if(msg.getStatus() > 1){
            return 3; // 2 状态已经是稳定形式
        }
        // 2 同意  3 拒绝
        if((status == 2 || status == 3) &&
                !Objects.equals(msg.getType(), MessageType.disk_expansion_application.getCode())){
            return 2; // 2 不允许设置这样的状态
        }
        if(Objects.equals(msg.getReceiver(), username)){
            messageMapper.updateStatusById(status, msgId);
            if(status ==2){
                System.out.println("修改用户的可用空间数据");
                String owner = msg.getSender();
                String content = msg.getContent();
                JSONObject jsonObject = new JSONObject(content);
                long size = Long.parseLong(jsonObject.get("size").toString());
                User userTarget = userMapper.findByUser(owner);
                long already = userTarget.getFileTotalSizeAllow();
                userMapper.expansionUser(userTarget.getUserId(), size * 1024 * 1024 + already);
            }
            return 1;
        }else {
            return 0; // 没有权限
        }
    }


    public long countAllNotReadNotice(String username){
        return messageMapper.countAllNotReadNotice(username);
    }
}
