/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.exporter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bstek.bdf.plugins.databasetool.dialect.ColumnTypeConstants;
import com.bstek.bdf.plugins.databasetool.dialect.ColumnTypeUtils;
import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Connection;
import com.bstek.bdf.plugins.databasetool.model.Table;

public class ExportSchemaToJavaBeanBuilder {

	private boolean commentFlag = true;
	private boolean doradoPropertyDef;
	private boolean hibernateAnnotation;

	private ExportSchemaToJavaBeanBuilder() {
		
	}

	protected static ExportSchemaToJavaBeanBuilder getInstance() {
		return new ExportSchemaToJavaBeanBuilder();
	}

	protected JavaBeanProperty createJavaBeanProperty(String packageName, Table table) throws Exception {
		JavaBeanProperty bean = convertTable(packageName, table);
		StringBuffer sb = new StringBuffer();
		sb.append(generateJavaBeanPackage(bean));
		sb.append(generateJavaBeanImport(bean));
		sb.append(generateJavaBeanBodyStart(bean));
		sb.append(generateJavaBeanField(bean));
		sb.append(generateJavaBeanConstructor(bean));
		sb.append(generateJavaBeanConstructorWithField(bean));
		sb.append(generateJavaBeanFieldMethod(bean));
		sb.append(generateJavaBeanBodyEnd(bean));
		bean.setContent(sb.toString());
		return bean;
	}

	private String generateJavaBeanPackage(JavaBeanProperty bean) {
		StringBuffer sb = new StringBuffer();
		if (bean.getPackageName() != null) {
			sb.append("package " + bean.getPackageName() + ";\n\n");
		}
		return sb.toString();
	}

	private String generateJavaBeanImport(JavaBeanProperty bean) {
		StringBuffer sb = new StringBuffer();
		StringBuffer fieldString = new StringBuffer();
		for (JavaBeanFieldProperty field : bean.getFields()) {
			fieldString.append("\tpublic " + field.getType() + " " + field.getName() + ";\n");
		}

		sb.append("import java.io.Serializable;\n");
		if (hibernateAnnotation) {
			sb.append("import javax.persistence.*;\n");
		}
		if (fieldString.indexOf("BigDecimal") != -1) {
			sb.append("import java.math.BigDecimal;\n");
		}
		if (fieldString.indexOf("Date") != -1) {
			sb.append("import java.util.Date;\n");
		}
		if (fieldString.indexOf("Set") != -1) {
			sb.append("import java.util.HashSet;\n");
			sb.append("import java.util.Set;\n");
		}
		if (doradoPropertyDef && bean.getFields().size() > 0) {
			sb.append("import com.bstek.dorado.annotation.PropertyDef;\n");
		}
		return sb.toString();
	}

	private String generateJavaBeanBodyStart(JavaBeanProperty bean) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		if (commentFlag) {
			sb.append("/**\n");
			sb.append(" * ");
			sb.append(bean.getComment());
			sb.append("\n");
			sb.append(" */\n");
		}
		if (hibernateAnnotation) {
			sb.append("@Entity");
			sb.append("\n");
			sb.append("@Table(name = \"" + bean.getTableName() + "\")");
			sb.append("\n");
		}
		sb.append("public class " + bean.getClassName() + " implements Serializable { \n");
		sb.append("\n");
		sb.append("\tprivate static final long serialVersionUID = 1L;\n");
		return sb.toString();
	}

	private String generateJavaBeanField(JavaBeanProperty bean) {
		StringBuffer sb = new StringBuffer();
		Map<String, Object> map;
		for (JavaBeanFieldProperty field : bean.getFields()) {
			sb.append("\n");
			if (commentFlag) {
				sb.append("\t/**\n");
				sb.append("\t * ");
				sb.append(field.getComment().replaceAll("\\s*", ""));
				sb.append("\n");
				sb.append("\t */\n");
			}
			if (field.isSimple()) {
				if (doradoPropertyDef) {
					map = new LinkedHashMap<String, Object>();
					map.put("label", "\"" + field.getLabel() + "\"");
					map.put("description", "\"" + field.getComment().replaceAll("\\s*", "") + "\"");
					String data = splitJoin("@PropertyDef", map);
					sb.append(data);
					sb.append("\n");
				}
				sb.append("\tprivate " + field.getType() + " " + field.getName() + ";\n");
			} else {
				sb.append("\tprivate " + field.getType() + " " + field.getName() + " = new Hash" + field.getType() + "(0)" + ";\n");
			}
		}
		return sb.toString();
	}

	private String generateJavaBeanConstructor(JavaBeanProperty bean) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append("public ");
		sb.append(bean.getClassName());
		sb.append("(){super();}");
		return sb.toString();
	}

	private String generateJavaBeanConstructorWithField(JavaBeanProperty bean) {
		if (bean != null && bean.getFields().size() > 0) {
			StringBuffer sb = new StringBuffer();
			sb.append("\n");
			sb.append("public ");
			sb.append(bean.getClassName());
			sb.append("(");

			List<String> list = new ArrayList<String>();
			for (JavaBeanFieldProperty field : bean.getFields()) {
				list.add(field.getType() + " " + field.getName());
			}
			sb.append(splitJoin(list));
			sb.append(")");
			sb.append("{ super();");
			for (JavaBeanFieldProperty field : bean.getFields()) {
				sb.append("this." + field.getName() + "=" + field.getName());
				sb.append(";");
			}

			sb.append("}");
			return sb.toString();
		}
		return "";

	}

	private String generateJavaBeanFieldMethod(JavaBeanProperty bean) {
		StringBuffer sb = new StringBuffer();
		StringBuffer returnFieldBuffer = new StringBuffer();
		sb.append("\n");
		int i = 1;
		for (JavaBeanFieldProperty field : bean.getFields()) {
			String name = field.getName();
			if (i != 1) {
				returnFieldBuffer.append(",");
			}
			returnFieldBuffer.append(name + "=\" + " + name + " + \"");

			String type = field.getType();
			String setMethodName = "set" + capitalize(name);
			sb.append("\tpublic void " + setMethodName + "(" + type + " " + name + ") {\n");
			sb.append("\t\tthis." + name + " = " + name + ";\n");
			sb.append("\t}\n");
			sb.append("\n");

			if (!field.getExtraDatas().isEmpty() && hibernateAnnotation) {
				for (String data : field.getExtraDatas()) {
					sb.append("\t");
					sb.append(data);
					sb.append("\n");
				}
			}
			String getMethodName = " get" + capitalize(name);
			if (type.equals("boolean")) {
				getMethodName = " is" + capitalize(name);
			}
			sb.append("\tpublic " + type + getMethodName + "() {\n");
			sb.append("\t\treturn " + name + ";\n");
			sb.append("\t}\n");
			sb.append("\n");
			i++;
		}
		String returnString = "\"" + bean.getClassName() + " [" + returnFieldBuffer + "]\"";
		sb.append("\tpublic String toString() {\n");
		sb.append("\t\treturn " + returnString + ";\n");
		sb.append("\t}\n");
		sb.append("\n");
		return sb.toString();
	}

	private String generateJavaBeanBodyEnd(JavaBeanProperty bean) {
		StringBuffer sb = new StringBuffer();
		sb.append("}\n");
		return sb.toString();
	}

	private JavaBeanProperty convertTable(String packageName, Table table) {
		JavaBeanProperty bean = new JavaBeanProperty();
		if (packageName != null && packageName.length() > 0) {
			bean.setPackageName(packageName);
		}
		bean.setClassName(bigHumpName(table.getName()));
		bean.setTableName(table.getName());
		bean.setComment(table.getLabel() + ":" + table.getComment());
		JavaBeanFieldProperty field;
		Map<String, Object> map;
		for (Column column : table.getColumns()) {
			if (!column.isFk()) {
				field = new JavaBeanFieldProperty();
				field.setName(smallHumpName(column.getName()));
				field.setType(convetJavaType(column.getType()));
				field.setColumnName(column.getName());
				field.setLabel(column.getLabel());
				field.setComment(column.getLabel() + ":" + column.getComment());
				field.setPk(column.isPk() ? true : false);
				if (column.isPk()) {
					field.addExtraData("@Id");
				}
				if(column.isAutoIncrement()){
					field.addExtraData("@GeneratedValue(strategy = GenerationType.AUTO)");
				}
				if (field.getType().equals("Date")) {
					if (column.getType().equals(ColumnTypeConstants.DATE)) {
						field.addExtraData("@Temporal(TemporalType.DATE)");
					} else if (column.getType().equals(ColumnTypeConstants.TIME)) {
						field.addExtraData("@Temporal(TemporalType.TIME)");
					} else {
						field.addExtraData("@Temporal(TemporalType.TIMESTAMP)");
					}
				}
				map = new LinkedHashMap<String, Object>();
				map.put("name", "\"" + column.getName() + "\"");
				if (field.getType().equals("String")) {
					if(column.getType().toLowerCase().equals("enum")||column.getType().toLowerCase().equals("set")){
						map.put("length", 255);
					}else{						
						map.put("length", column.getLength());
					}
				} else if (field.getType().equals("Blob")) {
					map.put("columnDefinition ", "\"BLOB\"");
					field.addExtraData("@Lob");
					field.addExtraData("@Basic(fetch = FetchType.LAZY)");
					field.setType("byte[]");
				} else if (field.getType().equals("Clob")) {
					map.put("columnDefinition ", "\"CLOB\"");
					field.addExtraData("@Lob");
					field.addExtraData("@Basic(fetch = FetchType.EAGER)");
					field.setType("String");
				}
				if (column.isUnique()) {
					map.put("unique", column.isUnique());
				}
				if (column.isNotNull()) {
					map.put("nullable", !column.isNotNull());
				}
				field.addExtraData(splitJoin("@Column", map));
				field.setSimple(true);
				bean.addField(field);

			}
		}
		buildOneToOneJavaBeanProperty(bean, table);
		buildManyToOneJavaBeanProperty(bean, table);
		buildOneToManyJavaBeanProperty(bean, table);
		return bean;
	}

	private void buildOneToManyJavaBeanProperty(JavaBeanProperty bean, Table table) {
		List<Connection> cons = table.getOutConnections();
		JavaBeanFieldProperty field;
		Map<String, Object> map;
		for (Connection con : cons) {
			if (Connection.ONETOMANY.equals(con.getType())) {
				field = new JavaBeanFieldProperty();
				field.setName(smallHumpName(con.getTarget().getName()) + "Set");
				field.setType("Set<" + bigHumpName(con.getTarget().getName()) + ">");
				field.setComment(con.getTarget().getLabel() + ":" + con.getTarget().getComment());
				field.setSimple(false);
				map = new LinkedHashMap<String, Object>();
				map.put("fetch", "FetchType.LAZY");
				map.put("mappedBy", "\"" + smallHumpName(con.getSource().getName()) + "\"");
				String extraData = splitJoin("@OneToMany", map);
				field.addExtraData(extraData);
				bean.addField(field);
			}
		}
	}

	private void buildOneToOneJavaBeanProperty(JavaBeanProperty bean, Table table) {
		List<Connection> cons = table.getOutConnections();
		JavaBeanFieldProperty field;
		Map<String, Object> map;
		for (Connection con : cons) {
			if (Connection.ONETOONE.equals(con.getType())) {
				field = new JavaBeanFieldProperty();
				field.setName(smallHumpName(con.getTarget().getName()));
				field.setType(bigHumpName(con.getTarget().getName()));
				field.setComment(con.getTarget().getLabel() + ":" + con.getTarget().getComment());
				field.setSimple(true);
				map = new LinkedHashMap<String, Object>();
				map.put("fetch", "FetchType.LAZY");
				map.put("optional", "true");
				map.put("mappedBy", "\"" + smallHumpName(con.getSource().getName()) + "\"");
				String extraData = splitJoin("@OneToOne", map);
				field.addExtraData(extraData);
				bean.addField(field);
			}
		}
		for (Column column : table.getColumns()) {
			if (column.isFk()) {
				cons = table.getInConnections();
				for (Connection con : cons) {
					if (Connection.ONETOONE.equals(con.getType()) && column.equals(con.getFkColumn())) {
						field = new JavaBeanFieldProperty();
						field.setName(smallHumpName(con.getSource().getName()));
						field.setType(bigHumpName(con.getSource().getName()));
						field.setComment(con.getSource().getLabel() + ":" + con.getSource().getComment());
						field.setSimple(true);
						map = new LinkedHashMap<String, Object>();
						map.put("fetch", "FetchType.LAZY");
						map.put("optional", "false");
						String extraData = splitJoin("@OneToOne", map);
						field.addExtraData(extraData);
						field.addExtraData(createJoinColumnExtraData(column));
						bean.addField(field);
					}
				}
			}
		}
	}

	private void buildManyToOneJavaBeanProperty(JavaBeanProperty bean, Table table) {
		JavaBeanFieldProperty field;
		for (Column column : table.getColumns()) {
			if (column.isFk()) {
				List<Connection> cons = table.getInConnections();
				for (Connection con : cons) {
					if (Connection.ONETOMANY.equals(con.getType()) && column.equals(con.getFkColumn())) {
						field = new JavaBeanFieldProperty();
						field.setName(smallHumpName(con.getSource().getName()));
						field.setType(bigHumpName(con.getSource().getName()));
						field.setComment(con.getSource().getLabel() + ":" + con.getSource().getComment());
						field.setSimple(true);
						field.addExtraData(createManyToOneExtraData());
						field.addExtraData(createJoinColumnExtraData(column));
						bean.addField(field);
					}
				}
			}
		}

	}

	private static String convetJavaType(String type) {
		return ColumnTypeUtils.getJavaType(type);
	}

	private String createManyToOneExtraData() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("fetch", "FetchType.LAZY");
		return splitJoin("@ManyToOne", map);
	}

	private String createJoinColumnExtraData(Column column) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("name", "\"" + column.getName() + "\"");
		if (column.isUnique()) {
			map.put("unique", column.isUnique());
		}
		if (column.isNotNull()) {
			map.put("nullable", !column.isNotNull());
		}
		return splitJoin("@JoinColumn", map);
	}

	private String capitalizeable(String s, boolean capitalize) {
		if (s == null || s.length() == 0) {
			return s;
		}
		StringBuffer sb = new StringBuffer();
		if (capitalize) {
			sb.append(Character.toUpperCase(s.charAt(0)));
		} else {
			sb.append(Character.toLowerCase(s.charAt(0)));
		}
		if (s.length() > 0) {
			sb.append(s.substring(1));
		}
		return sb.toString();
	}

	private String bigHumpName(String s) {
		return capitalize(humpName(s));
	}

	private String smallHumpName(String s) {
		return uncapitalize(humpName(s));
	}

	private String humpName(String s) {
		StringBuilder sb = new StringBuilder();
		s = s.toLowerCase();
		boolean capitalize = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '_') {
				capitalize = true;
			} else {
				if (capitalize) {
					c = Character.toUpperCase(c);
					capitalize = false;
				}
				sb.append(c);
			}
		}
		return sb.toString();

	}

	private String splitJoin(String name, Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return name;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(name);
		sb.append("(");
		StringBuffer splitJoinString = new StringBuffer();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			splitJoinString.append(entry.getKey());
			splitJoinString.append("=");
			splitJoinString.append(entry.getValue().toString());
			splitJoinString.append(",");
		}
		String value = splitJoinString.subSequence(0, splitJoinString.length() - 1).toString();
		sb.append(value);
		sb.append(")");
		return sb.toString();
	}

	private String splitJoin(List<String> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		StringBuffer splitJoinString = new StringBuffer();
		for (String s : list) {
			splitJoinString.append(s);
			splitJoinString.append(",");
		}
		return splitJoinString.subSequence(0, splitJoinString.length() - 1).toString();
	}

	private String capitalize(String s) {
		return capitalizeable(s, true);
	}

	private String uncapitalize(String s) {
		return capitalizeable(s, false);
	}

	public boolean isGenerateCommentFlag() {
		return commentFlag;
	}

	public void setGenerateCommentFlag(boolean generateCommentFlag) {
		this.commentFlag = generateCommentFlag;
	}

	public boolean isDoradoPropertyDef() {
		return doradoPropertyDef;
	}

	public boolean isHibernateAnotation() {
		return hibernateAnnotation;
	}

	public void setHibernateAnotation(boolean hibernateAnotation) {
		this.hibernateAnnotation = hibernateAnotation;
	}

	public void setDoradoPropertyDef(boolean doradoPropertyDef) {
		this.doradoPropertyDef = doradoPropertyDef;
	}

}
