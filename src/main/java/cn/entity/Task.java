package cn.entity;
import lombok.Data;
@Data       // 自动生成getter/setter/toString/hashCode/equals等方法
public class Task {
    private String taskId;
    private String title;
    private String createdAt;
    private String content;
}