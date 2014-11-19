package com.freshdirect.mobileapi.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoCartException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.Cart;
import com.freshdirect.mobileapi.model.MessageCodes;
import com.freshdirect.mobileapi.model.RequestData;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.User;
import com.freshdirect.mobileapi.model.tagwrapper.HttpContextWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.HttpSessionWrapper;
import com.freshdirect.mobileapi.service.Oas247Service;
import com.freshdirect.mobileapi.service.OasService;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.webapp.taglib.fdstore.CookieMonster;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public abstract class BaseController extends AbstractController implements MessageCodes {

    public final class AnonymousUser extends FDUser {
	    private static final long serialVersionUID = 6972700005485690087L;

	    @Override
	    public PricingContext getPricingContext() {
	        return PricingContext.DEFAULT;
	    }
    }

    private static final String JSON = "JSON";

    private static final Category LOGGER = LoggerFactory.getInstance(BaseController.class);

    public static final String JSON_RENDERED = "json-rendered";

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

    //    public Message createdErrorMessage(Collection<ActionError> errors) {
    //        Message responseMessage = new Message();
    //        responseMessage.setStatus(Message.STATUS_FAILED);
    //        responseMessage.addErrorMessages(errors);
    //        return responseMessage;
    //    }

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
                    user = getUserFromSession(request, response);
                    user.setUserPricingContext();
                    user.setEligibleForDDPP();
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
                Message responseMessage = getErrorMessage(ERR_INCOMPATIBLE_CLIENT, MobileApiProperties.getDiscoveryServiceUrl());
                setResponseMessage(model, responseMessage, user);
            } catch (Throwable e) {
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
            return getMapper().readValue(getPostData(request, response), valueType);
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
            try {
            	if (user != null && user.isLoggedIn() && !isFakeUser()) {
            		responseMessage.addNoticeMessages(oasService.getMessages(user));
            	} else {
                    responseMessage.addNoticeMessages(oasService.getMessages());
            	}
            } catch (ServiceException e) {
                LOGGER.warn("ServiceException while trying to get oas messages. not stopping execution.", e);
            }
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
    	if(source != null && source.trim().length() > 0 && EnumTransactionSource.getTransactionSource(source) != null) {
    		request.getSession().setAttribute(SessionName.APPLICATION, EnumTransactionSource.getTransactionSource(source).getCode());
    	} else {
    		request.getSession().setAttribute(SessionName.APPLICATION, EnumTransactionSource.IPHONE_WEBSITE.getCode());
    	}
        UserUtil.createSessionUser(request, response, user.getFDUser());
    }

    protected void setUserInSession(SessionUser sessionUser, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute(SessionName.USER, sessionUser.getFDSessionUser());
    }
    
    protected void removeUserInSession(SessionUser user, HttpServletRequest request, HttpServletResponse response) {
    	
    	user.touch();
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
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true); //May want to flip to false in production
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
    	FDUser user = new AnonymousUser();
		FDSessionUser sessionUser = new FDSessionUser(user, session);
    	session.setAttribute(SessionName.USER, sessionUser);
		return SessionUser.wrap(sessionUser);
    }
    
    protected boolean isFakeUser() {
    	return fakedUserForRequest.get();
    }
}
