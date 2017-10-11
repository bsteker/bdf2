package com.bstek.bdf2.jasperreports.service;

import java.util.Collection;

import com.bstek.bdf2.jasperreports.model.ReportDefinition;
import com.bstek.bdf2.jasperreports.model.ReportParameter;
import com.bstek.bdf2.jasperreports.model.ReportResource;

/**
 * @author Jacky.gao
 * @since 2013-5-1
 */
public interface IReportDefinitionService {
	public static final String BEAN_ID="bdf2.reportDefinitionService";
	ReportDefinition loadReportDefinition(String id);
	Collection<ReportParameter> loadReportParameter(String reportDefinitionId);
	Collection<ReportResource> loadReportResouces(String reportDefinitionId);
	Collection<ReportDefinition> loadSubReportDefinitions(String reportDefinitionId);
}
