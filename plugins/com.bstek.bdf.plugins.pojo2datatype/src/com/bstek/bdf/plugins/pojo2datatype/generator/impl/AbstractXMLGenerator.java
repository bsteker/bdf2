package com.bstek.bdf.plugins.pojo2datatype.generator.impl;

import java.io.PrintWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bstek.bdf.plugins.pojo2datatype.generator.DataType;
import com.bstek.bdf.plugins.pojo2datatype.generator.XMLGenerator;

/**
 * 抽象Model XML 生成器
 * @author Jake.Wang@bstek.com
 * @since Dec 25, 2012
 *
 */
public abstract class AbstractXMLGenerator implements XMLGenerator {
	protected Document document;
	protected DocumentBuilder docBuilder;
	
	public AbstractXMLGenerator(){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			docBuilder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public void generate(PrintWriter writer, List<DataType> dataTypeList){
		buildDomcument(dataTypeList);
		write(writer);
	}
	
	protected abstract void buildDomcument(List<DataType> dataTypeList);
	
	protected void buildDataType(List<DataType> dataTypeList, Element model) {
		for (DataType dataTypeObj : dataTypeList) {
			String name = dataTypeObj.getName();
			Element dataType = document.createElement("DataType");
			dataType.setAttribute("name", name);
			model.appendChild(dataType);

			String creationType = dataTypeObj.getCreationType();
			Element property = document.createElement("Property");
			property.setAttribute("name", "creationType");
			property.appendChild(document.createTextNode(creationType));
			dataType.appendChild(property);

			for (String propertyName : dataTypeObj.getProperties().keySet()) {
				Element propertyDef = document.createElement("PropertyDef");
				propertyDef.setAttribute("name", propertyName);
				dataType.appendChild(propertyDef);

				Element propertyType = document.createElement("Property");
				propertyType.setAttribute("name", "dataType");
				propertyType.appendChild(document.createTextNode(dataTypeObj
						.getProperties().get(propertyName)));
				propertyDef.appendChild(propertyType);
			}
		}
	}

	protected void write(PrintWriter writer) {
		try {
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(writer);
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
