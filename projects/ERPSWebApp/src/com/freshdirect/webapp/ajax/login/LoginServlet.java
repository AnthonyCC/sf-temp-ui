/**
 * 
 */
package com.freshdirect.webapp.ajax.login;

import java.io.IOException;
import java.io.Writer;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.SavedRecipientModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.giftcard.EnumGiftCardType;
import com.freshdirect.giftcard.RecipientModel;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.webapp.taglib.coremetrics.CmRegistrationTag;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;

/**
 * @author ksriram
 *
 */
public class LoginServlet extends HttpServlet {

	private static final String ERROR_CODE_400 = "400";
	private static final String BAD_REQUEST = "Bad request";
	private static final String DATA = "data";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 133867689987220127L;
	private static final Logger LOGGER = LoggerFactory.getInstance( LoginServlet.class );
	
	
	@Override
	/**
	 * This is an API to do the log-in based on the existing login logic(@LoginControllerTag.java).
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		LoginResponse loginResponse = new LoginResponse();
		LoginRequest loginRequest = parseRequestData(request, response, LoginRequest.class);
		
		if(loginRequest != null) {
			ActionResult actionResult = new ActionResult();
			String updatedSuccessPage = UserUtil.loginUser(request.getSession(), request, response, actionResult
															, loginRequest.getUserId(), loginRequest.getPassword(), "", "");
			loginResponse.setSuccessPage(updatedSuccessPage);
			if(actionResult.getErrors() == null || actionResult.getErrors().isEmpty()) {
				loginResponse.setSuccess(true);
			} else {
				loginResponse.addErrors(actionResult.getErrors());
			}		    
        } else{
			loginResponse.addError(new ActionError(ERROR_CODE_400, BAD_REQUEST)); //400 - Bad request.
		}
        writeResponse(response, loginResponse);
	}
	
	private void writeResponse(HttpServletResponse response,LoginResponse loginResponse) {
		try {
			Writer w =response.getWriter();
			getMapper().writeValue(w, loginResponse);
		} catch (JsonGenerationException e) {
			LOGGER.warn("JsonGenerationException ", e);
		} catch (JsonMappingException e) {
			LOGGER.warn("JsonMappingException ", e);
		} catch (IOException e) {
			LOGGER.warn("IOException ", e);
		}
	}
	
	protected final static <T> T parseRequestData( HttpServletRequest request, HttpServletResponse response, Class<T> typeClass) throws IOException {
		
		String reqJson = request.getParameter( DATA );
		if(reqJson == null){
			reqJson = (String)request.getAttribute( DATA );
			if(reqJson != null){
				reqJson = URLDecoder.decode(reqJson, "UTF-8");				
			}
		}
				
		LOGGER.debug( "Parsing request data: " + reqJson );		
		T reqData = null;
		try {
			reqData = getMapper().readValue(reqJson, typeClass);
		} catch (Exception e) {
			LOGGER.warn("Exception while parsing the request: "+e);
		}
		
		return reqData;
	}
	
	protected static ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES , false);
        return mapper;
    }
}
