/*
 * $Workfile:GetOrderTag.java$
 *
 * $Date:8/13/2003 6:40:45 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.delivery.DlvRestrictionManager;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionType;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.callcenter.GenericLocatorTag;

/**
 *
 * @version $Revision:9$
 * @author $Author:Gopal $
 */
public class GetRestrictedDlvTag extends AbstractGetterTag {

	
	private String restrictionId = null;
	private static Category LOGGER = LoggerFactory.getInstance(GetRestrictedDlvTag.class);

	public void setRestrictionId(String restrictionId) {
		this.restrictionId = restrictionId;
	}

	protected Object getResult() throws FDResourceException {
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String actionName = request.getParameter("actionType");
		
		if("editRestriction".equalsIgnoreCase(actionName))
		{
		    LOGGER.debug("restrictionId :"+restrictionId);
		    List restList=new ArrayList();
		    restList.add(DlvRestrictionManager.getDlvRestriction(restrictionId));
		    return restList; 	
		}
		else if("getPlatterRestriction".equalsIgnoreCase(actionName))
		{
		    //System.out.println("restrictionId :"+restrictionId);
		    return DlvRestrictionManager.getDlvRestrictions(EnumDlvRestrictionReason.PLATTER.getName(),EnumDlvRestrictionType.RECURRING_RESTRICTION.getName(),EnumDlvRestrictionCriterion.PURCHASE.getName()); 	
		}
		else if("getKosherRestriction".equalsIgnoreCase(actionName))
		{
		    //System.out.println("restrictionId :"+restrictionId);
		    return DlvRestrictionManager.getDlvRestrictions(EnumDlvRestrictionReason.KOSHER.getName(),EnumDlvRestrictionType.ONE_TIME_RESTRICTION.getName(),EnumDlvRestrictionCriterion.DELIVERY.getName()); 	
		}
		else if("editAddrRestriction".equalsIgnoreCase(actionName))
		{
		    //System.out.println("restrictionId :"+restrictionId);
			String address1=request.getParameter("addr1");		    
			String apartment = NVL.apply(request.getParameter("apart"), "");
		    String zipCode = NVL.apply(request.getParameter("zip"), "");
		    List restList=new ArrayList();   
		    restList.add(DlvRestrictionManager.getAddressRestriction(address1,apartment,zipCode));
		    return restList; 	
		}
		return Collections.EMPTY_LIST;
	}
	
	
	
	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			//return "com.freshdirect.delivery.restriction.RestrictionI";
			return "java.util.List";
		}

	}

}
