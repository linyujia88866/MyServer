package cn.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
@Slf4j
@Component
public class Send {

    @Resource
    private WebSocketServer webSocketServer;

    @Scheduled(fixedDelay = 30000)
    public void sendMsg() throws IOException {
        webSocketServer.sendAllMessage("hello"+new Date());
    }
}