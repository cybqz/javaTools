package com.cyb.generation.mybatis.entity;

import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ClientParam {

	private List<String> excludeTableList = new ArrayList<String>();

	private String basePath;
	private String packageName;
	private String controllerPackageName;
	private String charset = "UTF-8";

	private String dbName;
	private String driverClass;
	private String jdbcUrl;
	private String username;
	private String password;
	private boolean showSchema;

	private boolean lombok = false;
	private boolean uuid;
	private Map<Object,Object> param = null;

	public DataBaseConfig buildDataBaseConfig() {
		DataBaseConfig config = new DataBaseConfig();
		try {
			BeanUtils.copyProperties(config, this);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return config;
	}
}
