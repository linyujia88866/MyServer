package cn.vo;
import lombok.Data;

@Data       // 自动生成getter/setter/toString/hashCode/equals等方法
public class LinkVo {
    private long id;
    private String articleId;
    private String imgPath;
}