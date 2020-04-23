package com.cyb.generation.mybatis.generator.oracle;


import com.cyb.generation.mybatis.entity.DataBaseConfig;
import com.cyb.generation.mybatis.generator.SQLService;
import com.cyb.generation.mybatis.generator.TableSelector;

public class OracleService implements SQLService {

	@Override
	public TableSelector getTableSelector(DataBaseConfig dataBaseConfig) {
		return new OracleTableSelector(new OracleColumnSelector(dataBaseConfig), dataBaseConfig);
	}


}
