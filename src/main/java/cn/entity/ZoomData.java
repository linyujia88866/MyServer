package cn.entity;
import lombok.Data;
@Data       // 自动生成getter/setter/toString/hashCode/equals等方法
public class ZoomData {
    private long used;
    private long left;
    private long all;
}