package com.cyb.generation.mybatis.generator.mysql;

import com.cyb.generation.mybatis.entity.DataBaseConfig;
import com.cyb.generation.mybatis.generator.ColumnSelector;
import com.cyb.generation.mybatis.generator.TableDefinition;
import com.cyb.generation.mybatis.generator.TableSelector;

import java.util.Map;

/**
 * 查询mysql数据库表
 */
public class MySqlTableSelector extends TableSelector {

	public MySqlTableSelector(ColumnSelector columnSelector,
							  DataBaseConfig dataBaseConfig) {
		super(columnSelector, dataBaseConfig);
	}
	
	

	@Override
	public String getAllTablesSQL(String dbName) {
		String sql = "SHOW TABLE STATUS FROM " + dbName;
		return sql;
	}



	@Override
	protected String getShowTablesSQL(String dbName) {
		String sql = "SHOW TABLE STATUS FROM " + dbName;
		if(this.getSchTableNames() != null && this.getSchTableNames().size() > 0) {
			StringBuilder tables = new StringBuilder();
			for (String table : this.getSchTableNames()) {
				tables.append(",'").append(table).append("'");
			}
			sql += " WHERE NAME IN (" + tables.substring(1) + ")";
		}
		return sql;
	}

	@Override
	protected TableDefinition buildTableDefinition(Map<String, Object> tableMap) {
		TableDefinition tableDefinition = new TableDefinition();
		tableDefinition.setTableName((String)tableMap.get("NAME"));
		tableDefinition.setComment((String)tableMap.get("COMMENT"));
		return tableDefinition;
	}

}
