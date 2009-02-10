package com.freshdirect.transadmin.datamanager.report;

import com.freshdirect.transadmin.datamanager.report.model.CutOffReportData;



public interface ICutOffReport {
	
	String CSV_SEPARATOR = ",";
	
	String CUTOFFREPORT_TITLE = "UPS Route Cutoff Report";
	
	String CUTOFFREPORT_DATETITLE = "Date";
	
	String CUTOFFREPORT_CUTOFFTITLE = "Cut Off";
	
	String CUTOFFREPORT_ROUTETITLE = "Routes";
	
	String CUTOFFREPORT_STOPSTITLE = "Total Stops";
	
	void generateCutOffReport(String file, CutOffReportData reportData ) 
												throws ReportGenerationException;
}
