package com.freshdirect.dataloader.reservation;

import com.freshdirect.delivery.ejb.DlvManagerSB;
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;

public abstract class BaseReservationCronRunner extends BaseCapacityCronRunner {
		
		protected  abstract void processReservation(DlvManagerSB dlvManager,FDCustomerManagerSB sb,DlvReservationModel reservation) ;
	    
		
		public static FDIdentity getIdentity(String erpCustomerId) {
			return new FDIdentity(erpCustomerId);
		}
}
