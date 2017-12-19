package com.freshdirect.webapp.ajax.oauth2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.oauth2.data.OAuth2CodeAndTokenData;
import com.freshdirect.webapp.ajax.oauth2.data.Status;
import com.freshdirect.webapp.ajax.oauth2.data.TokenInfoResponse;
import com.freshdirect.webapp.ajax.oauth2.service.OAuth2Service;

public class OAuth2TokenInfoServlet extends BaseJsonServlet {
	private static final long serialVersionUID = -2749796871486100092L;
	private static final Logger LOGGER = LoggerFactory.getInstance(OAuth2TokenInfoServlet.class);
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
			String tokenId = request.getParameter("access_token");
			if (tokenId != null) {
				LOGGER.debug("tokenId = [" + tokenId + "]");
				OAuth2CodeAndTokenData codeOrTokenData = authService.getCodeOrTokenData(tokenId);
				codeOrTokenData.setSalt(null);
				writeResponseData(response, new TokenInfoResponse(Status.Code.SUCCESS, codeOrTokenData));
			}
			else {
				writeResponseData(response, new TokenInfoResponse(Status.Code.FAIL, "Bad Request: missing 'access_token'"));
			}
		} catch (Exception e) {
			TokenInfoResponse tokenResp = new TokenInfoResponse(Status.Code.ERROR, e.getMessage());
			writeResponseData(response, tokenResp);
		}
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}

}
