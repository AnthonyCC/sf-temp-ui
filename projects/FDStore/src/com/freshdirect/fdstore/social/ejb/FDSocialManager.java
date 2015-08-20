package com.freshdirect.fdstore.social.ejb;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.CreateException;

import org.apache.log4j.Category;

import com.freshdirect.common.ERPServiceLocator;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.ejb.FDServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;

@SuppressWarnings("deprecation")
public class FDSocialManager extends ERPServiceLocator{
	
	private static Category LOGGER = LoggerFactory.getInstance(FDSocialManager.class);
	
	private static FDSocialManagerHome socialManagerHome = null;
	
	private static FDServiceLocator LOCATOR = FDServiceLocator.getInstance();
	
	public static String getUserIdForUserToken(String userToken)  throws FDResourceException {
		lookupManagerHome();
		String userId="";
		try {
			FDSocialManagerSB sb = socialManagerHome.create();
			userId = sb.getUserIdForUserToken(userToken);
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
			FDSocialManagerSB sb = socialManagerHome.create();
			return sb.isUserEmailAlreadyExist(email);
			
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
			FDSocialManagerSB sb = socialManagerHome.create();
			return sb.getConnectedProvidersByUserId(userId);
			
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static boolean isSocialLoginOnlyUser(String userId) throws FDResourceException
	{
		lookupManagerHome();
		try {
			FDSocialManagerSB sb = socialManagerHome.create();
			return sb.isSocialLoginOnlyUser(userId);
			
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	
	}
	
	
	
	public static void mergeSocialAccountWithUser(String userId,String userToken, String identityToken, String provider, String displayName, String preferredUserName, String email, String emailVerified) throws FDResourceException
	{
		lookupManagerHome();
		try {
			FDSocialManagerSB sb = socialManagerHome.create();
			sb.mergeSocialAccountWithUser(userId, userToken, identityToken, provider, displayName, preferredUserName, email, emailVerified);
			
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	
	private static void invalidateManagerHome() {
		socialManagerHome = null;
	}

	private static void lookupManagerHome() {
		if (socialManagerHome != null) {
			return;
		}
		socialManagerHome = LOCATOR.getFDSocialLoginManagerHome();
	}

	public static void unlinkSocialAccountWithUser(String email, String userToken) throws FDResourceException {
		lookupManagerHome();
		try {
			FDSocialManagerSB sb = socialManagerHome.create();
			sb.unlinkSocialAccountWithUser(email, userToken);
			
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
		
	}

}
