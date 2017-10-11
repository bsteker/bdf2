package com.bstek.bdf.plugins.pojo2datatype.generator.impl;

import java.util.List;

import org.w3c.dom.Element;

import com.bstek.bdf.plugins.pojo2datatype.generator.DataType;

/**
 * 新Model文件生成器
 * @author Jake.Wang@bstek.com
 * @since Dec 25, 2012
 *
 */
public class ModelXMLGenerator extends AbstractXMLGenerator{
	public ModelXMLGenerator() {
		super();
		document = docBuilder.newDocument();
	}

	protected void buildDomcument(List<DataType> dataTypeList) {
		Element model = document.createElement("Model");
		document.appendChild(model);
		buildDataType(dataTypeList, model);
	}
}
