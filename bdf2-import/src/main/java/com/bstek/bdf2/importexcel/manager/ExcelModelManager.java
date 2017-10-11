package com.bstek.bdf2.importexcel.manager;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.importexcel.ImportExcelHibernateDao;
import com.bstek.bdf2.importexcel.model.ExcelModel;
import com.bstek.bdf2.importexcel.model.ExcelModelDetail;
import com.bstek.bdf2.importexcel.utils.ConnectionHelper;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.data.provider.Page;

/**
 * Excel导入数据库表维护
 * 
 * @author matt.yao@bstek.com
 * @since 2.0
 */
@Service(ExcelModelManager.BEAN_ID)
public class ExcelModelManager extends ImportExcelHibernateDao {

	public static final String BEAN_ID = "bdf2.ExcelModelManager";

	private ConnectionHelper connectionHelper = new ConnectionHelper();

	public void loadExcelModels(Page<ExcelModel> page, DetachedCriteria detachedCriteria) throws Exception {
		boolean noCore=Configure.getBoolean("bdf2.noCore",false);
		String companyId=null;
		if(noCore){
			companyId = "bstek";
		}else{
			companyId = ContextHolder.getLoginUser().getCompanyId();
		}
		detachedCriteria.add(Restrictions.eq("companyId", companyId));
		findPageByCriteria(detachedCriteria, page);
	}

	public void insertExcelModel(ExcelModel excelModel) throws Exception {
		boolean noCore=Configure.getBoolean("bdf2.noCore",false);
		String companyId=null;
		if(noCore){
			companyId = "bstek";
		}else{
			companyId = ContextHolder.getLoginUser().getCompanyId();
		}
		excelModel.setCreateDate(new Date());
		excelModel.setCompanyId(companyId);
		save(excelModel);
	}

	public void deleteExcelModelById(String excelModelId) throws Exception {
		Session session = this.getSessionFactory().openSession();
		String hqlDelete = "delete ExcelModel e where e.id = :excelModelId";
		try {
			session.createQuery(hqlDelete).setString("excelModelId", excelModelId).executeUpdate();
		} finally {
			session.flush();
			session.close();
		}
	}

	public void updateExcelModel(ExcelModel excelModel) throws Exception {
		update(excelModel);
	}

	@SuppressWarnings("rawtypes")
	public ExcelModel findExcelModelById(String excelModelId) throws Exception {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ExcelModel.class, "m");
		detachedCriteria.add(Restrictions.eq("id", excelModelId));
		List list = findByCriteria(detachedCriteria);
		if (list.isEmpty()) {
			return null;
		} else {
			return (ExcelModel) list.get(0);
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<ExcelModelDetail> findExcelModelDetailByModelId(String modelId) throws Exception {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ExcelModelDetail.class, "m");
		detachedCriteria.add(Restrictions.eq("excelModelId", modelId));
		detachedCriteria.addOrder(Order.asc("excelColumn"));
		List list = findByCriteria(detachedCriteria);
		return list;
	}

	public void insertExcelModelDetail(ExcelModelDetail excelModelDetail) throws Exception {
		save(excelModelDetail);
	}

	public void deleteExcelModelDetailById(String excelModelDetailId) throws Exception {
		Session session = this.getSessionFactory().openSession();
		String hqlDelete = "delete ExcelModelDetail e where e.id = :excelModelDetailId";
		try {
			session.createQuery(hqlDelete).setString("excelModelDetailId", excelModelDetailId).executeUpdate();
		} finally {
			session.flush();
			session.close();
		}
	}

	public void deleteExcelModelDetailByModelId(String excelModelId) throws Exception {
		Session session = this.getSessionFactory().openSession();
		String hqlDelete = "delete ExcelModelDetail e where e.excelModelId = :excelModelId";
		try {
			session.createQuery(hqlDelete).setString("excelModelId", excelModelId).executeUpdate();
		} finally {
			session.flush();
			session.close();
		}
	}

	public void updateExcelModelDetail(ExcelModelDetail excelModelDetail) throws Exception {
		update(excelModelDetail);
	}

	@SuppressWarnings("rawtypes")
	public ExcelModelDetail findExcelModelDetail(String modelId, int excelColumn) throws Exception {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ExcelModelDetail.class, "m");
		detachedCriteria.add(Restrictions.eq("excelModelId", modelId));
		detachedCriteria.add(Restrictions.eq("excelColumn", excelColumn));
		List list = findByCriteria(detachedCriteria);
		if (list.isEmpty()) {
			return null;
		} else {
			return (ExcelModelDetail) list.get(0);
		}
	}

	@SuppressWarnings("rawtypes")
	public ExcelModelDetail findExcelModelDetailByModelIdAndPrimaryKey(String modelId, String tableColumn) throws Exception {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ExcelModelDetail.class, "m");
		detachedCriteria.add(Restrictions.eq("excelModelId", modelId));
		detachedCriteria.add(Restrictions.eq("tableColumn", tableColumn));
		List list = findByCriteria(detachedCriteria);
		if (list.isEmpty()) {
			return null;
		} else {
			return (ExcelModelDetail) list.get(0);
		}
	}

	public List<String> findAllTables(String dataSourceName) throws Exception {
		List<String> tablesList = new ArrayList<String>();
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = connectionHelper.getConnection(dataSourceName);
			DatabaseMetaData metaData = conn.getMetaData();
			String url = metaData.getURL();
			String schema = null;
			if (url.toLowerCase().contains("oracle")) {
				schema = metaData.getUserName();
			}
			rs = metaData.getTables(null, schema, "%", new String[] { "TABLE" });
			while (rs.next()) {
				tablesList.add(rs.getString("TABLE_NAME").toLowerCase());
			}
			return tablesList;
		} finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

	public List<String> findTablePrimaryKeys(String dataSourceName, String tableName) throws Exception {
		List<String> primaryKeys = new ArrayList<String>();
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = connectionHelper.getConnection(dataSourceName);
			DatabaseMetaData metaData = conn.getMetaData();
			rs = metaData.getPrimaryKeys(null, null, tableName.toUpperCase());
			while (rs.next()) {
				primaryKeys.add(rs.getString("COLUMN_NAME").toLowerCase());
			}
			return primaryKeys;
		} finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

	public List<String> findTableColumnNames(String dataSourceName, String tableName) throws Exception {
		List<String> list = new ArrayList<String>();
		if (StringUtils.isNotEmpty(tableName)) {
			Connection conn = null;
			ResultSet rs = null;
			try {
				conn = connectionHelper.getConnection(dataSourceName);
				DatabaseMetaData metaData = conn.getMetaData();
				rs = metaData.getColumns(null, null, tableName.toUpperCase(), "%");
				while (rs.next()) {
					list.add(rs.getString("COLUMN_NAME").toLowerCase());
				}
				return list;
			} finally {
				if (conn != null) {
					conn.close();
				}
				if (rs != null) {
					rs.close();
				}
			}
		}
		return list;
	}

}
