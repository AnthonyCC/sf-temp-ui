package com.freshdirect.webapp.ajax.oauth2.util;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ClientDataValidator {
	private static final Logger LOGGER = LoggerFactory.getInstance(ClientDataValidator.class);

	public static boolean validateClientId(String clientId) {
		// LOGGER.debug("Client id: " + clientId);
		try {
			String[] validCliendIds = StringUtils.split(FDStoreProperties.getOAuth2ClientIds(), ",");
			return ArrayUtils.contains(validCliendIds, clientId);
		} catch (Exception e) {
			LOGGER.info("Invalide Client: " + clientId, e);
		}
		return false;
	}

	public static boolean validateClientSecret(String clientId, String secret) {
		// LOGGER.debug("Client id/secret: " + clientId + "/" + secret);
		try {
			String[] validCliendIds = StringUtils.split(FDStoreProperties.getOAuth2ClientIds(), ",");
			String[] validCliendSecrets = StringUtils.split(FDStoreProperties.getOAuth2ClientSecrets(), ",");
			if (ArrayUtils.contains(validCliendIds, clientId) && ArrayUtils.contains(validCliendSecrets, secret)
					&& (ArrayUtils.indexOf(validCliendIds, clientId) == ArrayUtils.indexOf(validCliendSecrets,
							secret))) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.info("Unauthorized Client: " + clientId + "/" + secret, e);
		}
		return false;
	}

	public static boolean validateClientRedirectUri(String clientId, String uri) {
		// LOGGER.debug("Client id/redirect_uri: " + clientId + "/" + uri);

		try {
			String uriPath = StringUtils.split(uri, "?")[0];
			String[] validCliendIds = StringUtils.split(FDStoreProperties.getOAuth2ClientIds(), ",");
			if (!ArrayUtils.contains(validCliendIds, clientId)) {
				return false;
			}
			
			String[] validClientRedirectUri = StringUtils.split(FDStoreProperties.getOAuth2ClientRedirectUris(clientId), ",");

			for (int i = 0; i < validClientRedirectUri.length; i++) {
				validClientRedirectUri[i] = StringUtils.split(validClientRedirectUri[i], "?")[0];
			}

			if (ArrayUtils.contains(validClientRedirectUri, uriPath)) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.info("Unauthorized Client: " + clientId + "/" + uri, e);
		}
		return false;
	}
}
