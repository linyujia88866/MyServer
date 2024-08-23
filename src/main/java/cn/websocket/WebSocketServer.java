package cn.websocket;

import cn.bo.CheckSocketTokenBo;
import cn.entity.Message;
import cn.enums.MessageType;
import cn.service.CheckSocketToken;
import cn.utils.JWTUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


@ServerEndpoint(value = "/websocket/link" , configurator = HttpSessionConfigurator.class)
@Component
@Slf4j
public class WebSocketServer {

    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
//    private static ConcurrentHashMap<WebSocketServer, String> webSocketMap2 = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String userId="";
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException {
        CheckSocketToken checkSocketToken = new CheckSocketToken();
        String token = getToken(config);
        CheckSocketTokenBo checkSocketTokenBo = checkSocketToken.checkToken(token);
        if(!checkSocketTokenBo.isPass()){
            log.info("session关闭，理由是{}", checkSocketTokenBo.getReason());
            session.close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, checkSocketTokenBo.getReason()));
            return;
        }

        String userId = JWTUtils.parseJWT(token);
        this.session = session;

        this.userId=userId;
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            webSocketMap.put(userId,this);
            //加入set中
        }else{
            webSocketMap.put(userId,this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }

        log.info("用户连接:"+userId+",当前在线人数为:" + getOnlineCount());

        try {
            Message message = new Message();
            message.setContent("用户"+userId+"连接成功");
            message.setType(MessageType.system_info.getCode());
            message.setSender("system");
            message.setReceiver(this.userId);
            sendMessage(message);
        } catch (IOException e) {
            log.error("用户:"+userId+",网络异常!!!!!!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public String getToken(EndpointConfig config){
        HttpSession httpSession= (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        return (String) httpSession.getAttribute("token");
    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {

        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
        log.info("用户退出:"+userId+",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session, EndpointConfig config) throws IOException, SQLException {
        log.info("用户消息:"+userId+",报文:"+message);
        Message userMessage = JSON.parseObject(message, Message.class);
        userMessage.setSender(this.userId);
        //  判断该消息是发给谁的
        if (userMessage.getReceiver().equals("all")){
            if(Objects.equals(userMessage.getType(), MessageType.system_notice.getCode())){
                if(Objects.equals(this.userId, "admin")){
                    // 发给所有人
                    insertDataToAll(userMessage);
                    for (WebSocketServer wsServer : webSocketMap.values()) {
                        if (webSocketMap.get(userMessage.getSender())!=wsServer){
                            userMessage.setReceiver(wsServer.userId);
                            wsServer.session.getAsyncRemote().sendText(JSON.toJSONString(userMessage));
                        }
                    }
                    return;
                }else {
                    Message message2 = new Message();
                    message2.setReceiver(this.userId);
                    message2.setContent("你不是管理员，不能发送通知");
                    message2.setSender("system");
                    message2.setType(MessageType.system_info.getCode());
                    DatabaseHelper.insertMessage(message2);
                    sendMessage(message2);
                }
                return;
            }

        }
        // 发送消息给指定的用户，
        DatabaseHelper.insertMessage(userMessage);
        sendInfo(userMessage.getContent(), userMessage.getReceiver());
    }

    public void insertDataToAll(Message m) throws SQLException {
        List<String> res = DatabaseHelper.getAllUsers();
        for(String username : res){
            m.setReceiver(username);
            DatabaseHelper.insertMessage(m);
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }


    /**
     * 实现服务器主动推送
     */
    public void sendMessage(Message message) throws IOException, SQLException {
        DatabaseHelper.insertMessage(message);
        this.session.getBasicRemote().sendText(JSON.toJSONString(message));
    }


    /**
     * 实现服务器主动推送
     */
    public void sendAllMessage(Message message) throws IOException, SQLException {
        ConcurrentHashMap.KeySetView<String, WebSocketServer> userIds = webSocketMap.keySet();
        for (String userId : userIds) {
            message.setReceiver(userId);
            WebSocketServer webSocketServer = webSocketMap.get(userId);
            webSocketServer.session.getBasicRemote().sendText(JSON.toJSONString(message));
            DatabaseHelper.insertMessage(message);
        }
    }

    /**
     * 发送自定义消息
     * */
    public void sendInfo(String message, String userId) throws IOException, SQLException {
        log.info("发送消息到:"+userId+"，报文:"+message);
        if(StringUtils.isNotBlank(userId)&&webSocketMap.containsKey(userId)){
            Message message1 = new Message();
            message1.setReceiver(userId);
            message1.setContent(message);
            message1.setSender(this.userId);
            webSocketMap.get(userId).sendMessage(message1);

            Message message2 = new Message();
            message2.setReceiver(this.userId);
            message2.setContent("已将消息发送给用户"+userId);
            message2.setSender("system");
            message2.setType(MessageType.system_info.getCode());
            sendMessage(message2);
        }else{
            Message message3 = new Message();
            message3.setReceiver(this.userId);
            message3.setContent("用户"+userId+",不在线！");
            message3.setSender("system");
            message3.setType(MessageType.system_info.getCode());
            sendMessage(message3);
            log.error("用户"+userId+",不在线！");
            sendMessage(message3);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}