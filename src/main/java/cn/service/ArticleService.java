package cn.service;


import cn.dao.ArticleMapper;
import cn.dao.UserMapper;
import cn.entity.Article;
import cn.entity.ArticleWithUser;
import cn.entity.User;
import cn.vo.ArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@Slf4j
public class ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserMapper userMapper;

    public Article findById(String id) {
        return articleMapper.findById(id);
    }

    public List<ArticleVo> getAllArticles(String username) {
        return articleMapper.getAllArticles(username);
    }

    public List<ArticleVo> getAllArticles(String username, int publish) {
        return articleMapper.getAllArticles_(username, publish);
    }

    public List<ArticleVo> getAllPubArticles() {
        return articleMapper.getAllPubArticles_();
    }

    public List<ArticleVo> getMyFavoriteArticles(String username) {
        User user = userMapper.findByUser(username);
        String userId = user.getUserId();
        return articleMapper.getMyFavoriteArticles(userId);
    }

    public void saveArticle(String articleId, String title, Timestamp createdAt, String content, String username, boolean publish) {
        articleMapper.add(articleId, title,createdAt,content, username, publish);
    }
    public int updateArticle(String articleId, String title, Timestamp createdAt, String content, String username) {
        return articleMapper.update(articleId, title,createdAt,content, username);
    }

    public int addOneRead(String articleId) {
        return articleMapper.addOneRead(articleId);
    }

    public int addOneLike(String articleId) {
        return articleMapper.addOneLike(articleId);
    }
    public int deductOneLike(String articleId) {
        return articleMapper.deductOneLike(articleId);
    }

    public int addOneGood(String articleId) {
        return articleMapper.addOneGood(articleId);
    }
    public int deductOneGood(String articleId) {
        return articleMapper.deductOneGood(articleId);
    }

    public int addLikeToArt(String articleId, String username) {
        User user = userMapper.findByUser(username);
        String user_id = user.getUserId();
        addOneLike(articleId);
        return articleMapper.addLikeToArt(articleId, user_id);
    }

    public int deductLikeToArt(String articleId, String username) {
        User user = userMapper.findByUser(username);
        String user_id = user.getUserId();
        deductOneLike(articleId);
        return articleMapper.deductLikeToArt(articleId, user_id);
    }

    public ArticleWithUser checkLikeToArt(String articleId, String username) {
        User user = userMapper.findByUser(username);
        String user_id = user.getUserId();
        return articleMapper.checkLikeToArt(articleId, user_id);
    }

    public int addGoodToArt(String articleId, String username) {
        User user = userMapper.findByUser(username);
        String user_id = user.getUserId();
        addOneGood(articleId);
        return articleMapper.addGoodToArt(articleId, user_id);
    }

    public int deductGoodToArt(String articleId, String username) {
        User user = userMapper.findByUser(username);
        String user_id = user.getUserId();
        deductOneGood(articleId);
        return articleMapper.deductGoodToArt(articleId, user_id);
    }

    public ArticleWithUser checkGoodToArt(String articleId, String username) {
        User user = userMapper.findByUser(username);
        String user_id = user.getUserId();
        return articleMapper.checkGoodToArt(articleId, user_id);
    }

    public int publishArticle(String articleId, String username) {
        return articleMapper.publish(articleId, username,true);
    }
}
