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

    public Article findById(String id, String username) {
        return articleMapper.findById(id, username);
    }

    public List<ArticleVo> getAllArticles(String username) {
        return articleMapper.getAllArticles(username);
    }

    public void saveArticle(String articleId, String title, Timestamp createdAt, String content, String username) {
        articleMapper.add(articleId, title,createdAt,content, username);
    }
    public int updateArticle(String articleId, String title, Timestamp createdAt, String content, String username) {
        return articleMapper.update(articleId, title,createdAt,content, username);
    }
}
