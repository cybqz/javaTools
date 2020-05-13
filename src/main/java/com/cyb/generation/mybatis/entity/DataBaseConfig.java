package com.cyb.generation.mybatis.entity;

import lombok.Data;
import java.util.Map;

@Data
public class DataBaseConfig {

	private String dbName;
	private String driverClass;
	private String jdbcUrl;
	private String username;
	private String password;
	private boolean uuid;
	private boolean showSchema = true;
	private Map<Object,Object> param = null;
}
