package cn.service;


import cn.dao.ArticleMapper;
import cn.entity.Article;
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

    public int publishArticle(String articleId, String username) {
        return articleMapper.publish(articleId, username,true);
    }
}
