package com.freshdirect.webapp.taglib.fdstore;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.PricingContext;

import com.freshdirect.delivery.DlvServiceSelectionResult;
import com.freshdirect.delivery.EnumDeliveryStatus;

import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.WineFilter;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDDeliveryTimeslotModel;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.semPixel.FDSemPixelCache;
import com.freshdirect.fdstore.semPixel.SemPixelModel;

import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;

import com.freshdirect.webapp.util.RobotRecognizer;

import org.apache.log4j.Category;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;


public class CheckLoginStatusTag extends com.freshdirect.framework.webapp.TagSupport
    implements SessionName {
    private static final long serialVersionUID = -5813651711727931409L;
    private static Category LOGGER = LoggerFactory.getInstance(CheckLoginStatusTag.class);
    private String id;
    private String redirectPage;
    private boolean guestAllowed = true;
    private boolean recognizedAllowed = true;
    private boolean noRedirect = false;
    private boolean redirected = false;
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

        if (RobotRecognizer.isHostileRobot(
                    (HttpServletRequest) pageContext.getRequest())) {
            doRedirect(true);

            return SKIP_BODY;
        }

        if (user != null) {
            user.touch();
        }

        if (user == null) {
            // try to figure out user identity based on persistent cookie
            try {
                LOGGER.debug("attempting to load user from cookie");
                user = CookieMonster.loadCookie((HttpServletRequest) pageContext.getRequest());
            } catch (FDResourceException ex) {
                LOGGER.warn(ex);
            }

            if (user != null) {
                LOGGER.debug("user was found!  placing in session");
                session.setAttribute(SessionName.USER, user);
            }

            // // new COS changes redirect corporate user to corporate page
            if (user != null) {
                LOGGER.debug("entering the corporate check" +
                    user.getUserServiceType());
            }

            LOGGER.debug("request.getRequestURI() :" + request.getRequestURI());

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

        //
        // 
        // Fix for APPDEV-755
        //
        if (guestAllowed &&
                (request.getRequestURI()
                            .indexOf("forget_password_main_confirmation.jsp") != -1) &&
                (request.getParameter("siteAccessPage") == null)) {
            //
            // make sure the robot has a user in it's session so that pages
            // won't blow up for it
            //
            if (user == null) {
                FDUser robotUser = new FDUser(new PrimaryKey("robot"));
                Set<EnumServiceType> availableServices = new HashSet<EnumServiceType>();
                availableServices.add(EnumServiceType.HOME);
                robotUser.setSelectedServiceType(EnumServiceType.HOME);
                robotUser.setAvailableServices(availableServices);
                robotUser.isLoggedIn(false);
                user = new FDSessionUser(robotUser, session);
                session.setAttribute(USER, user);
            }
        }

        //If user is coming from pretty URL redirect it to site_access_lite page
        //APPDEV-1196 removes site access lite
        if (user == null) {
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

        if ((user == null) ||
                ((user.getLevel() == FDSessionUser.GUEST) && !guestAllowed) ||
                ((user.getLevel() == FDSessionUser.RECOGNIZED) &&
                !recognizedAllowed) ||
                (user.isNotServiceable() &&
                (user.getLevel() != FDSessionUser.SIGNED_IN) &&
                !user.isDepotUser() && !guestAllowed)) {
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
                    FDUser robotUser = new FDUser(new PrimaryKey("robot"));
                    Set<EnumServiceType> availableServices = new HashSet<EnumServiceType>();
                    availableServices.add(EnumServiceType.HOME);
                    robotUser.setSelectedServiceType(EnumServiceType.HOME);
                    robotUser.setAvailableServices(availableServices);
                    robotUser.isLoggedIn(false);
                    user = new FDSessionUser(robotUser, session);
                    session.setAttribute(USER, user);
                }
            } else {
                if (request.getParameter("siteAccessPage") == null) { //if user navigates on site access do not redirect
                    doRedirect(user == null);

                    return SKIP_BODY;
                }
            }
        }

        if (this.id != null) {
            pageContext.setAttribute(this.id, user);
        }

        if (user != null) {
            ContentFactory.getInstance()
                          .setCurrentPricingContext(user.getPricingContext());
            WineFilter.clearAvailabilityCache((user.getPricingContext() != null)
                ? user.getPricingContext() : PricingContext.DEFAULT);
        } else {
            LOGGER.warn("cannot set pricing context");
            WineFilter.clearAvailabilityCache(PricingContext.DEFAULT);
        }

        // Set/clear masquerade agent for activity logging
        if (user != null) {
            FDActionInfo.setMasqueradeAgentTL(user.getMasqueradeAgent());
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

        DlvServiceSelectionResult serviceResult = null;

        try {
            serviceResult = checkByZipCode(request, result);
        } catch (FDResourceException e1) {
            e1.printStackTrace();
        }

        if (result.isSuccess() && (serviceResult != null)) {
            newSession();

            EnumDeliveryStatus dlvStatus = serviceResult.getServiceStatus(EnumServiceType.HOME);

            if (EnumDeliveryStatus.DELIVER.equals(dlvStatus) ||
                    EnumDeliveryStatus.PARTIALLY_DELIVER.equals(dlvStatus)) {
                try {
                    this.createUser(EnumServiceType.HOME,
                        serviceResult.getAvailableServices());
                } catch (FDResourceException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                //success, HOME user
                return EVAL_BODY_INCLUDE;
            } else {
                try {
                    this.createUser(EnumServiceType.PICKUP,
                        serviceResult.getAvailableServices());
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

            //parse out zip code from referer params
            String sem_refererQueryParams = NVL.apply(sem_refUrl.getQuery(), "");

            if (!"".equals(sem_refererQueryParams)) {
                sem_zipParse = sem_refererQueryParams.split("&");

                for (int m = 0; m < sem_zipParse.length; m++) {
                    StringTokenizer sem_stQueryParams = new StringTokenizer(sem_zipParse[m],
                            "=");

                    while (sem_stQueryParams.hasMoreTokens()) {
                        String key = sem_stQueryParams.nextToken();
                        String val = sem_stQueryParams.nextToken();

                        //we only care about zip code here
                        if ("zipcode".equals(key)) {
                            sem_zipCode = val;
                        }
                    }
                }
            }
        }

        //make sure we have values to check against
        if (!"".equals(sem_pixels) && !"".equals(sem_zipCode) &&
                !"null".equals(sem_referer)) {
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

    private DlvServiceSelectionResult checkByZipCode(
        HttpServletRequest request, ActionResult result)
        throws FDResourceException {
        this.populate(request);
        this.validate(result, false);

        if (result.isFailure()) {
            return null;
        }

        return FDDeliveryManager.getInstance()
                                .checkZipCode(this.address.getZipCode());
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

    private void createUser(EnumServiceType serviceType,
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

            user = new FDSessionUser(FDCustomerManager.createNewUser(
                        this.address, serviceType), session);
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
