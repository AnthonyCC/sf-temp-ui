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
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.crm.CallLogModel;
import com.freshdirect.crm.CrmCaseAction;
import com.freshdirect.crm.CrmCaseActionType;
import com.freshdirect.crm.CrmCaseInfo;
import com.freshdirect.crm.CrmCaseModel;
import com.freshdirect.crm.CrmCaseOrigin;
import com.freshdirect.crm.CrmCasePriority;
import com.freshdirect.crm.CrmCaseState;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.dao.AirclicDAO;
import com.freshdirect.delivery.model.AirclicCartonInfo;
import com.freshdirect.delivery.model.AirclicMessageVO;
import com.freshdirect.delivery.model.AirclicNextelVO;
import com.freshdirect.delivery.model.AirclicTextMessageVO;
import com.freshdirect.delivery.model.DeliveryExceptionModel;
import com.freshdirect.delivery.model.DeliverySummaryModel;
import com.freshdirect.delivery.model.DeliveryManifestVO;
import com.freshdirect.delivery.model.DispatchNextTelVO;
import com.freshdirect.delivery.model.RouteNextelVO;
import com.freshdirect.delivery.model.SignatureVO;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class AirclicManagerSessionBean extends SessionBeanSupport {


	private static final Category LOGGER = LoggerFactory.getInstance(AirclicManagerSessionBean.class);

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
		try 
		{
			conn = getConnection();
			return AirclicDAO.getMessages(conn);
		}
		catch (SQLException e) 
		{	
			throw new DlvResourceException(e);
		} 
		finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("AirclicManagerSB getAirclicMessages: Exception while cleaning: " + se);
			}
		}
	}
	
	public String saveMessage(AirclicTextMessageVO textMessage, List<String> nextelList)  throws DlvResourceException {
			
			if(ErpServicesProperties.isAirclicBlackhole())
			{
				saveMessageInQueue(textMessage);
				return "Message Queued";
			}
			else
			{
				Map<String,Set<String>> userIds = getUserId(textMessage);
				// send message only to selected userId's
				Iterator<Map.Entry<String, Set<String>>> itr = userIds.entrySet().iterator();
				while (itr.hasNext()) {
				    Map.Entry<String,  Set<String>> userEntry = itr.next();
				    if(null != nextelList && !nextelList.contains(userEntry.getKey())){
				        itr.remove();
				    }
				}
				saveMessageInQueue(textMessage);
				try{
				sendMessage(userIds, textMessage);
				}catch(DlvResourceException e){
					updateMessage(textMessage);
					throw e;
				}
				updateMessage(textMessage);
				createCase(textMessage);
				return "Message Sent to Airclic";
			}
	}

	public SignatureVO getSignatureDetails(String order) throws DlvResourceException
	{
		Connection conn = null;
		SignatureVO signatureVO =null;
		try
		{
			conn = getConnection();
			signatureVO = AirclicDAO.getSignatureDetails( conn, order);
		}
		catch (SQLException e) 
		{	
			throw new DlvResourceException(e);
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
		return signatureVO;
	}
	
	public byte[] getSignature(String order) throws DlvResourceException
	{
		Connection conn = null;
		byte[] _image =null;
		try
		{
			conn = getConnection();
			_image = AirclicDAO.getSignature( conn, order);
		}
		catch (SQLException e) 
		{	
			throw new DlvResourceException(e);
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
		return _image;
	}
	
	private Map<String,Set<String>> getUserId(AirclicTextMessageVO textMessage) throws DlvResourceException
	{

		Connection conn = null;
		try
		{
			conn = getConnection();
			return AirclicDAO.getUserId(conn, textMessage);
		}
		catch(SQLException e)
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
	public void sendMessage(Map<String,Set<String>> userIds , AirclicTextMessageVO textMessage) throws DlvResourceException
	{
		Connection conn = null;
		try
		{
			conn = getConnection();
			AirclicDAO.sendMessage(conn, userIds, textMessage);
			textMessage.setSentToAirclic("Y");
		}
		catch(SQLException e)
		{
			textMessage.setSentToAirclic("X");
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
	
	public void saveMessageInQueue(AirclicTextMessageVO textMessage) throws DlvResourceException
	{
		Connection conn = null;
		try
		{
			
			conn = getConnection();
			textMessage.setId(this.getNextId(conn, "DLV"));
			AirclicDAO.saveMessageInQueue(conn, textMessage);
		}
		catch(SQLException e)
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
	public void updateMessage(AirclicTextMessageVO textMessage) throws DlvResourceException
	{
		Connection conn = null;
		try
		{
			
			conn = getConnection();
			AirclicDAO.updateMessage(conn, textMessage);
		}
		catch(SQLException e)
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
	
	public void sendMessages() throws DlvResourceException
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
					Map<String,Set<String>> userIds = getUserId(textMessage);
					sendMessage(userIds, textMessage);
					updateMessage(textMessage);
					

				} catch (DlvResourceException e) {
					LOGGER.warn("Exception occured during sending the message", e);
					
					// just keep going :)
				}
			}
			
		}
		catch(SQLException e)
		{
			throw new DlvResourceException(e);
		}
		finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("AirclicManagerSB sendMessages: Exception while cleaning: " + se);
			}
		}
			
	}
	
	public void getSignatureData(Date deliveryDate) throws DlvResourceException {
		Connection conn = null;
		try
		{
			
			conn = getConnection();
			AirclicDAO.getSignatureData(conn, deliveryDate);
			
		}
		catch(SQLException e)
		{
			throw new DlvResourceException(e);
		}
		finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("AirclicManagerSB getSignatureData: Exception while cleaning: " + se);
			}
		}
	}
	
	public Map<String, DispatchNextTelVO> getDispatchResourceNextTel(Date dispatchDate) throws DlvResourceException, RemoteException {
		Connection conn = null;
		try {			
			conn = getConnection();
			return AirclicDAO.getDispatchResourceNextTel(conn, dispatchDate);
		} catch(Exception e) {
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("AirclicManagerSB getDispatchResourceNextTel: Exception while cleaning: " + se);
			}
		}
	}
	public Map<String, AirclicNextelVO> getNXOutScan(Date scanDate) throws DlvResourceException, RemoteException {
		Connection conn = null;
		try {			
			conn = getConnection();
			return AirclicDAO.getNXOutScan(conn, scanDate);
		} catch(Exception e) {
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("AirclicManagerSB getNXOutScan: Exception while cleaning: " + se);
			}
		}		
	}
	
	public void updateEmployeeNexTelData(List<DispatchNextTelVO> employeeNextTels) throws DlvResourceException, RemoteException {
		Connection conn = null;
		try {			
			conn = getConnection();
			AirclicDAO.updateEmployeeNexTelData(conn, employeeNextTels);
		} catch(Exception e) {
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("AirclicManagerSB updateEmployeeNexTelData: Exception while cleaning: " + se);
			}
		}
	}
	
	private void createCase(AirclicTextMessageVO textMessage)
	{
		try
		{
			CrmCaseInfo caseInfo = new CrmCaseInfo();
			caseInfo.setOrigin(CrmCaseOrigin.getEnum(CrmCaseOrigin.CODE_SYS)); // FIXME: ...
			CrmCaseSubject subject = CrmCaseSubject.getEnum(CrmCaseSubject.CODE_ORDER_ENROUTE_SPL_INSTRUCTION);
			caseInfo.setSubject(subject);
			caseInfo.setPriority(CrmCasePriority.getEnum(CrmCasePriority.CODE_LOW));
			caseInfo.setSummary("Message sent to delivery team");
			
			caseInfo.setAssignedAgentPK(new PrimaryKey(textMessage.getSender()));
			caseInfo.setCustomerPK((textMessage.getCustomerId()!=null)? new PrimaryKey(textMessage.getCustomerId()):null);
			caseInfo.setSalePK((textMessage.getOrderId()!=null)?new PrimaryKey(textMessage.getOrderId()):null);
			
			CrmCaseModel newCase = new CrmCaseModel(caseInfo);
			newCase.setState(CrmCaseState.getEnum(CrmCaseState.CODE_OPEN));
			newCase.setCrmCaseMedia("other");
			
			{
				CrmCaseAction caseAction = new CrmCaseAction();
				caseAction.setType(CrmCaseActionType.getEnum( CrmCaseActionType.CODE_NOTE ) );
				caseAction.setTimestamp(new Date());
				caseAction.setAgentPK(caseInfo.getAssignedAgentPK());
	
				caseAction.setNote(textMessage.getMessage());
				newCase.addAction(caseAction);																		
			}
			
			CrmManager.getInstance().createCase(newCase);
		}
		catch(Exception e)
		{
			LOGGER.warn("AirclicManagerSB createCase: Exception while creating case: " + e);
		}

	}
	
	public  List<AirclicCartonInfo> lookupCartonScanHistory(String orderId) throws DlvResourceException {
		Connection conn = null;
		try	{			
			conn = getConnection();
			return AirclicDAO.lookupCartonScanHistory(conn, orderId);			
		} catch(SQLException e) {
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("AirclicManagerSB lookupCartonScanHistory: Exception while cleaning: " + se);
			}
		}
	}
	
	public List<AirclicTextMessageVO> lookupAirclicMessages(String orderId) throws DlvResourceException {	
		Connection conn = null;  
		try	{			
			conn = getConnection();
			return AirclicDAO.lookupAirclicMessages(conn, orderId);			
		} catch(SQLException e) {
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
	
	public DeliveryManifestVO getDeliveryManifest(String orderId, Date deliveryDate) throws DlvResourceException {
		Connection conn = null;  
		try	{			
			conn = getConnection();
			return AirclicDAO.getDeliveryManifest(conn, orderId, deliveryDate);			
		} catch(SQLException e) {
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("AirclicManagerSB getDeliveryManifest: Exception while cleaning: " + se);
			}
		}
	}
	
	public List<RouteNextelVO> lookupNextels(AirclicTextMessageVO textMessage) throws DlvResourceException {
		Connection conn = null;  
		try	{			
			conn = getConnection();			
			return AirclicDAO.getRouteNextels(conn, textMessage);			
		} catch(SQLException e) {
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("AirclicManagerSB lookupNextels: Exception while cleaning: " + se);
			}
		}
	}
	
	public List<CallLogModel> getOrderCallLog(String orderId) throws DlvResourceException {
		Connection conn = null;  
		try	{			
			conn = getConnection();			
			return AirclicDAO.getOrderCallLog(conn, orderId);			
		} catch(SQLException e) {
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("AirclicManagerSB getOrderCallLog: Exception while cleaning: " + se);
			}
		}
	}
	
	public DeliverySummaryModel lookUpDeliverySummary(String orderId, String routeNo, Date deliveryDate) throws DlvResourceException {
		Connection conn = null;  
		try	{			
			conn = getConnection();			
			return AirclicDAO.lookUpDeliverySummary(conn, orderId, routeNo, deliveryDate);			
		} catch(SQLException e) {
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("AirclicManagerSB lookUpDeliverySummary: Exception while cleaning: " + se);
			}
		}
	}
	
	public Map<String, DeliveryExceptionModel>  getCartonScanInfo() throws DlvResourceException {
		Connection conn = null;  
		try	{			
			conn = getConnection();			
			return AirclicDAO.getCartonScanInfo(conn);			
		} catch(SQLException e) {
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("AirclicManagerSB getCartonScanInfo: Exception while cleaning: " + se);
			}
		}
	}
	
}
