package com.freshdirect.webapp.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.LateDlvReportLine;
import com.freshdirect.framework.util.log.LoggerFactory;


public class ExcelReportServletLateDelivery extends AbstractExcelReportServlet {
	
	private static Category LOGGER = LoggerFactory.getInstance(ExcelReportServletLateDelivery.class);

	protected String getWorksheetName() {
		return "Late Delivery Report";
	}

	protected List getColumnNames() {
		List cols = new ArrayList();
		
		cols.add("CASE/COMPLAINT CREATED");
		cols.add("DELIVERY WINDOW");
		cols.add("TRUCK NUMBER");
		cols.add("STOP SEQUENCE");
		cols.add("ORDER NUMBER");
		cols.add("FIRST NAME");
		cols.add("LAST NAME");
		cols.add("SOURCE");
		cols.add("CHEF'S TABLE");
		cols.add("UNDECLARED");

		return cols;
	}

	int month;
    int day;
    int year;
	
	protected void initialize(HttpServletRequest request) {
		
		Calendar cal = Calendar.getInstance();	
		
		month = request.getParameter("month") != null ? Integer.parseInt(request.getParameter("month")) : cal.get(Calendar.MONTH);
	    day   = request.getParameter("day") != null ? Integer.parseInt(request.getParameter("day")) : cal.get(Calendar.DAY_OF_MONTH);
	    year  = request.getParameter("year") != null ? Integer.parseInt(request.getParameter("year")) : cal.get(Calendar.YEAR);
	}
	
	protected List getReport() {
		List lateDeliveryReport = new ArrayList();
		
		Calendar cal = Calendar.getInstance();	
	    
		cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        Date dateParam = cal.getTime();
		
		try {			
			List actualLateDeliveryReport = CallCenterServices.getLateDeliveryReport(dateParam);
			for (Iterator iter=actualLateDeliveryReport.iterator(); iter.hasNext();) {
				Map rptLine = new HashMap();
				LateDlvReportLine ldrl = (LateDlvReportLine) iter.next();
				rptLine.put("CASE/COMPLAINT CREATED",CCFormatter.formatDateTime(ldrl.getTimeCaseOpened()));
				rptLine.put("DELIVERY WINDOW",(ldrl.getDisplayableStartTime()+" - "+ldrl.getDisplayableEndTime()));
				rptLine.put("TRUCK NUMBER",ldrl.getTruckNumber());
				rptLine.put("STOP SEQUENCE",ldrl.getStopSequence());
				rptLine.put("ORDER NUMBER",ldrl.getOrderNumber());
				rptLine.put("FIRST NAME",ldrl.getFirstName());
				rptLine.put("LAST NAME",ldrl.getLastName());
				rptLine.put("SOURCE",ldrl.getSource());
				rptLine.put("CHEF'S TABLE",(ldrl.isChefsTable() ? "YES" : "NO"));
				rptLine.put("UNDECLARED",(ldrl.isUndeclared() ? "YES" : "NO"));
				lateDeliveryReport.add(rptLine);
			}
		} catch (FDResourceException e) {
			e.printStackTrace();
		}
		
		return lateDeliveryReport;
	}

}
