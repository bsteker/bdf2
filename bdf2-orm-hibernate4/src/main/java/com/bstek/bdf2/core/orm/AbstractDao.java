package com.bstek.bdf2.core.orm;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Criterion;
import com.bstek.dorado.data.provider.Junction;
import com.bstek.dorado.data.provider.Or;
import com.bstek.dorado.data.provider.filter.FilterOperator;
import com.bstek.dorado.data.provider.filter.SingleValueFilterCriterion;

/**
 * @author Jacky.gao
 * @since 2013-4-27
 */
public abstract class AbstractDao implements IDao{
	protected String getModuleFixDataSourceName() {
		return null;
	}
	protected String getFixedCompanyId(){
		return Configure.getString(FIXED_COMPANY_ID);
	}
	
	protected String getDataSourceName(String dataSourcename){
		if(StringUtils.isEmpty(dataSourcename)){
			String currentDatasourceName=ContextHolder.getCurrentDataSourceName();
			if(StringUtils.isNotEmpty(getModuleFixDataSourceName())){
				currentDatasourceName=getModuleFixDataSourceName();
			}
			return currentDatasourceName;
		}else{
			return dataSourcename;
		}
	}
	
	/**
	 * @param criteria 要解析的目标Criteria对象
	 * @param useParameterName 在接装查询条件时是否采用参数名
	 * @param alias 别名字符串
	 * @return ParseResult对象，其中包含解析生成SQL拼装对象以及查询条件的值对象Map,其中key为查询字段名,value为具体条件值
	 */
	protected ParseResult parseCriteria(Criteria criteria,boolean useParameterName,String alias){
		int parameterNameCount=0;
		if(criteria==null || criteria.getCriterions().size()==0){
			return null;
		}
		ParseResult result=new ParseResult();
		StringBuffer sb=result.getAssemblySql();
		Map<String,Object> valueMap=result.getValueMap();
		int count=0;
		for(Criterion c : criteria.getCriterions()){
			if(count>0){
				sb.append(" and ");
			}
			count++;
			parameterNameCount=buildCriterion(sb, c,valueMap,parameterNameCount,useParameterName,alias);
		}
		return result;
	}

	private int buildCriterion(StringBuffer sb, Criterion c,Map<String,Object> valueMap,int parameterNameCount,boolean useParameterName,String alias) {
		if (c instanceof SingleValueFilterCriterion){
			parameterNameCount++;
			SingleValueFilterCriterion fc = (SingleValueFilterCriterion) c;
			String operator=buildOperator(fc.getFilterOperator());
			String propertyName=buildFieldName(fc.getProperty());
			if(StringUtils.isNotEmpty(alias)){
				sb.append(" "+alias+"."+propertyName);				
			}else{
				sb.append(" "+propertyName);								
			}
			sb.append(" "+processLike(operator)+" ");
			String prepareName=fc.getProperty()+"_"+parameterNameCount+"_";
			if(useParameterName){
				sb.append(" :"+prepareName+" ");				
			}else{
				sb.append(" ? ");								
			}
			if(operator.equals("like")){
				valueMap.put(prepareName,"%"+fc.getValue()+"%");								
			}else if(operator.startsWith("*")){
				valueMap.put(prepareName,"%"+fc.getValue());				
			}else if(operator.endsWith("*")){
				valueMap.put(prepareName,fc.getValue()+"%");				
			}else{
				valueMap.put(prepareName,fc.getValue());				
			}
		}
		if(c instanceof Junction){
			Junction jun=(Junction)c;
			String junction=" and ";
			if(jun instanceof Or){
				junction=" or ";
			}
			int count=0;
			Collection<Criterion> criterions=jun.getCriterions();
			if(criterions!=null){
				sb.append(" ( ");
				for(Criterion criterion:criterions){
					if(count>0){
						sb.append(junction);
					}
					count++;
					parameterNameCount=this.buildCriterion(sb, criterion,valueMap,parameterNameCount,useParameterName,alias);
				}
				sb.append(" ) ");
			}
		}
		return parameterNameCount;
	}
	
	protected String buildOperator(FilterOperator filterOperator){
		String operator="like";
		if(filterOperator!=null){
			operator=filterOperator.toString();
		}
		return operator;
	}
	private String processLike(String operator){
		String result=operator;
		if(operator.endsWith("*")){
			result=operator.substring(0,operator.length()-1);
		}
		if(operator.startsWith("*")){
			result=operator.substring(1,operator.length());
		}
		return result;
	}
	
	/**
	 * 子类可以覆盖该方法，以决定该如何根据给出的字段名构建拼装查询条件的字段名
	 * @param name 需要重新构建的查询字段名
	 * @return 返回构建好的字段名
	 */
	protected String buildFieldName(String name){
		return name;
	}
}
