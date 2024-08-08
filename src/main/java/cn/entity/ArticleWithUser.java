package cn.entity;
import lombok.Data;

@Data       // 自动生成getter/setter/toString/hashCode/equals等方法
public class ArticleWithUser {
    private String articleId;
    private String userId;
}