package com.freshdirect.fdstore.customer.accounts.external;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.customer.EnumExternalLoginSource;
import com.freshdirect.fdstore.customer.ejb.FDSessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ExternalAccountManagerSessionBean extends FDSessionBeanSupport {
	
	private static final long serialVersionUID = -4628071128029921271L;
	
	private final static Logger LOGGER = LoggerFactory.getInstance(ExternalAccountManagerSessionBean.class);
	
	public String getUserIdForUserToken(String userToken) 
	{
		String userId= null;
		try {
			userId = ExternalAccountDAO.getUserIdForUserToken(this.getConnection(), userToken);
			
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
		
		return userId;
	}
	
	public boolean isUserEmailAlreadyExist(String email)
	{
		try {
			return ExternalAccountDAO.isUserEmailAlreadyExist(this.getConnection(), email);
			
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
		return false;
	
	}
	
	public boolean isUserEmailAlreadyExist(String email, String provider)
	{
		try {
			return ExternalAccountDAO.isUserEmailAlreadyExist(this.getConnection(), email, provider);
			
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
		return false;
	
	}
	
	public void linkUserTokenToUserId(String customerId,String userId,String userToken, String identityToken, String provider, String displayName, String preferredUserName, String email, String emailVerified)
	{
		try {
			
			ExternalAccountDAO.linkUserTokenToUserId(this.getConnection(), customerId, userId, userToken, identityToken, provider, displayName, preferredUserName, email, emailVerified);
			
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public List<String> getConnectedProvidersByUserId(String userId, EnumExternalLoginSource source)
	{
		try {
			return ExternalAccountDAO.getConnectedProvidersByUserId(userId, source, this.getConnection());
			
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
		return null;
	}
	
	public boolean isExternalLoginOnlyUser(String userId, EnumExternalLoginSource source) 
	{
		try {
			return ExternalAccountDAO.isExternalLoginOnlyUser(userId, source, this.getConnection());
			
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
		return false;
	}
	
	
	
	public void unlinkExternalAccountWithUser(String userId, String userToken, String provider)
	{
		try {
			
			ExternalAccountDAO.unlinkExternalAccountWithUser(this.getConnection(), userId, userToken, provider);
			
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	
}
