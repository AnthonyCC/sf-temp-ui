package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang.CharEncoding;
import org.apache.log4j.Category;

import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.context.MasqueradeContext;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.WineFilter;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.fdstore.sempixel.FDSemPixelCache;
import com.freshdirect.fdstore.sempixel.SemPixelModel;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.delivery.model.EnumDeliveryStatus;
import com.freshdirect.webapp.util.LocatorUtil;
import com.freshdirect.webapp.util.RequestUtil;
import com.freshdirect.webapp.util.RobotRecognizer;
import com.freshdirect.webapp.util.RobotUtil;
import com.freshdirect.webapp.util.StoreContextUtil;

public class CheckLoginStatusTag extends com.freshdirect.framework.webapp.TagSupport implements SessionName {

    private static final long serialVersionUID = -5813651711727931409L;

    private static Category LOGGER = LoggerFactory.getInstance(CheckLoginStatusTag.class);
    
    private String id;
    private String redirectPage;
    private boolean guestAllowed = true;
    private boolean recognizedAllowed = true;
    private boolean noRedirect = false;
    private boolean redirected = false;
    private boolean ddppPreview = false;
    private String semZipCode = "";
    private String pixelNames = "";
    private AddressModel address;
    private EnumServiceType serviceType;
    private static final String EMPTY = "";
	private static final String USER_AGENT = "User-Agent";
    

    @Override
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

    @Override
    public int doStartTag() throws JspException {
        HttpSession session = pageContext.getSession();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
        this.redirected=false;

        /* APPDEV-2024 */
        if (request.getParameter("apc") != null) {
            session.setAttribute(SessionName.APC_PROMO, request.getParameter("apc"));
        }

        if (request.getParameter("TSAPROMO") != null) {
            session.setAttribute(SessionName.TSA_PROMO, request.getParameter("TSAPROMO"));
        }

        if (RobotRecognizer.isHostileRobot((HttpServletRequest) pageContext.getRequest())) {
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
                // LOGGER.debug("attempting to load user from cookie");
                user = CookieMonster.loadCookie((HttpServletRequest) pageContext.getRequest());
            } catch (FDResourceException ex) {
                LOGGER.warn(ex);
            }

            if (user != null) {
                // prevent asking for e-mail address in case of returning customer
                user.setFutureZoneNotificationEmailSentForCurrentAddress(true);
                LOGGER.debug("user was found!  placing in session");
                session.setAttribute(SessionName.USER, user);
            }

            // // new COS changes redirect corporate user to corporate page
            if (user != null) {
                LOGGER.debug("entering the corporate check" + user.getUserServiceType());
            }

            if (user != null) {
                FDCustomerCouponUtil.initCustomerCoupons(session);
            }

            if ((user != null) && EnumServiceType.CORPORATE.equals(user.getUserServiceType()) && user.getUserContext() != null && user.getUserContext().getStoreContext() != null
                    && !EnumEStoreId.FDX.equals(user.getUserContext().getStoreContext().getEStoreId())) {
                // only index page request will be redirected to corporate page
                if (request.getRequestURI().indexOf("index.jsp") != -1) {
                    this.redirectPage = "/department.jsp?deptId=COS";
                    doRedirect(true);

                    return SKIP_BODY;
                }
            }
        }

        // APPDEV-3197 try to identify friendly robot on every page before using IP Locator
        if (user == null && guestAllowed && RobotRecognizer.isFriendlyRobot((HttpServletRequest) pageContext.getRequest())) {
            user = RobotUtil.createRobotUser(session);
        }

        //
        //
        // Fix for APPDEV-755
        //
        if (guestAllowed && ((request.getRequestURI().indexOf("forget_password_main_confirmation.jsp") != -1) ||
        /* fix product info page used in erpsy daisy */
                (request.getParameter("sku2url") != null && !"".equalsIgnoreCase(request.getParameter("sku2url")))) && (request.getParameter("siteAccessPage") == null)) {
            //
            // make sure the robot has a user in it's session so that pages
            // won't blow up for it
            //
            if (user == null) {
                user = RobotUtil.createRobotUser(session);
            }
        }

        // If user is coming from pretty URL redirect it to site_access_lite page
        // APPDEV-1196 removes site access lite
        if (user == null) {
            // SEM Project (APPDEV-1598)
            // check if we can bypass now
            try {
                if (checkForBypass()) {
                    // we're good, create a new user
                    return createBypassedUser();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if ((request.getRequestURI().indexOf("index.jsp") <= -1) && (request.getParameter("siteAccessPage") == null)) {

                if (checkForwardToMobilePage()) {
                    return SKIP_BODY;
                }

                if ((user = LocatorUtil.useIpLocator(request.getSession(), request, (HttpServletResponse) pageContext.getResponse(), this.address)) == null) {
                    StringBuffer redirBuf = new StringBuffer();
                    LOGGER.debug("redirecting to: /site_access/site_access.jsp?successPage=" + request.getRequestURI());
                    redirBuf.append("/site_access/site_access.jsp?successPage=" + request.getRequestURI());

                    String requestQryString = request.getQueryString();

                    if ((requestQryString != null) && (requestQryString.trim().length() > 0)) {
                        try {
                            redirBuf.append(URLEncoder.encode("?" + requestQryString, CharEncoding.UTF_8));
                        } catch (UnsupportedEncodingException e) {
                            LOGGER.error(e);
                        }
                    }

                    this.redirectPage = redirBuf.toString();
                    doRedirect(true);

                    return SKIP_BODY;
                }
            }
        }

        if ((user == null) || ((user.getLevel() == FDSessionUser.GUEST) && !guestAllowed) || ((user.getLevel() == FDSessionUser.RECOGNIZED) && !recognizedAllowed)
                || (user.isNotServiceable() && (user.getLevel() != FDSessionUser.SIGNED_IN) && !user.isDepotUser() && !guestAllowed)) {
            //
            // redirect, unless this is a request from a friendly robot we want
            // to let in
            //
            if (guestAllowed && RobotRecognizer.isFriendlyRobot((HttpServletRequest) pageContext.getRequest())) {
                //
                // make sure the robot has a user in it's session so that pages
                // won't blow up for it
                //
                if (user == null) {
                    user = RobotUtil.createRobotUser(session);
                }
            } else {
                if (request.getParameter("siteAccessPage") == null) { // if user navigates on site access do not redirect

                    if (user == null && checkForwardToMobilePage()) {
                        return SKIP_BODY;
                    }

                    // only do IP Sniff if was null originally, else redirect to login page
                    if (user != null || (user = LocatorUtil.useIpLocator(request.getSession(), request, (HttpServletResponse) pageContext.getResponse(), this.address)) == null) {
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
            UserContext userCtx = user.getUserContext();
            ContentFactory.getInstance().setCurrentUserContext(userCtx);

            WineFilter.clearAvailabilityCache((userCtx != null) ? userCtx.getPricingContext() : PricingContext.DEFAULT);
            ContentFactory.getInstance().setEligibleForDDPP(FDStoreProperties.isDDPPEnabled() || user.isEligibleForDDPP());
        } else {
            LOGGER.warn("cannot set pricing context");
            ContentFactory.getInstance().setCurrentUserContext(UserContext.createDefault(CmsManager.getInstance().getEStoreEnum()));
            WineFilter.clearAvailabilityCache(PricingContext.DEFAULT);
            ContentFactory.getInstance().setEligibleForDDPP(FDStoreProperties.isDDPPEnabled());
        }

        // Set/clear masquerade agent for activity logging
        if (user != null) {
            MasqueradeContext masqueradeContext = user.getMasqueradeContext();
            FDActionInfo.setMasqueradeAgentTL(masqueradeContext == null ? null : masqueradeContext.getAgentId());
            user.setClientIp(RequestUtil.getClientIp(request));
            user.setServerName(RequestUtil.getServerName());
        }
        if (user != null) {
        	StringBuffer buf = new StringBuffer();
    		buf.append(getHttpHeader(request, USER_AGENT));
    		if(isMobile(buf.toString()) && FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user))
    			user.setMobilePlatForm(true);
        }
        
        /*
         * if this is a new session and the user types http://www.bestcellarsnewyork.com/ redirect to http://www.bestcellarsnewyork.com/department.jsp?deptId=win
         */
        if ((request.getServerName().toLowerCase().indexOf("bestcellarsnewyork") > -1) && session.isNew()) {
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
            response.sendRedirect(response.encodeRedirectURL(this.getRedirectURL(firstRequest)));

            //JspWriter writer = pageContext.getOut();
           // writer.close();
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
        StringBuffer redirBuf = new StringBuffer(this.guestAllowed ? "/about/index.jsp?siteAccessPage=aboutus&successPage="
                : (firstRequest ? "/login/login_main.jsp?successPage=" : "/login/login.jsp?successPage="));
        redirBuf.append(request.getRequestURI());

        String requestQryString = request.getQueryString();

        if ((requestQryString != null) && (requestQryString.trim().length() > 0)) {
            try {
                redirBuf.append(URLEncoder.encode("?" + requestQryString, CharEncoding.UTF_8));
            } catch (UnsupportedEncodingException e) {
                LOGGER.error(e);
            }
        }

        HttpSession session = pageContext.getSession();
        session.setAttribute(SessionName.PREV_SUCCESS_PAGE, request.getRequestURI());

        return redirBuf.toString();
    }

    @Override
    public int doEndTag() throws JspException {
    	return EVAL_PAGE;
       /* if (this.redirected) {
            return SKIP_PAGE;
        } else {
            return EVAL_PAGE;
        }*/
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
            LocatorUtil.newSession(request.getSession(), request, (HttpServletResponse) pageContext.getResponse());

            EnumDeliveryStatus dlvStatus = serviceResult.getServiceStatus(EnumServiceType.HOME);

            if (EnumDeliveryStatus.DELIVER.equals(dlvStatus) || EnumDeliveryStatus.PARTIALLY_DELIVER.equals(dlvStatus)) {
                try {
                    LocatorUtil.createUser(EnumServiceType.HOME, serviceResult.getAvailableServices(), request.getSession(), (HttpServletResponse) pageContext.getResponse(),
                            this.address);
                } catch (FDResourceException e) {
                    LOGGER.error(e);
                }

                // success, HOME user
                return EVAL_BODY_INCLUDE;
            } else {
                try {
                    LocatorUtil.createUser(EnumServiceType.PICKUP, serviceResult.getAvailableServices(), request.getSession(), (HttpServletResponse) pageContext.getResponse(),
                            this.address);
                } catch (FDResourceException e) {
                    e.printStackTrace();
                }

                // zip code may not have delivery, use a PICKUP user
                return EVAL_BODY_INCLUDE;
            }
        }

        return SKIP_BODY;
    }

    private boolean checkForBypass() throws MalformedURLException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        String sem_pixels = this.getPixelNames();
        String[] sem_parsePixels = new String[0];

        String sem_referer = NVL.apply(request.getHeader("referer"), "null");
        String sem_zipCode = "";

        if (!"".equals(sem_referer) && !"null".equals(sem_referer)) {
            URL sem_refUrl = new URL(sem_referer);
            sem_referer = sem_refUrl.getHost();

            // get zip code from request params
            sem_zipCode = NVL.apply(request.getParameter("zipcode"), "");
        }

        // make sure we have values to check against
        if (!"".equals(sem_pixels) && !"".equals(sem_zipCode)) {
            // load cache
            FDSemPixelCache spc = FDSemPixelCache.getInstance();

            // check if we're allowing all pixels
            if ("*".equals(this.getPixelNames())) {
                sem_pixels = ""; // reset string

                Collection<String> keys = spc.getCachedSemPixels().keySet();
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

                // we're returning a new model if not found, so check that the name matches
                if ((sem_parsePixels[n]).equals(curSemPixel.getName())) {
                    // matches check if enabled
                    if (curSemPixel.isEnabled()) {
                        // also enabled, check referer
                        List<String> sem_validRefers = curSemPixel.getValidReferers();

                        for (Iterator<String> lvr = sem_validRefers.iterator(); lvr.hasNext();) {
                            String curValidReferer = lvr.next();

                            if ((curValidReferer.indexOf(sem_referer) >= 0) || "*".equals(curValidReferer)) {
                                // referer matches
                                curSemPixel.setParam("zipMatch", curValidReferer); // add to params

                                List<String> sem_validZips = curSemPixel.getValidZipCodes();

                                for (Iterator<String> lvz = sem_validZips.iterator(); lvz.hasNext();) {
                                    String curValidZip = lvz.next();

                                    if ((curValidZip.indexOf(sem_zipCode) >= 0) || "*".equals(curValidZip)) {
                                        // zip matches
                                        curSemPixel.setParam("zipMatch", curValidZip); // add to params

                                        return true;
                                    }
                                }
                            }
                        }
                    }
                } // end each pixel check
            } // end pixels loop
        }

        // no matches
        return false;
    }

    private FDDeliveryServiceSelectionResult checkByZipCode(HttpServletRequest request, ActionResult result) throws FDResourceException {
        this.populate(request);
        this.validate(result, false);

        if (result.isFailure()) {
            return null;
        }

        return FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(this.address.getZipCode(), StoreContextUtil.getStoreContext(pageContext.getSession()).getEStoreId());
    }

    private void populate(HttpServletRequest request) {
        this.address = new AddressModel();

        String homeZipcode = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode()), "").trim();

        // check if we have a SEM zip, and override it here if so
        if (!"".equals(this.getSemZipCode())) {
            homeZipcode = this.getSemZipCode();
        }

        if (!"".equals(homeZipcode)) {
            this.address.setZipCode(homeZipcode);
            this.serviceType = getServiceType();
        }

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

            if ((zipCode.length() != 5) || !isNumber || zipCode.equals("00000")) {
                result.addError(true, EnumUserInfoName.DLV_ZIPCODE.getCode(), SystemMessageList.MSG_ZIP_CODE);
            }
        }

        if (validateAddress) {
            if ("".equals(this.address.getAddress1())) {
                result.addError(true, EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_REQUIRED);
            }

            if ((address.getCity() == null) || (address.getCity().trim().length() == 0)) {
                result.addError(true, EnumUserInfoName.DLV_CITY.getCode(), SystemMessageList.MSG_REQUIRED);
            }

            if ((address.getState() == null) || (address.getState().trim().length() == 0)) {
                result.addError(true, EnumUserInfoName.DLV_STATE.getCode(), SystemMessageList.MSG_REQUIRED);
            } else if (address.getState().length() < 2) {
                result.addError(new ActionError(EnumUserInfoName.DLV_STATE.getCode(), SystemMessageList.MSG_REQUIRED));
            } else {
                result.addError(!AddressUtil.validateState(address.getState()), EnumUserInfoName.DLV_STATE.getCode(), SystemMessageList.MSG_UNRECOGNIZE_STATE);
            }

            if (result.isSuccess()) {
                this.address = AddressUtil.scrubAddress(this.address, true, result);
            }
        }
    }

    public boolean isDdppPreview() {
        return ddppPreview;
    }

    public void setDdppPreview(boolean ddppPreview) {
        this.ddppPreview = ddppPreview;
    }

    private boolean checkForwardToMobilePage() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

        response.addHeader("Pragma", "no-cache");
        String noMobile = "FALSE";
        if (request.getParameter("noMobile") != null) {
            noMobile = request.getParameter("noMobile");
        }

        String UA = request.getHeader("User-Agent").toLowerCase();

        // check for iphone/ipod and change results
        if (FDStoreProperties.isIphoneLandingEnabled() && (UA.indexOf("iphone;") >= 0 || UA.indexOf("ipod;") >= 0)
                || FDStoreProperties.isAndroidLandingEnabled() && UA.indexOf("android") >= 0) {

            // check that site access isn't returning an error from the POST...
            if ("FALSE".equals(noMobile) && "GET".equals(request.getMethod())) {
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
    
    private String getHttpHeader(HttpServletRequest request, String headerName) {
		return request.getHeader(headerName) != null ? request.getHeader(headerName) : EMPTY;
	}
    private boolean isMobile(String useragent) {
		String ua = (useragent==null) ? "" : useragent.toLowerCase();
		if (!"".equals(ua)) {
			if(ua.matches("(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows ce|xda|xiino).*")||ua.substring(0,4).matches("(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-")) {
				return true;
			}
		}
		return false;
	}
}
