package com.bstek.bdf2.importexcel.view;

import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf2.core.orm.DataSourceRegister;
import com.bstek.bdf2.importexcel.ImportExcelHibernateDao;
import com.bstek.bdf2.importexcel.context.ImportContext;
import com.bstek.bdf2.importexcel.interceptor.ICellDataInterceptor;
import com.bstek.bdf2.importexcel.manager.ExcelModelManager;
import com.bstek.bdf2.importexcel.model.DbDataWrapper;
import com.bstek.bdf2.importexcel.model.ExcelDataWrapper;
import com.bstek.bdf2.importexcel.model.ExcelModel;
import com.bstek.bdf2.importexcel.model.ExcelModelDetail;
import com.bstek.bdf2.importexcel.parse.usermodel.ExcelUserModelParser;
import com.bstek.bdf2.importexcel.processor.IExcelProcessor;
import com.bstek.bdf2.importexcel.processor.impl.DefaultExcelProcessor;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author matt.yao@bstek.com
 * @since 2.0
 * 
 */
@Controller("bdf.ExcelMaintain")
public class ExcelMaintain extends ImportExcelHibernateDao {
	
	@Autowired
	@Qualifier(ExcelModelManager.BEAN_ID)
	public ExcelModelManager excelModelManager;
	
	@Autowired
	@Qualifier(ExcelUserModelParser.BEAN_ID)
	public ExcelUserModelParser excelParser;

	@DataProvider
	public void loadExcelModels(Page<ExcelModel> page, Criteria criteria) throws Exception {
		DetachedCriteria detachedCriteria =this.buildDetachedCriteria(criteria, ExcelModel.class, "m");
		excelModelManager.findPageByCriteria(detachedCriteria, page);
	}

	@DataProvider
	public List<ExcelModelDetail> loadExcelModelDetails(String excelModeId) throws Exception {
		return excelModelManager.findExcelModelDetailByModelId(excelModeId);
	}

	@Expose
	public String checkExcelModelId(String excelModelId) throws Exception {
		ExcelModel model = excelModelManager.findExcelModelById(excelModelId);
		if (model == null) {
			return null;
		} else {
			return "方案编号已经存在";
		}
	}

	@DataResolver
	public void saveExcelModels(Collection<ExcelModel> excelModels) throws Exception {
		for (ExcelModel excelModel : excelModels) {
			EntityState state = EntityUtils.getState(excelModel);
			if (state.equals(EntityState.NEW)) {
				excelModelManager.insertExcelModel(excelModel);
			} else if (state.equals(EntityState.MODIFIED)) {
				excelModelManager.updateExcelModel(excelModel);
			} else if (state.equals(EntityState.DELETED)) {
				excelModelManager.deleteExcelModelById(excelModel.getId());
				excelModelManager.deleteExcelModelDetailByModelId(excelModel.getId());
			}
			List<ExcelModelDetail> excelModelDetails = excelModel.getListExcelModelDetail();
			if (excelModelDetails != null) {
				this.saveExcelModelDetails(excelModelDetails);
			}
		}
	}

	public void saveExcelModelDetails(Collection<ExcelModelDetail> excelModelDetails) throws Exception {
		for (ExcelModelDetail excelModelDetail : excelModelDetails) {
			EntityState state = EntityUtils.getState(excelModelDetail);
			if (state.equals(EntityState.NEW)) {
				excelModelDetail.setId(new VMID().toString());
				excelModelManager.insertExcelModelDetail(excelModelDetail);
			} else if (state.equals(EntityState.MODIFIED)) {
				excelModelManager.updateExcelModelDetail(excelModelDetail);
			} else if (state.equals(EntityState.DELETED)) {
				excelModelManager.deleteExcelModelDetailById(excelModelDetail.getId());
			}
		}
	}

	/**
	 * 查询系统注册的数据源
	 * 
	 * @return
	 */
	@DataProvider
	public Collection<DbDataWrapper> loadDatasourceNames() {
		DbDataWrapper dbDataWrapper;
		List<DbDataWrapper> list = new ArrayList<DbDataWrapper>();
		Map<String, DataSourceRegister> dataSourceRegisters = DoradoContext.getAttachedWebApplicationContext().getBeansOfType(DataSourceRegister.class);
		for (DataSourceRegister dataSourceRegister : dataSourceRegisters.values()) {
			dbDataWrapper = new DbDataWrapper();
			dbDataWrapper.setDataSourceName(dataSourceRegister.getName());
			list.add(dbDataWrapper);
		}
		return list;
	}

	@Expose
	@DataProvider
	public Collection<DbDataWrapper> loadTables(String dataSourceName) throws Exception {
		Collection<DbDataWrapper> tableInfos = new ArrayList<DbDataWrapper>();
		if (StringUtils.isNotEmpty(dataSourceName)) {
			List<String> list = excelModelManager.findAllTables(dataSourceName);
			DbDataWrapper dbDataWrapper = null;
			for (String s : list) {
				dbDataWrapper = new DbDataWrapper();
				dbDataWrapper.setDataSourceName(dataSourceName);
				dbDataWrapper.setTableName(s);
				tableInfos.add(dbDataWrapper);
			}
		}
		return tableInfos;
	}

	@Expose
	@DataProvider
	public Collection<DbDataWrapper> loadTableColumnNames(Map<String, Object> map) throws Exception {
		Collection<DbDataWrapper> tableInfos = new ArrayList<DbDataWrapper>();
		if (map != null && map.get("dataSourceName") != null && map.get("tableName") != null) {
			String dataSourceName = (String) map.get("dataSourceName");
			String tableName = (String) map.get("tableName");
			List<String> list = excelModelManager.findTableColumnNames(dataSourceName, tableName);
			DbDataWrapper table = null;
			for (String s : list) {
				table = new DbDataWrapper();
				table.setTableColumn(s);
				tableInfos.add(table);
			}
		}
		return tableInfos;
	}

	@Expose
	@DataProvider
	public Collection<DbDataWrapper> loadTablePrimaryKeys(Map<String, Object> map) throws Exception {
		List<String> list = new ArrayList<String>();
		if (map != null) {
			String dataSourceName = (String) map.get("dataSourceName");
			String tableName = (String) map.get("tableName");
			list = excelModelManager.findTablePrimaryKeys(dataSourceName, tableName);
		}
		Collection<DbDataWrapper> tableInfos = new ArrayList<DbDataWrapper>();
		DbDataWrapper table = null;
		for (String s : list) {
			table = new DbDataWrapper();
			table.setTablePrimaryKey(s);
			tableInfos.add(table);
		}
		return tableInfos;
	}

	@DataProvider
	public Collection<DbDataWrapper> loadPrimaryKeyTypes() throws Exception {
		Collection<String> list = excelParser.getPrimaryTypes();
		Collection<DbDataWrapper> tableInfos = new ArrayList<DbDataWrapper>();
		DbDataWrapper table = null;
		for (String s : list) {
			table = new DbDataWrapper();
			table.setPrimaryKeyType(s);
			tableInfos.add(table);
		}
		return tableInfos;
	}

	@DataProvider
	public List<ExcelModelDetail> loadCellDataIntercepter() throws Exception {
		List<ExcelModelDetail> list = new ArrayList<ExcelModelDetail>();
		Map<String, ICellDataInterceptor> cellDataInterceptors = DoradoContext.getAttachedWebApplicationContext().getBeansOfType(ICellDataInterceptor.class);
		ExcelModelDetail excelModelDetail;
		for (Map.Entry<String, ICellDataInterceptor> entry : cellDataInterceptors.entrySet()) {
			excelModelDetail = new ExcelModelDetail();
			excelModelDetail.setName(entry.getKey() + "{" + entry.getValue().getName() + "}");
			excelModelDetail.setInterceptor(entry.getKey());
			list.add(excelModelDetail);
		}
		return list;
	}

	@DataProvider
	public List<ExcelModelDetail> loadExcelProcessor() throws Exception {
		List<ExcelModelDetail> list = new ArrayList<ExcelModelDetail>();
		Map<String, IExcelProcessor> excelProcessors = DoradoContext.getAttachedWebApplicationContext().getBeansOfType(IExcelProcessor.class);
		ExcelModelDetail excelModelDetail;
		for (Map.Entry<String, IExcelProcessor> entry : excelProcessors.entrySet()) {
			excelModelDetail = new ExcelModelDetail();
			if (!entry.getKey().equals(DefaultExcelProcessor.BEAN_ID)) {
				excelModelDetail.setName(entry.getKey() + "{" + entry.getValue().getName() + "}");
				excelModelDetail.setInterceptor(entry.getKey());
				list.add(excelModelDetail);
			}

		}
		return list;
	}

	@Expose
	@Transactional
	public int processParserdExcelData() throws Exception {
		return processParserdExcelData(null);
	}

	@Expose
	@Transactional
	public int processParserdExcelData(Map<String, Object> parameter) throws Exception {
		ImportContext.setParameters(parameter);
		return excelParser.processParserdExcelData();

	}

	@Expose
	public boolean validateCacheData() throws Exception {
		ExcelDataWrapper data = excelParser.getExcelDataWrapperCache();
		if (data != null && data.validate) {
			return true;
		}
		return false;

	}
}
