package cn.vo;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class FileVo {
    private String name;
    private long size;
    private ZonedDateTime time;
}
