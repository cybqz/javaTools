package com.cyb.generation.mybatis.generator.oracle;

import com.cyb.generation.mybatis.entity.DataBaseConfig;
import com.cyb.generation.mybatis.generator.ColumnSelector;
import com.cyb.generation.mybatis.generator.TableDefinition;
import com.cyb.generation.mybatis.generator.TableSelector;

import java.util.Map;

public class OracleTableSelector extends TableSelector {

	public OracleTableSelector(ColumnSelector columnSelector, DataBaseConfig dataBaseConfig) {
		super(columnSelector, dataBaseConfig);
	}

	@Override
	public String getAllTablesSQL(String dbName) {
		return "select table_name as \"NAME\",comments as \"COMMENT\" from user_tab_comments ";
	}

	@Override
	protected String getShowTablesSQL(String dbName) {
		String sql = "select table_name as \"NAME\",comments as \"COMMENT\" from user_tab_comments ";
		if(this.getSchTableNames() != null && this.getSchTableNames().size() > 0) {
			StringBuilder tables = new StringBuilder();
			for (String table : this.getSchTableNames()) {
				tables.append(",'").append(table).append("'");
			}
			sql += " WHERE table_name IN (" + tables.substring(1) + ")";
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
