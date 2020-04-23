package com.cyb.generation.util;

import com.cyb.generation.mybatis.entity.DataBaseConfig;
import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;
import org.apache.ibatis.jdbc.SqlRunner;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 执行SQL语句的帮助类
 * @author hc.tang
 *
 */
public class SqlHelper {

	private static final String DRIVER = "driver";
	private static final String URL = "url";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";

	/**
	 * @param dataBaseConfig
	 * @param sql
	 * @param params
	 * @return
	 */
	public static  List<Map<String, Object>> runSql(DataBaseConfig dataBaseConfig, String sql,
													Map<String, Object> params) {
		
		DataSource dataSource = buildDataSource(dataBaseConfig);
		
		String runSql = buildSqlWithParams(dataSource, sql, params);
		String[] sqls = runSql.split(";");
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			SqlRunner runner = buildSqlRunner(conn);
			int sqlCount = sqls.length;
			if(sqlCount == 1) {
				return runner.selectAll(sqls[0],new Object[]{});
			}else {
				for (int i = 0; i < sqlCount-1; i++) {
					runner.run(sqls[i]);
				}
				return runner.selectAll(sqls[sqlCount - 1],new Object[]{});
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
			try {
				conn.setReadOnly(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}finally{
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// 参数绑定
	private static String buildSqlWithParams(DataSource dataSource,String sql,Map<String, Object> params){
		Configuration configuration = buildConfiguration(dataSource);
		
		TextSqlNode node = new TextSqlNode(sql);	
		
		DynamicSqlSource dynamicSqlSource = new DynamicSqlSource(configuration,node);
		
		BoundSql boundSql = dynamicSqlSource.getBoundSql(params);
		
		return boundSql.getSql();
	}
	
	public static  List<Map<String, Object>> runSql(DataBaseConfig dataBaseConfig, String sql) {
		return runSql(dataBaseConfig,sql,null);
	}
	
	private static SqlRunner buildSqlRunner(Connection connection){
		return new SqlRunner(connection);
	}
	
	private static DataSource buildDataSource(DataBaseConfig dataBaseConfig){
		Properties properties = new Properties();
		
		properties.setProperty(DRIVER, dataBaseConfig.getDriverClass());
		properties.setProperty(URL, dataBaseConfig.getJdbcUrl());
		properties.setProperty(USERNAME, dataBaseConfig.getUsername());
		properties.setProperty(PASSWORD, dataBaseConfig.getPassword());
		
		PooledDataSourceFactory pooledDataSourceFactory = new PooledDataSourceFactory();
		pooledDataSourceFactory.setProperties(properties);

		return pooledDataSourceFactory.getDataSource();
	}
	
	private static Configuration buildConfiguration(DataSource dataSource){
		TransactionFactory transactionFactory = new JdbcTransactionFactory();
		Environment environment = new Environment("development", transactionFactory, dataSource);
		
		return new Configuration(environment);
	}

}
