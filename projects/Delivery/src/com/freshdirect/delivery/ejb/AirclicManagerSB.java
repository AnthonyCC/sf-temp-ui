package com.freshdirect.delivery.ejb;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.crm.CallLogModel;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.model.AirclicCartonInfo;
import com.freshdirect.delivery.model.AirclicMessageVO;
import com.freshdirect.delivery.model.AirclicNextelVO;
import com.freshdirect.delivery.model.AirclicTextMessageVO;
import com.freshdirect.delivery.model.DeliveryExceptionModel;
import com.freshdirect.delivery.model.DeliveryManifestVO;
import com.freshdirect.delivery.model.DeliverySummaryModel;
import com.freshdirect.delivery.model.DispatchNextTelVO;
import com.freshdirect.delivery.model.RouteNextelVO;
import com.freshdirect.delivery.model.SignatureVO;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.sms.CrmSmsDisplayInfo;

public interface AirclicManagerSB extends EJBObject {

	public List<AirclicMessageVO> getAirclicMessages()  throws DlvResourceException,RemoteException;
	
	public String saveMessage(AirclicTextMessageVO textMessage, List<String> nextelList)  throws DlvResourceException,RemoteException;
	
	public byte[] getSignature(String order) throws DlvResourceException, RemoteException;
	
	public void sendMessages() throws RemoteException, DlvResourceException;
	
	public void getSignatureData(Date deliveryDate) throws RemoteException,DlvResourceException;
	
	SignatureVO getSignatureDetails(String order) throws DlvResourceException, RemoteException;
	
	Map<String, DispatchNextTelVO> getDispatchResourceNextTel(Date dispatchDate) throws DlvResourceException,RemoteException;;
	 
	Map<String, AirclicNextelVO> getNXOutScan(Date scanDate) throws DlvResourceException, RemoteException;
	
	void updateEmployeeNexTelData(List<DispatchNextTelVO> employeeNextTels) throws DlvResourceException,RemoteException;
	
	public List<AirclicCartonInfo> lookupCartonScanHistory(String orderId)  throws DlvResourceException, RemoteException;
	
	public List<AirclicTextMessageVO> lookupAirclicMessages(String orderId) throws DlvResourceException, RemoteException;
	
	public DeliveryManifestVO getDeliveryManifest(String orderId, Date deliveryDate) throws DlvResourceException, RemoteException;
	
	public List<RouteNextelVO> lookupNextels(AirclicTextMessageVO textMessage) throws DlvResourceException, RemoteException;
	
	public List<CallLogModel> getOrderCallLog(String orderId) throws DlvResourceException, RemoteException;
	
	public DeliverySummaryModel lookUpDeliverySummary(String orderId, String routeNo, Date deliveryDate) throws DlvResourceException, RemoteException;

	public Map<String, DeliveryExceptionModel> getCartonScanInfo() throws DlvResourceException, RemoteException;
	
	public List<CrmSmsDisplayInfo> getSmsInfo(String orderId) throws DlvResourceException, RemoteException;
}   
