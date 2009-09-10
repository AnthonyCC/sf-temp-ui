package com.freshdirect.fdstore.util;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.fdstore.*;
import com.freshdirect.fdstore.customer.*;
import com.freshdirect.fdstore.warmup.ejb.InventoryWarmupHome;
import com.freshdirect.fdstore.warmup.ejb.InventoryWarmupSB;

public class CartFactory {

	public static FDCartModel createCart(FDIdentity identity) throws FDResourceException {
        
        FDCartModel cart = new FDCartModel();
        
        Collection addrs = FDCustomerManager.getShipToAddresses(identity);
        ErpAddressModel address = (ErpAddressModel)(addrs.toArray())[0];
        
        cart.setDeliveryAddress(address);
        Collection ccards = FDCustomerManager.getPaymentMethods(identity);
        cart.setPaymentMethod((ErpPaymentMethodI)((ccards.toArray())[0]));
        
        Calendar begCal = Calendar.getInstance();
        begCal.add(Calendar.DATE, 1);
        
        Calendar endCal = Calendar.getInstance();
        endCal.add(Calendar.DATE, 7);
        endCal.set(Calendar.HOUR, 12);
        endCal.set(Calendar.MINUTE, 00);
        endCal.set(Calendar.AM_PM, Calendar.AM);
        
        //FDDeliveryManager.getInstance().scrubAddress(dlvAddress);
            
        try {
            DlvZoneInfoModel zInfo = FDDeliveryManager.getInstance().getZoneInfo(address, new java.util.Date());
            
			FDReservation deliveryReservation =
				new FDReservation(
					null,
					new FDTimeslot(new DlvTimeslotModel()),
					endCal.getTime(), EnumReservationType.STANDARD_RESERVATION, identity.getErpCustomerPK(), null, false,false, null);
            
            cart.setZoneInfo(zInfo);
            cart.setDeliveryReservation(deliveryReservation);
            
            return cart;
            
        } catch (FDInvalidAddressException fdiae) {
            throw new FDResourceException(fdiae);
        }
    }
    

	public static FDIdentity getRandomIdentity() throws FDResourceException {
		try {
			Context ctx = FDStoreProperties.getInitialContext();
			InventoryWarmupHome home = (InventoryWarmupHome) ctx.lookup("freshdirect.fdstore.InventoryWarmup");
			InventoryWarmupSB sb = home.create();
			
			return sb.getRandomCustomerIdentity();
		} catch (RemoteException ex) {
			throw new FDResourceException(ex);
		} catch (NamingException ex) {
			throw new FDResourceException(ex);
		} catch (CreateException ex) {
			throw new FDResourceException(ex);
		}
	}

}
