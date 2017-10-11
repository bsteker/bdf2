package com.bstek.bdf.plugins.pojo2datatype.generator.impl;

import java.util.List;

import org.w3c.dom.Element;

import com.bstek.bdf.plugins.pojo2datatype.generator.DataType;

/**
 * 指定Model文件追加器
 * @author Jake.Wang@bstek.com
 * @since Dec 25, 2012
 *
 */
public class ModelXMLAppender extends AbstractXMLGenerator{

	public ModelXMLAppender(String filePath) {
		super();
		try {
			document = docBuilder.parse(filePath);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	protected void buildDomcument(List<DataType> dataTypeList) {
		Element model = (Element) document.getFirstChild();
		buildDataType(dataTypeList, model);
	}
}
