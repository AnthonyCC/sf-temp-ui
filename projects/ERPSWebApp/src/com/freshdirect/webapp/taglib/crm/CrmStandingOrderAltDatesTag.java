package com.freshdirect.webapp.taglib.crm;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.standingorders.FDStandingOrderAltDeliveryDate;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class CrmStandingOrderAltDatesTag extends AbstractControllerTag{
	
	public static final String NO_FILTER_LABEL = "ALL";
	public static final int DESCRIPTION_LENGTH = 35;
	
	private static final long serialVersionUID = 5304815417711379398L;
	private String filter;
	private String intervals;
	private DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	
	public void setFilter(String filter) {
		this.filter = filter;
	}

	public void setIntervals(String intervals) {
		this.intervals = intervals;
	}

	
	protected  boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		//handle post
		String operation = request.getParameter("operation");
		
		if (operation!=null){
			if (operation.equals("filter_change")){
				String newFilter = request.getParameter("filter");
				request.getSession().setAttribute("CrmStandingOrderAltDatesFilter", newFilter);
	
			} else if (operation.startsWith("alt_delivery_date_")) { 
				
				String altDeliveryDateOperation= operation.substring("alt_delivery_date_".length());
				
				try {
					FDStandingOrderAltDeliveryDate altDeliveryDate = getAltDeliveryDateFromRequest(request);
					
					if ("create".equals(altDeliveryDateOperation)){
						FDStandingOrdersManager.getInstance().addStandingOrderAltDeliveryDate(altDeliveryDate);

					} else if ("save".equals(altDeliveryDateOperation)){
						FDStandingOrdersManager.getInstance().updateStandingOrderAltDeliveryDate(getAltDeliveryDateFromRequest(request));

					} else if ("delete".equals(altDeliveryDateOperation)){
						FDStandingOrdersManager.getInstance().deleteStandingOrderAltDeliveryDate(getAltDeliveryDateFromRequest(request));
					}
					
					pageContext.setAttribute("successMsg", Character.toUpperCase(altDeliveryDateOperation.charAt(0)) + altDeliveryDateOperation.substring(1) + " successful for " + altDeliveryDate);
				} catch (FDResourceException e) {
					if (e.getFDStackTrace().contains("ORA-00001")){
						actionResult.addError(new ActionError("so_error", "Error in "+altDeliveryDateOperation+": Original Date must be unique."));
					} else {
						throw new JspException("Failed to "+altDeliveryDateOperation+" the standing order alternative delivery dates: ",e);
					}
				} catch (IllegalArgumentException e) {
					actionResult.addError(new ActionError("so_error", "Error in "+altDeliveryDateOperation+": "+e.getMessage()));
				}
			}
		}
		return performGetAction(request, actionResult);
	}

	private FDStandingOrderAltDeliveryDate getAltDeliveryDateFromRequest(HttpServletRequest request){
		Date origDate;
		try {
			origDate = df.parse(request.getParameter("origDate"));
		} catch (ParseException e) {
			throw new IllegalArgumentException("Original Date - " + e.getMessage(),e); 
		}
		Date altDate;
		try {
			altDate = df.parse(request.getParameter("altDate"));
		} catch (ParseException e) {
			throw new IllegalArgumentException("Alternative Date - " + e.getMessage(),e); 
		}
		
		String desciption = request.getParameter("description");
		if (desciption!=null && desciption.length() > DESCRIPTION_LENGTH){
			throw new IllegalArgumentException("Description lenght too long, max "+DESCRIPTION_LENGTH+" characters allowed"); 
		}

		FDStandingOrderAltDeliveryDate altDeliveryDate = new FDStandingOrderAltDeliveryDate();
		altDeliveryDate.setOrigDate(origDate);
		altDeliveryDate.setAltDate(altDate);
		altDeliveryDate.setDescription(desciption);
		altDeliveryDate.setDateFormat(df);
		return altDeliveryDate;
	}
	
	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		DateFormat yearDf = new SimpleDateFormat("yyyy");
		String yearNow = yearDf.format(new Date());

		String filter = (String) request.getSession().getAttribute("CrmStandingOrderAltDatesFilter");
		if (filter==null){
			filter = yearNow;
			request.getSession().setAttribute("CrmStandingOrderAltDatesFilter",filter);
		}
		
		List<FDStandingOrderAltDeliveryDate> altDeliverDates;
		try {
			altDeliverDates= FDStandingOrdersManager.getInstance().getStandingOrderAltDeliveryDates();
		} catch (FDResourceException e) {
			throw new JspException("Failed to get the standing order alternative delivery dates: ",e);
		}
		
		LinkedList<String> intervals = new LinkedList<String>();

		List<FDStandingOrderAltDeliveryDate> altDeliverDatesFiltered = new ArrayList<FDStandingOrderAltDeliveryDate>();
		
		for (FDStandingOrderAltDeliveryDate altDeliverDate: altDeliverDates){

			altDeliverDate.setDateFormat(df);
			Date origDate = altDeliverDate.getOrigDate();
			String origDateYear = yearDf.format(origDate);

			if (NO_FILTER_LABEL.equals(filter) || origDateYear.equals(filter)){
				altDeliverDatesFiltered.add(altDeliverDate);
			}
			
			if (!intervals.contains(origDateYear)){
				intervals.add(origDateYear);
			}
		}

		if (!intervals.contains(yearNow)){
			intervals.add(yearNow);
		}

		Collections.sort(intervals, Collections.reverseOrder());
		intervals.push(NO_FILTER_LABEL);
		
		pageContext.setAttribute(this.id, altDeliverDatesFiltered);
		pageContext.setAttribute(this.filter, filter);
		pageContext.setAttribute(this.intervals, intervals);
		return true;
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		 public VariableInfo[] getVariableInfo(TagData data) {
		        return new VariableInfo[] {
		            new VariableInfo(data.getAttributeString("id"),
		                            "java.util.List",
		                            true,
		                            VariableInfo.NESTED),
		            new VariableInfo(data.getAttributeString("filter"),
		                            "java.lang.String",
		                            true,
		                            VariableInfo.NESTED),
		            new VariableInfo(data.getAttributeString("intervals"),
		                            "java.util.List",
		                            true,
		                            VariableInfo.NESTED),
                    new VariableInfo(data.getAttributeString("result"),
		                            "com.freshdirect.framework.webapp.ActionResult",
		                            true,
		                            VariableInfo.NESTED)             
		        };

		    }
	}
}
