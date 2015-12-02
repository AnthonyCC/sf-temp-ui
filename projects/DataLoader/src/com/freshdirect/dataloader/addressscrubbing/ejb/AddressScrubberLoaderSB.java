package com.freshdirect.dataloader.addressscrubbing.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.logistics.controller.data.response.AddressScrubbingResponse;


/**
 * @author Aniwesh Vatsal
 *
 */
public interface AddressScrubberLoaderSB extends EJBObject{
	
	public void getAddress()  throws RemoteException;
	
	public void scrubbedAddLogBatchInsert(List<AddressScrubbingResponse> lst) throws RemoteException;
	
	public void scrubbedAddBatchUpdate(List<AddressScrubbingResponse>  addressScrubbingResponses)throws RemoteException;

}
