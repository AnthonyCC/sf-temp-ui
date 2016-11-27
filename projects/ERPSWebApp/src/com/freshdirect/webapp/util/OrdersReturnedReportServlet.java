package com.freshdirect.webapp.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerOrderInfo;
import com.freshdirect.fdstore.customer.FDCustomerReservationInfo;
import com.freshdirect.fdstore.customer.LateDlvReportLine;
import com.freshdirect.framework.util.GenericSearchCriteria;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.callcenter.GenericLocatorTag;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;


public class OrdersReturnedReportServlet extends AbstractExcelReportServlet {
	
	private static Category LOGGER = LoggerFactory.getInstance(OrdersForResvReportServlet.class);
	
	protected String getWorksheetName() {
		return "Orders Returned Report";
	}

	protected List getColumnNames() {
		List cols = new ArrayList();
		
		cols.add("ORDER #");
		cols.add("FIRST NAME");
		cols.add("LAST NAME");
		cols.add("EMAIL");
		cols.add("HOME PHONE");
		cols.add("ALT. PHONE");
		cols.add("DELIVERY DATE");
		cols.add("ORDER STATUS");
		cols.add("AMOUNT");
		cols.add("START TIME");
		cols.add("END TIME");
		cols.add("WAVE NUMBER");
		cols.add("TRUCK NUMBER");
		return cols;
	}

	
	protected void initialize(HttpServletRequest request) {
		
	}
	protected List getReport() {
		return null;
		
	}
	
	protected List getReport(HttpServletRequest request) throws FDResourceException {
		//String type = request.getParameter("type");
		
		try {		
			return getStandardReport(request);
		} catch (FDResourceException e) {
			throw e;
		}
	}

	private List getStandardReport(HttpServletRequest request) throws FDResourceException {
		
		HttpSession session = request.getSession();
		GenericSearchCriteria criteria = (GenericSearchCriteria)session.getAttribute("RETURN_ORDERS_CRITERIA");
		if(criteria == null ){
			LOGGER.error("Session Data RETURN_ORDERS_CRITERIA is missing.");
			throw new FDResourceException(SystemMessageList.MSG_TECHNICAL_ERROR);
		}

		List returnOrders =CallCenterServices.doGenericSearch(criteria);
		if(returnOrders == null || returnOrders.size() == 0){
			LOGGER.error("The Return order search return null or empty List.");
			throw new FDResourceException("No Orders to Export.");
		}
		List ordersReport = new ArrayList(); 
		for (Iterator iter=returnOrders.iterator(); iter.hasNext();) {
			FDCustomerOrderInfo order = (FDCustomerOrderInfo) iter.next();
			Map orderLine = createReportLine(order);
			ordersReport.add(orderLine);
		}
		return ordersReport;
	}
	
//	private List getSuccessFailureReport(HttpServletRequest request) throws FDResourceException {
//		HttpSession session = request.getSession();
//		Map results = (Map)session.getAttribute("EXPORT_RESULTS");
//		if(results == null || results.size() == 0){
//			LOGGER.error("Session Data RESULTS is missing.");
//			throw new FDResourceException("There is no Results Data to Export");
//		}
//
//		List successOrders = (List)results.get("SUCCESS_ORDERS");
//		List failureOrders = (List)results.get("FAILURE_ORDERS");
//		List ordersReport = new ArrayList(); 
//		for (Iterator iter= successOrders.iterator(); iter.hasNext();) {
//			FDCustomerOrderInfo order = (FDCustomerOrderInfo) iter.next();
//			Map orderLine = createReportLine(order);
//			orderLine.put("RETURN STATUS", "Returned");
//			ordersReport.add(orderLine);
//			
//		}
//		for (Iterator iter= failureOrders.iterator(); iter.hasNext();) {
//			FDCustomerOrderInfo order = (FDCustomerOrderInfo) iter.next();
//			Map orderLine = createReportLine(order);
//			orderLine.put("RETURN STATUS", "Failed");
//			ordersReport.add(orderLine);
//		}
//		return ordersReport;
//	}

	private Map createReportLine(FDCustomerOrderInfo order) {
		Map orderLine = new HashMap();
		orderLine.put("ORDER #",order.getSaleId());
		orderLine.put("FIRST NAME", order.getFirstName());
		orderLine.put("LAST NAME", order.getLastName());
		orderLine.put("EMAIL", order.getEmail());
		orderLine.put("HOME PHONE", order.getPhone());
		orderLine.put("ALT. PHONE", order.getAltPhone());
		orderLine.put("DELIVERY DATE", CCFormatter.defaultFormatDate(order.getDeliveryDate()));
		orderLine.put("ORDER STATUS", order.getOrderStatus().getDisplayName());
		orderLine.put("AMOUNT", JspMethods.formatPrice(order.getAmount()));
		orderLine.put("START TIME", CCFormatter.formatDeliveryTime(order.getStartTime()));
		orderLine.put("END TIME", CCFormatter.formatDeliveryTime(order.getEndTime()));
		/*orderLine.put("WAVE NUMBER", order.getWaveNum());*/
		orderLine.put("TRUCK NUMBER", order.getRouteNum());
		return orderLine;
	}

}
