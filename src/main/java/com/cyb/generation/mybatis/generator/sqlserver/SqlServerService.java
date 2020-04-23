package com.cyb.generation.mybatis.generator.sqlserver;

import com.cyb.generation.mybatis.entity.DataBaseConfig;
import com.cyb.generation.mybatis.generator.SQLService;
import com.cyb.generation.mybatis.generator.TableSelector;

public class SqlServerService implements SQLService {

	@Override
	public TableSelector getTableSelector(DataBaseConfig dataBaseConfig) {
		return new SqlServerTableSelector(new SqlServerColumnSelector(dataBaseConfig), dataBaseConfig);
	}

}
