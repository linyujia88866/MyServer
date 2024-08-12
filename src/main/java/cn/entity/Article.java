package cn.entity;
import lombok.Data;
@Data       // 自动生成getter/setter/toString/hashCode/equals等方法
public class Article {
    private String articleId;
    private String title;
    private String createdAt;
    private String content;
    private String username;
    private short publish;
    private int readCount;
    private int likeCount;
    private int goodCount;
    private int commentCount;
}