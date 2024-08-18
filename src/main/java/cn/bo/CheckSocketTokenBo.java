package cn.bo;
import lombok.Data;

@Data       // 自动生成getter/setter/toString/hashCode/equals等方法
public class CheckSocketTokenBo {
    private String reason;
    private boolean pass;
}