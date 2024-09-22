package cn.vo;
import lombok.Data;

import java.sql.Timestamp;

@Data       // 自动生成getter/setter/toString/hashCode/equals等方法
public class ArticleVo {
    private String title;
    private String articleId;
    private Timestamp createdAt;
    private String username;
    private int readCount;
    private int goodCount;
    private int likeCount;
    private int commentCount;
}