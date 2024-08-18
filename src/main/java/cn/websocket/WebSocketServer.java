package cn.websocket;

import cn.bo.CheckSocketTokenBo;
import cn.service.CheckSocketToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;


@ServerEndpoint(value = "/websocket/{userId}" , configurator = HttpSessionConfigurator.class)
@Component
@Slf4j
public class WebSocketServer {

    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String userId="";


    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId,EndpointConfig config) throws IOException {
        CheckSocketToken checkSocketToken = new CheckSocketToken();
        HttpSession httpSession= (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String token  = (String) httpSession.getAttribute("token");
        String reason = "";
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
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("用户:"+userId+",网络异常!!!!!!");
        }
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
    public void onMessage(String message, Session session) throws IOException {
        log.info("用户消息:"+userId+",报文:"+message);
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
        sendMessage("收到");
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
//        error.printStackTrace();
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
    public static void sendInfo(String message,@PathParam("userId") String userId) throws IOException {
        log.info("发送消息到:"+userId+"，报文:"+message);
        if(StringUtils.isNotBlank(userId)&&webSocketMap.containsKey(userId)){
            webSocketMap.get(userId).sendMessage(message);
        }else{
            log.error("用户"+userId+",不在线！");
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