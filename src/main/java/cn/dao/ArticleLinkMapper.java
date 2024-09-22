package cn.dao;

import cn.entity.Article;
import cn.vo.ArticleVo;
import cn.vo.LinkVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleLinkMapper {
    @Insert("insert into article_link(article_id, img_path) values (#{articleId},#{imgPath})")
    int insertLink(String articleId, String imgPath);

    @Insert("insert into article_link_each(article_id, img_path) values (#{articleId},#{imgPath})")
    int insertLinkEach(String articleId, String imgPath);

    @Select("select id, article_id as articleId, img_path as imgPath from article_link_each")
    List<LinkVo> getAllLink();

    //查询
    @Select("select * from article_link where article_id = #{articleId}  ")
    LinkVo findById(String articleId);

    @Delete("DELETE FROM article_link WHERE article_id = #{articleId}")
    int deleteById(String articleId);

    @Delete("DELETE FROM article_link_each WHERE img_path = #{imgPath}")
    int deleteByPath(String imgPath);
}
