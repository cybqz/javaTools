package com.cyb.generation.mybatis.generator;


import com.cyb.generation.mybatis.entity.DataBaseConfig;

public interface SQLService {

	public TableSelector getTableSelector(DataBaseConfig dataBaseConfig);

}
