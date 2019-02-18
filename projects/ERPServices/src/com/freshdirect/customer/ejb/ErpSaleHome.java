package com.freshdirect.customer.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpCreateOrderModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.payment.EnumPaymentMethodType;

/**
 * ErpSale entity home interface.
 * @version    $Revision$
 * @author     $Author$
 */
public interface ErpSaleHome extends EJBHome {

    /**
     * Create sales with a create order transcation. Status will be NEW.
     */
	public ErpSaleEB create(PrimaryKey customerPk, ErpCreateOrderModel createOrder, Set<String> usedPromotionCodes, String dlvPassId,EnumSaleType type) throws CreateException, RemoteException;

	public ErpSaleEB findByPrimaryKey(PrimaryKey pk) throws FinderException, RemoteException;
	
	public Collection<ErpSaleEB> findMultipleByPrimaryKeys(Collection<PrimaryKey> pks) throws FinderException, RemoteException;
	
	public ErpSaleEB findByComplaintId(String complaintId) throws FinderException, RemoteException;

	public Collection<PrimaryKey> findByStatus(EnumSaleStatus status) throws FinderException, RemoteException;

	public Collection<PrimaryKey> findByDeliveryPassId(String dlvPassId) throws FinderException, RemoteException;
	
	public ErpSaleEB findByCriteria(String customerID, EnumSaleType saleType, EnumSaleStatus saleStatus, EnumPaymentMethodType pymtMethodType) throws FinderException, RemoteException;
	
	public ErpSaleEB findByCriteria(String customerID,	EnumSaleType saleType, EnumSaleStatus saleStatus, List<EnumPaymentMethodType> pymtMethodTypes) throws FinderException, RemoteException;
	
}