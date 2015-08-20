package com.freshdirect.fdstore.social.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

public interface FDSocialManagerSB extends EJBObject  {
	
	public String getUserIdForUserToken(String userToken) throws RemoteException;
	
	public boolean isUserEmailAlreadyExist(String email) throws RemoteException;
	
	public void mergeSocialAccountWithUser(String userId,String userToken, String identityToken, String provider, String displayName, String preferredUserName, String email, String emailVerified) throws RemoteException;

	public List<String> getConnectedProvidersByUserId(String userId) throws RemoteException;
	
	public boolean isSocialLoginOnlyUser(String userId) throws RemoteException;

	public void unlinkSocialAccountWithUser(String email, String userToken) throws RemoteException;
	
}
