package com.freshdirect.fdstore.ecoupon;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.ecoupon.model.CouponCart;
import com.freshdirect.fdstore.ecoupon.model.ErpCouponTransactionModel;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityContext;
import com.freshdirect.fdstore.ecoupon.model.FDCouponCustomer;
import com.freshdirect.fdstore.ecoupon.model.FDCouponEligibleInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCouponInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCustomerCouponHistoryInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCustomerCouponWallet;
import com.freshdirect.fdstore.ecoupon.service.CouponServiceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;

public class FDCouponManager {

	private static Category LOGGER = LoggerFactory.getInstance(FDCouponManager.class);

	private static FDCouponManagerHome managerHome = null;
	
	public static void loadAndSaveCoupons(FDCouponActivityContext couponActivityContext) throws FDResourceException,CouponServiceException{
		
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCouponManagerSB)) {
				FDECommerceService.getInstance().loadAndSaveCoupons(couponActivityContext);
			} else {
				lookupManagerHome();
				FDCouponManagerSB sb = managerHome.create();
				sb.loadAndSaveCoupons(couponActivityContext);
			}
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}
	
	public static List<FDCouponInfo> getActiveCoupons() throws FDResourceException{
		
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCouponManagerSB)) {
				return FDECommerceService.getInstance().getActiveCoupons();
			} else {
				lookupManagerHome();
				FDCouponManagerSB sb = managerHome.create();
				return sb.getActiveCoupons();
			}
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}	
	
	public static List<FDCouponInfo> getActiveCoupons(Date lastModified) throws FDResourceException{
		
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCouponManagerSB)) {
				return FDECommerceService.getInstance().getActiveCoupons(lastModified);
			} else {
				lookupManagerHome();
				FDCouponManagerSB sb = managerHome.create();
				return sb.getActiveCoupons(lastModified);
			}
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}
	
	public static FDCustomerCouponWallet getCouponsForUser(FDCouponCustomer couponCustomer,FDCouponActivityContext couponActivityContext) throws FDResourceException,CouponServiceException{
		
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCouponManagerSB)) {
				return FDECommerceService.getInstance().getCouponsForUser(couponCustomer, couponActivityContext);
			} else {
				lookupManagerHome();
				FDCouponManagerSB sb = managerHome.create();
				return sb.getCouponsForUser(couponCustomer, couponActivityContext);
			}
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}
	
	public static boolean doClipCoupon(String couponId, FDCouponCustomer couponCustomer,FDCouponActivityContext couponActivityContext) throws FDResourceException,CouponServiceException {
		
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCouponManagerSB)) {
				return FDECommerceService.getInstance().doClipCoupon(couponId, couponCustomer, couponActivityContext);
			} else {
				lookupManagerHome();
				FDCouponManagerSB sb = managerHome.create();
				return sb.doClipCoupon(couponId, couponCustomer, couponActivityContext);
			}
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}
	
	public static Map<String, FDCouponEligibleInfo> evaluateCartAndCoupons(CouponCart couponCart,FDCouponActivityContext couponActivityContext) throws FDResourceException,CouponServiceException{
		
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCouponManagerSB)) {
				return FDECommerceService.getInstance().evaluateCartAndCoupons(couponCart, couponActivityContext);
			} else {
				lookupManagerHome();
				FDCouponManagerSB sb = managerHome.create();
				return sb.evaluateCartAndCoupons(couponCart, couponActivityContext);
			}
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}
	
	/*public static Map<String,FDCouponEligibleInfo> submitOrder(CouponCart couponCart)throws FDResourceException,CouponServiceException{
		lookupManagerHome();
		
		try {
			FDCouponManagerSB sb = managerHome.create();
			return sb.submitOrder(couponCart); 
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}
	public static boolean confirmOrder(String orderId,FDCouponCustomer couponCustomer)throws FDResourceException,CouponServiceException{
		lookupManagerHome();
		
		try {
			FDCouponManagerSB sb = managerHome.create();
			return sb.confirmOrder(orderId,couponCustomer); 
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}
	public static boolean cancelOrder(String orderId,FDCouponCustomer couponCustomer)throws FDResourceException,CouponServiceException{
		lookupManagerHome();
		
		try {
			FDCouponManagerSB sb = managerHome.create();
			return sb.cancelOrder(orderId,couponCustomer); 
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}*/
	
	
	private static void lookupManagerHome() throws FDResourceException {
		if (managerHome != null) {
			return;
		}
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			managerHome = (FDCouponManagerHome) ctx.lookup(FDStoreProperties.getFDCouponManagerHome());
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (NamingException ne) {
				LOGGER.warn("Cannot close Context while trying to cleanup", ne);
			}
		}
	}
	
	private static void invalidateManagerHome() {
		managerHome = null;
	}
	
	public static void postCouponOrder(ErpCouponTransactionModel couponTransModel,FDCouponActivityContext couponActivityContext) throws FDResourceException{
		
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCouponManagerSB)) {
				FDECommerceService.getInstance().postCouponOrder(couponTransModel, couponActivityContext);
			} else {
				lookupManagerHome();
				FDCouponManagerSB sb = managerHome.create();
				sb.postCouponOrder(couponTransModel, couponActivityContext);
			}
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}
	
	public static ErpCouponTransactionModel getConfirmPendingCouponTransaction(String saleId) throws FDResourceException{
		
		try {
			lookupManagerHome();
			FDCouponManagerSB sb = managerHome.create();
			return sb.getConfirmPendingCouponTransaction(saleId);
			
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}
	
	public static void updateCouponTransaction(ErpCouponTransactionModel transModel) throws FDResourceException{
		
		try {
			
			lookupManagerHome();
			FDCouponManagerSB sb = managerHome.create();
			sb.updateCouponTransaction(transModel);
			
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}
	
	public static void postConfirmPendingCouponTransactions(String saleId) throws FDResourceException{
		
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCouponManagerSB)) {
				FDECommerceService.getInstance().postConfirmPendingCouponTransactions(saleId);
			} else {
				lookupManagerHome();
				FDCouponManagerSB sb = managerHome.create();
				sb.postConfirmPendingCouponTransactions(saleId);
			}
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}
}
