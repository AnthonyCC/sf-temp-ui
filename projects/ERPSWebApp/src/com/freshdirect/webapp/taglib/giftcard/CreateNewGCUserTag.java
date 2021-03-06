package com.freshdirect.webapp.taglib.giftcard;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.common.context.StoreContext;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.fdstore.CookieMonster;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SiteAccessControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.util.StoreContextUtil;

public class CreateNewGCUserTag  extends com.freshdirect.framework.webapp.BodyTagSupport{
	
	private static Category LOGGER = LoggerFactory.getInstance(SiteAccessControllerTag.class);
	
	private static final String GIFTCARD_DEFAULT_ZPCOD="11101";
	private static final EnumServiceType GIFTCARD_DEFAULT_SERVICE_TYPE=EnumServiceType.PICKUP;
	private String resultName;
	
	
	public void setResult(String resultName) {
		this.resultName = resultName;
	}
	
	@Override
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
		    	LOGGER.info("oldUser.getUser().getCookie():"+oldUser.getUser().getCookie());
		    }
			createGCUser(request,response);
			
			FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
			LOGGER.info("newUser.getUser().getCookie():"+user.getUser().getCookie());
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
		
			user.setRecipientList(oldUser.getRecipientList());
			user.setGiftCardType(oldUser.getGiftCardType());
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
		
		FDDeliveryServiceSelectionResult result=FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(GIFTCARD_DEFAULT_ZPCOD, 
				StoreContextUtil.getStoreContext(pageContext.getSession()).getEStoreId());//new DlvServiceSelectionResult();
		//result.addServiceStatus(EnumServiceType.HOME, EnumDeliveryStatus.DELIVER);
//		result.addServiceStatus(GIFTCARD_DEFAULT_SERVICE_TYPE, EnumDeliveryStatus.DELIVER);	
		StoreContext storeContext =StoreContextUtil.getStoreContext(request.getSession());
		FDUser fduser=FDCustomerManager.createNewUser(GIFTCARD_DEFAULT_ZPCOD, GIFTCARD_DEFAULT_SERVICE_TYPE, storeContext.getEStoreId());
		
		FDSessionUser user = new FDSessionUser(fduser,request.getSession());
		user.setSelectedServiceType(GIFTCARD_DEFAULT_SERVICE_TYPE);
		//Added the following line for zone pricing to keep user service type up-to-date.
		user.setZPServiceType(GIFTCARD_DEFAULT_SERVICE_TYPE);
		user.setAvailableServices(result.getAvailableServices());					
		CookieMonster.storeCookie(user, response);				
     		
		request.getSession().setAttribute(SessionName.USER, user);								
	}

}
