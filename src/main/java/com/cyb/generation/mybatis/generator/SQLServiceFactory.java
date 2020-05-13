package com.cyb.generation.mybatis.generator;


import com.cyb.generation.common.Constant;
import com.cyb.generation.mybatis.entity.DataBaseConfig;
import com.cyb.generation.mybatis.generator.mysql.MySqlService;
import com.cyb.generation.mybatis.generator.oracle.OracleService;
import com.cyb.generation.mybatis.generator.sqlserver.SqlServerService;

import java.util.HashMap;
import java.util.Map;

public class SQLServiceFactory {

	private static Map<String, SQLService> SERVICE_MAP = new HashMap<String, SQLService>(20);

	public static SQLService build(DataBaseConfig dataBaseConfig) {
		String driverClass = dataBaseConfig.getDriverClass();
		SQLService service = SERVICE_MAP.get(driverClass);
		if (service == null) {
			service = findSqlService(driverClass);
			if (service != null) {
				SERVICE_MAP.put(driverClass, service);
			} else {
				throw new RuntimeException("本系统暂不支持该数据源(" + dataBaseConfig.getDriverClass() + ")");
			}
		}
		return service;
	}

	private static SQLService findSqlService(String driverClass) {
		if (driverClass.contains(Constant.DATA_BASE_MYSQL)) {
			return new MySqlService();
		}
		if (driverClass.contains(Constant.DATA_BASE_JTDS)) {
			return new SqlServerService();
		}
		if (driverClass.contains(Constant.DATA_BASE_SQLSERVER)) {
			return new SqlServerService();
		}
		if (driverClass.contains(Constant.DATA_BASE_ORACLE)) {
			return new OracleService();
		}
		return null;
	}
}
