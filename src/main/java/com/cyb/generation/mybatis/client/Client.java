package com.cyb.generation.mybatis.client;

import com.cyb.generation.common.Constant;
import com.cyb.generation.mybatis.entity.ClientParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Client{
	
    public void gen() {

		String dest = System.getProperty("user.dir") +
					  Constant.DEFAULT_OUR_DIR +
					  DateFormatUtils.format(new Date(), "yyyy-MM-dd HH-mm-ss");

		System.out.println("生成中:\r\n\tdest:\t" + dest);

		ClientParam clientParam = new ClientParam();
		this.buildParam(clientParam);
		new Generator(clientParam, dest).generateCode();

		System.out.println("生成完毕");
	}

	private void buildParam(ClientParam clientParam) {

    	if(null == clientParam){
    		clientParam = new ClientParam();
		}
		// 加载全局配置
		Properties properties = new Properties();
		try {
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(Constant.GLOBLE_CONFIG);
			properties.load(inputStream);
		} catch (IOException e1) {
			System.out.println("读取文件错误");
			throw new RuntimeException(e1);
		}

		clientParam.setCharset(properties.getProperty("charset"));

		String excludeTableList = properties.getProperty("excludeTableList");
		if(StringUtils.isNotBlank(excludeTableList)){

			String[] excludeTableListArr = excludeTableList.split(",");
			clientParam.setExcludeTableList(Arrays.asList(excludeTableListArr));
		}

		clientParam.setPackageName(properties.getProperty("packageName"));
		clientParam.setLombok("true".equals(properties.getProperty("Lombok")));

		// jdbc
		clientParam.setDbName(properties.getProperty("dbName"));
		clientParam.setDriverClass(properties.getProperty("driverClass"));
		clientParam.setJdbcUrl(properties.getProperty("jdbcUrl"));
		clientParam.setUsername(properties.getProperty("username"));
		clientParam.setPassword(properties.getProperty("password"));
		clientParam.setShowSchema("true".equals(properties.getProperty("showSchema")));
		clientParam.setUuid("true".equals(properties.getProperty("uuid")));

		Map<String, Collection<String>> impllMap = this.buildImplMap(properties);
		properties.put("implMap", impllMap);
		
		Map<String, String> extMap = this.buildExtMap(properties);
		properties.put("extMap", extMap);

		clientParam.setParam(properties);
	}
	
	private Map<String, Collection<String>> buildImplMap(Properties properties) {
		String implMap = properties.getProperty("implMap", "");
		if(StringUtils.isBlank(implMap)) {
			return Collections.emptyMap();
		}
		Map<String, Collection<String>> map = new HashMap<>();
		// implMap=Student:Clonable,com.xx.PK;User:com.xx.BaseParam
		// ["Student:Clonable,com.xx.PK", "User:com.xx.BaseParam"]
		String[] arr = implMap.split(";");
		for (String block : arr) {
			this.addImpls(block, map, properties);
		}
		return map;
	}
	
	private Map<String, String> buildExtMap(Properties properties) {
		String implMap = properties.getProperty("extMap", "");
		if(StringUtils.isBlank(implMap)) {
			return Collections.emptyMap();
		}
		Map<String, String> map = new HashMap<>();
		// extMap=Student:Clonable;User:com.xx.BaseParam
		// ["Student:Clonable,com.xx.PK", "User:com.xx.BaseParam"]
		String[] arr = implMap.split(";");
		for (String block : arr) {
			this.addExt(block, map, properties);
		}
		return map;
	}
	
	private void addImpls(String block, Map<String, Collection<String>> map, Properties properties) {
		// Student:Clonable,com.xx.PK
		// ["Student", "Clonable,com.xx.PK"]
		String[] arr = block.split("\\:");
		String key = arr[0]; // Student
		String classNames = arr[1]; // Clonable,com.xx.PK
		
		String[] classArr = classNames.split(",");
		Set<String> value = new HashSet<>();
		for (String className : classArr) {
			value.add(className);
		}
		boolean isSerialable = "true".equals(properties.getProperty("serializable"));
		if(isSerialable) {
			value.add("java.io.Serializable");
		}
		
		map.put(key, value);
	}
	
	private void addExt(String block, Map<String, String> map, Properties properties) {
		// Student:Clonable
		// ["Student", "Clonable"]
		String[] arr = block.split("\\:");
		String key = arr[0]; // Student
		String className = arr[1]; // Clonable
		map.put(key, className);
	}
}
