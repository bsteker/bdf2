package com.bstek.bdf.plugins.pojo2datatype.generator.impl;

import com.bstek.bdf.plugins.pojo2datatype.generator.XMLGenerator;

/**
 * XMLGenerator对象工厂
 * @author Jake.Wang@bstek.com
 * @since Dec 21, 2012
 *
 */
public class XMLGeneratorFactory {

	/**
	 * 获取创建新的Model文件的XMLGenerator对象
	 * @return XMLGenerator对象
	 */
	public static XMLGenerator getXMLGenerator(){
		return new ModelXMLGenerator();
	}
	
	/**
	 * 获取将生成的Model数据追加到现有Model文件的XMLGenerator对象
	 * @param filePath 现有Model文件的路径
	 * @return XMLGenerator对象
	 */
	public static XMLGenerator getXMLGenerator(String filePath){
		return new ModelXMLAppender(filePath);
	}
}
