package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.EnumAddressType;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.delivery.DlvServiceSelectionResult;
import com.freshdirect.delivery.EnumDeliveryStatus;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.ActionError;

public class SiteAccessControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	private static Category LOGGER = LoggerFactory.getInstance(SiteAccessControllerTag.class);

	private String action = null;
	private String successPage = null;
	private String moreInfoPage = null;
	private String failureHomePage = null;
	private String altDeliveryHomePage = "alt_dlv_home.jsp";
	private String altDeliveryCorporatePage = "alt_dlv_corporate.jsp";
	private String failureCorporatePage = "/survey/cos_site_access_survey.jsp";
	
	private String resultName = null;

	private EnumServiceType serviceType = null;


	private AddressModel address = null;

	public void setAction(String action) {
		this.action = action;
	}

	public void setSuccessPage(String successPage) {		
		if (successPage != null && successPage.indexOf("://") != -1) {
			LOGGER.debug("successPage before throwing IllegalArgument Exception :"+successPage);
			throw new IllegalArgumentException("Invalid successPage specified");
		}
		
		this.successPage = successPage;
	}

	public void setMoreInfoPage(String moreInfoPage) {
		this.moreInfoPage = moreInfoPage;
	}

	public void setAltDeliveryHomePage(String altDeliveryHomePage) {
		this.altDeliveryHomePage = altDeliveryHomePage;
	}

	public void setAltDeliveryCorporatePage(String altDeliveryCommercialPage) {
		this.altDeliveryCorporatePage = altDeliveryCommercialPage;
	}
	
	public void setFailureCorporatePage (String failureCorporatePage){
		this.failureCorporatePage = failureCorporatePage;
	}
	
	public void setFailureHomePage(String failureHomePage) {
		this.failureHomePage = failureHomePage;
	}

	public void setResult(String resultName) {
		this.resultName = resultName;
	}
	
	public void setServiceType(String serviceTypeStr) {
		if(serviceTypeStr!=null){
			this.serviceType=EnumServiceType.getEnum(serviceTypeStr); 
		}
		if(this.serviceType==null){
			throw new IllegalArgumentException("Invalid ServiceType specified");
		}
	}
	
	
	private void newSession() {
		HttpSession session = pageContext.getSession();
		// clear session
		// [segabor]: instead of wiping out all session entries delete just the 'customer'
		session.removeAttribute(SessionName.USER);
		// remove cookie
		CookieMonster.clearCookie((HttpServletResponse)pageContext.getResponse());
	}

	public int doStartTag() throws JspException {
		ActionResult result = new ActionResult();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		if ("POST".equalsIgnoreCase(request.getMethod())) {
			try {
				if ("saveEmail".equalsIgnoreCase(action)) {
					saveEmail(request, result);
					if (result.isSuccess()) {
						return doRedirect(successPage);
					}
				} else if ("checkByZipCode".equalsIgnoreCase(action)) {
					DlvServiceSelectionResult serviceResult = checkByZipCode(request, result);

					//System.out.println(" WEB result :"+result);
					if (result.isSuccess()) {
						newSession();
						
						if("WEB".equals(this.serviceType.getName())){
							EnumDeliveryStatus homeDlvStatus = serviceResult.getServiceStatus(EnumServiceType.HOME);
							
							if (EnumDeliveryStatus.DELIVER.equals(homeDlvStatus)) {
								this.createUser(EnumServiceType.HOME, serviceResult.getAvailableServices());
							} else {
								EnumDeliveryStatus corpDlvStatus = serviceResult.getServiceStatus(EnumServiceType.CORPORATE);
								if (EnumDeliveryStatus.DELIVER.equals(corpDlvStatus)) {
									this.createUser(EnumServiceType.CORPORATE, serviceResult.getAvailableServices());
								}else { 
									this.createUser(EnumServiceType.PICKUP, serviceResult.getAvailableServices());
								}
							}
							//System.out.println(" WEB this.serviceType :"+this.serviceType);
							doRedirect(successPage);
						} else {
							EnumDeliveryStatus dlvStatus = serviceResult.getServiceStatus(this.serviceType);
							//System.out.println(" NOTWEB this.serviceType :"+this.serviceType);
							
							if (EnumDeliveryStatus.DELIVER.equals(dlvStatus)) {
								this.createUser(this.serviceType, serviceResult.getAvailableServices());
							} else { 
								this.createUser(EnumServiceType.PICKUP, serviceResult.getAvailableServices());
							}
						
							if (this.moreInfoPage.indexOf('?') < 0) {
								this.moreInfoPage += "?serviceType=" + this.serviceType.getName();
								
							} else {
								this.moreInfoPage += "&serviceType=" + this.serviceType.getName();
							}
						
							if (EnumServiceType.CORPORATE.equals(this.serviceType)) {																					
								if(EnumDeliveryStatus.DONOT_DELIVER.equals(dlvStatus)){
									// check home delivry is available
									if(EnumDeliveryStatus.DELIVER.equals(serviceResult.getServiceStatus(EnumServiceType.HOME))){
										// show E No Corporate HOME delivarable Survey presented /site_access/alt_dlv_home.jsp
										doRedirect(altDeliveryHomePage);
									}
									else if(EnumDeliveryStatus.DONOT_DELIVER.equals(serviceResult.getServiceStatus(EnumServiceType.HOME))){
										// forward to site_access/cos_site_access_survey.jsp
										doRedirect(failureCorporatePage);
									}
									else if(EnumDeliveryStatus.PARTIALLY_DELIVER.equals(serviceResult.getServiceStatus(EnumServiceType.HOME))){
										// forward to more address page									
										doRedirect(moreInfoPage);
									}
									//System.out.println(" EnumDeliveryStatus.DONOT_DELIVER :"+EnumDeliveryStatus.DONOT_DELIVER);
									//return doRedirect(failureHomePage);
								}
	//							else if(EnumDeliveryStatus.PARTIALLY_DELIVER.equals(dlvStatus)){
	//								// forward to more address page
	//								doRedirect(moreInfoPage);
	//							}
								//return doRedirect(moreInfoPage);
							}
						
							if (EnumServiceType.HOME.equals(this.serviceType)) { 
								if(EnumDeliveryStatus.RARELY_DELIVER.equals(dlvStatus)){								
									// forward to /site_access/delivery.jsp with rarely deliver message( not required now)
									return doRedirect(failureHomePage);
								}
								else if(EnumDeliveryStatus.DONOT_DELIVER.equals(dlvStatus)){								
									// forward to /site_access/delivery.jsp with no deliver message(not required now) 
									return doRedirect(failureHomePage);
								}
							}						
							if (EnumDeliveryStatus.PARTIALLY_DELIVER.equals(dlvStatus)) {
								return doRedirect(moreInfoPage);
							}							
							if (EnumDeliveryStatus.DELIVER.equals(dlvStatus)) {
								return doRedirect(successPage);
							}
							return doRedirect(failureHomePage);
						}
					}
				} else if ("checkByAddress".equalsIgnoreCase(action)) {
					checkByAddress(request, result);
				}
			} catch (FDResourceException re) {
				LOGGER.warn("FDResourceException occured", re);
				result.addError(true, "technicalDifficulty", SystemMessageList.MSG_TECHNICAL_ERROR);
			}
		}
		pageContext.setAttribute(resultName, result);
		return EVAL_BODY_BUFFERED;
	}

	private int doRedirect(String url) throws JspException {
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		try {
			response.sendRedirect(response.encodeRedirectURL(url));
			JspWriter writer = pageContext.getOut();
			writer.close();
			return SKIP_BODY;
		} catch (IOException ioe) {
			throw new JspException(ioe.getMessage());
		}
	}

	private DlvServiceSelectionResult checkByZipCode(HttpServletRequest request, ActionResult result) throws FDResourceException {
		this.populate(request);
		this.validate(result, false);

		if (result.isFailure()) {
			//Reset successpage to null
			setSuccessPage(null);
			//Passing an indicator to the site access page that zip code validation failed.
			request.setAttribute("failed", "true");
			return null;
		}

		return FDDeliveryManager.getInstance().checkZipCode(this.address.getZipCode());
	}

	private void populate(HttpServletRequest request) {
		this.address = new AddressModel();
		String homeZipcode = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode()),"").trim();
		String corpZipcode = NVL.apply(request.getParameter(EnumUserInfoName.DLV_CORP_ZIPCODE.getCode()),"").trim();
		
		System.out.println("successPage in populate: " + successPage);
		String gcLanding = FDStoreProperties.getGiftCardLandingUrl();
		String rhLanding = FDStoreProperties.getRobinHoodLandingUrl();
		boolean isGiftCardEnabled = FDStoreProperties.isGiftCardEnabled();
		boolean isRobinHoodEnabled = FDStoreProperties.isRobinHoodEnabled();
		
		if((successPage.indexOf(gcLanding)>-1 && isGiftCardEnabled)||(successPage.indexOf(rhLanding)>-1 && isRobinHoodEnabled)){
			if(!"".equals(homeZipcode) && "".equals(corpZipcode)){
				this.address.setZipCode(homeZipcode);
			}else{
				this.address.setZipCode(corpZipcode);
			}
			this.serviceType = EnumServiceType.getEnum(NVL.apply("WEB", "").trim());
		}else{
			if(!"".equals(homeZipcode) && "".equals(corpZipcode)){
				this.address.setZipCode(homeZipcode);
				this.serviceType = EnumServiceType.getEnum(NVL.apply(request.getParameter("serviceType"), "").trim());
			}else{
				this.address.setZipCode(corpZipcode);
				this.serviceType = EnumServiceType.getEnum(NVL.apply(request.getParameter("corpServiceType"), "").trim());
			}
		}
		//this.serviceType = EnumServiceType.getEnum(NVL.apply(request.getParameter("serviceType"), "").trim());
		this.address.setAddress1(NVL.apply(request.getParameter(EnumUserInfoName.DLV_ADDRESS_1.getCode()), "").trim());		
		this.address.setApartment(NVL.apply(request.getParameter(EnumUserInfoName.DLV_APARTMENT.getCode()), "").trim());
		this.address.setCity(NVL.apply(request.getParameter(EnumUserInfoName.DLV_CITY.getCode()), "").trim());
		this.address.setState(NVL.apply(request.getParameter(EnumUserInfoName.DLV_STATE.getCode()), "").trim());				
	}

	private void validate(ActionResult result, boolean validateAddress) throws FDResourceException {
		String zipCode = this.address.getZipCode();
		if ("".equals(zipCode)) {
			result.addError(true, EnumUserInfoName.DLV_ZIPCODE.getCode(), SystemMessageList.MSG_REQUIRED);
		} else {
			boolean isNumber = true;
			try {
				Integer.parseInt(zipCode);
			} catch (NumberFormatException ne) {
				isNumber = false;
			}
			if (zipCode.length() != 5 || !isNumber || zipCode.equals("00000")) {
				result.addError(true, EnumUserInfoName.DLV_ZIPCODE.getCode(), SystemMessageList.MSG_ZIP_CODE);
			}
		}
		if (validateAddress) {
			if ("".equals(this.address.getAddress1())) {
				result.addError(true, EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_REQUIRED);
			}
			if (address.getCity()==null || address.getCity().trim().length()==0) {
				result.addError(true, EnumUserInfoName.DLV_CITY.getCode(), SystemMessageList.MSG_REQUIRED);
			}
			if (address.getState()==null || address.getState().trim().length()==0) {
				result.addError(true, EnumUserInfoName.DLV_STATE.getCode(), SystemMessageList.MSG_REQUIRED);
			}else if (address.getState().length() < 2) {
				result.addError(new ActionError(EnumUserInfoName.DLV_STATE.getCode(), SystemMessageList.MSG_REQUIRED));
			} else {
				result.addError(!AddressUtil.validateState(address.getState()), EnumUserInfoName.DLV_STATE.getCode(),
					SystemMessageList.MSG_UNRECOGNIZE_STATE);
			}
			
			if (result.isSuccess()) {
				this.address = AddressUtil.scrubAddress(this.address, true, result);
			}
		}
	}

	private int checkByAddress(HttpServletRequest request, ActionResult result) throws FDResourceException, JspException {
		this.populate(request);
		this.validate(result, true);
		if (result.isFailure()) {
			return EVAL_BODY_BUFFERED;
		}
		try {

			EnumServiceType altServiceType = EnumServiceType.HOME.equals(this.serviceType) ? EnumServiceType.CORPORATE : EnumServiceType.HOME;
			
			DlvServiceSelectionResult serviceResult = FDDeliveryManager.getInstance().checkAddress(this.address);
			EnumDeliveryStatus reqStatus = serviceResult.getServiceStatus(this.serviceType);
			EnumDeliveryStatus altStatus = serviceResult.getServiceStatus(altServiceType);
			
			boolean reqDeliverable = EnumDeliveryStatus.DELIVER.equals(reqStatus);
			boolean altDeliverable = EnumDeliveryStatus.DELIVER.equals(altStatus);
			
			boolean validServiceType = !(EnumServiceType.HOME.equals(this.serviceType) && EnumAddressType.FIRM.equals(this.address
				.getAddressType()));

			int serviceAvailability = getServiceAvailability(validServiceType, reqDeliverable, altDeliverable);

			EnumServiceType st = serviceAvailability==AV_REQUESTED ? this.serviceType : (serviceAvailability==AV_ALTERNATE ? altServiceType : EnumServiceType.PICKUP);
			this.createUser(st, serviceResult.getAvailableServices());
    	       		
			String page = getRedirectPage(this.serviceType, serviceAvailability);
			return this.doRedirect(page);

		} catch (FDInvalidAddressException ae) {
			result.addError(true, EnumUserInfoName.DLV_CANT_GEOCODE.getCode(), 
					MessageFormat.format(SystemMessageList.MSG_CANT_GEOCODE_ZIP_CHECK,new Object[] {UserUtil.getCustomerServiceContact(request)}));
		}
		return EVAL_BODY_BUFFERED;
	}

	
	private final static int AV_REQUESTED = 0;
	private final static int AV_ALTERNATE = 1;
	private final static int AV_UNAVAIL = 2;

	protected int getServiceAvailability(boolean validServiceType, boolean reqDeliverable, boolean altDeliverable) {
		if (validServiceType && reqDeliverable) {
			return AV_REQUESTED;
		} else if (altDeliverable) {
			return AV_ALTERNATE;
		} else {
			return AV_UNAVAIL;
		}
	}

	protected String getRedirectPage(EnumServiceType reqServiceType, int result) {
		switch (result) {
			case AV_REQUESTED:
				return successPage;
			case AV_ALTERNATE:
				if (EnumServiceType.HOME.equals(reqServiceType)) {
					return this.altDeliveryCorporatePage;
				}
				return this.altDeliveryHomePage;
			default:
				if (EnumServiceType.CORPORATE.equals(reqServiceType)) {
					return this.failureCorporatePage;
				}
				return this.failureHomePage;
		}
	}
	
	private void saveEmail(HttpServletRequest request, ActionResult result) throws FDResourceException {
		HttpSession session = pageContext.getSession();
		String email = request.getParameter("email");
		

		if (email != null)
			email = email.trim();
		if ((email != null) && (!"".equals(email)) && (!com.freshdirect.mail.EmailUtil.isValidEmailAddress(email))) {
			result.addError(true, "email", SystemMessageList.MSG_EMAIL_FORMAT);
			return;
		}

		if (!result.isSuccess()) {
			LOGGER.debug("THERE IS AN ERROR!!");
			return;
		}

		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

		if ((email != null) && (!"".equals(email))) {					
			FDDeliveryManager.getInstance().saveFutureZoneNotification(email, user.getZipCode(),this.serviceType);
			LOGGER.debug("SAVED FUTURE ZONE TO NOTIFY");
		}
	}

	private void createUser(EnumServiceType serviceType, Set availableServices) throws FDResourceException {
		HttpSession session = pageContext.getSession();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		

		if ((user == null) || ((user.getZipCode() == null) && (user.getDepotCode() == null))) {
			//
			// if there is no user object or a dummy user object created in
			// CallCenter, make a new using this zipcode
			// make sure to hang on to the cart that might be in progress in
			// CallCenter
			//
			FDCartModel oldCart = null;
			if (user != null) {
				oldCart = user.getShoppingCart();
			}
			user = new FDSessionUser(FDCustomerManager.createNewUser(this.address, serviceType), session);
			user.setSelectedServiceType(serviceType);
			//Added the following line for zone pricing to keep user service type up-to-date.
			user.setZPServiceType(serviceType);
			user.setAvailableServices(availableServices);

			
			if (oldCart != null) {
				user.setShoppingCart(oldCart);
			}

			CookieMonster.storeCookie(user, response);
			session.setAttribute(SessionName.USER, user);

		} else {	
			//
			// otherwise, just update the zipcode in their existing object if
			// they haven't yet registered
			//
			if (user.getLevel() < FDUser.RECOGNIZED) {
				user.setAddress(this.address);
				user.setSelectedServiceType(serviceType);
				//Added the following line for zone pricing to keep user service type up-to-date.
				user.setZPServiceType(serviceType);
				user.setAvailableServices(availableServices);

				CookieMonster.storeCookie(user, response);
				FDCustomerManager.storeUser(user.getUser());
				session.setAttribute(SessionName.USER, user);
			}
						
		}
        //The previous recommendations of the current session need to be removed.
        session.removeAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS);
        session.removeAttribute(SessionName.SAVINGS_FEATURE_LOOK_UP_TABLE);
        session.removeAttribute(SessionName.PREV_SAVINGS_VARIANT);
		
	}

}
