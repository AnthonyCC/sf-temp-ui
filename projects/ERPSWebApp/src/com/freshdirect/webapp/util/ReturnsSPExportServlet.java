package com.freshdirect.webapp.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerOrderInfo;
import com.freshdirect.framework.util.GenericSearchCriteria;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

/**@author ekracoff on Aug 10, 2004*/
public class ReturnsSPExportServlet extends AbstractSPExportServlet{

	private static Category LOGGER = LoggerFactory.getInstance(ReturnsSPExportServlet.class);


	protected void initialize(HttpServletRequest request){
		
	}
	
	protected String getFileName(){
		return "return_orders_SP.txt";
	}
	
	protected List getReportData(HttpServletRequest request) throws FDResourceException{
		GenericSearchCriteria criteria = (GenericSearchCriteria)request.getSession().getAttribute("RETURN_ORDERS_CRITERIA");
		if(criteria == null ){
			LOGGER.error("Session Data RETURN_ORDERS_CRITERIA is missing.");
			throw new FDResourceException(SystemMessageList.MSG_TECHNICAL_ERROR);
		}

		List returnOrders =CallCenterServices.doGenericSearch(criteria);
		if(returnOrders == null || returnOrders.size() == 0){
			LOGGER.error("The Return order search return null or empty List.");
			throw new FDResourceException("No Orders to Export.");
		}
		List reportData = new ArrayList(); 
		for (Iterator iter= returnOrders.iterator(); iter.hasNext();) {
			FDCustomerOrderInfo order = (FDCustomerOrderInfo) iter.next();
			Map orderLine = createReportLine(order);
			reportData.add(orderLine);
		}
		return reportData;
	}

	private Map createReportLine(FDCustomerOrderInfo order) {
		Map reportLine = new HashMap();
		reportLine.put("FIRST NAME", order.getFirstName());
		reportLine.put("EMAIL", order.getEmail());
		reportLine.put("EMAIL TYPE", order.getEmailType());
		return reportLine;
	}

}
