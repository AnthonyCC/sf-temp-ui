package com.freshdirect.webapp.taglib.standingorder;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDOrderHistory;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.JSHelper;
import com.freshdirect.webapp.util.StandingOrderHelper;

/**
 * This tag creates a web control that allows
 * choosing next delivery date for a standing order instance
 * 
 * @author segabor
 *
 */
public class NextDeliveryDateChooserTag extends BodyTagSupport {
	private static final long serialVersionUID = -7884700195544361977L;
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("EEEE, M/d/y");

    private static final String ATTR_NAME_SELECTABLE = "sodlv_selectable";
    private static final String ATTR_NAME_CANDIDATES = "sodlv_candidates";

    FDStandingOrder standingOrder;
	
	String inputName = "soDeliveryWeekOffset";
	
	
	public void setStandingOrder(FDStandingOrder standingOrder) {
		this.standingOrder = standingOrder;
	}

	/**
	 * Sets the name attribute of the input field
	 * Optional field.
	 * 
	 * @param inputName
	 */
	public void setInputName(String inputName) {
		this.inputName = inputName;
	}
	
	
	@Override
	public int doStartTag() throws JspException {
		StandingOrderHelper.DeliveryTime __c_dt = new StandingOrderHelper.DeliveryTime(standingOrder);


		final int __c_dow = __c_dt.getDay();
		int weekSel = __c_dt.getWeekOffset(); /* week offset starting from current week */
		
		// fix invalid offset (ie. falls off [1..freq] interval )
		if (weekSel < 1)
			weekSel = 1; // the week of next delivery should fall in the next week
		else if (weekSel > standingOrder.getFrequency() ) {
			weekSel = standingOrder.getFrequency();
		}




		/*
		 * Calculate earliest available delivery week offset
		 */
		int offsetStart = 1;
		try {
			List<FDOrderInfoI> instances = null;

			FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
			FDOrderHistory history = (FDOrderHistory) FDCustomerManager.getOrderHistoryInfo(user.getIdentity());
			
			instances = history.getStandingOrderInstances(this.standingOrder.getId());
			
			offsetStart = StandingOrderHelper.getFirstAvailableWeekOffset(instances);
		} catch (FDResourceException exc ) {
			// LOGGER.error("Failed to retrieve scheduled orders for standing order " + this.standingOrder.getId(), exc);
		}

		// fetch candidate dates
		final List<List<Date>> __allCandidates = StandingOrderHelper.getAllDeliveryDateCandidates(standingOrder, offsetStart);
		


		/* Generate HTML control */
		
		StringBuilder content = new StringBuilder();
		
		String domId = inputName;
		if (standingOrder.getFrequency() == 1) {
			// DoW -- day of week
			// __c_dow-1    -- the index for the DoW of the current delivery date
			// __c_week-1   -- the week offset
			
			content.append("<input name=\"").append(inputName).append("\" type=\"hidden\" value=\"1\">\n")
				.append("<span id=\"").append(domId).append("\">")
				.append( DATE_FORMAT.format(__allCandidates.get(__c_dow-1).get(weekSel-1)) )
				.append("</span>\n");
			
		} else {
			content.append("<select id=\"").append(domId).append("\" name=\"").append(inputName).append("\">\n");

			// week offset, zero means current week
			int offset = 1;
			for (Date d : __allCandidates.get(__c_dow-1)) {
				content.append("<option value=\"").append(offset).append("\"");
				if (offset == weekSel) {
					content.append(" selected=\"selected\"");
				}
				content.append(">");
				content.append(DATE_FORMAT.format(d));
				content.append("</option>\n");

				offset++;
			}

			content.append("</select>\n");
		}



		try {
			pageContext.getOut().append( content );
		} catch (IOException e) {
			throw new JspException(e);
		}

		
		// calculate some vars for the javascript code
		pageContext.setAttribute(ATTR_NAME_SELECTABLE, Boolean.toString( standingOrder.getFrequency() > 1) );
		
		StringBuilder candidates = new StringBuilder("[null");
		for (List<Date> aList : __allCandidates) {
			List<String> dStrings = new ArrayList<String>(aList.size());
			for (Date d : aList) { dStrings.add(DATE_FORMAT.format(d));}
			candidates.append(", ").append( JSHelper.listToJSArray(dStrings) );
		}
		candidates.append("]");
		pageContext.setAttribute(ATTR_NAME_CANDIDATES, candidates.toString() );

		
		
		return EVAL_BODY_INCLUDE;
	}
	

	public static class TagEI extends TagExtraInfo {

		@Override
		public VariableInfo[] getVariableInfo(TagData data) {
            return new VariableInfo[] { 
                    new VariableInfo(
                            ATTR_NAME_SELECTABLE, 
                            "java.lang.String", 
                            true, 
                            VariableInfo.AT_BEGIN),
                    new VariableInfo(
                    		ATTR_NAME_CANDIDATES, 
                            "java.lang.String", 
                            true, 
                            VariableInfo.AT_BEGIN)};
        }
	}
}
