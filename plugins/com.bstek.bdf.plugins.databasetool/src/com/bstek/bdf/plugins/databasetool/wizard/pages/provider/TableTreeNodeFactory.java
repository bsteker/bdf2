package com.bstek.bdf.plugins.databasetool.wizard.pages.provider;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bstek.bdf.plugins.databasetool.dialect.DbDriverMetaData;
import com.bstek.bdf.plugins.databasetool.dialect.DbJdbcUtils;

public class TableTreeNodeFactory {
	private static TableTreeNodeFactory obj = new TableTreeNodeFactory();

	private TableTreeNodeFactory() {

	}

	public static TableTreeNodeFactory getInstence() {
		return obj;
	}

	public TableTreeNode[] getTreeDatas(DbDriverMetaData dbDriverMetaData) throws Exception {
		Connection conn = null;
		try {
			conn = DbJdbcUtils.getConnection(dbDriverMetaData.getDbType(), dbDriverMetaData.getUrl(), dbDriverMetaData.getUsername(),
					dbDriverMetaData.getPassword());
			Map<String, List<String>> map = DbJdbcUtils.getDefaultSchemaTables(conn);
			TableTreeNode[] treeDatas = covertMapToObject(map);
			return treeDatas;
		} finally {
			DbJdbcUtils.closeConnection(conn);
		}
	}

	private TableTreeNode[] covertMapToObject(Map<String, List<String>> map) {
		List<TableTreeNode> list = new ArrayList<TableTreeNode>();
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			String schema = entry.getKey();
			List<String> tableNames = entry.getValue();
			TableTreeNode parent = new TableTreeNode();
			parent.setName(schema);
			parent.setTable(false);
			List<TableTreeNode> childrenList = new ArrayList<TableTreeNode>();
			for (String tableName : tableNames) {
				TableTreeNode child = new TableTreeNode();
				child.setName(tableName);
				child.setTable(true);
				child.setParent(parent);
				childrenList.add(child);
			}
			parent.setChildren(childrenList.toArray(new TableTreeNode[] {}));
			list.add(parent);
		}
		return list.toArray(new TableTreeNode[] {});

	}

}
