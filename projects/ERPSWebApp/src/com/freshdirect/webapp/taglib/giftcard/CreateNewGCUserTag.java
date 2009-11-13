package com.freshdirect.webapp.taglib.giftcard;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import weblogic.auddi.util.Logger;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ActivityLog;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.delivery.DlvServiceSelectionResult;
import com.freshdirect.delivery.EnumDeliveryStatus;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.giftcard.FDGiftCardInfoList;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.CookieMonster;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SiteAccessControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class CreateNewGCUserTag  extends com.freshdirect.framework.webapp.BodyTagSupport{
	
	private static Category LOGGER = LoggerFactory.getInstance(SiteAccessControllerTag.class);
	
	private static final String GIFTCARD_DEFAULT_ZPCOD="11101";
	private static final EnumServiceType GIFTCARD_DEFAULT_SERVICE_TYPE=EnumServiceType.PICKUP;
	private String resultName;
	
	
	public void setResult(String resultName) {
		this.resultName = resultName;
	}
	
	public int doStartTag() throws JspException {
		
	  ActionResult result=null;	
	 try{
		 result = new ActionResult();	 
	   HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
	   HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
	   LOGGER.info("request.getMethod() :"+request.getMethod());
	   if ("GET".equalsIgnoreCase(request.getMethod()) && request.getParameter("firstTimeVisit")==null) {
		   		  		   						
			HttpSession session = pageContext.getSession(); 
		    FDSessionUser oldUser = (FDSessionUser)session.getAttribute(SessionName.USER);
		    if(null !=oldUser && null!=oldUser.getUser().getCookie()){
		    	Logger.info("oldUser.getUser().getCookie():"+oldUser.getUser().getCookie());
		    }
			createGCUser(request,response);
			
			FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
			Logger.info("newUser.getUser().getCookie():"+user.getUser().getCookie());
			switchOldGCContent(oldUser,user);
	   }
				
	 } catch (FDResourceException re) {
			LOGGER.warn("FDResourceException occured while trying to create the new Gift Card User", re);
			result.addError(true, "technicalDifficulty", SystemMessageList.MSG_TECHNICAL_ERROR);
	}
	 
		pageContext.setAttribute(resultName, result);
	 

	 return EVAL_BODY_INCLUDE;
	}
	
	
	private void switchOldGCContent(FDSessionUser oldUser,FDSessionUser user) throws FDResourceException  {
		
		FDCartModel oldCart = null;
			
		if (oldUser != null) {
			FDUser oldFdUser=oldUser.getUser();			
			oldCart = oldUser.getShoppingCart();
		
			user.setRecipientList(oldUser.getRecipentList());
	     	//Set the last entered sender name and sender email to request.
	        user.setLastSenderName(oldUser.getLastSenderName());
	     	user.setLastSenderEmail(oldUser.getLastSenderEmail());     	
	     	user.setLastRecipAdded(true);
	     	user.setDonationCart(oldUser.getDonationCart());
	        user.setDonationTotalQuantity(oldUser.getDonationTotalQuantity());
	
	     	
	     	user.setDonationCart(oldUser.getDonationCart());
	     	user.setDonationTotalQuantity(oldUser.getDonationTotalQuantity());
		}
	}
	
	
	private void createGCUser(HttpServletRequest request,HttpServletResponse response) throws FDResourceException {
		
		DlvServiceSelectionResult result=FDDeliveryManager.getInstance().checkZipCode(GIFTCARD_DEFAULT_ZPCOD);//new DlvServiceSelectionResult();
		//result.addServiceStatus(EnumServiceType.HOME, EnumDeliveryStatus.DELIVER);
//		result.addServiceStatus(GIFTCARD_DEFAULT_SERVICE_TYPE, EnumDeliveryStatus.DELIVER);				
		FDUser fduser=FDCustomerManager.createNewUser(GIFTCARD_DEFAULT_ZPCOD, GIFTCARD_DEFAULT_SERVICE_TYPE);
		
		FDSessionUser user = new FDSessionUser(fduser,request.getSession());
		user.setSelectedServiceType(GIFTCARD_DEFAULT_SERVICE_TYPE);
		user.setAvailableServices(result.getAvailableServices());					
		CookieMonster.storeCookie(user, response);				
     		
     	//newSession();
		request.getSession().setAttribute(SessionName.USER, user);								
	}
	
	
//	private void newSession() {
//		HttpSession session = pageContext.getSession();
//		// clear session
//		// [segabor]: instead of wiping out all session entrise delete just the 'customer'
//		session.removeAttribute(SessionName.USER);
//		// remove cookie
//		//CookieMonster.clearCookie((HttpServletResponse)pageContext.getResponse());
//	}

}
