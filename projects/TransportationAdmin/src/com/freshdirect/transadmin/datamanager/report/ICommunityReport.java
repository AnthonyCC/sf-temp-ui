package com.freshdirect.transadmin.datamanager.report;

import java.util.Map;



public interface ICommunityReport {
	
	String CSV_SEPARATOR = ",";
	
	String REPORT_TITLE = "Community Report";
	
	String REPORT_DATETITLE = "Date";
	
	String REPORT_CUTOFFTITLE = "Cut Off";
		
	void generateCommunityReport(String file, Map reportData, Map stopCount, String routeDate, String cutOff)
												throws ReportGenerationException;
}
