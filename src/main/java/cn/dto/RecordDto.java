package cn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordDto {
	private Integer spend;
	private String cname;
	private String comment;
	private Date date;
}
