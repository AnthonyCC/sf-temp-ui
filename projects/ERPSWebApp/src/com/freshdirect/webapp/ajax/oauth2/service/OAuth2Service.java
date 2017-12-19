package com.freshdirect.webapp.ajax.oauth2.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.OAuthResponse.OAuthResponseBuilder;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.oauth2.data.OAuth2CodeAndTokenData;
import com.freshdirect.webapp.ajax.oauth2.data.OAuth2InvalidCodeTokenException;
import com.freshdirect.webapp.ajax.oauth2.data.OAuth2Type;
import com.freshdirect.webapp.ajax.oauth2.data.Status;
import com.freshdirect.webapp.ajax.oauth2.util.ClientDataValidator;
import com.freshdirect.webapp.ajax.oauth2.util.FDUserTokenGenerator;
import com.google.common.base.Joiner;

public class OAuth2Service {
	private static final Logger LOGGER = LoggerFactory.getInstance(OAuth2Service.class);

	private static final String ERROR_INVALID_CLIENT_DESCRIPTION = "Client authentication failed";
	private static final String ERROR_INVALID_AUTH_CODE = "Invalid authorization code";
	private static final String ERROR_INVALID_REDIRECT_URI = "Failed to validate registered redirect_uri";

	private static final OAuth2Service INSTANCE = new OAuth2Service();

	private OAuth2Service() {
	}

	public static OAuth2Service defaultService() {
		return INSTANCE;
	}

	/**
	 * Build auth code by given request and user data.
	 * 
	 * @param request
	 * @param user
	 * @return
	 * @throws URISyntaxException
	 * @throws OAuthSystemException
	 */
	public OAuthResponse getAuthCode(HttpServletRequest request, FDUserI user)
			throws URISyntaxException, OAuthSystemException {

		String redirectUri = null;

		try {
			OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);

			String clientId = oauthRequest.getClientId();
			Set<String> scopes = oauthRequest.getScopes();
			redirectUri = oauthRequest.getRedirectURI();
			String responseType = oauthRequest.getResponseType();

			LOGGER.info("OAuth request for auth-code from client: " + clientId);

			if (!ClientDataValidator.validateClientId(clientId)) {
				return OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
						.setError(OAuthError.TokenResponse.INVALID_CLIENT)
						.setErrorDescription(ERROR_INVALID_CLIENT_DESCRIPTION).buildJSONMessage();
			}

			if (!ClientDataValidator.validateClientRedirectUri(clientId, redirectUri)) {
				return OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
						.setError(OAuthError.TokenResponse.INVALID_CLIENT)
						.setErrorDescription(ERROR_INVALID_REDIRECT_URI).buildJSONMessage();
			}

			OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request,
					HttpServletResponse.SC_FOUND);

			if (ResponseType.CODE.toString().equals(responseType)) {
				// Data flow of generating auth-code for Web App, Mobile App
				OAuth2CodeAndTokenData data = new OAuth2CodeAndTokenData(user.getUserId(),
						user.getIdentity().getErpCustomerPK(), user.getIdentity().getFDCustomerPK(), scopes, clientId,
						FDStoreProperties.getDefaultAuthCodeExpiration(), OAuth2Type.CODE);
				String codeJson = OAuth2CodeAndTokenData.toJson(data);
				FDUserTokenGenerator generator = FDUserTokenGenerator.newInstance(codeJson);
				OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(generator);

				final String authorizationCode = oauthIssuerImpl.authorizationCode();
				LOGGER.debug("An authorizationCode is generated: [" + authorizationCode + "]");
				builder.setCode(authorizationCode);

			} else if (ResponseType.TOKEN.toString().equals(responseType)) {
				// Data flow of generating token directly for native App
				OAuth2CodeAndTokenData data = new OAuth2CodeAndTokenData(user.getUserId(),
						user.getIdentity().getErpCustomerPK(), user.getIdentity().getFDCustomerPK(), scopes, clientId,
						FDStoreProperties.getDefaultAccessTokenExpiration(), OAuth2Type.TOKEN);

				String tokenJson = OAuth2CodeAndTokenData.toJson(data);
				FDUserTokenGenerator generator = FDUserTokenGenerator.newInstance(tokenJson);

				OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(generator);
				final String accessToken = oauthIssuerImpl.accessToken();
				LOGGER.debug("An accessToken is generated: [" + accessToken + "]");
				builder.setAccessToken(accessToken);
				builder.setExpiresIn(FDStoreProperties.getDefaultAccessTokenExpiration());

				// addTokenInDatabase(accessToken);
			}

			final OAuthResponse oauthResp = builder.location(redirectUri).buildQueryMessage();
			return oauthResp;

		} catch (OAuthProblemException e) {
			e.printStackTrace();
			final OAuthResponse oauthResp = OAuthASResponse.errorResponse(HttpServletResponse.SC_NOT_FOUND)
					.setError(e.getMessage()).location(redirectUri).buildQueryMessage();
			return oauthResp;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			final OAuthResponse oauthResp = OAuthASResponse.errorResponse(HttpServletResponse.SC_NOT_FOUND)
					.setError(e.getMessage()).location(redirectUri).buildQueryMessage();
			return oauthResp;
		}
	}

	/**
	 * Build access token by provided authorization_code or refresh_token.
	 * 
	 * @param request
	 * @param user
	 * @return
	 * @throws OAuthSystemException
	 */
	public OAuthResponse getAccessToken(HttpServletRequest request, FDUserI user) throws OAuthSystemException {
		try {
			OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);

			String clientId = oauthRequest.getClientId();
			String clientSecret = oauthRequest.getClientSecret();
			String grantType = oauthRequest.getGrantType();
			String redirectUri = oauthRequest.getRedirectURI();
			String authCode = oauthRequest.getParam(OAuth.OAUTH_CODE);
			String refreshToken = oauthRequest.getRefreshToken();

			return getAccessToken(clientId, clientSecret, grantType, redirectUri, authCode, refreshToken, user);

		} catch (OAuthProblemException ex) {
			ex.printStackTrace();
			OAuthResponse oaResp = OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
					.setError(OAuthError.TokenResponse.INVALID_REQUEST).error(ex).buildJSONMessage();
			return oaResp;
		}
	}

	public OAuthResponse getAccessToken(String clientId, String clientSecret, String grantType, String redirectUri,
			String authCode, String refreshToken, FDUserI user) throws OAuthSystemException {
		try {
			if (!ClientDataValidator.validateClientId(clientId)) {
				return OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
						.setError(OAuthError.TokenResponse.INVALID_CLIENT)
						.setErrorDescription(ERROR_INVALID_CLIENT_DESCRIPTION).buildJSONMessage();
			}

			if (!ClientDataValidator.validateClientSecret(clientId, clientSecret)) {
				return OAuthASResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
						.setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
						.setErrorDescription(ERROR_INVALID_CLIENT_DESCRIPTION).buildJSONMessage();
			}

			if (!ClientDataValidator.validateClientRedirectUri(clientId, redirectUri)) {
				return OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
						.setError(OAuthError.TokenResponse.INVALID_CLIENT)
						.setErrorDescription(ERROR_INVALID_REDIRECT_URI).buildJSONMessage();
			}

			if (GrantType.AUTHORIZATION_CODE.toString().equals(grantType)) {
				OAuth2CodeAndTokenData authCodeData = getCodeOrTokenData(authCode);
				if (authCodeData == null || (authCodeData.getType() != OAuth2Type.CODE)) {
					return OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
							.setError(OAuthError.TokenResponse.INVALID_GRANT)
							.setErrorDescription(ERROR_INVALID_AUTH_CODE).buildHeaderMessage();
				}

				OAuthResponse oaResp = buildTokenResponse(user.getUserId(), user.getIdentity().getErpCustomerPK(),
						user.getIdentity().getFDCustomerPK(), clientId, authCodeData);
				return oaResp;
			} else if (GrantType.REFRESH_TOKEN.toString().equals(grantType)) {
				OAuthResponse oaResp = refreshTokenResponse(refreshToken, user.getIdentity().getErpCustomerPK(),
						clientId);
				return oaResp;
			} else {
				return OAuthResponse.errorResponse(HttpServletResponse.SC_NOT_FOUND)
						.setError(OAuthError.TokenResponse.UNSUPPORTED_GRANT_TYPE)
						.setErrorDescription("Supported grant_type = [authorization_code | refresh_token]")
						.buildJSONMessage();
			}

		} catch (OAuth2InvalidCodeTokenException ex) {
			OAuthResponse oaResp = OAuthResponse.errorResponse(HttpServletResponse.SC_NOT_FOUND)
					.setError(OAuthError.TokenResponse.INVALID_GRANT).setErrorDescription(ex.getMessage())
					.buildJSONMessage();
			return oaResp;
		} catch (Exception ex) {
			ex.printStackTrace();
			OAuthResponse oaResp = OAuthResponse.errorResponse(HttpServletResponse.SC_NOT_FOUND)
					.setError(ex.getMessage()).buildJSONMessage();
			return oaResp;
		}
	}

	/**
	 * Refresh token (create brand new one) after validating the previous token,
	 * giving it new expiration date.
	 * 
	 * @param refreshToken
	 * @param erpCustomerPK
	 * @param clientId
	 * @return
	 * @throws JsonProcessingException
	 * @throws OAuthSystemException
	 * @throws OAuth2InvalidCodeTokenException
	 */
	private OAuthResponse refreshTokenResponse(String refreshToken, String erpCustomerPK, String clientId)
			throws JsonProcessingException, OAuthSystemException, OAuth2InvalidCodeTokenException {

		OAuth2CodeAndTokenData oldRefreshTokenData = getCodeOrTokenData(refreshToken);
		Status status = oldRefreshTokenData.isRefreshTokenValid();

		// validate if refreshToken was issued for given app and user
		if (oldRefreshTokenData == null || Status.Code.SUCCESS != status.getCode()) {
			return OAuthResponse.errorResponse(HttpServletResponse.SC_NOT_FOUND)
					.setError(OAuthError.TokenResponse.INVALID_GRANT).setErrorDescription("Not a valid refresh token.")
					.buildJSONMessage();
		}

		if (!StringUtils.equals(oldRefreshTokenData.getAppId(), clientId)
				|| !StringUtils.equals(oldRefreshTokenData.getErpCustomerPK(), erpCustomerPK)) {
			return OAuthResponse.errorResponse(HttpServletResponse.SC_NOT_FOUND)
					.setError(OAuthError.TokenResponse.INVALID_GRANT)
					.setErrorDescription("Mismatch: the user erpCustomerPK or appId doesn't match").buildJSONMessage();
		}

		OAuth2CodeAndTokenData newTokenData = new OAuth2CodeAndTokenData(oldRefreshTokenData);
		newTokenData.setType(OAuth2Type.TOKEN);
		newTokenData.refreshExpiration(FDStoreProperties.getDefaultAccessTokenExpiration());
		FDUserTokenGenerator generator = FDUserTokenGenerator.newInstance(OAuth2CodeAndTokenData.toJson(newTokenData));
		OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(generator);
		final String newAccessToken = oauthIssuerImpl.accessToken();

		String newRefreshToken = refreshToken;
		if (FDStoreProperties.getDefaultRefreshTokenExpiration() > 0) {
			oldRefreshTokenData.addSalt();
			oldRefreshTokenData.refreshExpiration(FDStoreProperties.getDefaultRefreshTokenExpiration());
			generator = FDUserTokenGenerator.newInstance(OAuth2CodeAndTokenData.toJson(oldRefreshTokenData));
			oauthIssuerImpl = new OAuthIssuerImpl(generator);
			newRefreshToken = oauthIssuerImpl.refreshToken();
		}

		Set<String> sameScope = newTokenData.getScope();
		String sameScopeStr = Joiner.on(" ").join(sameScope);

		OAuthResponseBuilder builder = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
				.setAccessToken(newAccessToken).setRefreshToken(newRefreshToken).setExpiresIn("3600")
				.setScope(sameScopeStr);
		OAuthResponse oaResp = builder.buildHeaderMessage();
		return oaResp;
	}

	/**
	 * Build a access token for given user, app id and scope.
	 * 
	 * @param userId
	 * @param erpCustomerPK
	 * @param fdCustomerPK
	 * @param clientId
	 * @param authCodeData
	 * @return
	 * @throws JsonProcessingException
	 * @throws OAuthSystemException
	 */
	private OAuthResponse buildTokenResponse(String userId, String erpCustomerPK, String fdCustomerPK, String clientId,
			OAuth2CodeAndTokenData authCodeData) throws JsonProcessingException, OAuthSystemException {

		Set<String> scopes = authCodeData.getScope();
		OAuth2CodeAndTokenData accessTokenData = new OAuth2CodeAndTokenData(userId, erpCustomerPK, fdCustomerPK, scopes,
				clientId, FDStoreProperties.getDefaultAccessTokenExpiration(), OAuth2Type.TOKEN);
		FDUserTokenGenerator generator = FDUserTokenGenerator
				.newInstance(OAuth2CodeAndTokenData.toJson(accessTokenData));
		OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(generator);
		final String accessToken = oauthIssuerImpl.accessToken();

		OAuth2CodeAndTokenData refreshTokenData = new OAuth2CodeAndTokenData(accessTokenData);
		refreshTokenData.setType(OAuth2Type.REFRESHTOKEN);
		refreshTokenData.refreshExpiration(FDStoreProperties.getDefaultRefreshTokenExpiration());
		generator = FDUserTokenGenerator.newInstance(OAuth2CodeAndTokenData.toJson(refreshTokenData));
		oauthIssuerImpl = new OAuthIssuerImpl(generator);
		final String refreshToken = oauthIssuerImpl.refreshToken();

		String scopesStr = Joiner.on(" ").join(scopes);

		OAuthResponseBuilder builder = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
				.setAccessToken(accessToken).setRefreshToken(refreshToken).setExpiresIn("3600").setScope(scopesStr);
		OAuthResponse oaResp = builder.buildHeaderMessage();
		return oaResp;
	}

	/**
	 * Get user erpCustomerPK by access token.
	 * 
	 * @param tokenId
	 * @return user's erpCustomerPK
	 * @throws OAuth2InvalidCodeTokenException
	 */
	public String getErpCustomerPKByAccessToken(String tokenId) throws OAuth2InvalidCodeTokenException {
		OAuth2CodeAndTokenData accessTokenData = this.getCodeOrTokenData(tokenId);
		if (accessTokenData == null) {
			return null;
		}
		return accessTokenData.getErpCustomerPK();
	}

	/**
	 * Get user fdCustomerPK by access token.
	 * 
	 * @param tokenId
	 * @return user's fdCustomerPK
	 * @throws OAuth2InvalidCodeTokenException
	 */
	public String getFdCustomerPKByAccessToken(String tokenId) throws OAuth2InvalidCodeTokenException {
		OAuth2CodeAndTokenData accessTokenData = this.getCodeOrTokenData(tokenId);
		if (accessTokenData == null) {
			return null;
		}
		return accessTokenData.getFdCustomerPK();
	}

	/**
	 * Get user identity by access token
	 * 
	 * @param tokenId
	 * @return user's FDIdentity instance
	 * @throws OAuth2InvalidCodeTokenException
	 */
	public FDIdentity getUserIdentityByAccessToken(String tokenId) throws OAuth2InvalidCodeTokenException {
		OAuth2CodeAndTokenData accessTokenData = this.getCodeOrTokenData(tokenId);
		if (accessTokenData == null) {
			return null;
		}
		return new FDIdentity(accessTokenData.getErpCustomerPK(), accessTokenData.getFdCustomerPK());
	}

	/**
	 * Get complete token information by code/token
	 * 
	 * @param codeOrToken
	 * @return token data
	 */
	public OAuth2CodeAndTokenData getCodeOrTokenData(String codeOrToken) throws OAuth2InvalidCodeTokenException {
		if (codeOrToken == null) {
			return null;
		}
		// no DAO access, directly de-cipher
		try {
			FDUserTokenGenerator gen = FDUserTokenGenerator.getDefault();
			String json = gen.decrypt(codeOrToken);
			OAuth2CodeAndTokenData data = OAuth2CodeAndTokenData.fromJson(json);

			Status status = null;
			if (data.getType() == OAuth2Type.CODE) {
				status = data.isCodeValid();
			} else if (data.getType() == OAuth2Type.REFRESHTOKEN) {
				status = data.isRefreshTokenValid();
			} else {
				status = data.isTokenValid();
			}

			if (Status.Code.SUCCESS == status.getCode()) {
				return data;
			} else {
				throw new OAuth2InvalidCodeTokenException(status.getMessage());
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
			throw new OAuth2InvalidCodeTokenException(e.getMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new OAuth2InvalidCodeTokenException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new OAuth2InvalidCodeTokenException(e.getMessage());
		}
	}
}
