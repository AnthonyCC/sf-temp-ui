/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.customer;

import com.freshdirect.framework.core.PrimaryKey;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Customer interface
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-interface
 */
public interface ErpCustomerI {
	/**
	 * @link aggregationByValue
	 * @supplierCardinality 0..*
	 * @clientCardinality 1
	 * @label ship to
	 */
	/*# private ErpAddressModel lnkAddresses; */

	/**
	 * @link aggregationByValue
	 * @supplierCardinality 0..*
	 * @clientCardinality 1
	 */
	/*# private ErpPaymentMethodI lnkCreditCards; */

	/**
	 * @clientCardinality 1
	 * @supplierCardinality 1 
	 */
	/*# ErpCustomerInfoModel lnkCustomerInfo; */

    /**@link aggregationByValue
     * @clientCardinality 1
     * @supplierCardinality 0..**/
    /*#SalesDocumentI lnkErpTransactionGroupI;*/

    /**
     * @clientCardinality 1
     * @supplierCardinality 0..* 
     */
    /*#ErpCreditI lnkErpCreditI;*/

    /**
     * @supplierCardinality 0..*
     * @clientCardinality 1 
     * @directed
     * @link aggregationByValue
     */
    /*#ErpCustomerCreditModel lnkErpCreditModel;*/

	public String getUserId() throws RemoteException;

	public String getSapId() throws RemoteException;

	public void setSapId(String sapId) throws RemoteException;
		
	public boolean isActive() throws RemoteException;
	
	public void setActive(boolean active) throws RemoteException;
		
	/**
	 * Get all ShipToAddresses.
	 *
	 * @return collection of ShipToAddress model objects
	 */
	public List getShipToAddresses() throws RemoteException;

	/**
	 * Add a new ShipToAddress.
	 *
	 * @param element ShipToAddress model object
	 */
	public void addShipToAddress(ErpAddressModel element) throws RemoteException;

	/**
	 * Update an existing ShipToAddress, based on PK.
	 *
	 * @param element ShipToAddress model object, with PK
	 *
	 * @throws CollectionException if the PK was not found.
	 */
	public void updateShipToAddress(ErpAddressModel element) throws RemoteException;

	/**
	 * Remove an existing ShipToAddress by PK.
	 *
	 * @param id ShipToAddress PK to remove
	 *
	 * @return false if not found
	 */
	public boolean removeShipToAddress(PrimaryKey pk) throws RemoteException;
	
	public List getCustomerCredits() throws RemoteException;
	
	public void addCustomerCredit(ErpCustomerCreditModel element) throws RemoteException;
	
	public void updateCustomerCredit(ErpCustomerCreditModel element) throws RemoteException;
	
	public boolean removeCustomerCredit(PrimaryKey pk) throws RemoteException;
	
	public List getPaymentMethods() throws RemoteException;
	
	public void addPaymentMethod(ErpPaymentMethodI element) throws RemoteException;
	
	public void updatePaymentMethod(ErpPaymentMethodI element) throws RemoteException;
	
	public boolean removePaymentMethod(PrimaryKey pk) throws RemoteException;

	public ErpCustomerInfoModel getCustomerInfo() throws RemoteException;
	public void setCustomerInfo(ErpCustomerInfoModel customerInfo) throws RemoteException;

	public List getCustomerAlerts() throws RemoteException;

	public void addCustomerAlert(ErpCustomerAlertModel element) throws RemoteException;

	public void updateCustomerAlert(ErpCustomerAlertModel element) throws RemoteException;

	public boolean removeCustomerAlert(PrimaryKey pk) throws RemoteException;

}

