package cn.dto;
import lombok.Data;
@Data       // 自动生成getter/setter/toString/hashCode/equals等方法
public class TaskDto {
    private String title;
    private String content;
}