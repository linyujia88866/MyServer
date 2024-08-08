package cn.dao;


import cn.entity.Article;
import cn.entity.ArticleWithUser;
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
    @Select("select title, createdAt,content, username, publish, readCount, likeCount, commentCount from articles where articleId = #{articleId}  ")
    Article findById(String articleId);

    @Select("select articleId, title, createdAt, readCount, likeCount, commentCount from articles where username = #{username} ORDER BY createdAt DESC")
    List<ArticleVo> getAllArticles(String username);

    @Select("select articleId, title, createdAt, readCount, likeCount, commentCount from articles where username = #{username} and publish = #{publish} ORDER BY createdAt DESC")
    List<ArticleVo> getAllArticles_(String username, int publish);

    @Select("select articleId, title, createdAt, username, readCount, likeCount, commentCount from articles where publish = 1 ORDER BY createdAt DESC")
    List<ArticleVo> getAllPubArticles_();
    //新增
    @Insert("insert into articles(articleId, title, createdAt, content, username, publish)" +
            "values  (#{articleId},#{title},#{createdAt},#{content},#{username},#{publish})")
    void add(String articleId, String title, Timestamp createdAt, String content, String username, boolean publish);

    @Update("UPDATE articles SET title = #{title}, createdAt = #{createdAt}, content= #{content} WHERE articleId = #{articleId}")
    int update(String articleId, String title, Timestamp createdAt, String content, String username);

    @Update("UPDATE articles SET readCount = readCount + 1 WHERE articleId = #{articleId}")
    int addOneRead(String articleId);

    @Update("UPDATE articles SET likeCount = likeCount + 1 WHERE articleId = #{articleId}")
    int addOneLike(String articleId);

    @Insert("insert into articles_users(articleId, userId) values (#{articleId},#{user_id})")
    int addLikeToArt(String articleId, String user_id);

    @Select("select * from articles_users where articleId = #{articleId} and  userId = #{user_id}")
    ArticleWithUser checkLikeToArt(String articleId, String user_id);

    @Update("UPDATE articles SET likeCount = likeCount - 1 WHERE articleId = #{articleId}")
    int deductOneLike(String articleId);


    @Update("UPDATE articles SET publish = #{publish} WHERE articleId = #{articleId} and username = #{username}")
    int publish(String articleId, String username, boolean publish);
}
