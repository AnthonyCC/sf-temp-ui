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

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.model.AirclicMessageVO;
import com.freshdirect.delivery.model.AirclicNextelVO;
import com.freshdirect.delivery.model.AirclicTextMessageVO;
import com.freshdirect.delivery.model.DispatchNextTelVO;
import com.freshdirect.delivery.model.SignatureVO;

public interface AirclicManagerSB extends EJBObject {

	public List<AirclicMessageVO> getAirclicMessages()  throws DlvResourceException,RemoteException;
	
	public String saveMessage(AirclicTextMessageVO textMessage)  throws DlvResourceException,RemoteException;
	
	public byte[] getSignature(String order) throws RemoteException;
	
	public void sendMessages() throws RemoteException, DlvResourceException;
	
	public void getSignatureData(Date deliveryDate) throws RemoteException;
	
	SignatureVO getSignatureDetails(String order) throws RemoteException;
	
	Map<String, DispatchNextTelVO> getDispatchResourceNextTel(Date dispatchDate) throws DlvResourceException,RemoteException;;
	 
	Map<String, AirclicNextelVO> getNXOutScan(Date scanDate) throws DlvResourceException, RemoteException;
	
	void updateEmployeeNexTelData(List<DispatchNextTelVO> employeeNextTels) throws DlvResourceException,RemoteException;
	
	Map<String, String> getNextTelAssets() throws DlvResourceException, RemoteException;
	
}   
