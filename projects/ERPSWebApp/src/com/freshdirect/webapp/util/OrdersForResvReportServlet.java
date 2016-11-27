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


public class OrdersForResvReportServlet extends AbstractExcelReportServlet {
	
	private static Category LOGGER = LoggerFactory.getInstance(OrdersForResvReportServlet.class);
	
	protected String getWorksheetName() {
		return "Orders By Reservations Report";
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
		cols.add("CUTOFF TIME");
		cols.add("START TIME");
		cols.add("END TIME");
		cols.add("RESERVATION TYPE");

		return cols;
	}

	
	protected void initialize(HttpServletRequest request) {
		
	}
	protected List getReport() {
		return null;
		
	}
	
	protected List getReport(HttpServletRequest request) throws FDResourceException {
		List ordersReport = new ArrayList();
		
		try {			
			HttpSession session = request.getSession();
			GenericSearchCriteria criteria = (GenericSearchCriteria)session.getAttribute("ORDERS_BY_RESV_CRITERIA");
			if(criteria == null ){
				LOGGER.error("Session Data ORDERS_BY_RESV_CRITERIA is missing.");
				throw new FDResourceException(SystemMessageList.MSG_TECHNICAL_ERROR);
			}

			List orders =CallCenterServices.doGenericSearch(criteria);
			if(orders == null || orders.size() == 0){
				LOGGER.error("The Reservation Orders is return null or empty.");
				throw new FDResourceException("No Orders to Export.");
			}

			String filterType = (String)session.getAttribute("FILTER_TYPE");
			filterType = (filterType != null) ? filterType : "ALL";
			List subList = null;
			if(filterType.equals("ALL")){
				subList = new ArrayList(orders);	
			}else{
				subList = GenericLocatorTag.filterOrdersByResvType(orders, filterType);
			}
			 
			for (Iterator iter=subList.iterator(); iter.hasNext();) {
				Map orderLine = new HashMap();
				FDCustomerOrderInfo order = (FDCustomerOrderInfo) iter.next();
				orderLine.put("ORDER #",order.getSaleId());
				orderLine.put("FIRST NAME", order.getFirstName());
				orderLine.put("LAST NAME", order.getLastName());
				orderLine.put("EMAIL", order.getEmail());
				orderLine.put("HOME PHONE", order.getPhone());
				orderLine.put("ALT. PHONE", order.getAltPhone());
				orderLine.put("DELIVERY DATE", CCFormatter.defaultFormatDate(order.getDeliveryDate()));
				orderLine.put("ORDER STATUS", order.getOrderStatus().getDisplayName());
				orderLine.put("AMOUNT", JspMethods.formatPrice(order.getAmount()));
				orderLine.put("CUTOFF TIME", CCFormatter.formatDeliveryTime(order.getCutoffTime()));
				orderLine.put("START TIME", CCFormatter.formatDeliveryTime(order.getStartTime()));
				orderLine.put("END TIME", CCFormatter.formatDeliveryTime(order.getEndTime()));
				orderLine.put("RESERVATION TYPE", order.getRsvType().getDescription());
				ordersReport.add(orderLine);
			}
		} catch (FDResourceException e) {
			throw e;
		}
		
		return ordersReport;
	}

}
