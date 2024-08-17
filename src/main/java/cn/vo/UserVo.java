package cn.vo;
import lombok.Data;

@Data       // 自动生成getter/setter/toString/hashCode/equals等方法
public class UserVo {
    private String userId;
    private String username;
    private int authority;
    private int status;
}