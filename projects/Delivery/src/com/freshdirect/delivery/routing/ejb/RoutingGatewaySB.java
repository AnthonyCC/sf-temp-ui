package com.freshdirect.delivery.routing.ejb;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import javax.ejb.EJBObject;
import com.freshdirect.common.address.AddressI;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDTimeslot;


/**
*
*
* @version $Revision$
* @author $Author$
*/
public interface RoutingGatewaySB extends EJBObject {
   	
	public void sendShippingAddress(AddressI address) throws RemoteException;
	
	public void sendDateRangeAndZoneForTimeslots(List<FDTimeslot> timeSlots, ContactAddressModel address) throws RemoteException;
	
	public void sendReserveTimeslotRequest(FDReservation  reservation, ContactAddressModel address)throws RemoteException;
	
	public void sendCommitReservationRequest(DlvReservationModel reservation,ContactAddressModel address) throws RemoteException;
	
	public void sendReleaseReservationRequest(DlvReservationModel reservation,ContactAddressModel address) throws RemoteException;

}
