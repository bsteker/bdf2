/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2013, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.bstek.bdf.plugins.databasetool.model.persistence;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.bstek.bdf.plugins.databasetool.model.Column;
import com.bstek.bdf.plugins.databasetool.model.Connection;
import com.bstek.bdf.plugins.databasetool.model.ConnectionBendpoint;
import com.bstek.bdf.plugins.databasetool.model.Schema;
import com.bstek.bdf.plugins.databasetool.model.Table;
import com.bstek.bdf.plugins.databasetool.model.base.BaseModel;

public class ParseXmlToSchema extends W3cParser {
	private Schema schema;
	private Map<String, Table> tableMap = new HashMap<String, Table>();
	private Map<String, Column> columnMap = new HashMap<String, Column>();

	ParseXmlToSchema(Schema schema) {
		this.schema = schema;
	}

	protected void read(InputStream stream) throws Exception {
		Document document = getDocument(stream);
		readSchema(document, schema);
		readTables(document, schema);
		readConnections(document, schema);
	}

	private void readSchema(Document document, Schema schema) {
		Element elementSchema = document.getDocumentElement();
		String currentDbType = readAttribute(elementSchema, Schema.DB_TYPE);
		schema.setCurrentDbType(currentDbType);
		Rectangle r = readElementConstraintsInfo(elementSchema);
		schema.setConstraints(r);
	}

	private void readTables(Document document, Schema schema) {
		NodeList elementTables = document.getElementsByTagName(Table.SIMPLE_NAME);
		Table table = null;
		Element elementTable = null;
		for (int i = 0; i < elementTables.getLength(); i++) {
			elementTable = (Element) elementTables.item(i);
			table = new Table();
			table.setSchema(schema);
			table.setId(readAttribute(elementTable, BaseModel.ID));
			Rectangle r = readElementConstraintsInfo(elementTable);
			table.setConstraints(r);
			table.setName(readElement(elementTable, Table.NAME));
			table.setLabel(readElement(elementTable, Table.LABEL));
			table.setComment(readElement(elementTable, Table.COMMENT));
			tableMap.put(table.getId(), table);
			schema.addTable(table);
			readColumns(elementTable, table);
		}
	}

	private void readColumns(Element elementTable, Table table) {
		NodeList elementColumns = elementTable.getElementsByTagName(Column.SIMPLE_NAME);
		Column column = null;
		Element elementColumn = null;
		for (int i = 0; i < elementColumns.getLength(); i++) {
			elementColumn = (Element) elementColumns.item(i);
			column = new Column();
			column.setTable(table);
			column.setId(readAttribute(elementColumn, BaseModel.ID));
			column.setPk(Boolean.valueOf(readElement(elementColumn, Column.PK)));
			column.setFk(Boolean.valueOf(readElement(elementColumn, Column.FK)));
			column.setAutoIncrement(Boolean.valueOf(readElement(elementColumn, Column.AUTOINCREMENT)));
			column.setName(readElement(elementColumn, Column.NAME));
			column.setLabel(readElement(elementColumn, Column.LABEL));
			column.setType(readElement(elementColumn, Column.TYPE));
			column.setLength(readElement(elementColumn, Column.LENGTH));
			column.setDecimalLength(readElement(elementColumn, Column.DECIMALLENGTH));
			column.setDefaultValue(readElement(elementColumn, Column.DEFAULTVALUE));
			column.setNotNull(Boolean.valueOf(readElement(elementColumn, Column.NOTNULL)));
			column.setUnique(Boolean.valueOf(readElement(elementColumn, Column.UNIQUE)));
			column.setComment(readElement(elementColumn, Column.COMMENT));
			column.setTable(table);
			columnMap.put(column.getId(), column);
			table.addColumn(column);
		}
	}

	private void readConnections(Document document, Schema schema) {
		NodeList elementConnections = document.getElementsByTagName(Connection.SIMPLE_NAME);
		Connection connection = null;
		Element elementConnection = null;
		for (int i = 0; i < elementConnections.getLength(); i++) {
			elementConnection = (Element) elementConnections.item(i);
			connection = new Connection();
			String sourceId = readElement(elementConnection, Connection.SOURCE);
			String targetId = readElement(elementConnection, Connection.TARGET);
			String pk = readElement(elementConnection, Connection.PKCOLUMN);
			String fk = readElement(elementConnection, Connection.FKCOLUMN);
			String type=readElement(elementConnection, Connection.TYPE);
			String constraintName=readElement(elementConnection, Connection.CONSTRAINTNAME);
			Table source = tableMap.get(sourceId);
			Table target = tableMap.get(targetId);
			connection.setType(type);
			connection.setConstraintName(constraintName);
			connection.setSource(source);
			connection.setTarget(target);
			connection.setPkColumn(columnMap.get(pk));
			connection.setFkColumn(columnMap.get(fk));
			source.addOutConnection(connection);
			target.addInConnection(connection);
			readBendpoints(elementConnection, connection);

		}
	}

	private void readBendpoints(Element elementConnection, Connection connection) {
		List<Bendpoint> list = new ArrayList<Bendpoint>();
		NodeList elementBendpoints = elementConnection.getElementsByTagName(ConnectionBendpoint.SIMPLE_NAME);
		ConnectionBendpoint bendpoint = null;
		Element elementBendpoint = null;
		for (int i = 0; i < elementBendpoints.getLength(); i++) {
			elementBendpoint = (Element) elementBendpoints.item(i);
			bendpoint = new ConnectionBendpoint();
			String dx1 = readElement(elementBendpoint, ConnectionBendpoint.DX1);
			String dy1 = readElement(elementBendpoint, ConnectionBendpoint.DY1);
			String dx2 = readElement(elementBendpoint, ConnectionBendpoint.DX2);
			String dy2 = readElement(elementBendpoint, ConnectionBendpoint.DY2);
			bendpoint.setRelativeDimensions(new Dimension(Integer.valueOf(dx1), Integer.valueOf(dy1)),
					new Dimension(Integer.valueOf(dx2), Integer.valueOf(dy2)));
			list.add(bendpoint);
		}
		connection.setBendpoints(list);
	}

	private Rectangle readElementConstraintsInfo(Element element) {
		String x = readElement(element, BaseModel.X);
		String y = readElement(element, BaseModel.Y);
		String width = readElement(element, BaseModel.WIDTH);
		String height = readElement(element, BaseModel.HEIGHT);
		Rectangle r = new Rectangle(Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(width), Integer.valueOf(height));
		return r;
	}

}
