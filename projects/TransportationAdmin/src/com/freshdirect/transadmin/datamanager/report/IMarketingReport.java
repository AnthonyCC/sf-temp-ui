package com.freshdirect.transadmin.datamanager.report;

import java.util.Date;

import com.freshdirect.transadmin.datamanager.report.model.CrisisManagerReportData;



public interface IMarketingReport {
	
	String CSV_SEPARATOR = ",";
	
	String MARKETINGREPORT_TITLE = "Crisis Management Marketing Report";
	
	String MARKETINGREPORT_DATETITLE = "Date";
	
	String MARKETINGREPORT_ORDERSTITLE = "Total Orders";
	
	String MARKETINGREPORT_STANDINGORDERITLE = "Total Standing Orders";
	
	void generateMarketingReport(String file, CrisisManagerReportData reportData ) 
												throws ReportGenerationException;
}
