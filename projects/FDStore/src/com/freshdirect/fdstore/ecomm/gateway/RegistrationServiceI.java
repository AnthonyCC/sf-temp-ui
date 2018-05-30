package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.RegistrationResult;
import com.freshdirect.fdstore.survey.FDSurveyResponse;

public interface RegistrationServiceI {

	/**
	 * Register and log in a new customer.
	 *
	 * @param ErpCustomerModel
	 *            erpCustomer
	 * @param FDCustomerModel
	 *            fdCustomer
	 * @param String
	 *            cookie
	 *
	 * @return the resulting FDIdentity
	 * @throws FDResourceException
	 *             if an error occurred using remote resources
	 * @throws ErpDuplicateUserIdException
	 *             if user enters an email address already in the system
	 */
	public RegistrationResult register(FDActionInfo info, ErpCustomerModel erpCustomer, FDCustomerModel fdCustomer,
			String cookie, boolean pickupOnly, boolean eligibleForPromotion, FDSurveyResponse survey,
			EnumServiceType serviceType, boolean isGiftCardBuyer)
			throws FDResourceException, ErpDuplicateUserIdException, RemoteException;

}
