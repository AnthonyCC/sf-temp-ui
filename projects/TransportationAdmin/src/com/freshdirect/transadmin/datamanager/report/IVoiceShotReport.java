package com.freshdirect.transadmin.datamanager.report;

import com.freshdirect.transadmin.datamanager.report.model.CrisisManagerReportData;

public interface IVoiceShotReport {
	
	String CSV_SEPARATOR = ",";
	
	String VOICESHOTREPORT_TITLE = "Crisis Management Marketing Report";
	
	String VOICESHOTREPORT_DATETITLE = "Date";
	
	String VOICESHOTREPORT_ORDERSTITLE = "Total Orders";
	
	String VOICESHOTREPORT_STANDINGORDERITLE = "Total Standing Orders";
	
	void generateVoiceShotReport(String file, CrisisManagerReportData reportData ) 
												throws ReportGenerationException;
}
