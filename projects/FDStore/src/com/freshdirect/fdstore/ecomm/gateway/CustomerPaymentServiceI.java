package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpPaymentMethodException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.core.PrimaryKey;

public interface CustomerPaymentServiceI {

	public List<ErpPaymentMethodI> getPaymentMethods(FDIdentity identity) throws FDResourceException, RemoteException;

	public void addPaymentMethod(FDActionInfo info, ErpPaymentMethodI paymentMethod, boolean paymentechEnabled,
			boolean isDebitCardSwitch) throws FDResourceException, RemoteException, ErpPaymentMethodException, ErpFraudException;

	/**
	 * update a payment method for the customer
	 *
	 * @param identity
	 *            the customer's identity reference
	 * @param paymentMethod
	 *            ErpPaymentMethodI to update
	 *
	 * @throws FDResourceException
	 *             if an error occurred using remote resources
	 */
	public void updatePaymentMethod(FDActionInfo info, ErpPaymentMethodI paymentMethod)
			throws FDResourceException, RemoteException, ErpPaymentMethodException;

	/**
	 * remove a payment method for the customer
	 *
	 * @param identity
	 *            the customer's identity reference
	 * @param pk
	 *            PrimaryKey of the paymentMethod to remove
	 *
	 *            throws FDResourceException if an error occurred using remote
	 *            resources
	 */
	public void removePaymentMethod(FDActionInfo info, ErpPaymentMethodI paymentMethod, boolean isDebitCardSwitch)
			throws FDResourceException, RemoteException;

	public String getDefaultPaymentMethodPK(String fdCustomerId) throws FDResourceException, RemoteException;
	
	public void setDefaultPaymentMethod(FDActionInfo info, String paymentMethodPK, EnumPaymentMethodDefaultType type, boolean isDebitCardSwitch) throws FDResourceException,RemoteException;

	public int resetDefaultPaymentValueType(String fdCustomerId) throws FDResourceException, RemoteException;

}
