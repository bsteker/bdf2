/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.common;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.hibernate.annotations.common.util.StringHelper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import bsh.EvalError;
import bsh.Interpreter;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.core.orm.ParseResult;
import com.bstek.bdf2.rapido.Constants;
import com.bstek.bdf2.rapido.RapidoAppJdbcDao;
import com.bstek.bdf2.rapido.action.IAction;
import com.bstek.bdf2.rapido.bsh.VariableInfo;
import com.bstek.bdf2.rapido.bsh.VariableRegister;
import com.bstek.bdf2.rapido.domain.KeyGenerateType;
import com.bstek.bdf2.rapido.key.IGenerator;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

/**
 * 为在线设计页面提供数据同时处理页面提交上来的数据
 * @author jacky.gao@bstek.com
 * @since 2012-7-19
 */
@SuppressWarnings("unchecked")
public class CommonPR extends RapidoAppJdbcDao{
	private VelocityHelper velocityHelper;
	private Collection<VariableRegister> variableRegisters;
	private Collection<VelocityVariableRegister> velocityVariableRegisters;
	@Expose
	public Map<String,Object> ajaxInvoke(com.bstek.dorado.data.resolver.DataResolver dataResolver,Map<String,Object> parameter) throws Exception{
		Map<String,Object> metaData=dataResolver.getMetaData();
		metaData.putAll(parameter);
		String actionBeanIds=(String)metaData.get(Constants.ACTION_BEANS_ID);
		Map<String,Object> map=this.executeActions(actionBeanIds, metaData);
		Map<String,Object> executeBeanshellScriptMap=executeBeanshellScript(metaData);
		if(executeBeanshellScriptMap!=null){
			map.putAll(executeBeanshellScriptMap);
		}
		return map;
	}
	
	
	@DataResolver
	public Map<String,Object> saveEntityData(com.bstek.dorado.data.resolver.DataResolver dataResolver,Object data,Map<String,Object> parameter) throws Exception{
		Map<String,Object> metaData=dataResolver.getMetaData();
		metaData.putAll(parameter);
		if(data instanceof Collection){
			//save multiple data
			Collection<Map<String,Object>> coll=(Collection<Map<String,Object>>)data;
			saveData(coll,metaData);
		}else if(data instanceof Map){
			//save single data
			Map<String,Object> mapData=(Map<String,Object>)data;
			System.out.println(mapData);
		}
		String actionBeanIds=(String)metaData.get(Constants.ACTION_BEANS_ID);
		Map<String,Object> map=this.executeActions(actionBeanIds, metaData);
		Map<String,Object> executeBeanshellScriptMap=executeBeanshellScript(metaData);
		if(executeBeanshellScriptMap!=null){
			map.putAll(executeBeanshellScriptMap);
		}
		return map;
	}
	
	private Map<String,Object> executeBeanshellScript(Map<String,Object> parameter){
		Map<String,Object> map=null;
		String beanShellScript=(String)parameter.get(Constants.BEAN_SHELL_SCRIPT);
		if(StringHelper.isNotEmpty(beanShellScript)){
			try{
				Object obj=generatorInterpreter(parameter).eval(beanShellScript);
				if(obj!=null){
					if(!(obj instanceof Map)){
						throw new RuntimeException("BeanShell脚本:\r\r"+beanShellScript+"\r\r返回值是一个"+obj.getClass().getName()+"对象，而不是一个Map类型的对象！");
					}else{
						map=(Map<String,Object>)obj;
					}				
				}
			}catch(EvalError e){
				throw new RuntimeException("BeanShell脚本:\r\r"+beanShellScript+"\r\r执行失败",e);
			}
		}
		return map;
	}
	
	private Interpreter generatorInterpreter(Map<String,Object> parameter){
		Interpreter interpreter=new Interpreter();
		try {
			interpreter.set("parameter", parameter);
			for(VariableRegister variableRegister:variableRegisters){
				for(VariableInfo var:variableRegister.register().values()){
						interpreter.set(var.getName(), var.getVariableExecutor().execute());
				}
			}
		} catch (EvalError e) {
			throw new RuntimeException(e);
		}
		return interpreter;
	}

	
	private void saveData(Collection<Map<String,Object>> data,Map<String,Object> parameter){
		for(Map<String,Object> map:data){
			EntityState es=EntityUtils.getState(map);
			if(es.equals(EntityState.NEW)){
				this.insertData(map,parameter);
			}
			if(es.equals(EntityState.DELETED)){
				this.deleteData(map,parameter);
			}
			if(es.equals(EntityState.MODIFIED)){
				this.updateData(map,parameter);
			}
			for(String key:map.keySet()){
				Object obj=map.get(key);
				if(obj instanceof Collection){
					Collection<Map<String,Object>> children=(Collection<Map<String,Object>>)obj;
					this.saveData(children,parameter);
				}
			}
		}
	}
	
	private Map<String,Object> executeActions(String actionBeanIds,Map<String,Object> parameter){
		Map<String,Object> map=new HashMap<String,Object>();
		if(StringHelper.isEmpty(actionBeanIds))return map;
		String[] actions=actionBeanIds.split(",");
		for(String beanId:actions){
			IAction action=ContextHolder.getBean(beanId);
			Map<String,Object> resultMap=action.execute(parameter);
			if(resultMap!=null){
				map.putAll(resultMap);				
			}
		}
		return map;
	}
	
	private void insertData(Map<String,Object> map,Map<String,Object> parameter){
		String entityTablePrimaryKeys=(String)parameter.get(Constants.ENTITY_TABLE_PRIMARY_KEYS);
		String entityTableName=(String)parameter.get(Constants.ENTITY_TABLE_NAME);
		Collection<Map<String,Object>> entityFields=(Collection<Map<String,Object>>)parameter.get(Constants.ENTITY_FIELDS);
		StringBuffer sb=new StringBuffer();
		StringBuffer valuesSB=new StringBuffer();
		sb.append("insert into "+entityTableName+"(");
		if(StringHelper.isEmpty(entityTablePrimaryKeys)){
			throw new IllegalArgumentException(entityTableName+"中未定义主键，无法新增数据！");
		}
		String[] primaryKeys=entityTablePrimaryKeys.split(",");
		if(primaryKeys.length==0){
			throw new IllegalArgumentException(entityTableName+"中未定义主键，无法新增数据！");
		}
		int i=0;
		List<Object> params=new ArrayList<Object>();
		for(String field:map.keySet()){
			Map<String,Object> entityField=this.retriveEntityField(entityFields, field);
			if(entityField==null)continue;
			boolean key=false;
			for(String primaryKey:primaryKeys){
				if(field.equals(primaryKey)){
					key=true;
					break;
				}
			}
			if(i>0){
				sb.append(",");					
				valuesSB.append(",");					
			}
			if(key){
				//调用主键生成器生成主键值
				if(!entityField.get("keyGenerateType").toString().equals(KeyGenerateType.autoincrement.toString())){
					valuesSB.append("?");
					String beanId=(String)entityField.get("keyGenerator");
					if(StringHelper.isNotEmpty(beanId)){
						sb.append(field);
						IGenerator<?> generator=(IGenerator<?>)ContextHolder.getBean(beanId);
						Object keyValue=generator.execute(parameter);
						map.put(field, keyValue);
						params.add(keyValue);				
						i++;
					}else{
						sb.append(field);
						params.add(map.get(field));				
						i++;
					}
				}
			}else if("true".equals(entityField.get("submittable").toString())){
				sb.append(field);
				valuesSB.append("?");
				params.add(map.get(field));				
				i++;
			}
		}
		sb.append(") values(");
		sb.append(valuesSB.toString());
		sb.append(")");
		this.getJdbcTemplate().update(sb.toString(),params.toArray());
	}
	
	private Map<String,Object> retriveEntityField(Collection<Map<String,Object>> entityFields,String fieldName){
		Map<String,Object> entityField=null;
		for(Map<String,Object> fieldMap:entityFields){
			if(fieldMap.get("name").toString().equals(fieldName)){
				entityField=fieldMap;
				break;
			}
		}
		return entityField;
	}
	
	private void updateData(Map<String,Object> map,Map<String,Object> parameter){
		String entityTablePrimaryKeys=(String)parameter.get(Constants.ENTITY_TABLE_PRIMARY_KEYS);
		String entityTableName=(String)parameter.get(Constants.ENTITY_TABLE_NAME);
		Collection<Map<String,Object>> entityFields=(Collection<Map<String,Object>>)parameter.get(Constants.ENTITY_FIELDS);
		StringBuffer sb=new StringBuffer();
		sb.append("update "+entityTableName+" set ");
		if(StringHelper.isEmpty(entityTablePrimaryKeys)){
			throw new IllegalArgumentException(entityTableName+"中未定义主键，无法新增数据！");
		}
		String[] primaryKeys=entityTablePrimaryKeys.split(",");
		if(primaryKeys.length==0){
			throw new IllegalArgumentException(entityTableName+"中未定义主键，无法新增数据！");
		}
		int i=0;
		List<Object> params=new ArrayList<Object>();
		for(String field:map.keySet()){
			Map<String,Object> entityField=this.retriveEntityField(entityFields, field);
			if(entityField==null)continue;
			boolean update=true;
			for(String primaryKey:primaryKeys){
				if(field.equals(primaryKey)){
					update=false;
					break;
				}
			}
			if(update){
				if(i>0){
					sb.append(",");					
				}
				sb.append(field+"=?");
				params.add(map.get(field));
				i++;
			}
		}
		sb.append(" where 1=1");
		for(String fieldName:primaryKeys){
			params.add(map.get(fieldName));
			sb.append(" and "+fieldName+"=?");
		}
		this.getJdbcTemplate().update(sb.toString(),params.toArray());
	}
	
	private void deleteData(Map<String,Object> map,Map<String,Object> parameter){
		String entityTablePrimaryKeys=(String)parameter.get(Constants.ENTITY_TABLE_PRIMARY_KEYS);
		String entityTableName=(String)parameter.get(Constants.ENTITY_TABLE_NAME);
		StringBuffer sb=new StringBuffer();
		sb.append("delete from "+entityTableName+" where 1=1");
		List<Object> params=new ArrayList<Object>();
		String[] primaryKeys=entityTablePrimaryKeys.split(",");
		if(primaryKeys.length==0){
			throw new IllegalArgumentException(entityTableName+"中未定义主键，无法新增数据！");
		}
		for(String fieldName:primaryKeys){
			params.add(map.get(fieldName));
			sb.append(" and "+fieldName+"=?");
		}
		this.getJdbcTemplate().update(sb.toString(),params.toArray());
	}
	
	@DataProvider
	public Collection<Map<String,Object>> mappingProvider(String querySql) throws Exception{
		return this.getJdbcTemplate().queryForList(querySql);
	}
	@DataProvider
	public Collection<Map<String,Object>> loadEntityData(com.bstek.dorado.data.provider.DataProvider dataProvider,Criteria criteria,Map<String,Object> parameter) throws Exception{
		Map<String,Object> metaData=dataProvider.getMetaData();
		metaData.putAll(parameter);
		String entityQuerySql = (String)metaData.get(Constants.QUERY_SQL);
		return loadData(metaData, entityQuerySql,criteria);
	}
	
	@DataProvider
	public Collection<Map<String,Object>> loadEntityData(com.bstek.dorado.data.provider.DataProvider dataProvider,Map<String,Object> parameter) throws Exception{
		Map<String,Object> metaData=dataProvider.getMetaData();
		metaData.putAll(parameter);
		String entityQuerySql = (String)metaData.get(Constants.QUERY_SQL);
		return loadData(metaData, entityQuerySql,null);
	}

	@DataProvider
	public void loadEntityData(com.bstek.dorado.data.provider.DataProvider dataProvider,Page<Map<String,Object>> page,Map<String,Object> parameter) throws Exception{
		Map<String,Object> metaData=dataProvider.getMetaData();
		metaData.putAll(parameter);
		String entityQuerySql = (String)metaData.get(Constants.QUERY_SQL);
		loadPageData(page, parameter, entityQuerySql,null);
	}
	@DataProvider
	public void loadEntityData(com.bstek.dorado.data.provider.DataProvider dataProvider,Criteria criteria,Page<Map<String,Object>> page,Map<String,Object> parameter) throws Exception{
		Map<String,Object> metaData=dataProvider.getMetaData();
		metaData.putAll(parameter);
		String entityQuerySql = (String)metaData.get(Constants.QUERY_SQL);
		loadPageData(page, parameter, entityQuerySql,criteria);
	}

	private void loadPageData(Page<Map<String, Object>> page,Map<String, Object> parameter, String entityQuerySql,Criteria criteria) {
		Map<String,Object> entityQueryParameters=(Map<String,Object>)parameter.get(Constants.ENTITY_QUERY_PARAMETERS);
		Map<String, Object> queryParam = retriveQueryParameters(parameter,entityQueryParameters);
		String querySql=parseQuerySql(entityQuerySql,parameter);
		ParseResult result=this.parseCriteria(criteria,true,"x");
		if(result!=null){
			querySql="select * from ("+querySql+") x where "+result.getAssemblySql();
			if(queryParam==null){
				queryParam=new HashMap<String,Object>();
			}
			queryParam.putAll(result.getValueMap());
		}
		if(queryParam==null){
			this.pagingQuery(page, querySql);
		}else{
			this.pagingQuery(page, querySql, queryParam);
		}
	}
	
	private Collection<Map<String, Object>> loadData(
			Map<String, Object> parameter, String entityQuerySql,Criteria criteria) {
		Map<String,Object> entityQueryParameters=(Map<String,Object>)parameter.get(Constants.ENTITY_QUERY_PARAMETERS);
		Map<String, Object> queryParam = retriveQueryParameters(parameter,entityQueryParameters);
		String querySql=parseQuerySql(entityQuerySql,parameter);
		ParseResult result=this.parseCriteria(criteria,true,"x");
		if(result!=null){
			querySql="select * from ("+querySql+") x where "+result.getAssemblySql().toString();
			if(queryParam==null){
				queryParam=new HashMap<String,Object>();
			}
			queryParam.putAll(result.getValueMap());
		}
		if(queryParam==null){
			return this.getJdbcTemplate().queryForList(querySql);
		}else{
			return this.getNamedParameterJdbcTemplate().queryForList(querySql, queryParam);			
		}
	}

	private Map<String, Object> retriveQueryParameters(
			Map<String, Object> parameter, Map<String,Object> map) {
		if(map==null || map.size()==0){
			return null;
		}
		Map<String,Object> queryParam=new HashMap<String,Object>();
		for(String pname:map.keySet()){
			if(parameter.containsKey(pname)){
				queryParam.put(pname,parameter.get(pname));
			}
		}
		return queryParam;
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
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		super.setApplicationContext(applicationContext);
		variableRegisters=applicationContext.getBeansOfType(VariableRegister.class).values();
		velocityVariableRegisters=applicationContext.getBeansOfType(VelocityVariableRegister.class).values();
	}

	public VelocityHelper getVelocityHelper() {
		return velocityHelper;
	}

	public void setVelocityHelper(VelocityHelper velocityHelper) {
		this.velocityHelper = velocityHelper;
	}
}
