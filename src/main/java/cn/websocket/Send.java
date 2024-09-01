package cn.websocket;

import cn.entity.Message;
import cn.enums.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
@Slf4j
@Component
public class Send {

    @Resource
    private WebSocketServer webSocketServer;

    @Scheduled(fixedDelay = 10000)
    public void sendMsg() throws IOException, SQLException {
        Message message = new Message();
        message.setSender("system");
        message.setType(MessageType.heartbeat.getCode());
        message.setContent("Hello, "+new Date());
        webSocketServer.sendAllMessage(message);
    }
}