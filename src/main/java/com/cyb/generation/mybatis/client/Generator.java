package com.cyb.generation.mybatis.client;

import com.alibaba.fastjson.JSON;
import com.cyb.generation.mybatis.entity.ClientParam;
import com.cyb.generation.mybatis.entity.DataBaseConfig;
import com.cyb.generation.mybatis.entity.TplInfo;
import com.cyb.generation.mybatis.generator.*;
import com.cyb.generation.util.FileUtil;
import com.cyb.generation.util.FormatUtil;
import com.cyb.generation.util.VelocityUtil;
import org.apache.velocity.VelocityContext;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;

public class Generator {

	private ClientParam clientParam;
	private String dest;

	public Generator(ClientParam clientParam, String dest) {
		this.clientParam = clientParam;
		this.dest = dest;
	}

	public void generateCode() {

		SQLContext sqlContext = this.buildClientSQLContextList();

		List<String> tplList = new ArrayList<>();
		tplList.add("mapper.tpl");
		tplList.add("mybatis.tpl");
		if(this.clientParam.isLombok()){

			tplList.add("entity_lombok.tpl");
		}else{
			tplList.add("entity.tpl");
		}

		System.out.println(this.clientParam.isLombok()?"\t使用Lombok":"\t未使用Lombok");

		setPackageName(sqlContext, clientParam.getPackageName());

		FileUtil.createFolder(this.dest);

		for (String tplFile : tplList) {
			String dest = "";
			TplInfo template = this.buildTempObj(tplFile.trim());
			if (template == null) {
				continue;
			}
			String content = doGenerator(sqlContext, template.getContent());
			String fileName = doGenerator(sqlContext, template.getFileName());
			String savePath = doGenerator(sqlContext, template.getSavePath());

			content = this.formatCode(fileName, content);

			if(fileName.indexOf(".java") > -1){

				dest += this.dest + "java/" + this.clientParam.getPackageName().replace(".", File.separator);
			}else if(fileName.indexOf(".xml") > -1){

				dest = this.dest + "resources/";
			}

			String saveDir = dest + File.separator + savePath;

			FileUtil.createFolder(saveDir);

			String filePathName = saveDir + File.separator +fileName;

			FileUtil.write(content, filePathName, clientParam.getCharset());
		}

	}

	private TplInfo buildTempObj(String tplFile) {
		if (StringUtils.isEmpty(tplFile)) {
			return null;
		}

		String json = FileUtil.readFromClassPath("/tpl/" + tplFile);
		if (StringUtils.isEmpty(json)) {
			return null;
		}

		TplInfo temp = JSON.parseObject(json, TplInfo.class);
		String contentFileName = temp.getContentFileName();

		if(StringUtils.isEmpty(contentFileName)) {
			contentFileName = tplFile + "_cont";
			temp.setContentFileName(contentFileName);
		}

		if (temp != null) {
			String contentClassPath = "/tpl/" + temp.getContentFileName();
			String content = FileUtil.readFromClassPath(contentClassPath);
			temp.setContent(content);
		}

		return temp;
	}

	/**
	 * 返回SQL上下文列表
	 *
	 * @return
	 */
	private SQLContext buildClientSQLContextList() {

		DataBaseConfig dataBaseConfig = this.clientParam.buildDataBaseConfig();
		List<String> excludeTableList = this.clientParam.getExcludeTableList();

		SQLService service = SQLServiceFactory.build(dataBaseConfig);
		TableSelector tableSelector = service.getTableSelector(dataBaseConfig);

		List<TableDefinition> tableDefinitions = tableSelector.getTableDefinitions();
		for (TableDefinition td : tableDefinitions) {
			for(String excludeTable : excludeTableList){
				if(excludeTable.equals(td.getTableName())){
					System.out.println(excludeTable + "\t已排除");
					continue;
				}
			}
			if(td.getPkColumn() == null) {
				System.err.println("表" + td.getTableName() + "没有设置主键");
			}
		}

		SQLContext context = new SQLContext(tableDefinitions.get(0));
		context.setParam(this.clientParam.getParam());

		return context;
	}
	
	private void setPackageName(SQLContext sqlContext, String packageName) {
		if (StringUtils.hasText(packageName)) {
			sqlContext.setPackageName(packageName);
		}
	}

	private String doGenerator(SQLContext sqlContext, String template) {
		if (template == null) {
			return "";
		}
		VelocityContext context = new VelocityContext();
		
		TableDefinition tableDefinition = sqlContext.getTableDefinition();

		List<ColumnDefinition> columnDefinitionList = tableDefinition.getColumnDefinitions();
		
		context.put("context", sqlContext);
		context.put("param", sqlContext.getParam());
		context.put("table", tableDefinition);
		context.put("pk", tableDefinition.getPkColumn());
		context.put("columns", columnDefinitionList);
		context.put("valuesColumns", getValuesColumns(columnDefinitionList));
		context.put("baseColumns", getBaseColumns(columnDefinitionList));
		context.put("baseColumnsListExcludePK", getBaseColumnsListExcludePK(columnDefinitionList));

		this.putImpls(context, sqlContext);
		this.putExt(context, sqlContext);

		return VelocityUtil.generate(context, template);
	}

	private String getValuesColumns(List<ColumnDefinition> columnDefinitionList){

		StringBuilder stringBuilder = new StringBuilder();

		for(ColumnDefinition columnDefinition : columnDefinitionList){
			stringBuilder.append(",#{").append(columnDefinition.getColumnName()).append("}");
		}

		String result = stringBuilder.toString();
		return result.substring(1, result.length());
	}

	/**
	 * 获取除主键外的Columns
	 * @param columnDefinitionList
	 * @return
	 */
	private String getBaseColumnsListExcludePK(List<ColumnDefinition> columnDefinitionList){

		StringBuilder stringBuilder = new StringBuilder();

		for(ColumnDefinition columnDefinition : columnDefinitionList){
			if(!columnDefinition.getIsPk()){
				stringBuilder.append(",t.").append(columnDefinition.getColumnName());
			}
		}

		String result = stringBuilder.toString();
		return result.substring(1, result.length());
	}

	/**
	 *获取BaseColumns
	 *
	 * @param columnDefinitionList
	 * @return
	 */
	private String getBaseColumns(List<ColumnDefinition> columnDefinitionList){

		StringBuilder stringBuilder = new StringBuilder();

		for(ColumnDefinition columnDefinition : columnDefinitionList){
			stringBuilder.append(",").append(columnDefinition.getColumnName());
		}

		String result = stringBuilder.toString();
		return result.substring(1, result.length());
	}

	@SuppressWarnings("unchecked")
	private void putImpls(VelocityContext context, SQLContext sqlContext) {
		Map<String, Collection<String>> impllMap = (Map<String, Collection<String>>)sqlContext.getParam().get("implMap");
		String javaBeanName = sqlContext.getJavaBeanName();
		Collection<String> impls = impllMap.get(javaBeanName);
		if(impls == null) {
			impls = Collections.emptyList();
		}
		context.put("impls", impls);
	}
	
	@SuppressWarnings("unchecked")
	private void putExt(VelocityContext context, SQLContext sqlContext) {
		String extClass = String.valueOf(sqlContext.getParam().get("globalExt"));
		Map<String, String> extMap = (Map<String, String>)sqlContext.getParam().get("extMap");
		String javaBeanName = sqlContext.getJavaBeanName();
		String overrideExtClass = extMap.get(javaBeanName);
		if(StringUtils.hasText(overrideExtClass)) {
			extClass = overrideExtClass;
		}
		context.put("extClass", extClass);
	}

	// 格式化代码
	private String formatCode(String fileName, String content) {
		if (fileName.endsWith(".xml")) {
			//return FormatUtil.formatXml(content);
		}
        if(fileName.toLowerCase().endsWith(".java")) {
            return FormatUtil.formatJava(content);
        }
		return content;
	}
}
