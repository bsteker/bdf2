package com.bstek.bdf.plugins.pojo2datatype.generator;

import java.io.PrintWriter;
import java.util.List;

/**
 * Model XML生成器接口
 * @author Jake.Wang@bstek.com
 * @since Dec 21, 2012
 *
 */
public interface XMLGenerator {
	/**
	 * 根据DataType列表，生成Model XML并保存到文件
	 * @param writer
	 * @param dataTypeList
	 */
	public void generate(PrintWriter writer, List<DataType> dataTypeList);
}
