/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.view.entity;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.velocity.VelocityContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import com.bstek.bdf2.rapido.RapidoAppJdbcDao;
import com.bstek.bdf2.rapido.common.RuleSetHelper;
import com.bstek.bdf2.rapido.common.VelocityHelper;
import com.bstek.bdf2.rapido.common.VelocityVariableRegister;
import com.bstek.bdf2.rapido.domain.Entity;
import com.bstek.bdf2.rapido.domain.EntityField;
import com.bstek.bdf2.rapido.domain.Mapping;
import com.bstek.bdf2.rapido.domain.MetaData;
import com.bstek.bdf2.rapido.domain.Parameter;
import com.bstek.bdf2.rapido.domain.Validator;
import com.bstek.bdf2.rapido.domain.ValidatorProperty;
import com.bstek.bdf2.rapido.key.IGenerator;
import com.bstek.bdf2.rapido.manager.EntityFieldManager;
import com.bstek.bdf2.rapido.manager.EntityManager;
import com.bstek.bdf2.rapido.manager.MappingManager;
import com.bstek.bdf2.rapido.manager.MetadataManager;
import com.bstek.bdf2.rapido.manager.ParameterManager;
import com.bstek.bdf2.rapido.manager.ValidatorManager;
import com.bstek.bdf2.rapido.view.entity.def.ValidatorDef;
import com.bstek.bdf2.rapido.view.wizard.def.ColumnDef;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.idesupport.model.Child;
import com.bstek.dorado.idesupport.model.Rule;
import com.bstek.dorado.idesupport.model.RuleSet;
import com.bstek.dorado.web.DoradoContext;

public class EntityPR extends RapidoAppJdbcDao{
	private EntityManager entityManager;
	private ParameterManager parameterManager;
	private EntityFieldManager entityFieldManager;
	private MappingManager mappingManager;
	private ValidatorManager validatorManager;
	private MetadataManager metadataManager;
	private RuleSetHelper ruleSetHelper;
	private VelocityHelper velocityHelper;
	private Collection<VelocityVariableRegister> velocityVariableRegisters;
	@SuppressWarnings("rawtypes")
	private Map<String,IGenerator> keyGeneratorMaps;
	@SuppressWarnings("unchecked")
	@Expose
	public Collection<ColumnDef> buildSQLFields(Map<String,Object> map) throws Exception{
		String querySql=(String)map.get("querySql");
		querySql=parseQuerySql(querySql,map);
		final String tableName=(String)map.get("tableName");
		Collection<Map<String,Object>> parameters=(Collection<Map<String,Object>>)map.get("parameters");
		MapSqlParameterSource sqlParameters=new MapSqlParameterSource();
		if(parameters!=null){
			for(Map<String,Object> pmap:parameters){
				sqlParameters.addValue((String)pmap.get("name"), pmap.get("value"));
			}
		}
		final List<String> primaryKeyList=this.loadTablePrimaryKeys(tableName);
		
		DataSource ds=this.getJdbcTemplate().getDataSource();
		Connection con = DataSourceUtils.getConnection(ds);
		Collection<ColumnDef> columns=null;
		try{
			final DatabaseMetaData databaseMetaData=con.getMetaData();
			columns=this.getNamedParameterJdbcTemplate().query(querySql, sqlParameters, new ResultSetExtractor<Collection<ColumnDef>>(){
				public Collection<ColumnDef> extractData(ResultSet rs) throws SQLException,DataAccessException {
					Collection<ColumnDef> result=new ArrayList<ColumnDef>();
					ResultSetMetaData rsmd = rs.getMetaData();
					int count = rsmd.getColumnCount();
					for (int i = 1; i <= count; i++) {
						ColumnDef col=new ColumnDef();
						col.setTableName(rsmd.getTableName(i));
						col.setName(rsmd.getColumnName(i));
						int columnType=rsmd.getColumnType(i);
						String type="String";
						if(columnType==Types.INTEGER || columnType ==Types.BIGINT || columnType==Types.SMALLINT || columnType==Types.TINYINT){
							type="int";
						}else if(columnType==Types.BOOLEAN){
							type="Boolean";
						}else if(columnType==Types.DATE){
							type="Date";
						}else if(columnType==Types.TIMESTAMP){
							type="DateTime";
						}else if(columnType==Types.TIME){
							type="Time";
						}else if(columnType==Types.DECIMAL || columnType==Types.NUMERIC){
							type="BigDecimal";
						}else if(columnType==Types.FLOAT){
							type="Float";
						}else if(columnType==Types.DOUBLE){
							type="double";
						}
						col.setLabel(getColumnRemarks(col.getTableName(),col.getName(),databaseMetaData));
						setPrimaryKey(col,tableName,primaryKeyList);
						col.setType(type);
						result.add(col);
					}
					return result;
				}
			});
			
		}finally{
			JdbcUtils.closeConnection(con);	
		}
		return columns;
	}
	
	
	private String getColumnRemarks(String tableName,String columnName,DatabaseMetaData databaseMetaData) throws SQLException{
		String remarks=null;
		ResultSet rs=null;
		try{
			rs=databaseMetaData.getColumns(null, null, tableName,"%");
			while(rs.next()){
				String colName=rs.getString("COLUMN_NAME");
				if(columnName.equals(colName)){
					remarks=rs.getString("REMARKS");
					break;
				}
			}
		}finally{
			if(rs!=null)rs.close();
		}
		return remarks;
	}
	
	@Expose
	public Collection<MetaData> matchMetadata(String fieldName) throws Exception{
		Page<MetaData> page=new Page<MetaData>(1000000,1);
		Map<String,Object> map=new HashMap<String,Object>();
		metadataManager.loadMetadataByPage(page, map);
		Collection<MetaData> metadatas=new ArrayList<MetaData>();
		for(MetaData md:page.getEntities()){
			int pos=md.getName().indexOf(fieldName);
			if(pos>-1){
				metadatas.add(md);
			}
		}
		return metadatas;
	}
	
	private void setPrimaryKey(ColumnDef column,String tableName,List<String> primaryKeyList){
		for(String key:primaryKeyList){
			if(column.getTableName().equals(tableName) && column.getName().equals(key)){
				column.setPrimaryKey(true);
				break;
			}
		}
	}
	
	private List<String> loadTablePrimaryKeys(String tableName)throws Exception{
		DataSource ds=this.getJdbcTemplate().getDataSource();
		Connection con = DataSourceUtils.getConnection(ds);
		List<String> primaryKeyList=new ArrayList<String>();
		Statement stmt = null;
		ResultSet rs=null;
		try{
			DatabaseMetaData metaData = con.getMetaData();
			rs = metaData.getPrimaryKeys(null, null, tableName.toUpperCase());
			while (rs.next()) {
				primaryKeyList.add(rs.getString("COLUMN_NAME"));
			}
		}finally{
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(stmt);
			JdbcUtils.closeConnection(con);
		}
		return primaryKeyList;
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider
	public Collection<Map<String,Object>> previewData(Map<String,Object> params){
		String querySql=(String)params.get("querySql");
		List<Map<String,Object>> parameters=(List<Map<String,Object>>)params.get("parameters");
		Map<String,Object> map=new HashMap<String,Object>();
		for(Map<String,Object> m:parameters){
			map.put((String)m.get("name"), m.get("value"));
		}
		querySql=parseQuerySql(querySql,map);
		return this.getNamedParameterJdbcTemplate().queryForList(querySql, map);
	}
	
	@Expose
	public Collection<Parameter> buildParameters(String querySql){
		String regex=":[^\\s\u4e00-\u9fa5][0-9a-zA-Z]([^\\s\u4e00-\u9fa5]*[0-9a-zA-Z])?";
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(querySql);
		Collection<Parameter> params=new ArrayList<Parameter>();
		while(matcher.find()){
			String str=matcher.group();
			Parameter p=new Parameter();
			p.setName(str.substring(1,str.length()));
			params.add(p);
		}
		return params;
	}
	@DataProvider
	public Collection<Map<String,Object>> loadKeyGenerators(){
		Collection<Map<String,Object>> generators=new ArrayList<Map<String,Object>>();
		for(String beanId:this.keyGeneratorMaps.keySet()){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("value", this.keyGeneratorMaps.get(beanId).desc());
			map.put("key", beanId);
			generators.add(map);
		}
		return generators;
	}
	
	@DataProvider
	public Collection<Entity> loadEntitys(Map<String,Object> map){
		if(map==null){
			map=new HashMap<String,Object>();
			String packageId=(String)DoradoContext.getCurrent().getAttribute(DoradoContext.VIEW,"packageId");
			map.put("packageId", packageId);
		}
		return entityManager.loadEntitys(map);
	}
	@DataProvider
	public Collection<ValidatorDef> loadValidatorDefs()throws Exception{
		RuleSet ruleSet=ruleSetHelper.getRuleSet();
		Rule rule=ruleSet.getRule("BasePropertyDef");
		if(rule==null){
			return null;			
		}
		Collection<ValidatorDef> validators=new ArrayList<ValidatorDef>();
		Child child=rule.getChild("Validators");
		for(Rule r:child.getConcreteRules()){
			ValidatorDef def=new ValidatorDef();
			def.setName(r.getName());
			validators.add(def);
		}
		return validators;
	}
	@DataProvider
	public Collection<ValidatorDef> loadValidatorDefProperties(String validatorName)throws Exception{
		RuleSet ruleSet=ruleSetHelper.getRuleSet();
		Rule rule=ruleSet.getRule(validatorName);
		if(rule==null){
			return null;			
		}
		Collection<ValidatorDef> validators=new ArrayList<ValidatorDef>();
		for(String name:rule.getProperties().keySet()){
			ValidatorDef def=new ValidatorDef();
			def.setName(name);
			validators.add(def);
		}
		return validators;
	}
	@DataProvider
	public Collection<MetaData> loadMetadata(String packageId){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("packageId", packageId);
		Page<MetaData> page=new Page<MetaData>(100000,1);
		metadataManager.loadMetadataByPage(page, map);
		return page.getEntities();
	}
	@DataProvider
	public Collection<Validator> loadValidators(String fieldId){
		return validatorManager.loadValidators(fieldId);
	}
	
	@DataProvider
	public Collection<ValidatorProperty> loadValidatorProperties(String validatorId){
		return validatorManager.loadValidatorProperties(validatorId);
	}
	
	@DataResolver
	public void saveEntitys(Collection<Entity> entitys){
		for(Entity entity:entitys){
			EntityState state=EntityUtils.getState(entity);
			if(state.equals(EntityState.NEW)){
				entityManager.insertEntity(entity);
			}
			if(state.equals(EntityState.MODIFIED)){
				entityManager.updateEntity(entity);
			}
			if(state.equals(EntityState.DELETED)){
				entityManager.deleteEntity(entity.getId());
			}
			if(entity.getParameters()!=null){
				for(Parameter p:entity.getParameters()){
					EntityState st=EntityUtils.getState(p);
					if(st.equals(EntityState.NEW)){
						parameterManager.insertParameter(p);
						entityManager.insertEntityParameter(entity.getId(), p.getId());
					}
					if(st.equals(EntityState.MODIFIED)){
						parameterManager.updateParameter(p);
					}
					if(st.equals(EntityState.DELETED)){
						entityManager.deleteEntityParameter(entity.getId(), p.getId());
						parameterManager.deleteParameter(p.getId());
					}
				}
			}
			if(entity.getEntityFields()!=null){
				for(EntityField field:entity.getEntityFields()){
					EntityState es=EntityUtils.getState(field);
					if(es.equals(EntityState.NEW)){
						field.setEntityId(entity.getId());
						entityFieldManager.insertEntityField(field);
					}
					if(es.equals(EntityState.MODIFIED)){
						entityFieldManager.updateEntityField(field);
					}
					if(es.equals(EntityState.DELETED)){
						entityFieldManager.deleteEntityField(field.getId());
					}
					if(field.getValidators()!=null){
						for(Validator v:field.getValidators()){
							v.setFieldId(field.getId());
							EntityState s=EntityUtils.getState(v);
							if(s.equals(EntityState.NEW)){
								validatorManager.insertValidator(v);
							}
							if(s.equals(EntityState.MODIFIED)){
								validatorManager.updateValidator(v);
							}
							if(s.equals(EntityState.DELETED)){
								validatorManager.deleteValidator(v.getId());
							}
							if(v.getProperties()!=null){
								for(ValidatorProperty vp:v.getProperties()){
									vp.setValidatorId(v.getId());
									EntityState entityState=EntityUtils.getState(vp);
									if(entityState.equals(EntityState.NEW)){
										validatorManager.insertValidatorProperty(vp);
									}
									if(entityState.equals(EntityState.MODIFIED)){
										validatorManager.updateValidatorProperty(vp);
									}
									if(entityState.equals(EntityState.DELETED)){
										validatorManager.deleteValidatorProperty(vp.getId());
									}
								}
							}
						}
					}
				}
			}
			if(entity.getChildren()!=null){
				this.saveEntitys(entity.getChildren());
			}
		}
	}
	
	@DataProvider
	public Collection<Mapping> loadMappings(Map<String,Object> map){
		return mappingManager.loadMappings(map);
	}
	
	@DataProvider
	public Collection<Map<String,Object>> loadVariables(){
		Collection<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		Map<String,Object> m=new HashMap<String,Object>();
		m.put("name", "parameters");
		m.put("desc", "系统内置的Velocity变量，该变量中包含所有可能传入的查询条件， 通过对parameters变量中是否包含某个查询条件的值的判断，以实现SQL查询条件的动态拼装");
		list.add(m);
		
		for(VelocityVariableRegister reg:velocityVariableRegisters){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("name", reg.getVariableName());
			map.put("desc", reg.getVariableDesc());
			list.add(map);
		}
		return list;
	}

	public EntityFieldManager getEntityFieldManager() {
		return entityFieldManager;
	}

	public void setEntityFieldManager(EntityFieldManager entityFieldManager) {
		this.entityFieldManager = entityFieldManager;
	}

	public ParameterManager getParameterManager() {
		return parameterManager;
	}

	public void setParameterManager(ParameterManager parameterManager) {
		this.parameterManager = parameterManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	public MappingManager getMappingManager() {
		return mappingManager;
	}
	public void setMappingManager(MappingManager mappingManager) {
		this.mappingManager = mappingManager;
	}
	public ValidatorManager getValidatorManager() {
		return validatorManager;
	}
	public void setValidatorManager(ValidatorManager validatorManager) {
		this.validatorManager = validatorManager;
	}
	public RuleSetHelper getRuleSetHelper() {
		return ruleSetHelper;
	}
	public void setRuleSetHelper(RuleSetHelper ruleSetHelper) {
		this.ruleSetHelper = ruleSetHelper;
	}

	public MetadataManager getMetadataManager() {
		return metadataManager;
	}

	public void setMetadataManager(MetadataManager metadataManager) {
		this.metadataManager = metadataManager;
	}

	private String parseQuerySql(String sql,Map<String,Object> parameters){
		VelocityContext context=new VelocityContext();
		context.put("parameters", parameters);
		if(velocityVariableRegisters!=null && velocityVariableRegisters.size()>0){
			for(VelocityVariableRegister reg:velocityVariableRegisters){
				context.put(reg.getVariableName(),reg.getVariable());
			}			
		}
		StringWriter writer=new StringWriter();
		velocityHelper.getVelocityEngine().evaluate(context, writer, "querySql", sql.toString());
		return writer.toString();
	}
	
	public VelocityHelper getVelocityHelper() {
		return velocityHelper;
	}

	public void setVelocityHelper(VelocityHelper velocityHelper) {
		this.velocityHelper = velocityHelper;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		super.setApplicationContext(applicationContext);
		this.keyGeneratorMaps=applicationContext.getBeansOfType(IGenerator.class);
		velocityVariableRegisters=applicationContext.getBeansOfType(VelocityVariableRegister.class).values();
	}
}
