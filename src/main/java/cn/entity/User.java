package cn.entity;
import lombok.Data;
@Data       // 自动生成getter/setter/toString/hashCode/equals等方法
public class User {
    private String userId;
    private String username;
    private String password;
    private int authority;
    private int status;
    private long fileTotalSizeAllow;
}