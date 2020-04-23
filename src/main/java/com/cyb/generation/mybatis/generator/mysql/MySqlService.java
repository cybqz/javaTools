package com.cyb.generation.mybatis.generator.mysql;

import com.cyb.generation.mybatis.entity.DataBaseConfig;
import com.cyb.generation.mybatis.generator.SQLService;
import com.cyb.generation.mybatis.generator.TableSelector;

public class MySqlService implements SQLService {

	@Override
	public TableSelector getTableSelector(DataBaseConfig dataBaseConfig) {
		return new MySqlTableSelector(new MySqlColumnSelector(dataBaseConfig), dataBaseConfig);
	}

}
