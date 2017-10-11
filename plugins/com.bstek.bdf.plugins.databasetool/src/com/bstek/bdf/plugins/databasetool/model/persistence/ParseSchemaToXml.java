/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.model.persistence;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Connection;
import com.bstek.bdf.plugins.databasetool.model.ConnectionBendpoint;
import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.model.Table;
import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;

public class ParseSchemaToXml extends W3cParser {
	private final Schema schema;
	private Map<String, String> prettyIdMap = new HashMap<String, String>();
	private int counter = 1;
	private Document document;

	ParseSchemaToXml(Schema schema) {
		this.schema = schema;
	}

	protected void write(OutputStream outputStream) throws Exception {
		counter = 1;
		prettyIdMap.clear();
		document = getDocument();
		Element elementSchema = writeSchema(document, schema);
		writeTables(elementSchema, schema);
		writeConnections(elementSchema, schema);
		saveXml(outputStream, document);

	}

	private Element writeSchema(Document document, Schema schema) {
		Element elementSchema = document.createElement(Schema.SIMPLE_NAME);
		writeAttribute(elementSchema, Schema.DB_TYPE, schema.getCurrentDbType());
		writeElementConstraintsInfo(elementSchema, schema.getConstraints());
		document.appendChild(elementSchema);
		return elementSchema;
	}

	private void writeTables(Element elementSchema, Schema schema) {
		Element elementTable = null;
		for (Table table : schema.getTables()) {
			elementTable = document.createElement(Table.SIMPLE_NAME);
			writeAttribute(elementTable, BaseModel.ID, covertPrettyId(table.getId()));
			writeElementConstraintsInfo(elementTable, table.getConstraints());
			writeElement(elementTable, Table.NAME, table.getName());
			writeElement(elementTable, Table.LABEL, table.getLabel());
			writeElement(elementTable, Table.COMMENT, table.getComment());
			elementSchema.appendChild(elementTable);
			writeColums(elementTable, table);
		}
	}

	private void writeConnections(Element elementSchema, Schema schema) {
		for (Table table : schema.getTables()) {
			List<Connection> connections = table.getInConnections();
			Element elementConnection = null;
			for (Connection connection : connections) {
				elementConnection = document.createElement(Connection.SIMPLE_NAME);
				writeElement(elementConnection, Connection.TYPE, connection.getType());
				writeElement(elementConnection, Connection.CONSTRAINTNAME, connection.getConstraintName());
				writeElement(elementConnection, Connection.SOURCE, covertPrettyId(connection.getSource().getId()));
				writeElement(elementConnection, Connection.TARGET, covertPrettyId(connection.getTarget().getId()));
				writeElement(elementConnection, Connection.PKCOLUMN, covertPrettyId(connection.getPkColumn().getId()));
				writeElement(elementConnection, Connection.FKCOLUMN, covertPrettyId(connection.getFkColumn().getId()));
				writeBendpoints(elementConnection, connection.getBendpoints());
				elementSchema.appendChild(elementConnection);
			}
		}
	}

	private void writeBendpoints(Element elementConnection, List<Bendpoint> list) {
		ConnectionBendpoint b = null;
		for (Bendpoint bendpoint : list) {
			b = (ConnectionBendpoint) bendpoint;
			Element element = document.createElement(ConnectionBendpoint.SIMPLE_NAME);
			writeElement(element, ConnectionBendpoint.DX1, b.getFirstRelativeDimension().width);
			writeElement(element, ConnectionBendpoint.DY1, b.getFirstRelativeDimension().height);
			writeElement(element, ConnectionBendpoint.DX2, b.getSecondRelativeDimension().width);
			writeElement(element, ConnectionBendpoint.DY2, b.getSecondRelativeDimension().height);
			elementConnection.appendChild(element);
		}
	}

	private void writeColums(Element elementTable, Table table) {
		Element elementColumn = null;
		for (Column column : table.getColumns()) {
			elementColumn = document.createElement(Column.SIMPLE_NAME);
			writeAttribute(elementColumn, BaseModel.ID, covertPrettyId(column.getId()));
			writeElementConstraintsInfo(elementColumn, column.getConstraints());
			writeElement(elementColumn, Column.PK, column.isPk());
			writeElement(elementColumn, Column.FK, column.isFk());
			writeElement(elementColumn, Column.AUTOINCREMENT, column.isAutoIncrement());
			writeElement(elementColumn, Column.NAME, column.getName());
			writeElement(elementColumn, Column.LABEL, column.getLabel());
			writeElement(elementColumn, Column.TYPE, column.getType());
			writeElement(elementColumn, Column.LENGTH, column.getLength());
			writeElement(elementColumn, Column.DECIMALLENGTH, column.getDecimalLength());
			writeElement(elementColumn, Column.DEFAULTVALUE, column.getDefaultValue());
			writeElement(elementColumn, Column.NOTNULL, column.isNotNull());
			writeElement(elementColumn, Column.UNIQUE, column.isUnique());
			writeElement(elementColumn, Column.DEFAULTVALUE, column.getDefaultValue());
			writeElement(elementColumn, Column.COMMENT, column.getComment());
			elementTable.appendChild(elementColumn);
		}
	}

	private void writeElementConstraintsInfo(Element element, Rectangle constraints) {
		writeElement(element, BaseModel.X, constraints.getLocation().x);
		writeElement(element, BaseModel.Y, constraints.getLocation().y);
		writeElement(element, BaseModel.WIDTH, constraints.getSize().width());
		writeElement(element, BaseModel.HEIGHT, constraints.getSize().height());
	}

	private Element writeElement(Element root, String key, Object value) {
		return writeElement(document, root, key, value);
	}

	private String covertPrettyId(String id) {
		if (id != null) {
			String s = prettyIdMap.get(id);
			if (s == null) {
				String c = String.valueOf(counter++);
				prettyIdMap.put(id, c);
				return c;
			}
			return s;
		}
		return null;
	}

}
