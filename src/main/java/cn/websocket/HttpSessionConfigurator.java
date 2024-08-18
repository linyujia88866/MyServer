package cn.websocket;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 从websocket中获取用户session
 *
 *
 */
@Slf4j
public class HttpSessionConfigurator extends Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        List<String> cookies = null;
        String token = "";
        Map<String, List<String>> headers = request.getHeaders();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            if(Objects.equals(key, "cookie")){
                cookies = value;
            }
        }
        assert cookies != null;
        try{
            for(String cookie : cookies){

                String[] res = cookie.split(";");
                for(String x : res){
                    List<String> res2 = Arrays.asList(x.split("="));
                    if(res2.get(0).trim().equals("token")){
                        token = res2.get(1);
                    }
                }
//                List<String> res1 = Arrays.asList(res.get(1).split("="));
//                token = res1.get(1);
            }
        } catch (Exception e){
            token = "";
        }
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        httpSession.setAttribute("token", token);
        sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }
}