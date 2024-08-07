package cn.vo;
import lombok.Data;

@Data       // 自动生成getter/setter/toString/hashCode/equals等方法
public class ArticleVo {
    private String title;
    private String articleId;
    private String createdAt;
    private String username;
    private int readCount;
    private int likeCount;
    private int commentCount;
}