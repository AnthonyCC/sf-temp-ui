package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.freshdirect.customer.ErpComplaintReason;

public interface ErpComplaintManagerServiceI {
	
	public Map<String, List<ErpComplaintReason>> getReasons(boolean excludeCartonReq) throws RemoteException;
	 
	public Map<String,String> getComplaintCodes() throws RemoteException;

	public Collection<String> getPendingComplaintSaleIds() throws RemoteException;

	public void rejectMakegoodComplaint(String makegood_sale_id) throws RemoteException;
	 
	public ErpComplaintReason getReasonByCompCode(String cCode) throws RemoteException;

}
