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

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.sempixel.FDSemPixelCache;
import com.freshdirect.fdstore.sempixel.SemPixelModel;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.delivery.model.EnumDeliveryStatus;
import com.freshdirect.webapp.ajax.location.LocationHandlerService;
import com.freshdirect.webapp.util.LocatorUtil;
import com.freshdirect.webapp.util.RobotRecognizer;
import com.freshdirect.webapp.util.RobotUtil;
import com.freshdirect.webapp.util.StoreContextUtil;

public class CheckLoginStatusTag extends com.freshdirect.framework.webapp.TagSupport {

    private static final long serialVersionUID = -5813651711727931409L;

    private static final Category LOGGER = LoggerFactory.getInstance(CheckLoginStatusTag.class);

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

    @Override
    public int doStartTag() throws JspException {
        HttpSession session = pageContext.getSession();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

        /* APPDEV-2024 */
        if (request.getParameter("apc") != null) {
            session.setAttribute(SessionName.APC_PROMO, request.getParameter("apc"));
        }

        if (request.getParameter("TSAPROMO") != null) {
            session.setAttribute(SessionName.TSA_PROMO, request.getParameter("TSAPROMO"));
        }

        try {
            if (user == null) {
                user = UserUtil.createSessionUser(request, response, guestAllowed);
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

                user = LocatorUtil.useIpLocator(request.getSession(), request, (HttpServletResponse) pageContext.getResponse());

                if (user == null) {
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

                if (guestAllowed && RobotRecognizer.isFriendlyRobot(request.getHeader("User-Agent"), request.getServerName())) {
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
                    if (user != null || (user = LocatorUtil.useIpLocator(request.getSession(), request, (HttpServletResponse) pageContext.getResponse())) == null) {
                        doRedirect(user == null);
                        return SKIP_BODY;
                    }
                }
            }
        }

        /*
         * if this is a new session and the user types http://www.bestcellarsnewyork.com/ redirect to http://www.bestcellarsnewyork.com/department.jsp?deptId=win
         */
        if ((request.getServerName().toLowerCase().indexOf("bestcellarsnewyork") > -1) && session.isNew()) {
            this.redirectPage = "/department.jsp?deptId=win";
            doRedirect(true);
        }

            UserUtil.touchUser(request, user);
            UserUtil.updateUserRelatedContexts(user);

            EnumServiceType serviceType = EnumServiceType.getEnum(request.getParameter("serviceType"));
            LocationHandlerService.getDefaultService().updateServiceType(user, serviceType);

            if (this.id != null) {
                pageContext.setAttribute(this.id, user);
            }

        } catch (InvalidUserException e) {
            doRedirect(true);
            return SKIP_BODY;
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
            UserUtil.newSession(request.getSession(), (HttpServletResponse) pageContext.getResponse());

            EnumDeliveryStatus dlvStatus = serviceResult.getServiceStatus(EnumServiceType.HOME);

            if (EnumDeliveryStatus.DELIVER.equals(dlvStatus) || EnumDeliveryStatus.PARTIALLY_DELIVER.equals(dlvStatus)) {
                try {
                    UserUtil.createSessionUser(EnumServiceType.HOME, serviceResult.getAvailableServices(), request.getSession(), (HttpServletResponse) pageContext.getResponse(),
                            this.address);
                } catch (FDResourceException e) {
                    LOGGER.error(e);
                }

                // success, HOME user
                return EVAL_BODY_INCLUDE;
            } else {
                try {
                    UserUtil.createSessionUser(EnumServiceType.PICKUP, serviceResult.getAvailableServices(), request.getSession(), (HttpServletResponse) pageContext.getResponse(),
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
    
}
