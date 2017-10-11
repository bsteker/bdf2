/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.model.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.bstek.bdf.plugins.databasetool.model.Schema;

public class PersistenceHelper {

	public static InputStream convertObjectToXml(Schema schema) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			new ParseSchemaToXml(schema).write(byteArrayOutputStream);
			ByteArrayInputStream stream = new ByteArrayInputStream(byteArrayOutputStream.toString().getBytes("UTF-8"));
			return stream;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void convertXmlToObject(InputStream in,Schema schema) {
		try {
			new ParseXmlToSchema(schema).read(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
