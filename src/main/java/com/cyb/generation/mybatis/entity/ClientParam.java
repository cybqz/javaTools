package com.cyb.generation.mybatis.entity;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientParam {

	private List<String> excludeTableList = new ArrayList<String>();

	private String packageName;
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


	public List<String> getExcludeTableList() {
		return excludeTableList;
	}

	public void setExcludeTableList(List<String> excludeTableList) {
		this.excludeTableList = excludeTableList;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public boolean isShowSchema() {
		return showSchema;
	}

	public void setShowSchema(boolean showSchema) {
		this.showSchema = showSchema;
	}

	public boolean isUuid() {
		return uuid;
	}

	public void setUuid(boolean uuid) {
		this.uuid = uuid;
	}

	public boolean isLombok() {
		return lombok;
	}

	public void setLombok(boolean lombok) {
		this.lombok = lombok;
	}

	public Map<Object, Object> getParam() {
		return param;
	}

	public void setParam(Map<Object, Object> param) {
		this.param = param;
	}
	
}
