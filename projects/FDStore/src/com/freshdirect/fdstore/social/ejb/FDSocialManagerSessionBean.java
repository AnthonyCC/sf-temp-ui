package com.freshdirect.fdstore.social.ejb;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.customer.ejb.FDSessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDSocialManagerSessionBean extends FDSessionBeanSupport {
	
	private static final long serialVersionUID = -4628071128029921271L;
	
	private final static Logger LOGGER = LoggerFactory.getInstance(FDSocialManagerSessionBean.class);
	
	public String getUserIdForUserToken(String userToken) 
	{
		String userId= null;
		try {
			userId = FDSocialDAO.getUserIdForUserToken(this.getConnection(), userToken);
			
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
		
		return userId;
	}
	
	public boolean isUserEmailAlreadyExist(String email)
	{
		try {
			return FDSocialDAO.isUserEmailAlreadyExist(this.getConnection(), email);
			
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
		return false;
	
	}
	
	public void mergeSocialAccountWithUser(String userId,String userToken, String identityToken, String provider, String displayName, String preferredUserName, String email, String emailVerified)
	{
		try {
			
			FDSocialDAO.linkUserTokenToUserId(this.getConnection(), userId, userToken, identityToken, provider, displayName, preferredUserName, email, emailVerified);
			
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public List<String> getConnectedProvidersByUserId(String userId)
	{
		try {
			return FDSocialDAO.getConnectedProvidersByUserId(userId,this.getConnection());
			
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
		return null;
	}
	
	public boolean isSocialLoginOnlyUser(String userId) 
	{
		try {
			return FDSocialDAO.isSocialLoginOnlyUser(userId,this.getConnection());
			
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
		return false;
	}
	
	
	
	public void unlinkSocialAccountWithUser(String userId,String userToken)
	{
		try {
			
			FDSocialDAO.unLinkUserTokenFromUserId(this.getConnection(), userId, userToken);
			
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	
}
