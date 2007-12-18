/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.callcenter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;


/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class AlternateAddressInfoTag extends com.freshdirect.framework.webapp.BodyTagSupport {
	
	private static Category LOGGER = LoggerFactory.getInstance(AlternateAddressInfoTag.class);

	private String altDeliveryInfo = "";

	public void setAltDeliveryInfo(String s) {
		if(s != null){
			this.altDeliveryInfo = s;
		}
	}
	
	private static Pattern altPattern = 
		Pattern.compile("Leave package with (.*),\\s*(.*) in apartment (.*)[.]\\s*Telephone[: ]([ 0-9\\(\\)\\-]*).*");


	public int doStartTag() throws JspException {
		
		int ni = altDeliveryInfo.lastIndexOf("NOTE:");

		String fldDlvInstructions= ni == -1 ? "": altDeliveryInfo.substring(ni+5).trim();
		String toMatch = ni == -1 ? altDeliveryInfo : altDeliveryInfo.substring(0,ni).trim();
		
		String fldAltFirstName = "";
		String fldAltLastName = "";
		String fldAltApartment = "";
		String fldAltPhone = "";
		
		boolean leaveWithDoorman = false;
		boolean leaveWithNeighbor = false;

		Matcher m = altPattern.matcher(toMatch);

		if (m.matches()) { 
			// then we have alternate delivery in the form
			// {Leave packagge with {LAST}, {FIRST} in apartment {APT}. Telephone: {TEL}.}
			
			fldAltLastName = m.group(1);
			fldAltFirstName = m.group(2);
			fldAltApartment = m.group(3);
			fldAltPhone = m.group(4);
			
			leaveWithNeighbor = true;
			
			LOGGER.debug("Alternate delivery with neighbor: First name: " + fldAltFirstName + ", Last name: " + fldAltLastName + ", APT: " +
					fldAltApartment + ", Tel: " + fldAltPhone);
		} else if (toMatch.indexOf("doorman") != -1) {
			LOGGER.debug("Alternate delivery: leave with doorman");
			leaveWithDoorman = true;
		}
		
		pageContext.setAttribute("fldAltFirstName", fldAltFirstName);
		pageContext.setAttribute("fldAltLastName", fldAltLastName);
		pageContext.setAttribute("fldAltApartment", fldAltApartment);
		pageContext.setAttribute("fldAltPhone", fldAltPhone);
		pageContext.setAttribute("fldDlvInstructions", fldDlvInstructions);

		pageContext.setAttribute("leaveWithDoorman", new Boolean(leaveWithDoorman));
		pageContext.setAttribute("leaveWithNeighbor", new Boolean(leaveWithNeighbor));

		return EVAL_BODY_BUFFERED;
	}


} // class AlternateAddressInfoTag
