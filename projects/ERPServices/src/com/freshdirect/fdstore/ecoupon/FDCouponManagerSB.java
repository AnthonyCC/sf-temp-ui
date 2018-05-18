package com.freshdirect.fdstore.ecoupon;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.ecoupon.model.CouponCart;
import com.freshdirect.fdstore.ecoupon.model.ErpCouponTransactionModel;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityContext;
import com.freshdirect.fdstore.ecoupon.model.FDCouponCustomer;
import com.freshdirect.fdstore.ecoupon.model.FDCouponEligibleInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCouponInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCustomerCouponHistoryInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCustomerCouponWallet;
import com.freshdirect.fdstore.ecoupon.service.CouponServiceException;

/**
 * 
 * @deprecated
 *
 */
public interface FDCouponManagerSB extends EJBObject{
	@Deprecated
	public void loadAndSaveCoupons( FDCouponActivityContext context) throws FDResourceException, CouponServiceException, RemoteException;
	@Deprecated
	public List<FDCouponInfo> getActiveCoupons() throws FDResourceException, RemoteException;
	@Deprecated
	public List<FDCouponInfo> getActiveCoupons(Date lastModified) throws FDResourceException, RemoteException;
	@Deprecated
	public FDCustomerCouponWallet getCouponsForUser(FDCouponCustomer couponCustomer, FDCouponActivityContext context) throws CouponServiceException, RemoteException;	
	@Deprecated
	public boolean doClipCoupon(String couponId, FDCouponCustomer couponCustomer, FDCouponActivityContext context) throws CouponServiceException,RemoteException;
	@Deprecated
	public Map<String, FDCouponEligibleInfo> evaluateCartAndCoupons(CouponCart couponCart, FDCouponActivityContext context) throws CouponServiceException,RemoteException;
	@Deprecated
	public void postSubmitPendingCouponTransactions() throws FDResourceException,RemoteException;
	@Deprecated
	public void postCancelPendingCouponTransactions() throws FDResourceException,RemoteException;
	@Deprecated
	public void postConfirmPendingCouponTransactions() throws FDResourceException,RemoteException;
	@Deprecated
	public void postCouponOrder(ErpCouponTransactionModel couponTransModel, FDCouponActivityContext context) throws FDResourceException,RemoteException;
	@Deprecated
	public void postConfirmPendingCouponTransactions(String saleId) throws FDResourceException,RemoteException;
	@Deprecated
	public List<String> getConfirmPendingCouponSales() throws FDResourceException, RemoteException;
	@Deprecated
	public List<String> getSubmitPendingCouponSales() throws FDResourceException, RemoteException;
	@Deprecated
	public void postSubmitPendingCouponTransactions(String saleId) throws FDResourceException,RemoteException;
}
