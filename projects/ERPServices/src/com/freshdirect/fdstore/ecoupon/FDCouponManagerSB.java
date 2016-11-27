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

public interface FDCouponManagerSB extends EJBObject{
	
	public void loadAndSaveCoupons( FDCouponActivityContext context) throws FDResourceException, CouponServiceException, RemoteException;
	public List<FDCouponInfo> getActiveCoupons() throws FDResourceException, RemoteException;
	public List<FDCouponInfo> getActiveCoupons(Date lastModified) throws FDResourceException, RemoteException;
	public FDCustomerCouponWallet getCouponsForUser(FDCouponCustomer couponCustomer, FDCouponActivityContext context) throws CouponServiceException, RemoteException;	
	public boolean doClipCoupon(String couponId, FDCouponCustomer couponCustomer, FDCouponActivityContext context) throws CouponServiceException,RemoteException;
	public Map<String, FDCouponEligibleInfo> evaluateCartAndCoupons(CouponCart couponCart, FDCouponActivityContext context) throws CouponServiceException,RemoteException;
	/*public Map<String, FDCouponEligibleInfo> submitOrder(CouponCart couponCart)throws CouponServiceException,RemoteException;
	public boolean confirmOrder(String orderId,FDCouponCustomer couponCustomer)throws CouponServiceException,RemoteException;
	public boolean cancelOrder(String orderId,FDCouponCustomer couponCustomer)throws CouponServiceException,RemoteException;*/
	public List<FDCouponInfo> loadCoupons(FDCouponActivityContext couponActivityContext) throws FDResourceException, CouponServiceException, RemoteException;
	public List<FDCouponInfo> getCouponsForCRMSearch(String searchTerm) throws FDResourceException,RemoteException;
	public void postSubmitPendingCouponTransactions() throws FDResourceException,RemoteException;
	public void postCancelPendingCouponTransactions() throws FDResourceException,RemoteException;
	public void postConfirmPendingCouponTransactions() throws FDResourceException,RemoteException;
	public void postCouponOrder(ErpCouponTransactionModel couponTransModel, FDCouponActivityContext context) throws FDResourceException,RemoteException;
	public int getMaxCouponsVersion() throws FDResourceException, RemoteException;
	public List<FDCustomerCouponHistoryInfo> getCustomersCouponHistoryInfo(String customerId) throws FDResourceException,RemoteException;
	public ErpCouponTransactionModel getConfirmPendingCouponTransaction(String saleId) throws FDResourceException,RemoteException;
	public void updateCouponTransaction(ErpCouponTransactionModel transModel) throws FDResourceException,RemoteException;
	public void postConfirmPendingCouponTransactions(String saleId) throws FDResourceException,RemoteException;
	public List<String> getConfirmPendingCouponSales() throws FDResourceException, RemoteException;
	public List<String> getSubmitPendingCouponSales() throws FDResourceException, RemoteException;
	public void postSubmitPendingCouponTransactions(String saleId) throws FDResourceException,RemoteException;
}
