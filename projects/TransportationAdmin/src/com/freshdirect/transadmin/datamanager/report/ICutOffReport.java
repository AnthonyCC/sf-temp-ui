package com.freshdirect.transadmin.datamanager.report;

import java.text.ParseException;

import com.freshdirect.transadmin.datamanager.report.model.CutOffReportData;



public interface ICutOffReport {
	
	String CSV_SEPARATOR = ",";
	
	String CUTOFFREPORT_TITLE = "UPS Route Cutoff Report";
	
	String CUTOFFREPORT_DATETITLE = "Date";
	
	String CUTOFFREPORT_CUTOFFTITLE = "Cut Off";
	
	String CUTOFFREPORT_ROUTETITLE = "Routes";
	
	String CUTOFFREPORT_STOPSTITLE = "Total Stops";
	
	String CUTOFFREPORT_TRAILERTITLE = "Trailers";
	
	String CUTOFFREPORT_CONTAINERSTITLE = "Max Containers";

	String CUTOFFREPORT_CARTONSTITLE = "Max Cartons";

	void generateCutOffReport(String file, CutOffReportData reportData ) 
												throws ReportGenerationException, ParseException;
}
