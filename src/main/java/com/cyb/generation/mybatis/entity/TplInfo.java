package com.cyb.generation.mybatis.entity;

import lombok.Data;

@Data
public class TplInfo {

	private String savePath;
	private String fileName;
	private String content;
	private String contentFileName; // 内容文件名,默认为模板文件名 + "_cont",如dao.tpl_cont
}
