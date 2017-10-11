package com.bstek.bdf.plugins.pojo2datatype.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.bstek.bdf.plugins.pojo2datatype.generator.impl.XMLGeneratorFactory;
import com.bstek.bdf.plugins.pojo2datatype.model.Node;

/**
 * Model文件构建器
 * @author Jake.Wang@bstek.com
 * @since Dec 21, 2012
 * 
 */
public class ModelBuilder {
	private List<DataType> dataTypeList = new ArrayList<DataType>();

	public void build(List<Node> nodes, String filePath, boolean isNew) {
		DataType.setDataTypeList(nodes, dataTypeList);
		if (isNew) {
			create(filePath);
		} else {
			append(filePath);
		}
	}

	private void create(String filePath) {
		PrintWriter writer = createNewModelFile(filePath);
		try {
			XMLGeneratorFactory.getXMLGenerator()
					.generate(writer, dataTypeList);
		} finally {
			writer.close();
		}
	}

	private PrintWriter createNewModelFile(String filePath) {
		File newFile = new File(filePath);
		if (!newFile.exists()) {
			try {
				newFile.createNewFile();
				return new PrintWriter(new BufferedWriter(new FileWriter(
						newFile, true)));
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		throw new RuntimeException("Can't Create File!");
	}

	private void append(String filePath) {
		XMLGeneratorFactory.getXMLGenerator(filePath).generate(
				openExistingModelFile(filePath), dataTypeList);
	}

	private PrintWriter openExistingModelFile(String filePath) {
		try {
			return new PrintWriter(new BufferedWriter(new FileWriter(filePath,
					false)));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
