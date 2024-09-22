package cn.websocket;

import cn.config.MinioConfig;
import cn.dao.ArticleLinkMapper;
import cn.dao.ArticleMapper;
import cn.entity.Article;
import cn.entity.Message;
import cn.enums.MessageType;
import cn.vo.LinkVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class Send {
    @Autowired
    private MinioConfig minioConfig;

    @Resource
    private WebSocketServer webSocketServer;

    @Autowired
    private ArticleLinkMapper articleLinkMapper;

    @Autowired
    private ArticleMapper articleMapper;

//    每分钟通知一次时间
    @Scheduled(fixedDelay = 60000)
    public void sendMsg() throws IOException, SQLException {
        Message message = new Message();
        message.setSender("system");
        message.setType(MessageType.heartbeat.getCode());
        message.setContent("Hello, "+new Date());
        webSocketServer.sendAllMessage(message);
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void clearImgFileLinkInArticle() throws Exception {
        List<LinkVo> res =  articleLinkMapper.getAllLink();
        log.info("Scheduled task begin, getAllLink, {}",res);
        for(LinkVo linkVo: res){
            String articleId = linkVo.getArticleId();
            String imgPath = linkVo.getImgPath();
            log.info("imgPath, {}", imgPath);
            Article article = articleMapper.findById(articleId);
            if(article == null){
                log.info("article is not exist, {}", articleId);
                LinkVo linkVo1 = articleLinkMapper.findById(articleId);
                if(linkVo1 != null){
                    articleLinkMapper.deleteById(articleId);
                }
                removeObject(imgPath);
            } else {
                LinkVo linkVo2 = articleLinkMapper.findById(articleId);
                if(linkVo2!=null){
                    String imgPath2 = linkVo2.getImgPath();
                    String[] items =  imgPath2.substring(1, imgPath2.length() - 1).trim().split(", ");
                    // 使用逗号作为分隔符分割字符串
                    // 将分割后的字符串数组转换成列表
                    List<String> list = Arrays.asList(items);
                    if(list.contains(imgPath.replace("%2F","/"))){
                        log.info("img is still in use, {}", imgPath);
                    } else {
                        log.info("img is not in use now, {}", imgPath);
                        removeObject(imgPath);
                    }
                }
            }
        }
        log.info("Scheduled task end, ");
    }

    private void removeObject(String imgPath) throws Exception {
        boolean isImgExist = minioConfig.isObjectExist("pic-link",
                imgPath.replaceFirst("/pic-link", "").replace("%2F", "/"));
        log.info("isImgExist, {}", isImgExist);
        if(isImgExist){
            minioConfig.removeObject("pic-link", imgPath.replaceFirst("/pic-link", "")
                    .replace("%2F", "/"));
        }
        articleLinkMapper.deleteByPath(imgPath);
    }
}