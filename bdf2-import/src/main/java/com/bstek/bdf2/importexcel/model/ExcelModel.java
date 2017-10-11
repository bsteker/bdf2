package com.bstek.bdf2.importexcel.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "BDF2_EXCEL_MODEL")
public class ExcelModel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID_", length = 60)
	private String id;
	@Column(name = "NAME_", length = 60)
	private String name;
	@Column(name = "EXCEL_SHEET_NAME_", length = 60)
	private String excelSheetName;
	@Column(name = "TABEL_NAME_", length = 120)
	private String tableName;
	@Column(name = "TABLE_LABEL_", length = 120)
	private String tableLabel;
	@Column(name = "PRIMARYKEY_", length = 60)
	private String primaryKey;
	@Column(name = "PRIMARYKEY_TYPE_", length = 60)
	private String primaryKeyType;
	@Column(name = "SEQUENCE_NAME_", length = 120)
	private String sequenceName;
	@Column(name = "DB_TYPE_", length = 60)
	private String dbType;
	@Column(name = "START_ROW_")
	private int startRow;
	@Column(name = "END_ROW_")
	private int endRow;
	@Column(name = "START_COLUMN_")
	private int startColumn;
	@Column(name = "END_COLUMN_")
	private int endColumn;
	@Column(name = "PROCESSOR_", length = 120)
	private String processor;
	@Column(name = "HELP_DOC_", length = 60)
	private String helpDoc;
	@Column(name = "COMMENT_", length = 120)
	private String comment;
	@Column(name = "DATASOURCE_NAME_", length = 60)
	private String datasourceName;
	@Column(name = "COMPANYID_", length = 60)
	private String companyId;
	@Column(name = "CREATE_DATE_")
	private Date createDate;

	@Transient
	private List<ExcelModelDetail> listExcelModelDetail=new ArrayList<ExcelModelDetail>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExcelSheetName() {
		return excelSheetName;
	}

	public void setExcelSheetName(String excelSheetName) {
		this.excelSheetName = excelSheetName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableLabel() {
		return tableLabel;
	}

	public void setTableLabel(String tableLabel) {
		this.tableLabel = tableLabel;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getPrimaryKeyType() {
		return primaryKeyType;
	}

	public void setPrimaryKeyType(String primaryKeyType) {
		this.primaryKeyType = primaryKeyType;
	}

	public String getSequenceName() {
		return sequenceName;
	}

	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public int getStartColumn() {
		return startColumn;
	}

	public void setStartColumn(int startColumn) {
		this.startColumn = startColumn;
	}

	public int getEndColumn() {
		return endColumn;
	}

	public void setEndColumn(int endColumn) {
		this.endColumn = endColumn;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public String getHelpDoc() {
		return helpDoc;
	}

	public void setHelpDoc(String helpDoc) {
		this.helpDoc = helpDoc;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDatasourceName() {
		return datasourceName;
	}

	public void setDatasourceName(String datasourceName) {
		this.datasourceName = datasourceName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public List<ExcelModelDetail> getListExcelModelDetail() {
		return listExcelModelDetail;
	}

	public void setListExcelModelDetail(
			List<ExcelModelDetail> listExcelModelDetail) {
		this.listExcelModelDetail = listExcelModelDetail;
	}

}
