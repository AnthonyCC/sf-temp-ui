package com.freshdirect.transadmin.datamanager.report;

import java.text.ParseException;

import com.freshdirect.transadmin.datamanager.report.model.CrisisManagerReportData;

public interface ICrisisManagerReport {
	
	String CSV_SEPARATOR = ",";
	
	String MARKETINGREPORT_DATETITLE = "Date";
	
	String MARKETINGREPORT_ORDERSTITLE = "Total Orders";
	
	String MARKETINGREPORT_STANDINGORDERITLE = "Total Standing Orders";
	
	void generateMarketingReport(String file, CrisisManagerReportData reportData ) 
												throws ReportGenerationException;
	
	void generateVoiceShotReport(String file, CrisisManagerReportData reportData ) 
												throws ReportGenerationException;
	
	void generateTimeSlotExceptionReport(String file, CrisisManagerReportData reportData ) 
												throws ReportGenerationException, ParseException;
	
	void generateSOSimulationReport(String file, CrisisManagerReportData reportData ) 
												throws ReportGenerationException, ParseException;
	
	void generateSOFailureReport(String file, CrisisManagerReportData reportData ) 
												throws ReportGenerationException, ParseException;
}
