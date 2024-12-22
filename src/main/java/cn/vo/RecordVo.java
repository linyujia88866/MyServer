package cn.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordVo {
    private Integer spend;
    private Integer id;
    private String name;
    private String comment;
    private Date date;
}

