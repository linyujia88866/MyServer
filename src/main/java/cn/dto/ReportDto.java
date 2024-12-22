package cn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {
	private Integer month;
	private Integer year;
	private String cname;
	private String comment;
	private Date date;
}
