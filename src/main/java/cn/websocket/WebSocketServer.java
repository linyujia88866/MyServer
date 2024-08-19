package cn.websocket;

import cn.bo.CheckSocketTokenBo;
import cn.entity.Message;
import cn.service.CheckSocketToken;
import cn.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;


@ServerEndpoint(value = "/websocket" , configurator = HttpSessionConfigurator.class)
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

//        if(user != null){
//            this.session = session;
//            this.httpSession = httpSession;
//        }else{
//            //用户未登陆
//            try {
//                session.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        String userId = JWTUtils.parseJWT(token);
        this.session = session;

        this.userId=userId;
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
//            webSocketMap2.remove(this);
            webSocketMap.put(userId,this);
//            webSocketMap2.put(this, userId);
            //加入set中
        }else{
            webSocketMap.put(userId,this);
//            webSocketMap2.put(this, userId);
            //加入set中
            addOnlineCount();
            //在线数加1
        }

        log.info("用户连接:"+userId+",当前在线人数为:" + getOnlineCount());

        try {
            sendMessage("用户"+userId+"连接成功");
        } catch (IOException e) {
            log.error("用户:"+userId+",网络异常!!!!!!");
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
//            webSocketMap2.remove(this);
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
    public void onMessage(String message, Session session, EndpointConfig config) throws IOException {
        log.info("用户消息:"+userId+",报文:"+message);
        log.info(this.userId);
        Message userMessage = JSON.parseObject(message, Message.class);
        userMessage.setSender(this.userId);
        //  判断该消息是发给谁的
        if (userMessage.getReceiver().equals("all")){
            // 发给所有人
            for (WebSocketServer wsServer : webSocketMap.values()) {
                if (webSocketMap.get(userMessage.getSender())!=wsServer){
                    wsServer.session.getAsyncRemote().sendText(JSON.toJSONString(userMessage));
                }
            }
            return;
        }
        sendInfo(JSON.toJSONString(userMessage), userMessage.getReceiver());
//        WebSocketServer wsServer = webSocketMap.get(userMessage.getReceiver());
//        wsServer.session.getAsyncRemote().sendText(JSON.toJSONString(userMessage));

        //可以群发消息
        //消息保存到数据库、redis
//        if(StringUtils.isNotBlank(message)){
//            try {
//                //解析发送的报文
//                JSONObject jsonObject = JSON.parseObject(message);
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//        sendMessage("收到");
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
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 实现服务器主动推送
     */
    public void sendAllMessage(String message) throws IOException {
        ConcurrentHashMap.KeySetView<String, WebSocketServer> userIds = webSocketMap.keySet();
        for (String userId : userIds) {
            WebSocketServer webSocketServer = webSocketMap.get(userId);
            webSocketServer.session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 发送自定义消息
     * */
    public void sendInfo(String message, String userId) throws IOException {
        log.info("发送消息到:"+userId+"，报文:"+message);
        if(StringUtils.isNotBlank(userId)&&webSocketMap.containsKey(userId)){
            webSocketMap.get(userId).sendMessage(message);
            sendMessage("已将消息发送给用户"+userId);
        }else{
            log.error("用户"+userId+",不在线！");
            sendMessage("用户"+userId+",不在线！");
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