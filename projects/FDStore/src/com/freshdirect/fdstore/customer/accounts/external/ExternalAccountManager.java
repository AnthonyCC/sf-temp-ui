package com.freshdirect.fdstore.customer.accounts.external;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumExternalLoginSource;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.ejb.FDServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.payment.service.IECommerceService;

public class ExternalAccountManager {

	private static Category LOGGER = LoggerFactory.getInstance(ExternalAccountManager.class);

	private static FDServiceLocator LOCATOR = FDServiceLocator.getInstance();

	public static String getUserIdForUserToken(String userToken) throws FDResourceException {

		String userId = "";
		try {

			IECommerceService service = FDECommerceService.getInstance();
			userId = service.getUserIdForUserToken(userToken);

			LOGGER.info("USER ID for token: " + userToken + " is:" + userId);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
		return userId;

	}

	public static boolean isUserEmailAlreadyExist(String email) throws FDResourceException {

		try {
			boolean isUserEmailExist = false;

			IECommerceService service = FDECommerceService.getInstance();
			isUserEmailExist = service.isUserEmailAlreadyExist(email);

			return isUserEmailExist;

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static int isUserEmailAlreadyExist(String email, String provider) throws FDResourceException {

		try {
			int isUserEmailExist = 0;

			IECommerceService service = FDECommerceService.getInstance();
			isUserEmailExist = service.isUserEmailAlreadyExist(email, provider);

			return isUserEmailExist;

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List<String> getConnectedProvidersByUserId(String userId, EnumExternalLoginSource source)
			throws FDResourceException {

		try {
			List<String> connectedproviders = null;

			IECommerceService service = FDECommerceService.getInstance();
			connectedproviders = service.getConnectedProvidersByUserId(userId, source);

			return connectedproviders;

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List<String> getConnectedProvidersByUserId(String userId) throws FDResourceException {

		try {
			List<String> connectedProviders = null;

			IECommerceService service = FDECommerceService.getInstance();
			connectedProviders = service.getConnectedProvidersByUserId(userId);

			return connectedProviders;

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static boolean isExternalLoginOnlyUser(String userId, EnumExternalLoginSource source)
			throws FDResourceException {

		try {

			IECommerceService service = FDECommerceService.getInstance();
			return service.isExternalLoginOnlyUser(userId, source);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static void linkUserTokenToUserId(String customerId, String userId, String userToken, String identityToken,
			String provider, String displayName, String preferredUserName, String email, String emailVerified)
			throws FDResourceException {

		try {

			IECommerceService service = FDECommerceService.getInstance();
			service.linkUserTokenToUserId(customerId, userId, userToken, identityToken, provider, displayName,
					preferredUserName, email, emailVerified);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void unlinkExternalAccountWithUser(String email, String userToken, String provider)
			throws FDResourceException {

		try {

			IECommerceService service = FDECommerceService.getInstance();
			service.unlinkExternalAccountWithUser(email, userToken, provider);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static void unlinkExternalAccountWithUser(String email, String provider) throws FDResourceException {

		try {

			IECommerceService service = FDECommerceService.getInstance();
			service.unlinkExternalAccountWithUser(email, provider);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static boolean isSocialLoginOnlyUser(String customer_id) throws FDResourceException {
		try {
			boolean isSocialLoginOnlyUser = false;

			IECommerceService service = FDECommerceService.getInstance();
			isSocialLoginOnlyUser = service.isSocialLoginOnlyUser(customer_id);

			return isSocialLoginOnlyUser;

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

}
