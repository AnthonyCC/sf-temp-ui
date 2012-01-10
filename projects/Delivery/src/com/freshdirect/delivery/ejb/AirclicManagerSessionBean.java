/*
 * AirclicManagerSessionBean.java
 *
 * Created on Dec 01, 2011, 12:00 PM
 */

package com.freshdirect.delivery.ejb;

/**
 *
 * @author tbalumuri
 * @version
 */
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.dao.AirclicDAO;
import com.freshdirect.delivery.model.AirclicMessageVO;
import com.freshdirect.delivery.model.AirclicTextMessageVO;
import com.freshdirect.framework.core.GatewaySessionBeanSupport;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class AirclicManagerSessionBean extends SessionBeanSupport {


	private static final Category LOGGER = LoggerFactory.getInstance(AirclicManagerSessionBean.class);

	private final static ServiceLocator LOCATOR = new ServiceLocator();

	/** Creates new AirclicManagerSessionBean */
	public AirclicManagerSessionBean() {
		super();
	}

	/**
	 * Template method that returns the cache key to use for caching resources.
	 *
	 * @return the bean's home interface name
	 */
	protected String getResourceCacheKey() {
		return "com.freshdirect.delivery.ejb.AirclicManagerHome";
	}

	
	public List<AirclicMessageVO> getAirclicMessages() throws DlvResourceException 
	{
		Connection conn = null;
		try {
			conn = getConnection();
			return AirclicDAO.getMessages(conn);
		} catch (SQLException e) {			
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("AirclicManagerSB getAirclicMessages: Exception while cleaning: " + se);
			}
		}
	}
	
	public String saveMessage(AirclicTextMessageVO textMessage)  throws DlvResourceException{
		try 
		{
				
			
			if(ErpServicesProperties.isAirclicBlackhole())
			{
				saveMessageInQueue(textMessage);
				return "Message Queued";
			}
			else
			{
				List<String> userIds = getUserId(textMessage);
				saveMessageInQueue(textMessage);
				sendMessage(userIds, textMessage);
				updateMessage(textMessage);
				return "Message Sent to Airclic";
			}
		} catch (Exception e) {			
			throw new DlvResourceException(e);
		}
	}

	public byte[] getSignature(String order) throws RemoteException
	{


		Connection conn = null;
		byte[] signBytes =null;
		try
		{
			conn = getConnection();
			signBytes = AirclicDAO.getSignature( conn, order);
		}
		catch(Exception e)
		{
			
		}
		finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("AirclicManagerSB getSignature: Exception while cleaning: " + se);
			}
		}
		return signBytes;
		
			
	
	
	}
	private List<String> getUserId(AirclicTextMessageVO textMessage) throws DlvResourceException
	{

		Connection conn = null;
		try
		{
			conn = getConnection();
			return AirclicDAO.getUserId(conn, textMessage);
		}
		catch(Exception e)
		{
			throw new DlvResourceException(e);
		}
		finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("AirclicManagerSB getUserId: Exception while cleaning: " + se);
			}
		}
		
			
	
	}
	public void sendMessage(List<String> userIds , AirclicTextMessageVO textMessage) throws DlvResourceException
	{
		Connection conn = null;
		try
		{
			conn = getConnection();
			AirclicDAO.sendMessage(conn, userIds, textMessage);
		}
		catch(Exception e)
		{
			throw new DlvResourceException(e);
		}
		finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("AirclicManagerSB sendMessage: Exception while cleaning: " + se);
			}
		}
			
	}
	
	public void saveMessageInQueue(AirclicTextMessageVO textMessage) throws DlvResourceException, RemoteException
	{
		Connection conn = null;
		try
		{
			
			conn = getConnection();
			textMessage.setId(this.getNextId(conn, "DLV"));
			AirclicDAO.saveMessageInQueue(conn, textMessage);
		}
		catch(Exception e)
		{
			throw new DlvResourceException(e);
		}
		finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("AirclicManagerSB saveMessageInQueue: Exception while cleaning: " + se);
			}
		}
			
	}
	public void updateMessage(AirclicTextMessageVO textMessage) throws DlvResourceException, RemoteException
	{
		Connection conn = null;
		try
		{
			
			conn = getConnection();
			AirclicDAO.updateMessage(conn, textMessage);
		}
		catch(Exception e)
		{
			throw new DlvResourceException(e);
		}
		finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("AirclicManagerSB updateMessage: Exception while cleaning: " + se);
			}
		}
			
	}
	
	public void sendMessages() throws DlvResourceException, RemoteException
	{
		Connection conn = null;
		try
		{
			
			conn = getConnection();
			AirclicTextMessageVO textMessage;
			List<AirclicTextMessageVO> messages = AirclicDAO.getUnsentMessages(conn);
			for (Iterator<AirclicTextMessageVO> i = messages.iterator(); i.hasNext();) {
				try {
				
					textMessage = i.next();
					List<String> userIds = getUserId(textMessage);
					sendMessage(userIds, textMessage);
					updateMessage(textMessage);
					

				} catch (Exception e) {
					LOGGER.warn("Exception occured during sending the message", e);
					
					// just keep going :)
				}
			}
			
		}
		catch(Exception e)
		{
			throw new DlvResourceException(e);
		}
		finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("AirclicManagerSB updateMessage: Exception while cleaning: " + se);
			}
		}
			
	}
	
	public void getSignatureData(Date deliveryDate) {
		Connection conn = null;
		try
		{
			
			conn = getConnection();
			AirclicDAO.getSignatureData(conn, deliveryDate);
			
		}
		catch(Exception e)
		{
			
		}
		finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("AirclicManagerSB updateMessage: Exception while cleaning: " + se);
			}
		}
	}
	
	
}
