package com.freshdirect.webapp.ajax.registration;

import java.io.IOException;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.Set;

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
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.delivery.DlvServiceSelectionResult;
import com.freshdirect.delivery.EnumDeliveryStatus;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.action.HttpContext;
import com.freshdirect.webapp.action.fdstore.RegistrationAction;
import com.freshdirect.webapp.taglib.fdstore.CookieMonster;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;
import com.freshdirect.webapp.util.AccountUtil;

public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = -2013348012745309318L;
	
	private static final String ERROR_CODE_400 = "400";
	private static final String BAD_REQUEST = "Bad request";
	private static final String DATA = "data";
	private static final String mergePage = "/login/merge_cart.jsp";
	
	private static final Logger LOGGER = LoggerFactory.getInstance( RegistrationServlet.class );

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		//LoginResponse loginResponse = new LoginResponse();
		//LoginRequest loginRequest = parseRequestData(request, response, LoginRequest.class);
		RegistrationResponse regResponse = new RegistrationResponse();
		RegistrationRequest regRequest = parseRequestData(request, response, RegistrationRequest.class);
		
		if(regRequest != null) {
			ActionResult actionResult = new ActionResult();
			
			UserUtil.registerUser(request.getSession(), request, response, actionResult, regRequest, regResponse, mergePage);
        } else{
        	regResponse.addError(new ActionError(ERROR_CODE_400, BAD_REQUEST)); //400 - Bad request.
		}
		writeResponse(response, regResponse);
		
	}
	

	

	private void writeResponse(HttpServletResponse response, RegistrationResponse regResponse) {
		try {
			Writer w =response.getWriter();
			getMapper().writeValue(w, regResponse);
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
				
		//don't leave this printing, prints password
		//LOGGER.debug( "Parsing request data: " + reqJson );		
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
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
	
}
