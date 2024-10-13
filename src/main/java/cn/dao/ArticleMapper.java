package cn.dao;


import cn.entity.Article;
import cn.entity.ArticleWithUser;
import cn.vo.ArticleVo;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface ArticleMapper {

    //查询
    @Select("select title, createdAt,content, username, publish, readCount, likeCount,goodCount, commentCount from articles where articleId = #{articleId}  ")
    Article findById(String articleId);

    @Select("select articleId, title, createdAt, readCount, likeCount, commentCount from articles where username = #{username} ORDER BY createdAt DESC")
    List<ArticleVo> getAllArticles(String username);

    @Select("select articleId, title, createdAt, readCount, likeCount,goodCount, commentCount from articles " +
            "where username = #{username} and publish = #{publish} ORDER BY createdAt DESC " +
            "LIMIT #{limit} OFFSET #{offset}")
    List<ArticleVo> getAllArticles_(String username, int publish, Integer limit, Integer offset);

    @Select("select COUNT(*) from articles " +
            "where username = #{username} and publish = #{publish} ORDER BY createdAt DESC ")
    Integer getAllArticlesCount_(String username, int publish);

    @Select("select articleId, title, createdAt, username, readCount, likeCount,goodCount, commentCount from articles " +
            "where publish = 1 and username <> 'admin' ORDER BY createdAt DESC LIMIT #{limit} OFFSET #{offset}")
    List<ArticleVo> getAllPubArticles_(Integer limit, Integer offset);

    @Select("select COUNT(*) from articles where publish = 1 and username <> 'admin'")
    Integer getAllPubArticlesCount_();

    @Select("select articleId, title, createdAt, username, readCount, likeCount,goodCount, commentCount from articles " +
            "where publish = 1 and username <> 'admin' and title LIKE CONCAT('%', #{name}, '%')" +
            "ORDER BY createdAt DESC " +
            "LIMIT #{limit} OFFSET #{offset}")
    List<ArticleVo> getAllPubArticlesSearch_(Integer limit, Integer offset, String name);

    @Select("SELECT articles.* FROM users " +
            "JOIN articles_users_like ON users.userId = articles_users_like.userId " +
            "JOIN articles ON articles_users_like.articleId = articles.articleId " +
            "WHERE users.userId = #{userId} " +
            "ORDER BY createdAt DESC " +
            "LIMIT #{limit} OFFSET #{offset}")
    List<ArticleVo> getMyFavoriteArticles(String userId, Integer limit, Integer offset);

    @Select("SELECT COUNT(*) FROM users " +
            "JOIN articles_users_like ON users.userId = articles_users_like.userId " +
            "JOIN articles ON articles_users_like.articleId = articles.articleId " +
            "WHERE users.userId = #{userId} " +
            "ORDER BY createdAt DESC")
    Integer getMyFavoriteArticlesCount(String userId);
    //新增
    @Insert("insert into articles(articleId, title, createdAt, content, username, publish)" +
            "values  (#{articleId},#{title},#{createdAt},#{content},#{username},#{publish})")
    void add(String articleId, String title, Timestamp createdAt, String content, String username, boolean publish);

    @Update("UPDATE articles SET title = #{title}, createdAt = #{createdAt}, content= #{content} WHERE articleId = #{articleId}")
    int update(String articleId, String title, Timestamp createdAt, String content, String username);

    @Update("UPDATE articles SET readCount = readCount + 1 WHERE articleId = #{articleId}")
    int addOneRead(String articleId);

    @Insert("insert into articles_users_like(articleId, userId) values (#{articleId},#{user_id})")
    int addLikeToArt(String articleId, String user_id);

    @Delete("DELETE FROM articles_users_like WHERE articleId = #{articleId} and  userId = #{user_id}")
    int deductLikeToArt(String articleId, String user_id);

    @Select("select articleId from articles_users_like where articleId = #{articleId} and  userId = #{user_id}")
    ArticleWithUser checkLikeToArt(String articleId, String user_id);

    @Update("UPDATE articles SET likeCount = likeCount + 1 WHERE articleId = #{articleId}")
    int addOneLike(String articleId);

    @Update("UPDATE articles SET likeCount = CASE " +
            "WHEN likeCount > 1 THEN  likeCount - 1 " +
            "ELSE likeCount " +
            "END " +
            "WHERE articleId = #{articleId}")
    int deductOneLike(String articleId);

    @Insert("insert into articles_users_good(articleId, userId) values (#{articleId},#{user_id})")
    int addGoodToArt(String articleId, String user_id);

    @Delete("DELETE FROM articles_users_good WHERE articleId = #{articleId} and  userId = #{user_id}")
    int deductGoodToArt(String articleId, String user_id);

    @Select("select articleId from articles_users_good where articleId = #{articleId} and  userId = #{user_id}")
    ArticleWithUser checkGoodToArt(String articleId, String user_id);

    @Update("UPDATE articles SET goodCount = goodCount + 1 WHERE articleId = #{articleId}")
    int addOneGood(String articleId);

    @Update("UPDATE articles SET goodCount = CASE " +
            "WHEN goodCount > 1 THEN  goodCount - 1 " +
            "ELSE goodCount " +
            "END " +
            "WHERE articleId = #{articleId}")
    int deductOneGood(String articleId);


    @Update("UPDATE articles SET publish = #{publish} WHERE articleId = #{articleId}")
    int publish(String articleId, String username, boolean publish);

    @Update("UPDATE articles SET publish = #{publish} WHERE articleId = #{articleId}")
    int cancelPublish(String articleId, String username, boolean publish);
}
