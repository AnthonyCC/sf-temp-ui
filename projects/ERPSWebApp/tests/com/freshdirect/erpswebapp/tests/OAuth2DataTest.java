package com.freshdirect.erpswebapp.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.junit.Test;

import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.webapp.ajax.oauth2.data.OAuth2CodeAndTokenData;
import com.freshdirect.webapp.ajax.oauth2.data.OAuth2Type;
import com.freshdirect.webapp.ajax.oauth2.service.OAuth2Service;

import junit.framework.TestCase;

public class OAuth2DataTest extends TestCase {

	private HttpServletRequest mockCodeHttpRequst = OAuth2TestHttpServletRequest.buildRequest(OAuth2Type.CODE);
	private HttpServletRequest mockTokenHttpRequst = OAuth2TestHttpServletRequest.buildRequest(OAuth2Type.TOKEN);

	@Test
	public void initTest() {
		Set<String> scope = new HashSet<String>(Arrays.asList("/api/get-order-history", "/api/get-user-profile",
				"/api/set-user-cart", "/api/set-user-cart"));

		OAuth2CodeAndTokenData token = new OAuth2CodeAndTokenData("userId@buyer.com", "12345678", "98765432", scope,
				"client_8907_storePower", 14400L, OAuth2Type.TOKEN);
		assertNotNull(token);

		OAuth2CodeAndTokenData code = new OAuth2CodeAndTokenData("userId@buyer.com", "12345678", "98765432", scope,
				"client_8907_storePower", 14400L, OAuth2Type.CODE);
		assertNotNull(code);

		OAuth2CodeAndTokenData refreshToken = new OAuth2CodeAndTokenData("userId@buyer.com", "12345678", "98765432",
				scope, "client_8907_storePower", 14400L, OAuth2Type.REFRESHTOKEN);
		assertNotNull(refreshToken);
	}

	@Test
	public void testSingleton() {
		OAuth2Service service = OAuth2Service.defaultService();
		assertNotNull(service);

		OAuth2Service service2 = OAuth2Service.defaultService();
		assertNotNull(service2);

		assertSame("Error: calling defaultService() should always return same singleton service.", service, service2);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testOAuth2_CodeData() throws URISyntaxException, OAuthSystemException {
		OAuth2Service service = OAuth2Service.defaultService();
		assertNotNull(service);

		FDUser user = new FDUser();
		user.setUserId("QA020@freshdirect.com");
		user.setIdentity(new FDIdentity("1234567", "12345679"));

		// code
		OAuthResponse resp = service.getAuthCode(this.mockCodeHttpRequst, user);
		assertEquals(resp.getResponseStatus(), 302);
		String locationUri = resp.getLocationUri();
		assertTrue(locationUri.startsWith("https://www.getpostman.com/oauth2/callback?"));
		assertTrue(locationUri.contains("code="));
		List<NameValuePair> kvs = URLEncodedUtils.parse(new URI(locationUri), "utf-8");
		assertTrue(kvs.size() > 0);
		String code = null;
		for (NameValuePair param : kvs) {
			if (param.getName().equals("code")) {
				code = param.getValue();
				break;
			}
		}
		assertNotNull(code);

		// token
		HttpServletRequest req = OAuth2TestHttpServletRequest.buildRequest(OAuth2Type.TOKEN);
		req.getParameterMap().put("code", new String[] { code });

		OAuthResponse resp2 = service.getAccessToken(req);
		assertEquals(resp2.getResponseStatus(), 200);
		assertTrue(resp2.getBody().length() > 0);
	}

	@Test
	public void testOAuth2_ExpiredTokenData() throws OAuthSystemException, URISyntaxException {
		OAuth2Service service = OAuth2Service.defaultService();
		assertNotNull(service);

		FDUser user = new FDUser();
		user.setUserId("QA020@freshdirect.com");
		user.setIdentity(new FDIdentity("1234567", "12345679"));

		// token - expired
		OAuthResponse resp = service.getAccessToken(mockTokenHttpRequst);
		assertEquals(resp.getResponseStatus(), 404);
		assertTrue(resp.getBody().contains("This+is+an+expired+auth+code"));
	}
}

/**
 * Mock HttpRequest instance, HttpRequest is used in generating
 * OAuthAuthzRequest or OAuthTokenRequest
 */
class OAuth2TestHttpServletRequest implements HttpServletRequest {
	private Map<String, String[]> params;

	public static OAuth2TestHttpServletRequest buildRequest(OAuth2Type type) {
		if (type == OAuth2Type.TOKEN) {
			return getRequestForToken();
		} else {
			return getRequestForCode();
		}
	}

	public static OAuth2TestHttpServletRequest getRequestForToken() {
		OAuth2TestHttpServletRequest req = new OAuth2TestHttpServletRequest();
		req.params = new HashMap<String, String[]>();
		req.params.put("redirect_uri", new String[] { "https://www.getpostman.com/oauth2/callback" });
		req.params.put("grant_type", new String[] { "authorization_code" });
		req.params.put("client_id", new String[] { "storePower" });
		req.params.put("client_secret", new String[] { "freshDirect" });
		req.params.put("code", new String[] {
				"W9S3JlY1ylNx9rPU9R00GTf4aeM3Sd1oJI7UxlpM7YqOEVhG0pzITzQ3gkWysBdsxzMhk++tOTjibBR9dp9joz5jWMvbZHJR+XpZxsgZZS85D9T7BBn+LpTp60Zki41qmU2fCbX+B2Hso/0As71HOP+plZSnvSD6KSbBoMDzjct7yRb/IF1YFTmzeN085n6nRrahmAsMoKIttn7hXwuuBGYsDWwDhkAORjYFbFgpWmpHrbJujgn0hngd2WGoLs6EgiL228gDVIvAyrNyXAIjBCvI8F6h3fy/PsstB+BSZdA=" });
		return req;
	}

	public static OAuth2TestHttpServletRequest getRequestForCode() {
		OAuth2TestHttpServletRequest req = new OAuth2TestHttpServletRequest();
		req.params = new HashMap<String, String[]>();
		req.params.put("redirect_uri", new String[] { "https://www.getpostman.com/oauth2/callback" });
		req.params.put("response_type", new String[] { "code" });
		req.params.put("client_id", new String[] { "storePower" });
		req.params.put("client_secret", new String[] { "freshDirect" });
		req.params.put("scope", new String[] { "/api/orderhistory  /api/userdata  /api/profile" });
		return req;
	}

	public Map<String, String[]> getParameterMap() {
		return params;
	}

	public String getParameter(String name) {
		String[] matches = params.get(name);
		if (matches == null || matches.length == 0)
			return null;
		return matches[0];
	}

	@Override
	public Object getAttribute(String arg0) {
		return null;
	}

	@Override
	public Enumeration<?> getAttributeNames() {
		return null;
	}

	@Override
	public String getCharacterEncoding() {
		return null;
	}

	@Override
	public int getContentLength() {
		return 0;
	}

	@Override
	public String getContentType() {
		return "application/x-www-form-urlencoded";
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return null;
	}

	@Override
	public String getLocalAddr() {
		return null;
	}

	@Override
	public String getLocalName() {
		return null;
	}

	@Override
	public int getLocalPort() {
		return 0;
	}

	@Override
	public Locale getLocale() {
		return null;
	}

	@Override
	public Enumeration<?> getLocales() {
		return null;
	}

	@Override
	public Enumeration<?> getParameterNames() {
		return null;
	}

	@Override
	public String[] getParameterValues(String arg0) {
		return null;
	}

	@Override
	public String getProtocol() {
		return null;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return null;
	}

	@Override
	public String getRealPath(String arg0) {
		return null;
	}

	@Override
	public String getRemoteAddr() {
		return null;
	}

	@Override
	public String getRemoteHost() {
		return null;
	}

	@Override
	public int getRemotePort() {
		return 0;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		return null;
	}

	@Override
	public String getScheme() {
		return null;
	}

	@Override
	public String getServerName() {
		return null;
	}

	@Override
	public int getServerPort() {
		return 0;
	}

	@Override
	public boolean isSecure() {
		return false;
	}

	@Override
	public void removeAttribute(String arg0) {
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
	}

	@Override
	public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
	}

	@Override
	public String getAuthType() {
		return null;
	}

	@Override
	public String getContextPath() {
		return null;
	}

	@Override
	public Cookie[] getCookies() {
		return null;
	}

	@Override
	public long getDateHeader(String arg0) {
		return 0;
	}

	@Override
	public String getHeader(String arg0) {
		return null;
	}

	@Override
	public Enumeration<?> getHeaderNames() {
		return null;
	}

	@Override
	public Enumeration<?> getHeaders(String arg0) {
		return null;
	}

	@Override
	public int getIntHeader(String arg0) {
		return 0;
	}

	@Override
	public String getMethod() {
		return "POST";
	}

	@Override
	public String getPathInfo() {
		return null;
	}

	@Override
	public String getPathTranslated() {
		return null;
	}

	@Override
	public String getQueryString() {
		return null;
	}

	@Override
	public String getRemoteUser() {
		return null;
	}

	@Override
	public String getRequestURI() {
		return null;
	}

	@Override
	public StringBuffer getRequestURL() {
		return null;
	}

	@Override
	public String getRequestedSessionId() {
		return null;
	}

	@Override
	public String getServletPath() {
		return null;
	}

	@Override
	public HttpSession getSession() {
		return null;
	}

	@Override
	public HttpSession getSession(boolean arg0) {
		return null;
	}

	@Override
	public Principal getUserPrincipal() {
		return null;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return false;
	}

	@Override
	public boolean isUserInRole(String arg0) {
		return false;
	}
}