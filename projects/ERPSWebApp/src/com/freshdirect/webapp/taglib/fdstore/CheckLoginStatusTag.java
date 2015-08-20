package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.context.StoreContext;
import com.freshdirect.common.context.MasqueradeContext;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.customer.ServiceTypeUtil;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.WineFilter;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.iplocator.IpLocatorClient;
import com.freshdirect.fdstore.iplocator.IpLocatorData;
import com.freshdirect.fdstore.iplocator.IpLocatorEventDTO;
import com.freshdirect.fdstore.iplocator.IpLocatorException;
import com.freshdirect.fdstore.iplocator.IpLocatorUtil;
import com.freshdirect.fdstore.sempixel.FDSemPixelCache;
import com.freshdirect.fdstore.sempixel.SemPixelModel;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.util.LocatorUtil;
import com.freshdirect.logistics.delivery.model.EnumDeliveryStatus;
import com.freshdirect.webapp.util.RequestClassifier;
import com.freshdirect.webapp.util.RequestUtil;
import com.freshdirect.webapp.util.RobotRecognizer;
import com.freshdirect.webapp.util.RobotUtil;
import com.freshdirect.webapp.util.StoreContextUtil;


public class CheckLoginStatusTag extends com.freshdirect.framework.webapp.TagSupport
    implements SessionName {
    private static final long serialVersionUID = -5813651711727931409L;
    private static Category LOGGER = LoggerFactory.getInstance(CheckLoginStatusTag.class);
    private static String IP_LOCATOR_MOCKED_IP_ADDRESS = "iplocator_mocked_ip_address";
    private String id;
    private String redirectPage;
    private boolean guestAllowed = true;
    private boolean recognizedAllowed = true;
    private boolean noRedirect = false;
    private boolean redirected = false;
    private boolean ddppPreview = false;
    private String semZipCode = "";
    private String pixelNames = "";
    private AddressModel address = null;
    private EnumServiceType serviceType = null;
    

    public void setId(String id) {
        this.id = id;
    }

    public void setRedirectPage(String redirectPage) {
        this.redirectPage = redirectPage;
    }

    public void setGuestAllowed(boolean guestAllowed) {
        this.guestAllowed = guestAllowed;
    }

    public void setRecognizedAllowed(boolean recognizedAllowed) {
        this.recognizedAllowed = recognizedAllowed;
    }

    public boolean isNoRedirect() {
        return noRedirect;
    }

    public void setNoRedirect(boolean noRedirect) {
        this.noRedirect = noRedirect;
    }

    public void setSemZipCode(String semZipCode) {
        this.semZipCode = semZipCode;
    }

    public String getSemZipCode() {
        return semZipCode;
    }

    public void setPixelNames(String pixelNames) {
        this.pixelNames = pixelNames;
    }

    public String getPixelNames() {
        return pixelNames;
    }

    private EnumServiceType getServiceType() {
        return serviceType;
    }

    public int doStartTag() throws JspException {
        HttpSession session = pageContext.getSession();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
        
        
        /*APPDEV-2024*/
        if(request.getParameter("apc") != null) {
        	session.setAttribute(SessionName.APC_PROMO, request.getParameter("apc"));
        }
        
        if(request.getParameter("TSAPROMO") != null) {
        	session.setAttribute(SessionName.TSA_PROMO, request.getParameter("TSAPROMO"));
        }
        	

        if (RobotRecognizer.isHostileRobot(
                    (HttpServletRequest) pageContext.getRequest())) {
            doRedirect(true);

            return SKIP_BODY;
        }

        if (user != null) {
            user.touch();
        }

        if (user == null) {
        	StoreContextUtil.getStoreContext(session);
            // try to figure out user identity based on persistent cookie
            try {
                //LOGGER.debug("attempting to load user from cookie");
                user = CookieMonster.loadCookie((HttpServletRequest) pageContext.getRequest());
            } catch (FDResourceException ex) {
                LOGGER.warn(ex);
            }

            if (user != null) {
                user.setFutureZoneNotificationEmailSentForCurrentAddress(true); //prevent asking for e-mail address in case of returning customer
            	LOGGER.debug("user was found!  placing in session");
                session.setAttribute(SessionName.USER, user);
            }

            // // new COS changes redirect corporate user to corporate page
            if (user != null) {
                LOGGER.debug("entering the corporate check" +
                    user.getUserServiceType());
            }
            
            if(user != null) {            
                FDCustomerCouponUtil.initCustomerCoupons(session);
            }
            
            //LOGGER.debug("request.getRequestURI() :" + request.getRequestURI());

            if ((user != null) &&
                    EnumServiceType.CORPORATE.equals(user.getUserServiceType())) {
                // only index page request will be redirected to corporate page
                if (request.getRequestURI().indexOf("index.jsp") != -1) {
                    this.redirectPage = "/department.jsp?deptId=COS";
                    doRedirect(true);

                    return SKIP_BODY;
                }
            }
        }

        //APPDEV-3197 try to identify friendly robot on every page before using IP Locator
        if (user == null && guestAllowed && RobotRecognizer.isFriendlyRobot((HttpServletRequest) pageContext.getRequest())) {
           	user = RobotUtil.createRobotUser(session);
        }

        //
        // 
        // Fix for APPDEV-755
        //
        if (
        		guestAllowed &&
                (
                	(request.getRequestURI().indexOf("forget_password_main_confirmation.jsp") != -1) ||
                	/* fix product info page used in erpsy daisy */
                	( request.getParameter("sku2url") != null && !"".equalsIgnoreCase(request.getParameter("sku2url")) )
                ) &&
                (request.getParameter("siteAccessPage") == null)
        	) {
            //
            // make sure the robot has a user in it's session so that pages
            // won't blow up for it
            //
            if (user == null) {
            	user = RobotUtil.createRobotUser(session);
            }

           /* if ((user != null) && ddppPreview &&
                    !"".equals(NVL.apply(request.getParameter("ddppZoneId"), ""))) {
                PricingContext pricingContext = new PricingContext(request.getParameter(
                            "ddppZoneId"));

                ContentFactory.getInstance().setCurrentPricingContext(pricingContext);
                //user.setPricingContext(pricingContext);
            }*/
        }

        //If user is coming from pretty URL redirect it to site_access_lite page
        //APPDEV-1196 removes site access lite
        if (user == null ) {
            //SEM Project (APPDEV-1598)
            //check if we can bypass now
            try {
                if (checkForBypass()) {
                    //we're good, create a new user
                    return createBypassedUser();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if ((request.getRequestURI().indexOf("index.jsp") <= -1) &&
                    (request.getParameter("siteAccessPage") == null)) {
                
            	if (checkForwardToMobilePage()){
 	                return SKIP_BODY;
            	}
            	
            	if ((user = LocatorUtil.useIpLocator(request.getSession(), request, (HttpServletResponse)pageContext.getResponse(), this.address)) == null){

	            	StringBuffer redirBuf = new StringBuffer();
	                //redirBuf.append("/site_access/site_access_lite.jsp?successPage="+request.getRequestURI());
	                LOGGER.debug(
	                    "redirecting to: /site_access/site_access.jsp?successPage=" +
	                    request.getRequestURI());
	                redirBuf.append("/site_access/site_access.jsp?successPage=" +
	                    request.getRequestURI());
	
	                String requestQryString = request.getQueryString();
	
	                if ((requestQryString != null) &&
	                        (requestQryString.trim().length() > 0)) {
	                    redirBuf.append(URLEncoder.encode("?" +
	                            request.getQueryString()));
	                }
	
	                this.redirectPage = redirBuf.toString();
	                doRedirect(true);
	
	                return SKIP_BODY;
            	}
            }
        }

        if ((user == null) ||
            ((user.getLevel() == FDSessionUser.GUEST) && !guestAllowed) ||
            ((user.getLevel() == FDSessionUser.RECOGNIZED) && !recognizedAllowed) ||
            (user.isNotServiceable() && (user.getLevel() != FDSessionUser.SIGNED_IN) && !user.isDepotUser() && !guestAllowed)) {
            //
            // redirect, unless this is a request from a friendly robot we want
            // to let in
            //
            if (guestAllowed &&
                    RobotRecognizer.isFriendlyRobot(
                        (HttpServletRequest) pageContext.getRequest())) {
                //
                // make sure the robot has a user in it's session so that pages
                // won't blow up for it
                //
                if (user == null) {
                	user = RobotUtil.createRobotUser(session);
                }
            } else {
                if (request.getParameter("siteAccessPage") == null) { //if user navigates on site access do not redirect
                	
                	if (user==null && checkForwardToMobilePage()){
     	                return SKIP_BODY;
                	}
                	
                	if (user!=null || (user=LocatorUtil.useIpLocator(request.getSession(), request, (HttpServletResponse)pageContext.getResponse(), this.address))==null){ //only do IP Sniff if user was null originally, else redirect to login page
	                	doRedirect(user == null);
	                    return SKIP_BODY;
                	}
                }
            }
        }

        if (this.id != null) {
            pageContext.setAttribute(this.id, user);
        }

        if (user != null) {
        	UserContext userCtx=user.getUserContext();
            ContentFactory.getInstance().setCurrentUserContext(userCtx);
            
            WineFilter.clearAvailabilityCache((userCtx != null)
                ? userCtx.getPricingContext() : PricingContext.DEFAULT);
            ContentFactory.getInstance().setEligibleForDDPP(FDStoreProperties.isDDPPEnabled() || user.isEligibleForDDPP());
        } else {
            LOGGER.warn("cannot set pricing context");
            ContentFactory.getInstance().setCurrentUserContext(UserContext.createDefault());
            WineFilter.clearAvailabilityCache(PricingContext.DEFAULT);
            ContentFactory.getInstance().setEligibleForDDPP(FDStoreProperties.isDDPPEnabled());
        }

        // Set/clear masquerade agent for activity logging
        if (user != null) {
            MasqueradeContext masqueradeContext = user.getMasqueradeContext();
        	FDActionInfo.setMasqueradeAgentTL(masqueradeContext==null ? null : masqueradeContext.getAgentId());
            user.setClientIp(RequestUtil.getClientIp(request));
            user.setServerName(RequestUtil.getServerName());
        }

        /*
         *
         * if this is a new session and the user types
         * http://www.bestcellarsnewyork.com/ redirect to
         * http://www.bestcellarsnewyork.com/department.jsp?deptId=win
         */
        if ((request.getServerName().toLowerCase().indexOf("bestcellarsnewyork") > -1) &&
                session.isNew()) {
            this.redirectPage = "/department.jsp?deptId=win";
            doRedirect(true);
        }

        return EVAL_BODY_INCLUDE;
    }

    private void doRedirect(boolean firstRequest) throws JspException {
        if (noRedirect) {
            return;
        }

        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

        try {
            response.sendRedirect(response.encodeRedirectURL(
                    this.getRedirectURL(firstRequest)));

            JspWriter writer = pageContext.getOut();
            writer.close();
            this.redirected = true;
        } catch (IOException ioe) {
            throw new JspException(ioe.getMessage());
        }
    }

    protected String getRedirectURL(boolean firstRequest) {
        if (this.redirectPage != null) {
            return this.redirectPage;
        }

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        StringBuffer redirBuf = new StringBuffer(this.guestAllowed
                ? "/about/index.jsp?siteAccessPage=aboutus&successPage="
                : (firstRequest ? "/login/login_main.jsp?successPage="
                                : "/login/login.jsp?successPage="));
        redirBuf.append(request.getRequestURI());

        String requestQryString = request.getQueryString();

        if ((requestQryString != null) &&
                (requestQryString.trim().length() > 0)) {
            redirBuf.append(URLEncoder.encode("?" + request.getQueryString()));
        }

        return redirBuf.toString();
    }

    public int doEndTag() throws JspException {
        if (this.redirected) {
            return SKIP_PAGE;
        } else {
            return EVAL_PAGE;
        }
    }

    private int createBypassedUser() {
        ActionResult result = new ActionResult();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        FDDeliveryServiceSelectionResult serviceResult = null;

        try {
            serviceResult = checkByZipCode(request, result);
        } catch (FDResourceException e1) {
            e1.printStackTrace();
        }

        if (result.isSuccess() && (serviceResult != null)) {
        	LocatorUtil.newSession(request.getSession(), request, (HttpServletResponse)pageContext.getResponse());

            EnumDeliveryStatus dlvStatus = serviceResult.getServiceStatus(EnumServiceType.HOME);

            if (EnumDeliveryStatus.DELIVER.equals(dlvStatus) ||
                    EnumDeliveryStatus.PARTIALLY_DELIVER.equals(dlvStatus)) {
                try {
                	LocatorUtil.createUser(EnumServiceType.HOME,
                            serviceResult.getAvailableServices(), request.getSession(), (HttpServletResponse)pageContext.getResponse(), this.address);
                } catch (FDResourceException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                //success, HOME user
                return EVAL_BODY_INCLUDE;
            } else {
                try {
                    LocatorUtil.createUser(EnumServiceType.PICKUP,
                        serviceResult.getAvailableServices(), request.getSession(), (HttpServletResponse)pageContext.getResponse(), this.address);
                } catch (FDResourceException e) {
                    e.printStackTrace();
                }

                //zip code may not have delivery, use a PICKUP user
                return EVAL_BODY_INCLUDE;
            }
        }

        return SKIP_BODY;
    }

    @SuppressWarnings("unchecked")
    private boolean checkForBypass() throws MalformedURLException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        String sem_pixels = this.getPixelNames();
        String[] sem_zipParse = new String[0];
        String[] sem_parsePixels = new String[0];

        String sem_referer = NVL.apply(request.getHeader("referer"), "null");
        String sem_zipCode = "";

        if (!"".equals(sem_referer) && !"null".equals(sem_referer)) {
            URL sem_refUrl = new URL(sem_referer);
            sem_referer = sem_refUrl.getHost();

            //get zip code from request params
            sem_zipCode = NVL.apply(request.getParameter("zipcode"), "");
        }

        //make sure we have values to check against
        if (!"".equals(sem_pixels) && !"".equals(sem_zipCode)) {
            //load cache
            FDSemPixelCache spc = FDSemPixelCache.getInstance();

            //check if we're allowing all pixels
            if ("*".equals(this.getPixelNames())) {
                sem_pixels = ""; //reset string

                Collection keys = spc.getCachedSemPixels().keySet();
                int i = 1;

                for (Object key : keys) {
                    String curKey = (String) key;

                    if (!"".equals(curKey)) {
                        sem_pixels += curKey;

                        if (i != keys.size()) {
                            sem_pixels += ",";
                        }
                    }

                    i++;
                }
            }

            sem_parsePixels = sem_pixels.split(",");

            for (int n = 0; n < sem_parsePixels.length; n++) {
                SemPixelModel curSemPixel = null;

                curSemPixel = spc.getSemPixel(sem_parsePixels[n]);

                //we're returning a new model if not found, so check that the name matches
                if ((sem_parsePixels[n]).equals(curSemPixel.getName())) {
                    //matches check if enabled
                    if (curSemPixel.isEnabled()) {
                        //also enabled, check referer
                        List<String> sem_validRefers = curSemPixel.getValidReferers();

                        for (Iterator<String> lvr = sem_validRefers.iterator();
                                lvr.hasNext();) {
                            String curValidReferer = lvr.next();

                            if ((curValidReferer.indexOf(sem_referer) >= 0) ||
                                    "*".equals(curValidReferer)) {
                                //referer matches
                                curSemPixel.setParam("zipMatch", curValidReferer); //add to params

                                List<String> sem_validZips = curSemPixel.getValidZipCodes();

                                for (Iterator<String> lvz = sem_validZips.iterator();
                                        lvz.hasNext();) {
                                    String curValidZip = lvz.next();

                                    if ((curValidZip.indexOf(sem_zipCode) >= 0) ||
                                            "*".equals(curValidZip)) {
                                        //zip matches
                                        curSemPixel.setParam("zipMatch",
                                            curValidZip); //add to params

                                        return true;
                                    }
                                }
                            }
                        }
                    }
                } //end each pixel check
            } //end pixels loop
        }

        //no matches
        return false;
    }

    private void newSession() {
        HttpSession session = pageContext.getSession();
        // clear session
        // [segabor]: instead of wiping out all session entries delete just the 'customer'
        session.removeAttribute(SessionName.USER);
        // remove cookie
        CookieMonster.clearCookie((HttpServletResponse) pageContext.getResponse());
    }

    private FDDeliveryServiceSelectionResult checkByZipCode(
        HttpServletRequest request, ActionResult result)
        throws FDResourceException {
        this.populate(request);
        this.validate(result, false);

        if (result.isFailure()) {
            return null;
        }

        return FDDeliveryManager.getInstance()
                                .getDeliveryServicesByZipCode(this.address.getZipCode());
    }

    private void populate(HttpServletRequest request) {
        this.address = new AddressModel();

        String homeZipcode = NVL.apply(request.getParameter(
                    EnumUserInfoName.DLV_ZIPCODE.getCode()), "").trim();

        //check if we have a SEM zip, and override it here if so
        if (!"".equals(this.getSemZipCode())) {
            homeZipcode = this.getSemZipCode();
        }

        if (!"".equals(homeZipcode)) {
            this.address.setZipCode(homeZipcode);
            this.serviceType = getServiceType();
        }

        this.address.setAddress1(NVL.apply(request.getParameter(
                    EnumUserInfoName.DLV_ADDRESS_1.getCode()), "").trim());
        this.address.setApartment(NVL.apply(request.getParameter(
                    EnumUserInfoName.DLV_APARTMENT.getCode()), "").trim());
        this.address.setCity(NVL.apply(request.getParameter(
                    EnumUserInfoName.DLV_CITY.getCode()), "").trim());
        this.address.setState(NVL.apply(request.getParameter(
                    EnumUserInfoName.DLV_STATE.getCode()), "").trim());
    }

    private void validate(ActionResult result, boolean validateAddress)
        throws FDResourceException {
        String zipCode = this.address.getZipCode();

        if ("".equals(zipCode)) {
            result.addError(true, EnumUserInfoName.DLV_ZIPCODE.getCode(),
                SystemMessageList.MSG_REQUIRED);
        } else {
            boolean isNumber = true;

            try {
                Integer.parseInt(zipCode);
            } catch (NumberFormatException ne) {
                isNumber = false;
            }

            if ((zipCode.length() != 5) || !isNumber ||
                    zipCode.equals("00000")) {
                result.addError(true, EnumUserInfoName.DLV_ZIPCODE.getCode(),
                    SystemMessageList.MSG_ZIP_CODE);
            }
        }

        if (validateAddress) {
            if ("".equals(this.address.getAddress1())) {
                result.addError(true, EnumUserInfoName.DLV_ADDRESS_1.getCode(),
                    SystemMessageList.MSG_REQUIRED);
            }

            if ((address.getCity() == null) ||
                    (address.getCity().trim().length() == 0)) {
                result.addError(true, EnumUserInfoName.DLV_CITY.getCode(),
                    SystemMessageList.MSG_REQUIRED);
            }

            if ((address.getState() == null) ||
                    (address.getState().trim().length() == 0)) {
                result.addError(true, EnumUserInfoName.DLV_STATE.getCode(),
                    SystemMessageList.MSG_REQUIRED);
            } else if (address.getState().length() < 2) {
                result.addError(new ActionError(
                        EnumUserInfoName.DLV_STATE.getCode(),
                        SystemMessageList.MSG_REQUIRED));
            } else {
                result.addError(!AddressUtil.validateState(address.getState()),
                    EnumUserInfoName.DLV_STATE.getCode(),
                    SystemMessageList.MSG_UNRECOGNIZE_STATE);
            }

            if (result.isSuccess()) {
                this.address = AddressUtil.scrubAddress(this.address, true,
                        result);
            }
        }
    }

    private FDSessionUser createUser(EnumServiceType serviceType,
        Set<EnumServiceType> availableServices) throws FDResourceException {
        HttpSession session = pageContext.getSession();
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

        FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

        if ((user == null) ||
                ((user.getZipCode() == null) && (user.getDepotCode() == null))) {
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
            StoreContext storeContext =StoreContextUtil.getStoreContext(session);
            user = new FDSessionUser(FDCustomerManager.createNewUser(
                        this.address, serviceType,storeContext.getEStoreId()), session);
            user.setUserCreatedInThisSession(true);
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

        //To fetch and set customer's coupons.
		if(user != null){
			FDCustomerCouponUtil.initCustomerCoupons(session);
		}
        
        //The previous recommendations of the current session need to be removed.
        session.removeAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS);
        session.removeAttribute(SessionName.SAVINGS_FEATURE_LOOK_UP_TABLE);
        session.removeAttribute(SessionName.PREV_SAVINGS_VARIANT);
        return user;
    }

    public boolean isDdppPreview() {
        return ddppPreview;
    }

    public void setDdppPreview(boolean ddppPreview) {
        this.ddppPreview = ddppPreview;
    }
    
    private FDSessionUser useIpLocator(HttpServletRequest request) throws JspException{
    	FDSessionUser user = null;
    	
    	if (FDStoreProperties.isIpLocatorEnabled()) {
    		RequestClassifier requestClassifier = new RequestClassifier(request);
    		int rolloutPercent = FDStoreProperties.getIpLocatorRolloutPercent(); 
    		
    		if (requestClassifier.isInHashRange(rolloutPercent)){ //check if rolled out to user
		    	try {
	    			//used mocked ip address parameter (for testing) if exists
			    	String ip = NVL.apply(request.getParameter(IP_LOCATOR_MOCKED_IP_ADDRESS), RequestUtil.getClientIp(request)); 
		    		IpLocatorData ipLocatorData = IpLocatorClient.getInstance().getData(ip);
		    		
		    		IpLocatorEventDTO ipLocatorEventDTO = new IpLocatorEventDTO();
		    		ipLocatorEventDTO.setIp(ip);
		    		ipLocatorEventDTO.setIpLocZipCode(ipLocatorData.getZipCode());
		    		ipLocatorEventDTO.setIpLocCountry(ipLocatorData.getCountryCode());
		    		ipLocatorEventDTO.setIpLocRegion(ipLocatorData.getRegion());
		    		ipLocatorEventDTO.setIpLocCity(ipLocatorData.getCity());
		    		ipLocatorEventDTO.setUserAgent(requestClassifier.getUserAgent());
		    		ipLocatorEventDTO.setUaHashPercent(requestClassifier.getHashPercent());
		    		ipLocatorEventDTO.setIplocRolloutPercent(rolloutPercent);
		    		
		    		user = createUser(ipLocatorData, ipLocatorEventDTO);
	
		    	} catch (Exception e) {
					LOGGER.error("IP Locator failed: ", e);
				}

    		}    	
    	} 
    	
   		return user;
    }
    
    /** based on SiteAccessControllerTag.doStartTag()*/
    private FDSessionUser createUser(IpLocatorData ipLocatorData, IpLocatorEventDTO ipLocatorEventDTO) throws IpLocatorException{
    	FDSessionUser user = null;
    	
    	try {
    		boolean useIpLocatorData = IpLocatorUtil.validate(ipLocatorData);
	    	String zipCode = useIpLocatorData ? ipLocatorData.getZipCode() : FDStoreProperties.getDefaultPickupZoneId();
	    	
	    	newSession();
	    	address = new AddressModel();
	    	address.setZipCode(zipCode);
	    	Set<EnumServiceType> availableServices = FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(zipCode).getAvailableServices();
	    	
	    	//FDCustomerManager.createNewUser() inside createUser() will only use zipCode and resolve location based on that.
	    	//City and State information will be appended to user.address.
	    	user = createUser(ServiceTypeUtil.getPreferedServiceType(availableServices), availableServices);
	    	
	    	ipLocatorEventDTO.setFdUserId(user.getPrimaryKey());
	    	AddressModel address = user.getAddress();
	    	if (address != null){
		    	ipLocatorEventDTO.setFdZipCode(address.getZipCode());
		    	ipLocatorEventDTO.setFdState(address.getState());
		    	ipLocatorEventDTO.setFdCity(address.getCity());
	    	}
	    	
	    	if (FDStoreProperties.isIpLocatorEventLogEnabled()){
		    	try {
		    		//log IpLocatorEvent before appending data to user from IpLocatorData
		    		FDCustomerManager.logIpLocatorEvent(ipLocatorEventDTO);
		    	} catch (Exception e){
		    		LOGGER.error("logIpLocatorEvent failed", e);
		    	}
		    }
	    	LOGGER.debug("ipLocatorEventDTO: " + ipLocatorEventDTO);
	    	
	    	//If no data city/state is appended by createUser(), city and state fields will be taken from ipLocatorData
    		if (useIpLocatorData){
    			IpLocatorUtil.appendMissingFieldsToUserAddress(ipLocatorData, user.getUser());
    		}
	    	
    	} catch (Exception e) {
			LOGGER.error(e);
			throw new IpLocatorException(e);
		}
    	
    	return user;
    }
    

    private boolean checkForwardToMobilePage() throws JspException {
	    	HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
	    	HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
	
	    	response.addHeader("Pragma", "no-cache"); 
	    	String noMobile = "FALSE";
	    	if ( request.getParameter("noMobile") != null ) {
	    		noMobile = request.getParameter("noMobile");
	    	}
	
	    	String UA = request.getHeader("User-Agent").toLowerCase();

	    	//check for iphone/ipod and change results
	    	if (FDStoreProperties.isIphoneLandingEnabled() && (UA.indexOf("iphone;")>=0 || UA.indexOf("ipod;")>=0) ||
	    		FDStoreProperties.isAndroidLandingEnabled() && UA.indexOf("android")>=0 ) {

    			//check that site access isn't returning an error from the POST...
    			if ("FALSE".equals(noMobile) && "GET".equals(request.getMethod())){
					try {
						request.setAttribute("origReqUri", request.getRequestURI());
						request.getRequestDispatcher("/mobile/index.jsp").forward(request, response);
					} catch (ServletException e) {
						throw new JspException(e);
					} catch (IOException e) {
						throw new JspException(e);
					}
		            return true;
    			}
	    	}
	    	
	    	return false;
    }
}
