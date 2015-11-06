/**
 * 
 */
package com.freshdirect.webapp.ajax.login;

import java.io.IOException;
import java.io.Writer;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;

/**
 * @author ksriram
 *
 */
public class LoginServlet extends HttpServlet {

	private static final String DATA = "data";
	private static final String mergePage = "/login/merge_cart.jsp";
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
															, loginRequest.getUserId(), loginRequest.getPassword(), mergePage, loginRequest.getSuccessPage(), false);
			FDSessionUser user = (FDSessionUser) request.getSession().getAttribute(SessionName.USER);
			if(!user.getTcAcknowledge()){
				loginResponse.setMessage("TcAgreeFail");
				//updatedSuccessPage = "/registration/tcaccept_lite.jsp";
			}
			loginResponse.setSuccessPage(updatedSuccessPage);
			if(actionResult.getErrors() == null || actionResult.getErrors().isEmpty()) {
				loginResponse.setSuccess(true);
				request.getSession().setAttribute("fdLoginAttempt", Integer.valueOf(0));
			} else {
				Integer fdLoginAttempt = request.getSession().getAttribute("fdLoginAttempt") != null ? (Integer) request.getSession().getAttribute("fdLoginAttempt") : Integer.valueOf(0);
				fdLoginAttempt++;
				request.getSession().setAttribute("fdLoginAttempt", fdLoginAttempt);
				if(fdLoginAttempt >= FDStoreProperties.getMaxInvalidLoginAttempt()){
					//Should be the redirecting key to login page.
					loginResponse.setMessage("CaptchaRedirect");
				}
			}
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
