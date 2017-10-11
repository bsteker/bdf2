package com.bstek.bdf.plugins.pojo2datatype.generator;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bstek.bdf.plugins.pojo2datatype.model.Node;
import com.bstek.bdf.plugins.pojo2datatype.utils.ClassHelper;

/**
 * 表示DataType,用于生成DataType元素
 * @author Jake.Wang@bstek.com
 * @since Dec 22, 2012
 *
 */
public class DataType {
	private final static String DATA_TYPE_PREFIX = "dataType";
	private final static String JAVA_TYPE_PREFIX = "java.lang";
	private String name;
	private String creationType;
	public static boolean isCustomClassTypeFieldInterpreted = true;
	private Map<String, String> properties = new HashMap<String, String>();
	private static Map<String, String> objTypeMap = new HashMap<String, String>();
	
	static{
		objTypeMap.put("java.util.Date","Date");
		objTypeMap.put("java.sql.Date","Date");
		objTypeMap.put("java.sql.Time","Time");
		objTypeMap.put("java.sql.Timestamp","Date");
	}
	
	public DataType(String name, String creationType){
		this.name = name;
		this.creationType = creationType;
	}
	public void addProperty(String key, String value){
		properties.put(key, value);
	}
	
	public Map<String, String> getProperties() {
		return properties;
	}
	public String getCreationType() {
		return creationType;
	}
	public void setCreationType(String creationType) {
		this.creationType = creationType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static void setDataTypeList(List<Node> nodes,  List<DataType> dataTypeList) {
		for (Node node : nodes) {
			String packageName = node.getName();
			for (Node child : node.getChildren()) {
				DataType dataType = new DataType(DATA_TYPE_PREFIX + child.getName(), packageName + "." + child.getName());
				dataTypeList.add(dataType);
			}
		}
		setProperties(nodes, dataTypeList);
	}
	
	@SuppressWarnings("rawtypes")
	public static void setProperties(List<Node> nodes, List<DataType> dataTypeList) {
		List<DataType> dataTypeListCopy = getDataTypeListCopy(dataTypeList);
		for (DataType dataType : dataTypeList) {
			try {
				Class<?> clazz = ClassHelper.loadClass(dataType.getCreationType());
				for (Field field : clazz.getDeclaredFields()) {
					Class<?> fieldType = field.getType(); 
				    if(fieldType.isPrimitive() || fieldType.getName().startsWith(JAVA_TYPE_PREFIX)){
				    	dataType.addProperty(field.getName(), fieldType.getSimpleName());
				    }
				    
				    if(objTypeMap.get(fieldType.getName()) != null){
				    	dataType.addProperty(field.getName(), objTypeMap.get(fieldType.getName()));
				    }
				  
				    if(!isCustomClassTypeFieldInterpreted){
				    	continue;
				    }
				    if(fieldType.isAssignableFrom(List.class) || fieldType.isAssignableFrom(Set.class)){   
				             Type fc = field.getGenericType(); // 关键的地方，如果是List类型，得到其Generic的类型  
				             if(fc == null) continue;  
				             // 如果是泛型参数的类型   
				             if(fc instanceof ParameterizedType){   
				                   ParameterizedType pt = (ParameterizedType) fc;  
				                   // 得到泛型里的class类型对象。  
				                   Class genericClazz = (Class)pt.getActualTypeArguments()[0];
				                   for (DataType tmpDataType : dataTypeListCopy) {
										String creationType = tmpDataType.getCreationType();
										if(genericClazz.getName().equals(creationType)) {
											dataType.addProperty(field.getName(), "[" + tmpDataType.getName() + "]");
										}
									}
				             }   
				      }else{
				    	  for (DataType tmpDataType : dataTypeListCopy) {
								String creationType = tmpDataType.getCreationType();
								if(fieldType.getName().equals(creationType)){
									dataType.addProperty(field.getName(), tmpDataType.getName());
								}
							}
				      }
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
	
	private static List<DataType> getDataTypeListCopy(List<DataType> dataTypeList) {
		List<DataType> dataTypeListCopy = new ArrayList<DataType>();
		for(int i = 0; i<dataTypeList.size(); i++){
			dataTypeListCopy.add(null);
		}
		Collections.copy(dataTypeListCopy, dataTypeList);
		return dataTypeListCopy;
	}
}
