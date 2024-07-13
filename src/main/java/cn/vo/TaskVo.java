package cn.vo;
import lombok.Data;

@Data       // 自动生成getter/setter/toString/hashCode/equals等方法
public class TaskVo {
    private String title;
    private String taskId;
    private String createdAt;
}