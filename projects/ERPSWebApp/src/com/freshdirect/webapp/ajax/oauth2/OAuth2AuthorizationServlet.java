package com.freshdirect.webapp.ajax.oauth2;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.oauth2.service.OAuth2Service;

public class OAuth2AuthorizationServlet extends BaseJsonServlet {

	private static final long serialVersionUID = 1940324909550356375L;
	private static final Logger LOGGER = LoggerFactory.getInstance(OAuth2AuthorizationServlet.class);
	private OAuth2Service authService = OAuth2Service.defaultService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user)
			throws HttpErrorResponse {
		doPost(request, response, user);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user)
			throws HttpErrorResponse {
		try {
			final OAuthResponse oauthResp = authService.getAuthCode(request, user);

			String locationUri = oauthResp.getLocationUri();
			if (locationUri == null) {
				writeResponseData(response, oauthResp);
				return;
			}

			try {
				response.sendRedirect(locationUri);
				LOGGER.debug("Redirect to --> " + locationUri);
			} catch (IOException e) {
				LOGGER.error("Failed to redirect: ", e);
				e.printStackTrace();
				writeResponseData(response, "Failed to redirect to " + locationUri);
			}

		} catch (URISyntaxException e) {
			e.printStackTrace();
			writeResponseData(response, e.getInput() + " | " + e.getMessage());
		} catch (OAuthSystemException e) {
			e.printStackTrace();
			writeResponseData(response, e.getMessage());
		}
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}
}
