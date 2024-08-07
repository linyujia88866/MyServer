package cn.dto;
import lombok.Data;
@Data       // 自动生成getter/setter/toString/hashCode/equals等方法
public class ArticleDto {
    private String title;
    private String content;
    private Boolean publish;
}