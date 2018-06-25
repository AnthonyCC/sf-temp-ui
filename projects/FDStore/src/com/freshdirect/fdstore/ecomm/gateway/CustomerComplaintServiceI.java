package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;

import com.freshdirect.customer.ErpComplaintException;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.PrimaryKey;

public interface CustomerComplaintServiceI {

	/**
	 * Adds a complaint to the user's list of complaints and begins the associated
	 * credit issuing process
	 *
	 * @param ErpComplaintModel
	 *            represents the complaint
	 * @param String
	 *            the PK of the sale to which the complaint is to be added
	 * @throws FDResourceException
	 *             if an error occurred while accessing remote resources
	 * @throws ErpComplaintException
	 *             if order was not in proper state to accept complaints
	 */
	public String addComplaint(ErpComplaintModel complaint, String saleId, String erpCustomerId,
			String fdCustomerId, boolean autoApproveAuthorized, Double limit)
			throws FDResourceException, ErpComplaintException, RemoteException;

}
