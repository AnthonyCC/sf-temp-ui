package com.freshdirect.fdstore.customer.accounts.external;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.CreateException;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumExternalLoginSource;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.ejb.FDServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.payment.service.IECommerceService;

public class ExternalAccountManager{
	
	private static Category LOGGER = LoggerFactory.getInstance(ExternalAccountManager.class);
	
	private static ExternalAccountManagerHome externalLoginManagerHome = null;
	
	private static FDServiceLocator LOCATOR = FDServiceLocator.getInstance();
	
	public static String getUserIdForUserToken(String userToken)  throws FDResourceException {
		lookupManagerHome();
		String userId="";
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ExternalAccountManagerSB)){
				IECommerceService service = FDECommerceService.getInstance();
				userId=service.getUserIdForUserToken(userToken);
				
			}else{
				ExternalAccountManagerSB sb = externalLoginManagerHome.create();
				userId = sb.getUserIdForUserToken(userToken);
			}
			LOGGER.info("USER ID for token: "+userToken+" is:"+userId);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
		return userId;

	}
	
	public static boolean isUserEmailAlreadyExist(String email) throws FDResourceException {
		lookupManagerHome();
		try {
			boolean isUserEmailExist = false;
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ExternalAccountManagerSB)){
				IECommerceService service = FDECommerceService.getInstance();
				isUserEmailExist = service.isUserEmailAlreadyExist(email);
				
			}else{
				ExternalAccountManagerSB sb = externalLoginManagerHome.create();
				isUserEmailExist = sb.isUserEmailAlreadyExist(email);
			}
			return isUserEmailExist;
			
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static int isUserEmailAlreadyExist(String email, String provider)
			throws FDResourceException {
		lookupManagerHome();
		try {
			int isUserEmailExist = 0;
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ExternalAccountManagerSB)) {
				IECommerceService service = FDECommerceService.getInstance();
				isUserEmailExist = service.isUserEmailAlreadyExist(email,provider);
			} else {
				ExternalAccountManagerSB sb = externalLoginManagerHome.create();
				isUserEmailExist = sb.isUserEmailAlreadyExist(email, provider);
			}
			return isUserEmailExist;

		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}	
	
	
	public static List<String> getConnectedProvidersByUserId(String userId, EnumExternalLoginSource source) throws FDResourceException {
		lookupManagerHome();
		try {
			List<String> connectedproviders = null;
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ExternalAccountManagerSB)) {
				IECommerceService service = FDECommerceService.getInstance();
				connectedproviders = service.getConnectedProvidersByUserId(userId, source);
			} else {
				ExternalAccountManagerSB sb = externalLoginManagerHome.create();
				connectedproviders = sb.getConnectedProvidersByUserId(userId, source);
			}
			return connectedproviders;
			
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static List<String> getConnectedProvidersByUserId(String userId) throws FDResourceException {
		lookupManagerHome();
		try {
			List<String> connectedProviders = null;
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ExternalAccountManagerSB)) {
				IECommerceService service = FDECommerceService.getInstance();
				connectedProviders = service.getConnectedProvidersByUserId(userId);
			} else {
				ExternalAccountManagerSB sb = externalLoginManagerHome.create();
				connectedProviders = sb.getConnectedProvidersByUserId(userId);
			}
			return connectedProviders;
			
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static boolean isExternalLoginOnlyUser(String userId, EnumExternalLoginSource source) throws FDResourceException
	{
		lookupManagerHome();
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ExternalAccountManagerSB)) {
				IECommerceService service = FDECommerceService.getInstance();
				return service.isExternalLoginOnlyUser(userId, source);
			} else {
			ExternalAccountManagerSB sb = externalLoginManagerHome.create();
			return sb.isExternalLoginOnlyUser(userId, source);
			}
			
			
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	
	}
	
	
	
	public static void linkUserTokenToUserId(String customerId, String userId,String userToken, String identityToken, String provider, String displayName, String preferredUserName, String email, String emailVerified) throws FDResourceException
	{
		lookupManagerHome();
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ExternalAccountManagerSB)) {
				IECommerceService service = FDECommerceService.getInstance();
				service.linkUserTokenToUserId(customerId, userId, userToken, identityToken, provider, displayName, preferredUserName, email, emailVerified);
			} else {
				ExternalAccountManagerSB sb = externalLoginManagerHome.create();
				sb.linkUserTokenToUserId(customerId, userId, userToken, identityToken, provider, displayName, preferredUserName, email, emailVerified);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	
	private static void invalidateManagerHome() {
		externalLoginManagerHome = null;
	}

	private static void lookupManagerHome() {
		if (externalLoginManagerHome != null) {
			return;
		}
		externalLoginManagerHome = LOCATOR.getExternalLoginManagerHome();
	}

	public static void unlinkExternalAccountWithUser(String email, String userToken, String provider) throws FDResourceException {
		lookupManagerHome();
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ExternalAccountManagerSB)) {
				IECommerceService service = FDECommerceService.getInstance();
				service.unlinkExternalAccountWithUser(email, userToken, provider);
			} else {
				ExternalAccountManagerSB sb = externalLoginManagerHome.create();
				sb.unlinkExternalAccountWithUser(email, userToken, provider);
			}
			
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
		
	}
	
	public static void unlinkExternalAccountWithUser(String email, String provider) throws FDResourceException {
		lookupManagerHome();
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ExternalAccountManagerSB)) {
				IECommerceService service = FDECommerceService.getInstance();
				service.unlinkExternalAccountWithUser(email, provider);
			} else {
				ExternalAccountManagerSB sb = externalLoginManagerHome.create();
				sb.unlinkExternalAccountWithUser(email, provider);
			}
			
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
		
	}
	
	
	public static boolean isSocialLoginOnlyUser(String customer_id) throws FDResourceException {
		lookupManagerHome();
		try {
			boolean isSocialLoginOnlyUser = false;
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ExternalAccountManagerSB)) {
				IECommerceService service = FDECommerceService.getInstance();
				isSocialLoginOnlyUser = service.isSocialLoginOnlyUser(customer_id);
			} else {
				ExternalAccountManagerSB sb = externalLoginManagerHome.create();
			    isSocialLoginOnlyUser =  sb.isSocialLoginOnlyUser(customer_id);
			}
			return isSocialLoginOnlyUser;
			
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
		
	}	

}
