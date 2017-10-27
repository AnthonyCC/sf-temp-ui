package com.freshdirect.mobileapi.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpHeaders;
import org.apache.log4j.Category;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.FDCouponProperties;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.util.PublishId;
import com.freshdirect.common.context.FulfillmentContext;
import com.freshdirect.common.context.StoreContext;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.customer.EnumExternalLoginSource;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDActionNotAllowedException;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CMSComponentType;
import com.freshdirect.fdstore.content.CMSImageBannerModel;
import com.freshdirect.fdstore.content.CMSImageModel;
import com.freshdirect.fdstore.content.CMSPageRequest;
import com.freshdirect.fdstore.content.CMSSectionModel;
import com.freshdirect.fdstore.content.CMSWebPageModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ImageBanner;
import com.freshdirect.fdstore.content.StoreModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.accounts.external.ExternalAccountManager;
import com.freshdirect.fdstore.customer.ejb.FDCustomerEStoreModel;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.fdstore.util.Buildver;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.catalog.model.CatalogInfo;
import com.freshdirect.mobileapi.controller.data.CMSPotatoSectionModel;
import com.freshdirect.mobileapi.controller.data.EnumResponseAdditional;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.request.BrowseQuery;
import com.freshdirect.mobileapi.controller.data.response.CartDetail;
import com.freshdirect.mobileapi.controller.data.response.HasCartDetailField;
import com.freshdirect.mobileapi.controller.data.response.HasLoggedInField;
import com.freshdirect.mobileapi.controller.data.response.LoggedIn;
import com.freshdirect.mobileapi.controller.data.response.PageMessageResponse;
import com.freshdirect.mobileapi.controller.data.response.Timeslot;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoCartException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.filters.CORSFilter;
import com.freshdirect.mobileapi.model.Cart;
import com.freshdirect.mobileapi.model.MessageCodes;
import com.freshdirect.mobileapi.model.OrderHistory;
import com.freshdirect.mobileapi.model.OrderInfo;
import com.freshdirect.mobileapi.model.RequestData;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.User;
import com.freshdirect.mobileapi.model.tagwrapper.HttpContextWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.HttpSessionWrapper;
import com.freshdirect.mobileapi.service.Oas247Service;
import com.freshdirect.mobileapi.service.OasService;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.BrowseUtil;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.mobileapi.util.ProductPotatoUtil;
import com.freshdirect.wcms.CMSContentFactory;
import com.freshdirect.webapp.ajax.product.data.ProductPotatoData;
import com.freshdirect.webapp.taglib.fdstore.CookieMonster;
import com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;
import com.freshdirect.webapp.util.LocatorUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


public abstract class BaseController extends AbstractController implements MessageCodes {

    private static final Category LOGGER = LoggerFactory.getInstance(BaseController.class);

    private static final String JSON = "JSON";
    private static final String JSON_RENDERED = "json-rendered";
    private static final String FEED_PAGE_TYPE = "Feed";
    private static final String TODAYS_PICK_PAGE_TYPE = "TodaysPick";

    //new page types below only available in the new getPageComponent endpoint, not the legacy getPage endpoint
    private static final String CAROUSEL_BANNER_PAGE_TYPE = "CarouselBanner";
    private static final String TODAYS_PICK_HEAD_PAGE_TYPE = "TodaysPickHead";
    private static final String TODAYS_PICK_BODY_PAGE_TYPE = "TodaysPickBody";
    private static final String FEED_HEAD_PAGE_TYPE = "FeedHead";
    private static final String FEED_BODY_PAGE_TYPE = "FeedBody";
    

    public static OasService oasService = new Oas247Service();

    protected static Map<String, String> configParams = new HashMap<String, String>();

    private ThreadLocal<Boolean> fakedUserForRequest = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return Boolean.FALSE;
		}
    };

    private Resource file;

    public Resource getFile() {
        return file;
    }

    public void setFile(Resource file) {
        this.file = file;
    }

    public MobileSessionData getMobileSessionData(HttpServletRequest request) {
        MobileSessionData mobileSessionData = (MobileSessionData) request.getSession().getAttribute("MobileSessionData");
        if (mobileSessionData == null) {
            mobileSessionData = new MobileSessionData();
            request.getSession().setAttribute("MobileSessionData", mobileSessionData);
        }
        return mobileSessionData;
    }

    public void resetMobileSessionData(HttpServletRequest request) {
        request.getSession().setAttribute("MobileSessionData", null);
    }

    /**
     * This has a 
     */
    private static final String PRODUCT_TERMS_WRAPPER_TEMPLATE = "product-terms-wrapper.ftl";

    protected Configuration config = new Configuration();

    protected void postInit() throws Exception {
    }

    /**
     * init method for initializing freemarket template engine
     */
    public final void init() throws Exception {
        config.setServletContextForTemplateLoading(getServletContext(), "WEB-INF/templates");

        try {
            configParams = getMapper().readValue(file.getInputStream(), HashMap.class);
        } catch (JsonParseException e) {
            throw new JsonException(e);
        } catch (JsonMappingException e) {
            throw new JsonException(e);
        } catch (IOException e) {
            throw new JsonException(e);
        }

        postInit();
    }

    /**
     * Wraps HTML snippet product terms
     * 
     * @param terms
     * @return
     * @throws TemplateException 
     * @throws IOException 
     */
    protected String getProductWrappedTerms(String terms) throws IOException, TemplateException {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("terms", terms);
        return getTemplateValue(PRODUCT_TERMS_WRAPPER_TEMPLATE, root);
    }

    private String getTemplateValue(String templateName, Object root) throws IOException, TemplateException {
        Writer out = new StringWriter();
        Template temp = config.getTemplate(templateName);
        temp.process(root, out);
        return out.toString();
    }

    /** 
     * Extract what we need to get out of request/session wrappers in tagwrapper
     * and set it within our session (not request)
     * 
     * @param actualSession
     * @param resultBundle
     */
    protected void propogateSetSessionValues(HttpSession actualSession, ResultBundle resultBundle) {
        HttpSessionWrapper sessionWrapper = (HttpSessionWrapper) resultBundle.getExtraData(HttpContextWrapper.SESSION);
        if (null != sessionWrapper) {
            sessionWrapper.propogateSetValues(actualSession);
        }
    }

    protected RequestData qetRequestData(HttpServletRequest request) {
        RequestData requestData = new RequestData();
        requestData.setQueryString(request.getQueryString());
        requestData.setServer(request.getServerName());
        requestData.setRequestURI(request.getRequestURI());
        requestData.setSessionId(request.getSession().getId());
        return requestData;
    }

    public String getPostData(HttpServletRequest request, HttpServletResponse response) {
        String data = request.getParameter("data");
      /*  if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(data);
        }*/
        return data;
    }

    protected SessionUser getUserFromSession(HttpServletRequest request, HttpServletResponse response) throws NoSessionException {
        if (null == request.getSession().getAttribute(SessionName.USER)) {
            throw new NoSessionException("No session");
        }

        return SessionUser.wrap(request.getSession().getAttribute(SessionName.USER));
    }


    @Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
    	Throwable uncaughtException = null;
    	ModelAndView model = null;
    	try {
    		model = handleRequestInterna(request, response);
    	} catch (Throwable e) {
    		uncaughtException = e;
    	} finally {
    		if (uncaughtException != null) {
    			model = getModelAndView(JSON_RENDERED);
    			Message responseMessage = new Message();
    			responseMessage.addWarningMessage(traceFor(uncaughtException));
				setResponseMessage(model, responseMessage, null);
    		}
    	}
		return model;
	}

    
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    private final ModelAndView handleRequestInterna(HttpServletRequest request, HttpServletResponse response) throws JsonException,
            FDException, ServiceException {
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
		response.setHeader("Pragma", "no-cache"); 
		response.setDateHeader("Expires", 0);
        ModelAndView model = getModelAndView(JSON_RENDERED);
        String action = request.getParameter("action");
        String version = request.getParameter("ver");
        String paramVersion = "0";
        if ((null != version) && (!version.isEmpty())) {
            SessionUser user = null;
            Throwable uncaughtException = null;
            try {
                paramVersion = version;

                if (!paramVersion.equals(MobileApiProperties.getCurrentApiVersion())) {
                    throw new IllegalArgumentException();
                }

                try {
                    if (isExtraResponseRequested(request)) {
                        user = getUser(request, response);
                    } else {
                        user = getUserFromSession(request, response);
                        user.setUserContext();
                        user.setEligibleForDDPP();
                    }
                } catch (NoSessionException e) {
                    if (validateUser()) {
                        throw e;
                    }
                }
                if (validateCart() && (null != user)) {
                    Cart cart = user.getShoppingCart();
                    if (!cart.isSet()) {
                        throw new NoCartException("Where my cart?");
                    }
                }
                fakedUserForRequest.set(Boolean.FALSE);
                model = processRequest(request, response, model, action, user);
            } catch (NoSessionException e) {
                Message responseMessage = getErrorMessage(ERR_SESSION_EXPIRED, "Session does not exist in the server.");
                setResponseMessage(model, responseMessage, user);
            } catch (NoCartException e) {
                Message responseMessage = getErrorMessage(ERR_CART_EXPIRED, "User's shopping cart doesn't exists Que passo?");
                setResponseMessage(model, responseMessage, user);
//            } catch (NumberFormatException e) {
//                Message responseMessage = getErrorMessage(ERR_INCOMPATIBLE_CLIENT, MobileApiProperties.getDiscoveryServiceUrl());
//                setResponseMessage(model, responseMessage, user);
            } catch (IllegalArgumentException e) {
                //Message responseMessage = getErrorMessage(ERR_INCOMPATIBLE_CLIENT, MobileApiProperties.getDiscoveryServiceUrl());
                String printedTrace = traceFor(e);
				Message responseMessage = getErrorMessage(ERR_SYSTEM, printedTrace);
                setResponseMessage(model, responseMessage, user);
            } catch (FDActionNotAllowedException e) {
                Message responseMessage = getErrorMessage(VOUCHER_AUTHENTICATION, "This account is not eligible to add/edit/delete the delivery address.");
                setResponseMessage(model, responseMessage, user);
            }  catch (Throwable e) {
				uncaughtException = e;
			} finally {
				if (fakedUserForRequest.get().booleanValue()) {
					fakedUserForRequest.set(Boolean.FALSE);
					request.getSession().removeAttribute(SessionName.USER);
				}
				if (uncaughtException != null) {
//					throw uncaughtException;
					String printedTrace = traceFor(uncaughtException);
					Message responseMessage = getErrorMessage(ERR_SYSTEM, printedTrace);
	                setResponseMessage(model, responseMessage, user);
				}
			}
        }

        return model;
    }

    protected boolean isExtraResponseRequested(HttpServletRequest request) {
        String foodkickHeader = getExtraResponseRequests(request);
        return foodkickHeader != null && !foodkickHeader.isEmpty();
    }

    protected String getExtraResponseRequests(HttpServletRequest request) {
        String foodkickHeader = request.getHeader(CORSFilter.X_FD_EXTRA_RESPONSE_HEADER);
        return foodkickHeader;
    }

    protected boolean isResponseAdditionalEnable(HttpServletRequest request, EnumResponseAdditional responseAdditional) {
        boolean isResponseEnable = false;
        if (isExtraResponseRequested(request)) {
            String foodkickHeader = getExtraResponseRequests(request);
            for (String additional : StringUtils.commaDelimitedListToStringArray(foodkickHeader)) {
                if (responseAdditional.toString().equalsIgnoreCase(additional.trim())) {
                    isResponseEnable = true;
                    break;
                }
            }
        }
        return isResponseEnable;
    }
    
//    protected EnumSet<EnumResponseAdditional> getAllResponseAdditionalEnables(HttpServletRequest request) {
//    	EnumSet<EnumResponseAdditional> answer = null;
//    	if (isExtraResponseRequested(request)) {
//            String foodkickHeader = getExtraResponseRequests(request);
//            answer = parseResponseAdditionalEnables(foodkickHeader);
//        } else {
//            answer = parseResponseAdditionalEnables("");        	
//        }
//        return answer;
//    }

//    public static EnumSet<EnumResponseAdditional> parseResponseAdditionalEnables(String foodkickHeader) {
//    	EnumSet<EnumResponseAdditional> answer = EnumSet.noneOf(EnumResponseAdditional.class);
//        for (String additional : StringUtils.commaDelimitedListToStringArray(foodkickHeader)) {
//        	EnumResponseAdditional era = EnumResponseAdditional.fromString(additional);
//        	if(era != null) {
//        		answer.addAll(era.getImpliedSet());
//            }
//        }
//        return answer;
//    }


	protected String traceFor(Throwable e) {
		if (e == null) return "No Exception";
		while (true) {
			if (e.getCause() == null) break;
			e = e.getCause();
		}
	    StringWriter trace = new StringWriter();
	    e.printStackTrace(new PrintWriter(trace));
	    String printedTrace = "Exception: " + e.getMessage() + "\n" + trace.toString();
	    return printedTrace;
    }

    protected <T> T parseRequestObject(HttpServletRequest request, HttpServletResponse response, Class<T> valueType) throws JsonException {
        try {
            String data = getPostData(request, response);
            if (data != null){
                return getMapper().readValue(data, valueType);
            } else {
                return null;
            }
        } catch (JsonGenerationException e) {
            throw new JsonException(e);
        } catch (JsonMappingException e) {
            throw new JsonException(e);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    protected boolean validateCart() {
        return false;
    }

    protected boolean validateUser() {
        return true;
    }

    protected Message getErrorMessage(ActionResult result, HttpServletRequest request) {
        Message responseMessage = new Message();
        responseMessage.setStatus(Message.STATUS_FAILED);
        try {
            responseMessage.addErrorMessages(result.getErrors(), getUserFromSession(request, null));
        } catch (NoSessionException e) {
            responseMessage.addErrorMessages(result.getErrors(), null);
        }
        return responseMessage;
    }

    protected Message getErrorMessage(String code, String message) {
        Message responseMessage = new Message();
        responseMessage.setStatus(Message.STATUS_FAILED);
        responseMessage.addErrorMessage(code, message);
        return responseMessage;
    }

    protected Message getWarningMessage(String code, String message) {
        Message responseMessage = new Message();
        responseMessage.setStatus(Message.STATUS_FAILED);
        responseMessage.addWarningMessage(code, message);
        return responseMessage;
    }
    
    protected void setResponseMessage(ModelAndView model, Message responseMessage, SessionUser user) throws JsonException {
        try {
//            try {
//            	if (user != null && user.isLoggedIn() && !isFakeUser()) {
//            		responseMessage.addNoticeMessages(oasService.getMessages(user));
//            	} else {
//                    responseMessage.addNoticeMessages(oasService.getMessages());
//            	}
//            } catch (ServiceException e) {
//                LOGGER.warn("ServiceException whi/le trying to get oas messages. not stopping execution.", e);
//            }
            model.addObject("data", getJsonString(responseMessage));

        } catch (JsonGenerationException e) {
            throw new JsonException(e);
        } catch (JsonMappingException e) {
            throw new JsonException(e);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * The main method of concrete controllers
     * 
     * @param request
     * @param response
     * @param model
     * @param action
     * @param user
     * @return
     */
    protected abstract ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model,
            String action, SessionUser user) throws JsonException, FDException, ServiceException, NoSessionException;

    protected void createUserSession(User user, String source, HttpServletRequest request, HttpServletResponse response) throws FDResourceException {
        //Order is important here as "UserUtil.createSessionUser" will refer to the code set in the previous step
        request.getSession().setAttribute(SessionName.APPLICATION, getTransactionSourceCode(request, source));
        UserUtil.createSessionUser(request, response, user.getFDUser());
    }

    protected String getTransactionSourceCode(HttpServletRequest request, String source) {
        return getTransactionSourceEnum(request, source).getCode();
    }

    protected EnumTransactionSource getTransactionSourceEnum(HttpServletRequest request, String source) {
        EnumTransactionSource srcEnum = null;
        if (source != null && source.trim().length() > 0 && EnumTransactionSource.getTransactionSource(source) != null) {
            srcEnum = EnumTransactionSource.getTransactionSource(source);
        } else {
            if (isExtraResponseRequested(request)) {
                srcEnum = EnumTransactionSource.FOODKICK_WEBSITE;
            } else {
                srcEnum = EnumTransactionSource.IPHONE_WEBSITE;
            }
        }
        return srcEnum;
    }
    
    protected void setUserInSession(SessionUser sessionUser, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute(SessionName.USER, sessionUser.getFDSessionUser());
    }
    
    protected void removeUserInSession(SessionUser user, HttpServletRequest request, HttpServletResponse response) {
    	
    	if(user != null) {
	    	user.touch();	       
    	}
    	HttpSession session = request.getSession();
    	
    	// clear session
    	Enumeration e = session.getAttributeNames();
    	while (e.hasMoreElements()) {
    		String name = (String) e.nextElement();
    		session.removeAttribute(name);
    	}
    	// end session
    	session.invalidate();
    	// remove cookie
    	CookieMonster.clearCookie(response);
    	resetMobileSessionData(request);
    }

    protected ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        //mapper.configure(SerializationFeature.INDENT_OUTPUT, true); //May want to flip to false in production
        mapper.setSerializationInclusion( JsonInclude.Include.NON_NULL );
        return mapper;
    }

    protected ModelAndView getModelAndView(String name) {
        ModelAndView model = new ModelAndView(name);
        model.addObject(JSON, getMapper());
        return model;
    }

    protected String getJsonString(Object obj) throws JsonGenerationException, JsonMappingException, IOException {
        StringWriter writer = new StringWriter();
        getMapper().writeValue(writer, obj);
        return writer.toString();
    }
    
    protected String getSessionUserId(SessionUser user) {
    	if(user != null && user.getFDSessionUser() != null && user.getFDSessionUser().getIdentity() != null) {
    		return user.getFDSessionUser().getIdentity().getErpCustomerPK();
    	}
    	return null;
    }

    protected SessionUser fakeUser(HttpSession session) {
    	fakedUserForRequest.set(Boolean.TRUE);
    	FDUser user = new FDUser();
//    	user.setDefaultPricingContext();
    	user.setRobot(true);// I hate to do this but door3 didnt do anonymous browsing properly, revisit during fdx please
		FDSessionUser sessionUser = new FDSessionUser(user, session);
    	session.setAttribute(SessionName.USER, sessionUser);
		return SessionUser.wrap(sessionUser);
    }
    
    protected SessionUser fakeUser(HttpSession session, BrowseQuery query) {
    	fakedUserForRequest.set(Boolean.TRUE);
    	FDUser user = null;
    	if(query != null && query.getCatalogKey() != null){
    		CatalogInfo catalogInfo = BrowseUtil.getCatalogInfo(query.getCatalogKey());
    		FulfillmentContext fulfillmentContext=new FulfillmentContext();
    		fulfillmentContext.setPlantId(catalogInfo.getKey().getPlantId());
    		PricingContext pricingContext=new PricingContext(catalogInfo.getKey().getPricingZone());
    		UserContext userContext=new UserContext();
    		userContext.setFulfillmentContext(fulfillmentContext);
    		userContext.setPricingContext(pricingContext);
    		userContext.setStoreContext(StoreContext.createStoreContext(EnumEStoreId.valueOfContentId((ContentFactory.getInstance().getStoreKey().getId()))));
    		user=new FDUser(userContext);
    		catalogInfo.setShowKey(false);
    	} else {
    		user=new FDUser();
    	}
//    	user.setDefaultPricingContext();
    	user.setRobot(true);// I hate to do this but door3 didnt do anonymous browsing properly, revisit during fdx please
		FDSessionUser sessionUser = new FDSessionUser(user, session);
    	session.setAttribute(SessionName.USER, sessionUser);
		return SessionUser.wrap(sessionUser);
    }
    
    protected boolean isFakeUser() {
    	return fakedUserForRequest.get();
    }
	protected com.freshdirect.mobileapi.controller.data.response.Configuration getConfiguration(
			SessionUser user) {

		com.freshdirect.mobileapi.controller.data.response.Configuration configuration = new com.freshdirect.mobileapi.controller.data.response.Configuration();
		configuration.setAkamaiImageConvertorEnabled(FeatureRolloutArbiter
				.isFeatureRolledOut(EnumRolloutFeature.akamaiimageconvertor,
										user!=null && user.getFDSessionUser()!=null ? user.getFDSessionUser().getUser() : null));
		configuration.setApiCodeVersion(Buildver.getInstance().getBuildver());
		configuration.setStoreVersion(PublishId.getInstance().getPublishId());

		configuration.setVerifyPaymentMethod(FDStoreProperties
				.isPaymentMethodVerificationEnabled());
		configuration.setEcouponEnabled(FDCouponProperties.isCouponsEnabled());
		configuration.setAdServerUrl(FDStoreProperties.getAdServerUrl());

		configuration.setTipRange(FDStoreProperties.getTipRangeConfig());
		
		configuration.setMiddleTierUrl(FDStoreProperties.getMiddleTierProviderURL());
		configuration.setSocialLoginEnabled(FDStoreProperties.isSocialLoginEnabled());
		configuration.setMasterPassEnabled(MobileApiProperties.isMasterpassEnabled());
		configuration.setPayPalEnabled(MobileApiProperties.isPayPalEnabled());
		configuration.setDCSEnabled(FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, 
																				user!=null && user.getFDSessionUser()!=null ? user.getFDSessionUser().getUser() : null));

		return configuration;
	}
	
	protected LoggedIn formatLoginMessage(SessionUser user) throws FDException  {
	    LoggedIn responseMessage = new LoggedIn();

        OrderHistory history = user.getOrderHistory();
        String cutoffMessage = user.getCutoffInfo();

        OrderInfo closestPendingOrder = history
                .getClosestPendingOrderInfo(new Date());
        List<OrderInfo> orderHistoryInfo = new ArrayList<OrderInfo>();
        if (closestPendingOrder != null) {
            orderHistoryInfo.add(closestPendingOrder);
        }

        responseMessage.setChefTable(user.isChefsTable());
        responseMessage.setCustomerServicePhoneNumber(user
                .getCustomerServiceContact());
        if (user.getReservationTimeslot() != null) {
            responseMessage.setReservationTimeslot(new Timeslot(
                    user.getReservationTimeslot()));
        }
        responseMessage.setFirstName(user.getFirstName());
        responseMessage.setLastName(user.getLastName());
        responseMessage.setUsername(user.getUsername());
        responseMessage
                .setOrders(com.freshdirect.mobileapi.controller.data.response.OrderHistory.Order
                        .createOrderList(orderHistoryInfo, user));
        responseMessage
                .setSuccessMessage("User has been logged in successfully.");
        responseMessage.setItemsInCartCount(user
                .getItemsInCartCount());
        responseMessage.setOrderCount(history.getValidOrderCount());
        responseMessage.setFdxOrderCount(history.getValidOrderCount(EnumEStoreId.FDX));
        responseMessage.setFdUserId(user.getPrimaryKey());

        // DOOR3 FD-iPad FDIP-474
        //Note: The field is inapproformatLoginMessagepriately named. It is not referring to whether or not the user is on the FD mailing
        //list, but rather intended to represent whether or not the user is to be notified
        //in the event of service being introduced to their area.
        responseMessage.setOnMailingList( user.isFutureZoneNotificationEmailSentForCurrentAddress() );

        if (cutoffMessage != null) {
            responseMessage.addNoticeMessage(
                    MessageCodes.NOTICE_DELIVERY_CUTOFF, cutoffMessage);
        }

        if (!user.getFDSessionUser().isCouponsSystemAvailable() && FDCouponProperties.isDisplayMessageCouponsNotAvailable()) {
            responseMessage.addWarningMessage(
                    MessageCodes.WARNING_COUPONSYSTEM_UNAVAILABLE,
                    SystemMessageList.MSG_COUPONS_SYSTEM_NOT_AVAILABLE);
        }

        // With Mobile App having given ability to add/remove payment method
        // this is removed
        /*
         * if ((user.getPaymentMethods() == null) ||
         * (user.getPaymentMethods().size() == 0)) {
         * responseMessage.addWarningMessage(ERR_NO_PAYMENT_METHOD,
         * ERR_NO_PAYMENT_METHOD_MSG); }
         */
        responseMessage.setBrowseEnabled(MobileApiProperties
                .isBrowseEnabled());

        // Added during Mobile Coremetrics Implementation
        responseMessage.setSelectedServiceType(user
                .getSelectedServiceType() != null ? user
                .getSelectedServiceType().toString() : "");
        responseMessage.setCohort(user.getCohort());
        responseMessage.setTotalOrderCount(user.getTotalOrderCount());
        
        responseMessage.setTcAcknowledge(user.getTcAcknowledge());

        responseMessage.setMobileNumber(getMobileNumber(user));
        responseMessage.setPlantId(BrowseUtil.getPlantId(user));
        responseMessage.setProviders(ExternalAccountManager.getConnectedProvidersByUserId(user.getUsername(), EnumExternalLoginSource.SOCIAL));
        return responseMessage;
    }

    private String getMobileNumber(SessionUser user) throws FDResourceException {
        String mobileNumber = null;
        FDSessionUser fduser = user.getFDSessionUser();
        FDCustomerModel fdCustomerModel = FDCustomerFactory.getFDCustomer(fduser.getIdentity());
        FDCustomerEStoreModel customerSmsPreferenceModel = fdCustomerModel.getCustomerSmsPreferenceModel();

        if (EnumEStoreId.FDX.getContentId().equals(fduser.getUserContext().getStoreContext().getEStoreId().getContentId())) {
            mobileNumber = customerSmsPreferenceModel.getFdxMobileNumber() != null ? customerSmsPreferenceModel.getFdxMobileNumber().getPhone() : "";
        } else {
            mobileNumber = customerSmsPreferenceModel.getMobileNumber() != null ? customerSmsPreferenceModel.getMobileNumber().getPhone() : "";
        }
        return mobileNumber;
    }
	
    protected LoggedIn createLoginResponseMessage(SessionUser user) throws FDException {
        LoggedIn responseMessage = null;
        if (user!=null && user.isLoggedIn()) {
            responseMessage = formatLoginMessage(user);
        } else {
            responseMessage = new LoggedIn();
            responseMessage.setFailureMessage("User is not logged in.");
            responseMessage.setPlantId(BrowseUtil.getPlantId(user));
        }
        return responseMessage;
    }

    protected SessionUser getUser(HttpServletRequest request, HttpServletResponse response) throws NoSessionException {
        FDSessionUser fdSessionUser = (FDSessionUser) request.getSession().getAttribute(SessionName.USER);
        if (fdSessionUser == null) {
            try {
                fdSessionUser = UserUtil.getSessionUserByCookie(request);
            } catch (FDResourceException ex) {
                LOGGER.warn(ex);
            }
            if (fdSessionUser != null) {
                FDCustomerCouponUtil.initCustomerCoupons(request.getSession());
            } else {
                fdSessionUser = LocatorUtil.useIpLocator(request.getSession(), request, response);
            }
            EnumTransactionSource src = getTransactionSourceEnum(request, null);
            fdSessionUser.getUser().setApplication(src);
            fdSessionUser.isLoggedIn(true);
            request.getSession().setAttribute(SessionName.APPLICATION, src.getCode());
            request.getSession().setAttribute(SessionName.USER, fdSessionUser);
        }
        if (validateUser() && fdSessionUser.getIdentity() == null) {
            throw new NoSessionException("No session");
        }
        ContentFactory.getInstance().setCurrentUserContext(fdSessionUser.getUserContext());
        return SessionUser.wrap(fdSessionUser);
    }

    // Gives back Home and Today's Pick feeds
    protected void populateHomePages(SessionUser user, CMSPageRequest pageRequest, PageMessageResponse pageResponse, HttpServletRequest request) throws FDException {
        populateResponseWithEnabledAdditionsForWebClient(user, pageResponse, request, null);

        if (isResponseAdditionalEnable(request, EnumResponseAdditional.INCLUDE_FEEDS)) {
            for (CMSWebPageModel page : getPages(user, pageRequest)) {
                if (TODAYS_PICK_PAGE_TYPE.equalsIgnoreCase(page.getType())) {
                    pageResponse.setPick(page);
                } else if (FEED_PAGE_TYPE.equalsIgnoreCase(page.getType())) {
                    pageResponse.setPage(page);
                }
            }
        }
        
        // populate welcome carousel image banners
        if (isExtraResponseRequested(request)) {
            final StoreModel store = ContentFactory.getInstance().getStore();
            List<CMSImageBannerModel> cmsImageBanners = new ArrayList<CMSImageBannerModel>();
            List<ImageBanner> imageBanners = store.getWelcomeCarouselImageBanners();
            if (imageBanners != null) {
                for (ImageBanner imageBanner : imageBanners) {
                    CMSImageBannerModel cmsImageBanner = convertImageBanner(imageBanner);
                    if (cmsImageBanner != null){
                        cmsImageBanners.add(cmsImageBanner);
                    }
                }
            }
            pageResponse.setWelcomeCarouselBanners(cmsImageBanners);
        }
    }

    protected void populatePageComponents(SessionUser user, CMSPageRequest pageRequest, PageMessageResponse pageResponse, HttpServletRequest request) throws FDException {
    	String originalPageType = pageRequest.getPageType();
    	
    	if(TODAYS_PICK_HEAD_PAGE_TYPE.equals(originalPageType)) {
    		pageRequest.setPageType(TODAYS_PICK_PAGE_TYPE);
    		pageRequest.setHeadSectionIndexes();
    	} else if(TODAYS_PICK_BODY_PAGE_TYPE.equals(originalPageType)) {
    		pageRequest.setPageType(TODAYS_PICK_PAGE_TYPE);
    		pageRequest.setBodySectionIndexes();
    	} else if(FEED_HEAD_PAGE_TYPE.equals(originalPageType)) {
    		pageRequest.setPageType(FEED_PAGE_TYPE);
    		pageRequest.setHeadSectionIndexes();
    	} else if(FEED_BODY_PAGE_TYPE.equals(originalPageType)) {
    		pageRequest.setPageType(FEED_PAGE_TYPE);
    		pageRequest.setBodySectionIndexes();
    	}
    	
    	
        if (CAROUSEL_BANNER_PAGE_TYPE.equals(pageRequest.getPageType())) {
            final StoreModel store = ContentFactory.getInstance().getStore();
            List<CMSImageBannerModel> cmsImageBanners = new ArrayList<CMSImageBannerModel>();
            List<ImageBanner> imageBanners = store.getWelcomeCarouselImageBanners();
            if (imageBanners != null) {
                for (ImageBanner imageBanner : imageBanners) {
                    CMSImageBannerModel cmsImageBanner = convertImageBanner(imageBanner);
                    if (cmsImageBanner != null){
                        cmsImageBanners.add(cmsImageBanner);
                    }
                }
            }
            pageResponse.setWelcomeCarouselBanners(cmsImageBanners);
        } else if (TODAYS_PICK_PAGE_TYPE.equals(pageRequest.getPageType())){
        	List<CMSWebPageModel> pgs = getPages(user, pageRequest);
            for (CMSWebPageModel page : pgs) {
                if (pageRequest.getPageType().equals(page.getType())) {
                    pageResponse.setPick(page);                    
                }
            }        	
        } else if (FEED_PAGE_TYPE.equals(pageRequest.getPageType())){
        	List<CMSWebPageModel> pgs = getPages(user, pageRequest);
            for (CMSWebPageModel page : pgs) {
                if (pageRequest.getPageType().equals(page.getType())) {
                    pageResponse.setPage(page);
                }
            }        	
        }
        
        pageRequest.setPageType(originalPageType);
    }
    
    protected CMSImageBannerModel convertImageBanner(ImageBanner imageBanner) {
        CMSImageBannerModel cmsImageBanner = null;
        if (imageBanner != null){
            cmsImageBanner = new CMSImageBannerModel();
            cmsImageBanner.setComponentType(CMSComponentType.BANNER);
            cmsImageBanner.setName(imageBanner.getName());
            cmsImageBanner.setDescription(imageBanner.getDescription());
            cmsImageBanner.setType(imageBanner.getType());
            cmsImageBanner.setImage(convertImage(imageBanner.getImageBannerImage()));
            cmsImageBanner.setTarget(convertTarget(imageBanner.getTarget()));
            cmsImageBanner.setLinkOneTarget(convertTarget(imageBanner.getLinkOneTarget()));
            cmsImageBanner.setLinkOneText(imageBanner.getLinkOneText());
            cmsImageBanner.setLinkOneType(imageBanner.getLinkOneType());
            cmsImageBanner.setLinkOneURL(imageBanner.getLinkOneURL());
            cmsImageBanner.setLinkTwoTarget(convertTarget(imageBanner.getLinkTwoTarget()));
            cmsImageBanner.setLinkTwoText(imageBanner.getLinkTwoText());
            cmsImageBanner.setLinkTwoType(imageBanner.getLinkTwoType());
            cmsImageBanner.setLinkTwoURL(imageBanner.getLinkTwoURL());
        }
        return cmsImageBanner;
    }
    
    private String convertTarget(ContentNodeModel node) {
        String encodedKey = null;
        if (node != null) {
            ContentKey key = node.getContentKey();
            if (key != null) {
                encodedKey = key.getEncoded();
            }
        }
        return encodedKey;
    }

    private CMSImageModel convertImage(Image image) {
        CMSImageModel cmsImage = null;
        if (image != null) {
            cmsImage = new CMSImageModel();
            cmsImage.setPath(image.getPath());
            cmsImage.setHeight(image.getHeight());
            cmsImage.setWidth(image.getWidth());
        }
        return cmsImage;
    }

    protected void populateResponseWithEnabledAdditionsForWebClient(SessionUser user, Message messageResponse, HttpServletRequest request, LoggedIn loginMessage) throws FDException {
        if (isResponseAdditionalEnable(request, EnumResponseAdditional.INCLUDE_USERINFO) && messageResponse instanceof HasLoggedInField) {
            if (loginMessage == null) {
                loginMessage = createLoginResponseMessage(user);
            }

            if (!Message.STATUS_FAILED.equals(messageResponse.getStatus())) {
                messageResponse.setStatus(loginMessage.getStatus());
            }
            ((HasLoggedInField)messageResponse).setLogin(loginMessage);
            messageResponse.setConfiguration(getConfiguration(user));
        }

        if (isResponseAdditionalEnable(request, EnumResponseAdditional.INCLUDE_CART) && messageResponse instanceof HasCartDetailField) {
            final CartDetail cartDetail = user.getShoppingCart().getCartDetail(user, EnumCouponContext.VIEWCART);
            ProductPotatoUtil.populateCartDetailWithPotatoes(user.getFDSessionUser(), cartDetail);
            ((HasCartDetailField)messageResponse).setCartDetail(cartDetail);
        }
    }

    protected List<CMSWebPageModel> getPages(SessionUser user, CMSPageRequest pageRequest) {
        List<CMSWebPageModel> pages = null;
        if (pageRequest.isPreview()) {
            pages = CMSContentFactory.getInstance().getCMSPageByParameters(pageRequest);
        } else {
            pages = getCachedPages(pageRequest);
            if (isCachedPagesInvalid(pageRequest, pages)) {
                CMSContentFactory.getInstance().cacheAllPages(pageRequest);
                pages = getCachedPages(pageRequest);
            }
        }
        for (CMSWebPageModel page : pages) {
//            if(pageRequest.getStartSectionIndex() != null && pageRequest.getHowManySections() != null) {
//            	pageRequest.limitSections(page);
//            }
            addProductsToSection(user, page);
        }
        return pages;
    }

    private boolean isCachedPagesInvalid(CMSPageRequest pageRequest, List<CMSWebPageModel> cachedPages) {
        boolean invalid = cachedPages == null || cachedPages.isEmpty();

        if (!invalid) {
            List<CMSWebPageModel> pages = CMSContentFactory.getInstance().getCMSPages(pageRequest);
            Set<CMSWebPageModel> pageSet = new HashSet<CMSWebPageModel>(pages);
            Set<CMSWebPageModel> cachedPageSet = new HashSet<CMSWebPageModel>(cachedPages);
            invalid = !pageSet.equals(cachedPageSet);
        }

        return invalid;
    }

    private List<CMSWebPageModel> getCachedPages(CMSPageRequest pageRequest) {
        List<CMSWebPageModel> pages = new ArrayList<CMSWebPageModel>();
        List<String> webPageTypes = (pageRequest.getPageType() != null) 
        		? Arrays.asList(pageRequest.getCacheKey()) 
        		: Arrays.asList(pageRequest.asCacheKey(FEED_PAGE_TYPE), pageRequest.asCacheKey(TODAYS_PICK_PAGE_TYPE));
        for (String type : webPageTypes) {
            CMSWebPageModel cachedFeedPage = CMSContentFactory.getInstance().getCMSPageByName(type);
            if (cachedFeedPage != null) {
                pages.add(cachedFeedPage);
            }
        }
        return pages;
    }    
    
    private void addProductsToSection(SessionUser user, CMSWebPageModel page) {
        if (page != null) {
            List<CMSSectionModel> sectionWithProducts = new ArrayList<CMSSectionModel>();
            List<String> errorProductKeys = new ArrayList<String>();
            for (final CMSSectionModel section : page.getSections()) {
                final CMSPotatoSectionModel potatoSection = CMSPotatoSectionModel.withSection(section);
                potatoSection.setProducts(collectProductPotatos(user, section.getProductList(), errorProductKeys));
                if (areMustHaveSectionProductsAvailable(section.getMustHaveProdList(), section.getProductList(), errorProductKeys, user)) {
                    sectionWithProducts.add(potatoSection);
                }
                errorProductKeys.clear();
            }
            page.setSections(sectionWithProducts);
        }
    }

    private boolean areMustHaveSectionProductsAvailable(List<String> mustHaveProductkeys, List<String> productKeys, List<String> errorProductKeys, SessionUser user) {
        boolean allMustHaveProductsAreAvailable = true;
        if (mustHaveProductkeys != null && productKeys != null) {
            for (String mustHaveProductId : mustHaveProductkeys) {
                if (!productKeys.contains(mustHaveProductId) || errorProductKeys.contains(mustHaveProductId)) {
                    allMustHaveProductsAreAvailable = false;
                    break;
                }
                
                final String prodId = mustHaveProductId.substring(FDContentTypes.PRODUCT.getName().length() + 1);
                final ProductPotatoData data = ProductPotatoUtil.getProductPotato(prodId, null, user.getFDSessionUser(), false);
                if (data == null || !data.getProductData().isAvailable()) {
                	allMustHaveProductsAreAvailable = false;
                	break;
                }
            }
        }
        return allMustHaveProductsAreAvailable;
    }

    private List<ProductPotatoData> collectProductPotatos(SessionUser user, List<String> productKeys, List<String> errorProductKeys) {
        final List<ProductPotatoData> potatoes = new ArrayList<ProductPotatoData>();
        if (productKeys != null) {
            for (final String productKey : productKeys) {
                // extract CMS id
                final String prodId = productKey.substring(FDContentTypes.PRODUCT.getName().length() + 1);
                final ProductPotatoData data = ProductPotatoUtil.getProductPotato(prodId, null, user.getFDSessionUser(), false);
                if (data != null) {
                    potatoes.add(data);
                } else {
                    errorProductKeys.add(productKey);
                }
            }
        }
        return potatoes;
    }
    
    protected void setContextHeaders(HttpServletRequest request,  HttpContextWrapper wrapper) {
    	wrapper.setReferer(request.getHeader(HttpHeaders.REFERER));
    	wrapper.setTrueCleintIp(request.getHeader("True-Client-IP"));
		wrapper.setForwardedFrom(request.getHeader("X-Forwarded-For"));
		wrapper.setAkamaiEdgescape(request.getHeader("X-Akamai-Edgescape"));
		wrapper.setRemoteAddr(request.getRemoteAddr());
		
    }
}
