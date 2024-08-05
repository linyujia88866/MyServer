package cn.dao;


import cn.entity.Article;
import cn.vo.ArticleVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface ArticleMapper {

    //查询
    @Select("select title, createdAt,content , username from articles where articleId = #{articleId} and username = #{username} ")
    Article findById(String articleId, String username);

    @Select("select articleId, title, createdAt from articles where username = #{username} ORDER BY createdAt DESC")
    List<ArticleVo> getAllArticles(String username);
    //新增
    @Insert("insert into articles(articleId, title, createdAt, content, username)" +"values  (#{articleId},#{title},#{createdAt},#{content},#{username})")
    void add(String articleId, String title, Timestamp createdAt, String content, String username);

    @Update("UPDATE articles SET title = #{title}, createdAt = #{createdAt}, content= #{content} WHERE articleId = #{articleId} and username = #{username}")
    int update(String articleId, String title, Timestamp createdAt, String content, String username);
}
