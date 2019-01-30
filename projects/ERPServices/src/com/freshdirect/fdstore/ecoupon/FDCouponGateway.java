package com.freshdirect.fdstore.ecoupon;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.ecoupon.model.CouponCart;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityContext;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityLogModel;
import com.freshdirect.fdstore.ecoupon.model.FDCouponCustomer;
import com.freshdirect.fdstore.ecoupon.model.FDCouponEligibleInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCouponInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCouponUPCInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCustomerCouponWallet;
import com.freshdirect.fdstore.ecoupon.model.yt.YTCouponCartResponse;
import com.freshdirect.fdstore.ecoupon.model.yt.YTCouponEligibleInfo;
import com.freshdirect.fdstore.ecoupon.model.yt.YTCouponMetaDataResponse;
import com.freshdirect.fdstore.ecoupon.model.yt.YTCouponMetaInfo;
import com.freshdirect.fdstore.ecoupon.model.yt.YTCouponResponse;
import com.freshdirect.fdstore.ecoupon.model.yt.YTCouponResponseErrorCode;
import com.freshdirect.fdstore.ecoupon.model.yt.YTCustomerCouponResponse;
import com.freshdirect.fdstore.ecoupon.service.CouponProviderFactory;
import com.freshdirect.fdstore.ecoupon.service.CouponService;
import com.freshdirect.fdstore.ecoupon.service.CouponServiceCreationException;
import com.freshdirect.fdstore.ecoupon.service.CouponServiceException;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.payment.service.IECommerceService;

public class FDCouponGateway {
	
	private final static Logger LOG = LoggerFactory.getInstance(FDCouponGateway.class);
	
	private final static Object sync = new Object();

	private static CouponService theOnlyService;

	public static CouponService getService() throws CouponServiceCreationException {
		synchronized (sync) {
			if (theOnlyService == null) {
				theOnlyService = CouponProviderFactory.getCouponProvider().newService();
			}
			return theOnlyService;
		}
	}


	public static void resetService() {
		synchronized (sync) {
			try {
				getService().close();
				theOnlyService = null;
				LOG.info("CouponService was reset");
			} catch (CouponServiceCreationException e) {
			}
		}
	}
	
	public static List<FDCouponInfo> getCoupons(FDCouponActivityContext couponActivityContext) throws CouponServiceException{
		List<FDCouponInfo> coupons =new ArrayList<FDCouponInfo>();
		Date startTime = new Date();
		try {
			YTCouponMetaDataResponse response =getService().getCoupons();
			List<YTCouponMetaInfo> ytCoupons = response.getCoupons();		
			translateCouponMetaInfo(coupons, ytCoupons);
		} catch (CouponServiceCreationException e) {
			LOG.error("Exception while getting coupons metadata:"+e.getMessage());
		}
		Date endTime = new Date();
		FDCouponActivityLogModel couponActvity = new FDCouponActivityLogModel();
		couponActvity.setTransType(EnumCouponTransactionType.GET_COUPON_META_DATA);
		setCouponActivityParams(null, startTime, endTime, couponActivityContext,	couponActvity);
		
		logCouponActivity(couponActvity);
		return coupons;		
	}

	
	public static FDCustomerCouponWallet getCouponsForUser(FDCouponCustomer couponCustomer,FDCouponActivityContext couponActivityContext) throws CouponServiceException{
		FDCustomerCouponWallet customerCouponInfo = null;
		Date startTime = new Date();
		YTCustomerCouponResponse response =getService().getCouponsForUser(couponCustomer);
		if(null != response){
			customerCouponInfo= new FDCustomerCouponWallet();
			customerCouponInfo.addAvailableIds(response.getAvailable_ids());
			customerCouponInfo.addClippedActiveIds(response.getClipped_active_ids());
			customerCouponInfo.addClippedRedeemedIds(response.getClipped_redeemed_ids());			
			customerCouponInfo.addClippedPendingIds(response.getClipped_pending_ids());
			customerCouponInfo.addClippedExpiredIds(response.getClipped_expired_ids());			
		}
		Date endTime = new Date();
		FDCouponActivityLogModel couponActvity = new FDCouponActivityLogModel();
		couponActvity.setTransType(EnumCouponTransactionType.GET_CUSTOMER_COUPONS);
		setCouponActivityParams(couponCustomer, startTime, endTime,	couponActivityContext,couponActvity);		
		logCouponActivity(couponActvity);
		
		return customerCouponInfo;
	}	
	
	public static Boolean doClipCoupon(String couponId, FDCouponCustomer couponCustomer,FDCouponActivityContext couponActivityContext) throws CouponServiceException{
		Date startTime = new Date();
		YTCouponResponse response =getService().doClipCoupon(couponId, couponCustomer);
		Boolean isSuccess= false;
		isSuccess =(null !=response && "true".equalsIgnoreCase(response.getResult()));
		Date endTime = new Date();
		FDCouponActivityLogModel couponActvity = new FDCouponActivityLogModel();
		couponActvity.setTransType(EnumCouponTransactionType.CLIP_COUPON);
		couponActvity.setCouponId(couponId);		
		setCouponActivityParams(couponCustomer, startTime, endTime,	couponActivityContext,couponActvity);		
		logCouponActivity(couponActvity);
		
		if(!isSuccess){
			CouponServiceException csException =createCouponServiceException(response);
			//If the error code is '4100'(Coupon already clipped), then don't throw exception.
			if(csException.getMessage()!=null && csException.getMessage().indexOf("4100-")>-1){
				isSuccess=true;
			}else{
				throw csException;
			}
		}
		
		return isSuccess;
	}	
	
	public static Map<String,FDCouponEligibleInfo> evaluateCartAndCoupons(CouponCart couponCart,FDCouponActivityContext couponActivityContext) throws CouponServiceException{
		Date startTime = new Date();
		Map<String,FDCouponEligibleInfo> fdEligibleCoupons =null;
		YTCouponCartResponse response =getService().evaluateCartAndCoupons(couponCart);	
		List<YTCouponEligibleInfo> eligibleCoupons=null;
		Date endTime = new Date();
		FDCouponActivityLogModel couponActvity = new FDCouponActivityLogModel();
		FDCouponCustomer couponCustomer=couponCart.getCouponCustomer();
		couponActvity.setTransType(couponCart.getTranType());
		couponActvity.setSaleId(couponCart.getOrderId());		
		setCouponActivityParams(couponCustomer, startTime, endTime,couponActivityContext,couponActvity);		
		logCouponActivity(couponActvity);	
		
		Boolean isSuccess= false;
		isSuccess =(null !=response && "true".equalsIgnoreCase(response.getResult()));
		if(isSuccess){
			fdEligibleCoupons =new HashMap<String, FDCouponEligibleInfo>();
			eligibleCoupons =response.getApplied_coupons();
			translateEligibleCoupons(fdEligibleCoupons, eligibleCoupons);
		}else{
			CouponServiceException csException =createCouponServiceException(response);
			throw csException;
		}
		return fdEligibleCoupons;
	}


	
	public static Map<String,FDCouponEligibleInfo> submitOrder(CouponCart couponCart,FDCouponActivityContext couponActivityContext) throws CouponServiceException{
		Date startTime = new Date();
		YTCouponCartResponse response =getService().submitOrder(couponCart);
		Map<String,FDCouponEligibleInfo> fdEligibleCoupons =null;
		List<YTCouponEligibleInfo> eligibleCoupons=null;
		Date endTime = new Date();
		FDCouponActivityLogModel couponActvity = new FDCouponActivityLogModel();
		couponActvity.setTransType(couponCart.getTranType());
		FDCouponCustomer couponCustomer=couponCart.getCouponCustomer();
		couponActvity.setSaleId(couponCart.getOrderId());		
		setCouponActivityParams(couponCustomer, startTime, endTime,	couponActivityContext,couponActvity);		
		logCouponActivity(couponActvity);
		
		Boolean isSuccess= false;
		isSuccess =(null !=response && "true".equalsIgnoreCase(response.getResult()));
		if(isSuccess){
			fdEligibleCoupons =new HashMap<String, FDCouponEligibleInfo>();
			eligibleCoupons =response.getApplied_coupons();
			translateEligibleCoupons(fdEligibleCoupons, eligibleCoupons);
		}else{
			CouponServiceException csException =createCouponServiceException(response);
			throw csException;
		}
		return fdEligibleCoupons;
	}
	
	public static boolean cancelOrder(String orderId,FDCouponCustomer couponCustomer,FDCouponActivityContext couponActivityContext) throws CouponServiceException{
		Date startTime = new Date();
		YTCouponResponse response =getService().cancelOrder(orderId);
		Date endTime = new Date();
		Boolean isSuccess= false;
		FDCouponActivityLogModel couponActvity = new FDCouponActivityLogModel();
		couponActvity.setTransType(EnumCouponTransactionType.CANCEL_ORDER);		
		couponActvity.setSaleId(orderId);
		setCouponActivityParams(couponCustomer, startTime, endTime,	couponActivityContext,couponActvity);		
		logCouponActivity(couponActvity);
		
		isSuccess =(null !=response && "true".equalsIgnoreCase(response.getResult()));
		if(!isSuccess){
			CouponServiceException csException =createCouponServiceException(response);
			//If the error code is '330'(Already cancelled), then don't throw exception.
			if(csException.getMessage()!=null && csException.getMessage().toLowerCase().indexOf("330-no matching items in cart")>-1){
				isSuccess=true;
			}else{
				throw csException;
			}
		}
		return isSuccess;
	}


	
	public static boolean confirmOrder(String orderId,FDCouponCustomer couponCustomer,FDCouponActivityContext couponActivityContext,Set<String> coupons) throws CouponServiceException{
		Date startTime = new Date();
		YTCouponResponse response =getService().confirmOrder(orderId,coupons);
		Date endTime = new Date();
		Boolean isSuccess= false;
		FDCouponActivityLogModel couponActvity = new FDCouponActivityLogModel();
		couponActvity.setTransType(EnumCouponTransactionType.CONFIRM_ORDER);
		couponActvity.setSaleId(orderId);
		setCouponActivityParams(couponCustomer, startTime, endTime,couponActivityContext,	couponActvity);		
		logCouponActivity(couponActvity);
		
		isSuccess =(null !=response && "true".equalsIgnoreCase(response.getResult()));
		if(!isSuccess){
			CouponServiceException csException =createCouponServiceException(response);
			//If the error code is '330'(Already redeemed), then don't throw exception.
			if(csException.getMessage()!=null && csException.getMessage().toLowerCase().indexOf("330-no matching items in cart")>-1){
				isSuccess=true;
			}else{
				throw csException;
			}
		}
		return isSuccess;
	}
	

	private static void translateCouponMetaInfo(List<FDCouponInfo> coupons,
			List<YTCouponMetaInfo> ytCoupons) throws CouponServiceException{
		if(null !=ytCoupons){
			for (Iterator<YTCouponMetaInfo> iterator = ytCoupons.iterator(); iterator.hasNext();) {
				YTCouponMetaInfo ytCouponMetaInfo =  iterator.next();
				if(null == EnumCouponOfferType.getEnum(ytCouponMetaInfo.getOffer_type())){
					throw new CouponServiceException("Unsupported coupon offer type:"+ytCouponMetaInfo.getOffer_type()+", for coupon:"+ytCouponMetaInfo.getCoupon_id());
				}
				FDCouponInfo fdCouponInfo = new FDCouponInfo();
				fdCouponInfo.setCouponId(ytCouponMetaInfo.getCoupon_id());
				fdCouponInfo.setManufacturer(ytCouponMetaInfo.getManufacturer());
				fdCouponInfo.setBrandName(ytCouponMetaInfo.getBrand_name());
				fdCouponInfo.setCategory(ytCouponMetaInfo.getCategory());
				fdCouponInfo.setShortDescription(ytCouponMetaInfo.getShort_description());
				fdCouponInfo.setLongDescription(ytCouponMetaInfo.getLong_description());
				fdCouponInfo.setLongDescriptionHeader(ytCouponMetaInfo.getLong_description_header());
				fdCouponInfo.setRequirementDescription(ytCouponMetaInfo.getRequirement_description());
				fdCouponInfo.setRequiredQuantity(ytCouponMetaInfo.getRequirement_quantity());
				try {
					fdCouponInfo.setExpirationDate(DateUtil.parseMDY(ytCouponMetaInfo.getExpiration_date()));
				} catch (ParseException e) {
					
				}
				fdCouponInfo.setValue(ytCouponMetaInfo.getValue());
				fdCouponInfo.setImagePath(ytCouponMetaInfo.getImagePath());
				fdCouponInfo.setNewItem(ytCouponMetaInfo.getNewItem());
				fdCouponInfo.setTags(ytCouponMetaInfo.getTags());
				fdCouponInfo.setOfferPriority(ytCouponMetaInfo.getPriority());
				fdCouponInfo.setOfferType(EnumCouponOfferType.getEnum(ytCouponMetaInfo.getOffer_type()));
				fdCouponInfo.setEnabled(ytCouponMetaInfo.getEnabled());
				if(null !=ytCouponMetaInfo.getRequirement_upcs()){
					List<FDCouponUPCInfo> requiredUpcs = new ArrayList<FDCouponUPCInfo>();
					Set<String> upcs =new HashSet<String>();
					for (Iterator iterator2 = ytCouponMetaInfo.getRequirement_upcs().iterator(); iterator2.hasNext();) {
						String reqUpc = (String) iterator2.next();
						if(!upcs.contains(reqUpc)){
							upcs.add(reqUpc);
							FDCouponUPCInfo fdUpcInfo = new FDCouponUPCInfo();
							fdUpcInfo.setUpc(reqUpc);
							fdUpcInfo.setFdCouponId(fdCouponInfo.getCouponId());
							requiredUpcs.add(fdUpcInfo);
						}
					}
					fdCouponInfo.setRequiredUpcs(requiredUpcs);
				}
				coupons.add(fdCouponInfo);
			}
		}
	}
	
	private static void translateEligibleCoupons(
			Map<String,FDCouponEligibleInfo> fdEligibleCoupons,
			List<YTCouponEligibleInfo> eligibleCoupons) {
		if(null !=eligibleCoupons){
			for (Iterator<YTCouponEligibleInfo> iterator = eligibleCoupons.iterator(); iterator
					.hasNext();) {
				YTCouponEligibleInfo ytCouponEligibleInfo =  iterator.next();
				FDCouponEligibleInfo fdCouponEligibleInfo = new FDCouponEligibleInfo();
				fdCouponEligibleInfo.setCouponId(ytCouponEligibleInfo.getCoupon_id());
				fdCouponEligibleInfo.setDiscount(ytCouponEligibleInfo.getDiscount());
				fdCouponEligibleInfo.setDiscountedUpcs(ytCouponEligibleInfo.getDiscounted_upcs());					
				
				fdEligibleCoupons.put(fdCouponEligibleInfo.getCouponId(),fdCouponEligibleInfo);
			}
		}
	}
	
	private static CouponServiceException createCouponServiceException(
			YTCouponResponse response)  {
		String errorMessage="";
		String details="";
		if(null !=response && null !=response.getCodes() && !response.getCodes().isEmpty()){
			YTCouponResponseErrorCode errorCode =response.getCodes().get(0);
			errorMessage= errorCode.getCode()+"-"+errorCode.getText();
			details=errorCode.getDetails();
		}
		CouponServiceException csException =new CouponServiceException(errorMessage,details);
		return csException;
	}
	

	private static void setCouponActivityParams(
			FDCouponCustomer couponCustomer, Date startTime, Date endTime,FDCouponActivityContext couponActivityContext,
			FDCouponActivityLogModel couponActvity) {
		couponActvity.setStartTime(startTime);
		couponActvity.setEndTime(endTime);
		if(null !=couponCustomer){
			couponActvity.setCustomerId(couponCustomer.getCouponCustomerId());
			couponActvity.setFdUserId(couponCustomer.getCouponUserId());
		}
		if(null !=couponActivityContext){
			couponActvity.setSource(couponActivityContext.getSrc());
			couponActvity.setInitiator(null !=couponActivityContext.getAgent()?couponActivityContext.getAgent().getLdapId():couponActivityContext.getInitiator());			
		}
	}
	


	private static void logCouponActivity(FDCouponActivityLogModel couponActvityLog) {
		try {
				IECommerceService commerceService =   FDECommerceService.getInstance();
				commerceService.logCouponActivity(couponActvityLog);
			
		} catch (FDResourceException e) {
			LOG.info("Coupon activity logging failed. "+e);
		} catch (RemoteException e) {
			LOG.info("Coupon activity logging failed. "+e);
		}
	}
	
}
