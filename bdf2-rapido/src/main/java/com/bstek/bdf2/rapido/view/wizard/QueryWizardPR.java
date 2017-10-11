/*
 * This file is part of BDF
 * BDF,Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf2.rapido.view.wizard;

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
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import com.bstek.bdf2.rapido.RapidoAppJdbcDao;
import com.bstek.bdf2.rapido.domain.EntityField;
import com.bstek.bdf2.rapido.domain.Parameter;
import com.bstek.bdf2.rapido.view.wizard.def.ColumnDef;
import com.bstek.bdf2.rapido.view.wizard.def.JoinConditionDef;
import com.bstek.bdf2.rapido.view.wizard.def.OrderDef;
import com.bstek.bdf2.rapido.view.wizard.def.ParameterDef;
import com.bstek.bdf2.rapido.view.wizard.def.TableDef;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.web.DoradoContext;

public class QueryWizardPR extends RapidoAppJdbcDao{
	
	@DataResolver
	public Map<String,Object> buildTables(Collection<TableDef> tables){
		if(tables==null || tables.size()==0){
			return null;
		}
		TableDef table=tables.iterator().next();
		table.setMaster(true);
		StringBuffer queryFieldsSQL=new StringBuffer(1024);
		this.buildQueryFieldsSql(table, queryFieldsSQL);
		String fieldsSql=queryFieldsSQL.toString();
		if(fieldsSql.endsWith(",")){
			fieldsSql=fieldsSql.substring(0,fieldsSql.lastIndexOf(","));
		}
		StringBuffer querySql=new StringBuffer(1024);
		querySql.append("select ");
		querySql.append(fieldsSql);
		querySql.append(" \r ");
		querySql.append("from ");
		StringBuffer tableNameSB=new StringBuffer(1024);
		buildJoinTables(table,null,tableNameSB);
		querySql.append(tableNameSB.toString());
		
		StringBuffer parameterSB=new StringBuffer(1024);
		buildParameters(table,parameterSB);
		String parameter=parameterSB.toString();
		if(parameter.endsWith("and")){
			parameter=parameter.substring(0,parameter.lastIndexOf("and"));
		}
		if(!parameter.equals("")){
			querySql.append(" \r ");
			querySql.append("where "+parameter);			
		}
		
		StringBuffer orderSB=new StringBuffer(1024);
		buildOrders(table,orderSB);
		if(!orderSB.toString().equals("")){
			querySql.append(" \r ");
			querySql.append(orderSB.toString());
		}
		
		Collection<EntityField> fields=new ArrayList<EntityField>();
		String entityId=(String)DoradoContext.getCurrent().getAttribute(DoradoContext.VIEW,"entityId");
		buildEntityFields(table,fields,entityId);
		Collection<Parameter> parameters=new ArrayList<Parameter>();
		buildQueryParameters(table,parameters);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("querySql", querySql.toString());
		map.put("fields", fields);
		map.put("masterTable", table.getName());
		map.put("parameters", parameters);
		return map;
	}
	
	private void buildQueryParameters(TableDef table,Collection<Parameter> parameters){
		if(table.getParameters()!=null){
			for(ParameterDef p:table.getParameters()){
				if(p.getValue()!=null && p.getValue().startsWith(":")){
					Parameter param=new Parameter();
					param.setName(p.getValue().substring(1,p.getValue().length()));
					parameters.add(param);
				}
			}
		}
		if(table.getChildren()!=null){
			for(TableDef t:table.getChildren()){
				buildQueryParameters(t,parameters);
			}
		}
	}
	
	private void buildEntityFields(TableDef table,Collection<EntityField> fields,String entityId){
		if(table.getColumns()!=null){
			for(ColumnDef col:table.getColumns()){
				EntityField field=new EntityField();
				field.setName(col.getName());
				field.setEntityId(entityId);
				field.setTableName(table.getName());
				field.setDataType(col.getType());
				field.setLabel(col.getLabel());
				if(table.isMaster()){
					String primaryKeys=table.getPrimaryKeys();
					for(String key:primaryKeys.split(",")){
						if(key.equals(col.getName())){
							field.setPrimaryKey(true);
							break;
						}
					}
				}
				fields.add(field);
			}
		}
		if(table.getChildren()!=null){
			for(TableDef t:table.getChildren()){
				buildEntityFields(t,fields,entityId);
			}
		}
	}
	
	private void buildOrders(TableDef table,StringBuffer sb){
		if(table.getOrders()!=null){
			for(OrderDef order:table.getOrders()){
				if(sb.toString().equals("")){
					sb.append("order by ");
				}else{
					sb.append(",");					
				}
				sb.append(table.getAlias()+"."+order.getName()+" "+order.getOrderType());
			}
		}
		if(table.getChildren()!=null){
			for(TableDef t:table.getChildren()){
				buildOrders(t,sb);
			}
		}
	}
	
	private void buildParameters(TableDef table,StringBuffer sb){
		if(table.getParameters()!=null){
			for(ParameterDef p:table.getParameters()){
				if(!sb.toString().equals("")){
					sb.append(" and");					
				}
				sb.append(" "+table.getAlias()+"."+p.getName()+" "+p.getOperator()+" "+p.getValue());
			}
		}
		if(table.getChildren()!=null){
			for(TableDef t:table.getChildren()){
				buildParameters(t,sb);
			}
		}
	}
	
	private void buildJoinTables(TableDef table,String parentTableAlias,StringBuffer sb){
		if(table.getJoinConditions()!=null && table.getJoinType()!=null){
			int i=0;
			for(JoinConditionDef join:table.getJoinConditions()){
				if(parentTableAlias!=null){
					join.getSourceTable().setAlias(parentTableAlias);					
				}
				join.getJoinTable().setAlias(table.getAlias());
				if(i==0){
					sb.append(" "+table.getJoinType()+" join ");
					sb.append(table.getName()+" "+table.getAlias()+" on (");					
				}else{
					sb.append(" and ");
				}
				sb.append(join.getSourceTable().getAlias()+"."+join.getSourceField());
				sb.append("=");
				sb.append(join.getJoinTable().getAlias()+"."+join.getJoinField());
				i++;
			}
			if(table.getJoinConditions().size()>0){
				sb.append(") ");
			}
		}else{
			sb.append(table.getName()+" "+table.getAlias());
		}
		if(table.getChildren()!=null){
			for(TableDef t:table.getChildren()){
				buildJoinTables(t,table.getAlias(),sb);
			}
		}
	}
	
	private void buildQueryFieldsSql(TableDef table,StringBuffer sb){
		if(table.getColumns()==null)return;
		for(ColumnDef col:table.getColumns()){
			sb.append(table.getAlias()+"."+col.getName());				
			sb.append(",");
		}
		if(table.getChildren()!=null){
			for(TableDef t:table.getChildren()){
				buildQueryFieldsSql(t,sb);
			}
		}
	}
	
	@DataProvider
	public Collection<TableDef> loadDbTables() throws Exception{
		Collection<TableDef> result=new ArrayList<TableDef>();
		DataSource ds=this.getJdbcTemplate().getDataSource();
		Connection con = DataSourceUtils.getConnection(ds);
		Statement stmt = null;
		ResultSet rs=null;
		try{
			DatabaseMetaData metaData = con.getMetaData();
			String url=metaData.getURL();
			String schema=null;
			if(url.toLowerCase().contains("oracle")){
				schema=metaData.getUserName();
			}
			rs = metaData.getTables(null,schema, "%",new String[] { "TABLE" });
			while (rs.next()) {
				TableDef table=new TableDef();
				table.setName(rs.getString("TABLE_NAME"));
				table.setPrimaryKeys(loadTablePrimaryKeys(table.getName(),metaData));
				result.add(table);
			}
		}finally{
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(stmt);
			JdbcUtils.closeConnection(con);
		}
		return result;
	}
	@DataProvider
	public Collection<ColumnDef> loadTableColumns(String tableName) throws Exception{
		if(tableName==null)return null;
		Collection<ColumnDef> result=new ArrayList<ColumnDef>();
		DataSource ds=this.getJdbcTemplate().getDataSource();
		Connection con = DataSourceUtils.getConnection(ds);
		Statement stmt = null;
		ResultSet rs=null;
		try{
			con = DataSourceUtils.getConnection(ds);
			DatabaseMetaData databaseMetaData=con.getMetaData();
			stmt = con.createStatement();
			rs = stmt.executeQuery("select * from "+tableName);
			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			for (int i = 1; i <= count; i++) {
				ColumnDef col=new ColumnDef();
				col.setTableName(tableName);
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
				col.setType(type);
				result.add(col);
			}	
		}finally{
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(stmt);
			JdbcUtils.closeConnection(con);
		}
		return result;
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
	
	private String loadTablePrimaryKeys(String tableName,DatabaseMetaData metaData) throws Exception{
		String keys="";
		ResultSet rs = null;
		try {
			rs = metaData.getPrimaryKeys(null, null, tableName.toUpperCase());
			int i=0;
			while (rs.next()) {
				if(i>0){
					keys+=",";
				}
				i++;
				keys+=rs.getString("COLUMN_NAME");
			}
		} finally {
			JdbcUtils.closeResultSet(rs);
		}
		return keys;
	}
}
	
