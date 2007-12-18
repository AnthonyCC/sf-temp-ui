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
import com.freshdirect.fdstore.customer.FDCustomerReservationInfo;
import com.freshdirect.fdstore.customer.LateDlvReportLine;
import com.freshdirect.framework.util.GenericSearchCriteria;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;


public class ReservationsReportServlet extends AbstractExcelReportServlet {
	
	private static Category LOGGER = LoggerFactory.getInstance(ReservationsReportServlet.class);
	
	protected String getWorksheetName() {
		return "Uncommitted Reservations Report";
	}

	protected List getColumnNames() {
		List cols = new ArrayList();
		
		cols.add("CUSTOMER ID");
		cols.add("FIRST NAME");
		cols.add("LAST NAME");
		cols.add("EMAIL");
		cols.add("HOME PHONE");
		cols.add("ALT. PHONE");
		cols.add("BASE DATE");
		cols.add("CUTOFF TIME");
		cols.add("START TIME");
		cols.add("END TIME");
		cols.add("ZONE");

		return cols;
	}

	
	protected void initialize(HttpServletRequest request) {
		
	}
	protected List getReport() {
		return null;
		
	}
	
	protected List getReport(HttpServletRequest request) throws FDResourceException {
		List reservationsReport = new ArrayList();
		
		try {			
			HttpSession session = request.getSession();
			GenericSearchCriteria criteria = (GenericSearchCriteria)session.getAttribute("RESV_SEARCH_CRITERIA");
			if(criteria == null ){
				LOGGER.error("Session Data RESV_SEARCH_CRITERIA is missing.");
				throw new FDResourceException(SystemMessageList.MSG_TECHNICAL_ERROR);
			}

			List resvList =CallCenterServices.doGenericSearch(criteria);
			if(resvList == null || resvList.size() == 0){
				LOGGER.error("The Reservation List is return null or empty.");
				throw new FDResourceException("No Reservations to Export.");
			}
			 
			for (Iterator iter=resvList.iterator(); iter.hasNext();) {
				Map resvLine = new HashMap();
				FDCustomerReservationInfo reservation = (FDCustomerReservationInfo) iter.next();
				resvLine.put("CUSTOMER ID",reservation.getIdentity().getErpCustomerPK());
				resvLine.put("FIRST NAME", reservation.getFirstName());
				resvLine.put("LAST NAME", reservation.getLastName());
				resvLine.put("EMAIL", reservation.getEmail());
				resvLine.put("HOME PHONE", reservation.getPhone());
				resvLine.put("ALT. PHONE", reservation.getAltPhone());
				resvLine.put("BASE DATE", CCFormatter.defaultFormatDate(reservation.getBaseDate()));
				resvLine.put("CUTOFF TIME", CCFormatter.formatDeliveryTime(reservation.getCutoffTime()));
				resvLine.put("START TIME", CCFormatter.formatDeliveryTime(reservation.getStartTime()));
				resvLine.put("END TIME", CCFormatter.formatDeliveryTime(reservation.getEndTime()));
				resvLine.put("ZONE", reservation.getZone());
				reservationsReport.add(resvLine);
			}
		} catch (FDResourceException e) {
			throw e;
		}
		
		return reservationsReport;
	}

}
