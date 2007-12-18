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
import java.util.Collection;
import java.util.List;

/**
 * ErpTransactionGroup interface
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-interface
 */
public interface ErpSaleI {
	/**
	 * @link aggregationByValue
	 * @supplierCardinality 0..*
	 * @clientCardinality 1
	 */
	/*# private ErpChangeOrderModel lnkChangeOrders; */

	/**
	 * @clientCardinality 1
	 * @supplierCardinality 1
	 */
    /*#ErpCreateOrderModel lnkErpCreateOrderI;*/

	/**
	 * @clientCardinality 1
	 * @supplierCardinality 0..1
	 */
    /*#ErpCancelOrderModel lnkErpCancelOrderI;*/

    /**@link aggregationByValue
     * @clientCardinality 1
     * @supplierCardinality 0..**/
    /*#ErpInvoiceModel lnkErpInvoiceI;*/

    /**
     * @link aggregationByValue
     * @clientCardinality 1
     * @supplierCardinality 0..*
     */
    /*#ErpReturnModel lnkErpReturnI;*/

    /**
     * @link aggregationByValue
     * @clientCardinality 1
     * @supplierCardinality 0..*
     */
    /*#ErpPaymentModel lnkErpPaymentI;*/

    /**
     * @return PK of the ErpCustomer this sale is for
     */
    public PrimaryKey getCustomerPk() throws RemoteException;

    /**
     * @return the status of the order
     */
	public EnumSaleStatus getStatus() throws RemoteException;

    /**
     * @return order number in SAP, or null if it's not in SAP yet
     */
    public String getSapOrderNumber() throws RemoteException;

	/**
     * @return collection of ErpTransactionModel objects
     */
    public Collection getTransactions() throws RemoteException;

	/**
     * @return collection of ErpComplaintModel objects
     */
    public Collection getComplaints() throws RemoteException;

	/**
     * @return collection of ErpAuthorizationModel objects
     */
    public List getFailedAuthorizations() throws RemoteException;
}

