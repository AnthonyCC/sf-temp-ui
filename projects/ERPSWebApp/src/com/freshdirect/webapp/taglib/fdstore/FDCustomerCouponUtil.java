package com.freshdirect.webapp.taglib.fdstore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.customer.ErpCouponDiscountLineModel;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDModifyCartLineI;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDOrderTranslator;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.fdstore.ecoupon.EnumCouponDisplayStatus;
import com.freshdirect.fdstore.ecoupon.EnumCouponOfferType;
import com.freshdirect.fdstore.ecoupon.EnumCouponStatus;
import com.freshdirect.fdstore.ecoupon.EnumCouponTransactionType;
import com.freshdirect.fdstore.ecoupon.FDCouponFactory;
import com.freshdirect.fdstore.ecoupon.FDCouponManager;
import com.freshdirect.fdstore.ecoupon.FDCustomerCoupon;
import com.freshdirect.fdstore.ecoupon.model.CouponCart;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityContext;
import com.freshdirect.fdstore.ecoupon.model.FDCouponCustomer;
import com.freshdirect.fdstore.ecoupon.model.FDCouponEligibleInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCouponInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCustomerCouponWallet;
import com.freshdirect.fdstore.ecoupon.service.CouponServiceException;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentKeyFactory;
import com.freshdirect.storeapi.content.EnumSortingValue;
import com.freshdirect.storeapi.content.FilteringSortingItem;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.Recipe;
import com.freshdirect.storeapi.content.SearchResults;

public class FDCustomerCouponUtil implements Serializable {

	private static final long	serialVersionUID	= 7258375070671798676L;

	private static final Logger LOGGER = LoggerFactory.getInstance(FDCustomerCouponUtil.class);

	public static void initCustomerCoupons(HttpSession session){
		initCustomerCoupons(session,null);
	}

	public static void initCustomerCoupons(HttpSession session,String oldUserId){
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		if(null !=user && user.isEligibleForCoupons()){
			FDCustomerCouponWallet couponWallet =FDCustomerCouponUtil.getCustomerCoupons(session,oldUserId);
			user.setCouponWallet(couponWallet);
			//Evaluate Coupons, after adding/modifying the orderlines.
			FDCustomerCouponUtil.evaluateCartAndCoupons(session);
		}
	}

	public static FDCustomerCouponWallet getCustomerCoupons(HttpSession session,String oldUserId){
		FDCustomerCouponWallet couponWallet =new FDCustomerCouponWallet();
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		if(null !=user && user.isEligibleForCoupons()){
			try {
				FDCouponCustomer request = createCouponCustomerRequest(user,oldUserId);
				FDCouponActivityContext couponActivityContext =AccountActivityUtil.getCouponActivityContext(session);
				couponWallet=FDCouponManager.getCouponsForUser(request,couponActivityContext);
				if(null!=oldUserId){
					//get customer coupons again, after the merge, to get the latest & correct coupons.
					getCustomerCoupons(session,null);
				}
				user.setCouponWallet(couponWallet);
			} catch (FDResourceException e) {
				LOGGER.info("getCustomerCoupons failed:"+e);
			} catch (CouponServiceException cse){
				LOGGER.info("getCustomerCoupons failed:"+cse);
			}
		}
		return couponWallet;
	}

	public static FDCustomerCouponWallet getCustomerCoupons(HttpSession session){
		return 	getCustomerCoupons(session,null);
	}

	private static FDCouponCustomer createCouponCustomerRequest(
			FDUserI user) throws FDResourceException {
		return createCouponCustomerRequest(user,null);
	}

	private static FDCouponCustomer createCouponCustomerRequest(
			FDUserI user,String oldUserId) throws FDResourceException {
		FDCouponCustomer request = new FDCouponCustomer();
		if(user.getIdentity() != null && null != user.getFDCustomer()){
			request.setCouponCustomerId(user.getFDCustomer().getErpCustomerPK());
		}else{
			request.setZipCode(user.getZipCode());
		}
		if(null !=oldUserId){
			request.setCouponUserId(oldUserId);
		}else{
			request.setCouponUserId(user.getPrimaryKey());
		}
		return request;
	}

	public static boolean clipCoupon(HttpSession session,String couponId){
		boolean isSuccess = false;
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		try {
			if(null !=user && user.isEligibleForCoupons()){
				FDCouponCustomer request = createCouponCustomerRequest(user);
				FDCouponActivityContext couponActivityContext =AccountActivityUtil.getCouponActivityContext(session);
				isSuccess =FDCouponManager.doClipCoupon(couponId, request,couponActivityContext);
				if(isSuccess){
					user.updateClippedCoupon(couponId);
					user.setCouponEvaluationRequired(true);
				}
			}
		} catch (FDResourceException e) {
			LOGGER.info("Clip failed. Coupon Id:"+couponId+" .Exception -"+e);
		} catch (CouponServiceException cse){
			LOGGER.info("Clip failed. Coupon Id:"+couponId+" .Exception -"+cse);
		}
		return isSuccess;
	}

	public static boolean evaluateCartAndCoupons(HttpSession session,boolean filterCoupons){
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		boolean isCouponEvaluationNeeded = false;
		try {
			if(null !=user && user.isEligibleForCoupons()){
				//LOGGER.debug("isEligibleForCoupons:"+true);
				if(user.isRefreshCouponWalletRequired()){
					getCustomerCoupons(session);
				}

				clearAndRefreshCouponCartlines(user);

				FDCustomerCouponWallet custCoupons =user.getCouponWallet();

				if(null ==custCoupons){
					//LOGGER.debug("isCouponWalletEmpty:"+true);
					return true;//Nothing to evaluate
				}else{
					//Clear this map always before evaluating, to make sure the map will always have the entries for the clipped coupons of just the current cart lines .
					custCoupons.getClippedMinQtyNotMetIds().clear();
					custCoupons.getClippedFdFilteredIds().clear();
				}

				Set<String> appliedCoupons = new HashSet<String>();
				//Nothing to evaluate, if the cart is empty OR if the customer doesn't have clipped coupons OR if the cart doesn't have at least one line-item which has a customer clipped coupon.
				boolean isClippedOrderLinesAvailable=checkClippedOrderLines(custCoupons, user.getShoppingCart());
				//LOGGER.debug("isClippedOrderLinesAvailable:"+isClippedOrderLinesAvailable);
				if(isClippedOrderLinesAvailable){

					List<ErpOrderLineModel> orderLines = new ArrayList<ErpOrderLineModel>();
					FDOrderTranslator.translateOrderLines(user.getShoppingCart(), false, false, orderLines);
					FDCouponCustomer couponCustomer = createCouponCustomerRequest(user);
					FDCouponActivityContext couponActivityContext =AccountActivityUtil.getCouponActivityContext(session);

					Set<String> originalOrderCoupons = null;
					if(user.getShoppingCart() instanceof FDModifyCartModel){
						 FDModifyCartModel cart =(FDModifyCartModel)user.getShoppingCart() ;
						 originalOrderCoupons = cart.getOriginalOrderCoupons();
					}

					CouponCart couponCart = prepareCouponCart(user, orderLines,couponCustomer);

					Map<String, Double> couponDiscAmtMap = new HashMap<String,Double>();
					//LOGGER.debug("Before the evaluateCartAndCoupons call:");
					Map<String, FDCouponEligibleInfo> eligibleCouponsInfo =FDCouponManager.evaluateCartAndCoupons(couponCart,couponActivityContext);
					//LOGGER.debug("After the evaluateCartAndCoupons call:"+(null !=eligibleCouponsInfo?eligibleCouponsInfo.toString():eligibleCouponsInfo));
					if(null != eligibleCouponsInfo){
						List<FDCartLineI> fdSortedOrderlines = new ArrayList<FDCartLineI>(user.getShoppingCart().getOrderLines());
						Collections.sort(fdSortedOrderlines, new PriceComparator());
						for (Iterator<FDCartLineI> iterator = fdSortedOrderlines.iterator(); iterator.hasNext();) {
							FDCartLineI fdCartLine = iterator.next();
							String cartLineUpc= fdCartLine.getUpc();
							FDCouponInfo couponInfo =  getCouponInfo(custCoupons, fdCartLine);
							if(null !=couponInfo){
								if((custCoupons.isClipped(couponInfo.getCouponId())||(null !=originalOrderCoupons && originalOrderCoupons.contains(couponInfo.getCouponId()))) && !eligibleCouponsInfo.containsKey(couponInfo.getCouponId())){
									//Coupon is not applied to this line item because-either min.qty was not met Or coupon is expired recently.

									if(custCoupons.isExpired(couponInfo.getCouponId())){
										fdCartLine.setCouponStatus(EnumCouponStatus.COUPON_CLIPPED_EXPIRED);
									} else {
										fdCartLine.setCouponStatus(EnumCouponStatus.COUPON_MIN_QTY_NOT_MET);
										custCoupons.getClippedMinQtyNotMetIds().add(couponInfo.getCouponId());
									}
								}else{
									FDCouponEligibleInfo fdCouponEligibleInfo=eligibleCouponsInfo.get(couponInfo.getCouponId());
									if(null!=fdCouponEligibleInfo && null !=fdCouponEligibleInfo.getDiscountedUpcs()){
										if(!filterCoupons ||isCouponValidForTimeslot(couponInfo,user)){
											fdCartLine.setCouponApplied(true);
											 if(fdCouponEligibleInfo.getDiscountedUpcs().contains(StringUtil.convertToEan13(cartLineUpc))){
												 applyCouponDiscount(custCoupons,couponDiscAmtMap,fdCartLine, couponInfo,fdCouponEligibleInfo,user.getShoppingCart(),appliedCoupons);
											 }
										}else{
											isCouponEvaluationNeeded =true;
											custCoupons.getClippedFdFilteredIds().add(couponInfo.getCouponId());
										}
										if(user.getShoppingCart() instanceof FDModifyCartModel && !isCouponValidForTimeslot(couponInfo,user)){
											custCoupons.getClippedFdFilteredIds().add(couponInfo.getCouponId());
										}
									}
								}
							}
						}
					}

					//set the lowest possible delivery date to get all the coupons in the cart.
					if(filterCoupons && !custCoupons.getClippedFdFilteredIds().isEmpty()){
						setExpCouponMaxDeliveryDate(user, custCoupons);
					}
				}

				//Clear and update the latest applied coupons.
				custCoupons.getClippedAppliedIds().clear();
				custCoupons.getClippedAppliedIds().addAll(appliedCoupons);

			}

		} catch (FDResourceException e) {
			LOGGER.info("Exception in evaluateCartAndCoupons() "+e);
			return false;
		} catch (CouponServiceException cse){
			LOGGER.info("Exception in evaluateCartAndCoupons() "+cse);
			return false;
		} finally{
			if ( user != null ) {
				user.setCouponEvaluationRequired(isCouponEvaluationNeeded);
				user.setRefreshCouponWalletRequired(false);
			}
		}
		return true;

	}

	private static void setExpCouponMaxDeliveryDate(FDSessionUser user,
			FDCustomerCouponWallet custCoupons) {
		Set<String> filteredCouponIds =custCoupons.getClippedFdFilteredIds();
		Date expCouponMaxDeliveryDate = null;
		for (Iterator<String> iterator = filteredCouponIds.iterator(); iterator.hasNext();) {
			String coupId = iterator.next();
			FDCouponInfo coupon = FDCouponFactory.getInstance().getCoupon(coupId);
			if(null !=coupon){
				if(null == expCouponMaxDeliveryDate || coupon.getFdExpirationDate().before(expCouponMaxDeliveryDate)){
					expCouponMaxDeliveryDate = coupon.getFdExpirationDate();
				}
			}
		}
		String maxDlvDate = null !=expCouponMaxDeliveryDate? DateUtil.formatDate(expCouponMaxDeliveryDate):null;
		user.getShoppingCart().setExpCouponDeliveryDate(maxDlvDate);
	}

	public static boolean evaluateCartAndCoupons(HttpSession session){
		return evaluateCartAndCoupons(session,false);
	}
	private static CouponCart prepareCouponCart(FDSessionUser user,
			List<ErpOrderLineModel> orderLines, FDCouponCustomer couponCustomer) {
		CouponCart couponCart =null;
		if(user.getShoppingCart() instanceof FDModifyCartModel){
			EnumCouponTransactionType transType= EnumCouponTransactionType.PREVIEW_MODIFY_ORDER;
			FDModifyCartModel cart =(FDModifyCartModel)user.getShoppingCart() ;
			couponCart =new CouponCart(couponCustomer,orderLines,cart.getOriginalOrder().getErpSalesId() , transType);
		}else{
			EnumCouponTransactionType transType= EnumCouponTransactionType.PREVIEW_CREATE_ORDER;
			couponCart =new CouponCart(couponCustomer,orderLines,transType);
		}
		return couponCart;
	}

	private static FDCouponInfo getCouponInfo(
			FDCustomerCouponWallet custCoupons, FDCartLineI fdCartLine) {
		FDCouponInfo couponInfo =FDCouponFactory.getInstance().getCouponByUpc(fdCartLine.getUpc());
		if(fdCartLine instanceof FDModifyCartLineI){
			FDCartLineI originalOrderLine = ((FDModifyCartLineI)fdCartLine).getOriginalOrderLine();
			ErpCouponDiscountLineModel couponDiscount =originalOrderLine.getCouponDiscount();
			if(null!=couponDiscount && fdCartLine.getUpc().equalsIgnoreCase(originalOrderLine.getUpc()) && (!custCoupons.isExpired(couponDiscount.getCouponId())||(custCoupons.isExpired(couponDiscount.getCouponId()) && null ==couponInfo))){
				couponInfo = FDCouponFactory.getInstance().getCoupon(originalOrderLine.getCouponDiscount().getCouponId());
			}
		}
		return couponInfo;
	}

	private static void applyCouponDiscount(FDCustomerCouponWallet custCoupons,
			Map<String, Double> couponDiscAmtMap, FDCartLineI fdCartLine,
			FDCouponInfo couponInfo, FDCouponEligibleInfo fdCouponEligibleInfo,FDCartModel cart,Set<String> appliedCoupons)
			throws FDResourceException {

		//1. For buy any, if multiple line items independently eligible for the coupon,
		//            if the price matches for multiple line items, apply to first line item, otherwise, apply coupon to the highest priced item.
		//2. For buy 2 more, if combined line items together meets the coupon quantity, split the coupon discount across all those eligible line items.
//		fdCartLine.setCouponApplied(true);
		if(!custCoupons.getClippedAppliedIds().contains(couponInfo.getCouponId())){
			cart.getRecentlyAppliedCoupons().add(couponInfo.getCouponId());
		}
		appliedCoupons.add(couponInfo.getCouponId());
		if(null==couponDiscAmtMap.get(couponInfo.getCouponId())){
			couponDiscAmtMap.put(couponInfo.getCouponId(), Double.parseDouble(fdCouponEligibleInfo.getDiscount()));
		}
		Double remCoupDiscAmt =couponDiscAmtMap.get(couponInfo.getCouponId());
		if(remCoupDiscAmt>0){
			ErpCouponDiscountLineModel couponDiscount=null;
			EnumCouponOfferType couponOfferType = couponInfo.getOfferType();
			String couponDesc= couponInfo.getShortDescription();//+ ". Coupon expires on: "+couponInfo.getExpirationDate();
			if(fdCartLine.getPrice()>=remCoupDiscAmt){
				couponDiscount =new ErpCouponDiscountLineModel(couponInfo.getCouponId(),remCoupDiscAmt,couponInfo.getVersion(),Integer.parseInt(couponInfo.getRequiredQuantity()),couponDesc,couponOfferType);
				couponDiscAmtMap.put(couponInfo.getCouponId(),new Double(0));
			}else{
				Double discAmt =fdCartLine.getPrice();
				couponDiscount =new ErpCouponDiscountLineModel(couponInfo.getCouponId(),discAmt,couponInfo.getVersion(),Integer.parseInt(couponInfo.getRequiredQuantity()),couponDesc,couponOfferType);
				couponDiscAmtMap.put(couponInfo.getCouponId(),remCoupDiscAmt-discAmt);//Balance coupon amount to be discounted.
			}
			fdCartLine.setCouponDiscount(couponDiscount);
			fdCartLine.setCouponStatus(EnumCouponStatus.COUPON_APPLIED);
			try {
				fdCartLine.refreshConfiguration();
			} catch (FDInvalidConfigurationException e) {
				LOGGER.info("CartLine refresh configuration failed:"+e);
			}
			custCoupons.getClippedMinQtyNotMetIds().remove(couponInfo.getCouponId());
			custCoupons.getClippedFdFilteredIds().remove(couponInfo.getCouponId());
		}
	}

	private static void clearAndRefreshCouponCartlines(FDSessionUser user)
			throws FDResourceException {
		List<FDCartLineI> fdOrderLines;
		fdOrderLines =user.getShoppingCart().getOrderLines();
		user.getShoppingCart().setExpCouponDeliveryDate(null);
		user.getShoppingCart().getRecentlyAppliedCoupons().clear();
		for (Iterator<FDCartLineI> iterator = fdOrderLines.iterator(); iterator.hasNext();) {
			FDCartLineI fdCartLine = iterator.next();
			fdCartLine.setCouponStatus(null);//clear 'minimum qty not met'/expired message.
			fdCartLine.setCouponApplied(false);
			if(null !=fdCartLine.getCouponDiscount()){
				fdCartLine.clearCouponDiscount();
				try {
					fdCartLine.refreshConfiguration();
				} catch (FDInvalidConfigurationException e) {
					LOGGER.info("CartLine refresh configuration failed:"+e);
				}
			}
		}
	}

	/**
	 * Method to check whether the cart has at least one line-item which has a customer clipped coupon.
	 *
	 * @param custCoupons
	 * @param fdOrderLines
	 * @return
	 */
	private static boolean checkClippedOrderLines(
			FDCustomerCouponWallet custCoupons, FDCartI cart) {
		boolean isClippedOrderLinesAvailable=false;
		List<FDCartLineI> fdOrderLines = cart.getOrderLines();
		Set<String> originalOrderCoupons = null;
		if(cart instanceof FDModifyCartModel){
			originalOrderCoupons = ((FDModifyCartModel)cart).getOriginalOrderCoupons();
		}
		if(null!= fdOrderLines && !fdOrderLines.isEmpty() && null!=custCoupons ){
			for (Iterator<FDCartLineI> iterator = fdOrderLines.iterator(); iterator.hasNext();) {
				FDCartLineI fdCartLineI = iterator.next();
				FDCouponInfo coupon = getCouponInfo(custCoupons, fdCartLineI);

				if(null !=coupon && (custCoupons.getClippedActiveIds().contains(coupon.getCouponId()) || custCoupons.getClippedPendingIds().contains(coupon.getCouponId()) ||(null !=originalOrderCoupons && originalOrderCoupons.contains(coupon.getCouponId())))){
					isClippedOrderLinesAvailable= true;
					break;
				}

				}
			}

		return isClippedOrderLinesAvailable;
	}

	public static List<FDCartLineI> getOrderLinesByUpcs(List<FDCartLineI> fdOrderLines, List<String> upcs){
		List<FDCartLineI> upcOrderLines=new ArrayList<FDCartLineI>();
		if(null !=fdOrderLines && null !=upcs){
			for (Iterator<FDCartLineI> iterator = fdOrderLines.iterator(); iterator.hasNext();) {
				FDCartLineI fdCartLine = iterator.next();
				if(upcs.contains(fdCartLine.getUpc())){
					upcOrderLines.add(fdCartLine);
				}
			}
		}
		return upcOrderLines;
	}

	public static SearchResults getCouponsAsSearchResults(FDUserI user, boolean filterMobile) {


		List<FilteringSortingItem<ProductModel>> searchProductResults = new ArrayList<FilteringSortingItem<ProductModel>>();
		List<ProductModel> products = getCouponProducts(user, filterMobile);
		if(null !=products){
			for (ProductModel productModel : products) {
				FilteringSortingItem<ProductModel> item = new FilteringSortingItem<ProductModel>(productModel);
				item.putSortingValue(EnumSortingValue.PHRASE, 1);
				searchProductResults.add(item);
			}
		}
		return new SearchResults(searchProductResults, Collections.<FilteringSortingItem<Recipe>> emptyList(), Collections
				.<FilteringSortingItem<CategoryModel>> emptyList(), "", true);
	}

	public static List<ProductModel> getCouponProducts(FDUserI user,
			boolean filterMobile) {
		List<ProductModel> products = new ArrayList<ProductModel>();

		ContentKey key = ContentKeyFactory.getECouponsCategoryKey();

		CategoryModel currentFolder = (CategoryModel) ContentFactory.getInstance().getContentNodeByKey( key );
		if(null !=currentFolder){
			List<ProductModel> couponProducts = currentFolder.getProducts();
			FDCustomerCoupon coupon = null;
			if(null !=couponProducts){
				for (ProductModel product : couponProducts) {
					try {
						if ( null != product.getDefaultSku() && product.getDefaultSku().getProductInfo() != null ){
							coupon = user.getCustomerCoupon(product.getDefaultSku().getProductInfo().getUpc(), EnumCouponContext.PRODUCT);
							if(null != coupon)
							if(EnumCouponDisplayStatus.COUPON_CLIPPABLE.equals(coupon.getDisplayStatus())|| EnumCouponDisplayStatus.COUPON_CLIPPED_DISABLED.equals(coupon.getDisplayStatus())){
								if(product.isFullyAvailable()){
									if(filterMobile) {
										if(!product.isHidden()
					        				   && !product.isHideIphone()) {
											products.add(product);
										}
									} else {
										products.add(product);
									}
								}
							}
						}
					} catch (FDResourceException e) {
						e.printStackTrace();
					} catch (FDSkuNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return products;
	}

	private static class PriceComparator implements Comparator<FDCartLineI> {
		@Override
        public int compare(FDCartLineI line1, FDCartLineI line2) {

			if (line1.getPrice() >	line2.getPrice()) {
				return -1;
			} else if (line1.getPrice() <	line2.getPrice()) {
				return 1;
			} else {
				return 0;
			}
		}
	}


	public final static Comparator<FilteringSortingItem<ProductModel>> COUPON_POPULARITY_COMPARATOR = new Comparator<FilteringSortingItem<ProductModel>>() {

		@Override
        public int compare(FilteringSortingItem<ProductModel> fp1, FilteringSortingItem<ProductModel> fp2) {
			try {
				ProductModel p1 = fp1.getModel();
				ProductModel p2 = fp2.getModel();
				FDCouponInfo cp1 = FDCouponFactory.getInstance().getCouponByUpc(p1.getDefaultSku().getProductInfo().getUpc());
				FDCouponInfo cp2 = FDCouponFactory.getInstance().getCouponByUpc(p2.getDefaultSku().getProductInfo().getUpc());
				if(null != cp1 && null !=cp2){
					if(cp2.getOfferPriority()!=null && cp1.getOfferPriority()!=null){
						Integer cp1Int = Integer.valueOf(cp1.getOfferPriority());
						Integer cp2Int = Integer.valueOf(cp2.getOfferPriority());
						if(cp1Int.equals(cp2Int)){
							return p1.getFullName().compareTo(p2.getFullName());
						}
						return cp1Int.compareTo(cp2Int);
					}else if(cp1.getOfferPriority()!=null){
						 return 1;
					}
				}
			} catch (Exception e) {
				LOGGER.debug("Exception in COUPON_POPULARITY_COMPARATOR:"+e);
			}
			return 0;
		}
	};

	private static boolean isCouponValidForTimeslot(FDCouponInfo couponInfo,FDUserI user){
		boolean isValid= true;
		FDReservation reservation =user.getShoppingCart().getDeliveryReservation();
		if(null !=reservation && null != couponInfo){
			if(null !=reservation.getTimeslot() && reservation.getTimeslot().getDeliveryDate().after(couponInfo.getFdExpirationDate())){
				isValid = false;
			}
		}

		return isValid;
	}

}
