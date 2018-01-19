package com.freshdirect.webapp.ajax.oauth2;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.JsonHelper;
import com.freshdirect.webapp.ajax.oauth2.service.OAuth2Service;

public class OAuth2TokenServlet extends BaseJsonServlet {

	private static final long serialVersionUID = -3789357656582601355L;
	private static final Logger LOGGER = LoggerFactory.getInstance(OAuth2TokenServlet.class);
	private OAuth2Service authService = OAuth2Service.defaultService();

	@Override
	// For OAuth2 token endpoint, GET is disabled
	protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user)
			throws HttpErrorResponse {
		returnHttpError(HttpServletResponse.SC_FORBIDDEN);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user)
			throws HttpErrorResponse {
		OAuthResponse oauthResp;
		try {
			oauthResp = authService.getAccessToken(request);
			response.setStatus(oauthResp.getResponseStatus());
	        ServletOutputStream out = response.getOutputStream();
	        out.print(oauthResp.getBody());
	        out.flush();
			
		} catch (OAuthSystemException e) {
			returnHttpError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error", e); // 500 Internal Server Error
		} catch (IOException e) {
            returnHttpError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error writing response", e); // 500 Internal Server Error
        }
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}

	@Override
	protected int getRequiredUserLevel() {
		return FDUserI.GUEST;
	}
}
