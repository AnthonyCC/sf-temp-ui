package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Collection;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPromotionHistory;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.fdstore.customer.RegistrationResult;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.framework.core.PrimaryKey;

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
