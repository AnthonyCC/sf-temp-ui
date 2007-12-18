/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.dlv;

import javax.servlet.jsp.*;

import java.util.*;
import java.sql.Timestamp;

import com.freshdirect.delivery.*;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class DeliveryTimeSlotTag extends com.freshdirect.framework.webapp.BodyTagSupport {
	
	private String id = null;
	private String zoneCode = null;
	private int days = 1;  
	private GregorianCalendar begCal = null;
	
	public void setId(String id) {
		this.id = id;
	}
	public String getId(){
		return this.id;
	}
	public void setBegDate(GregorianCalendar begCal) {
		this.begCal = begCal;
	}
	public GregorianCalendar getBegDate() {
		return this.begCal;
	}
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	
	public String getZoneCode() {
		return this.zoneCode;
	}
	
	public void setDays(int days) {
		this.days = days; 
	}
	public int getDays() {
		return this.days;
	}
	
	public int doStartTag() throws JspException {
		ArrayList timeslots = null;
		Calendar endCal = new GregorianCalendar();
		Calendar currentCal = new GregorianCalendar();
		//currentCal.setTime(currentCal.getTime());
		endCal.setTime(currentCal.getTime());
		
		endCal.add(Calendar.DATE, 8);
		endCal.set(Calendar.HOUR, 12);
		endCal.set(Calendar.MINUTE, 0);
		endCal.set(Calendar.AM_PM, Calendar.AM);
		
		//Calendar cal = new GregorianCalendar();
		//Calendar begCal = null;
	
		//cal.add(Calendar.DATE, 1);
		//begCal = cal;
		begCal.set(Calendar.HOUR_OF_DAY,0);
		begCal.set(Calendar.MINUTE,1);
				
		Timestamp begTime = new Timestamp(begCal.getTime().getTime());
		Timestamp endTime = new Timestamp(endCal.getTime().getTime());
		Timestamp curTime = new Timestamp(currentCal.getTime().getTime());
		
		if (zoneCode != null ) {
			try {
				timeslots = DlvTemplateManager.getInstance().getTimeslotForDateRangeAndZone(begTime, endTime, curTime, zoneCode);
			}catch (DlvResourceException de) {
				throw new JspException(de.getMessage());
			} 
		}
		
		if (timeslots == null) {
			timeslots = new ArrayList();
		}		
					
		pageContext.setAttribute(id, timeslots);
		return EVAL_BODY_BUFFERED;
	}
}
