package com.freshdirect.webapp.ajax.expresscheckout.availability.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.deliverypass.DeliveryPassType;
import com.freshdirect.deliverypass.DlvPassAvailabilityInfo;
import com.freshdirect.deliverypass.EnumDlvPassProfileType;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.atp.FDAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDStockAvailabilityInfo;
import com.freshdirect.fdstore.atp.NullAvailability;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDModifyCartLineI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;
import com.freshdirect.webapp.util.FDEventUtil;
import com.freshdirect.webapp.util.JspMethods;
import com.freshdirect.webapp.util.StandingOrderHelper;

public class AvailabilityService {

	private static final String DELIVERY_PASS_ONLY = "dlv_pass_only";
	private static final String CART_IS_EMPTY = "cart_is_empty";
	private static final String DELIVERY_PASS_CANCELLED = "dlv_pass_cancelled";
	private static final String GENERAL_UNDER_ORDER_MINIMUM_MESSAGE_KEY = "generalUnderOrderMinimumMessageKey";
	
	//APPDEV-5516:Cart Carousel - Grand Giving Donation Technology. 
	//The warning type would be DONATION_PRODUCTS_ONLY but the warning message will be MSG_CONTAINS_DLV_PASS_ONLY
	private static final String DONATION_PRODUCTS_ONLY = "donation_products_only";

	private static final int DEFAULT_ATP_RESTRICTION_TIMEOUT = 30000;

	public static final String ATP_CHECKOUT="Checkout";

	private static final AvailabilityService INSTANCE = new AvailabilityService();

	private AvailabilityService() {
	}

	public static AvailabilityService defaultService() {
		return INSTANCE;
	}

	public boolean checkCartAtpAvailability(final FDUserI user)
			throws FDResourceException {
		FDCartModel cart = FDCustomerManager.checkAvailability(
				user.getIdentity(), user.getShoppingCart(),
				DEFAULT_ATP_RESTRICTION_TIMEOUT,ATP_CHECKOUT);
		System.out.println("WOW");
		performDeliveryPassAvailabilityCheck(user);
		user.updateUserState();
		return cart.isFullyAvailable();
	}

	public String checkCartAvailabilityAdjustResult(FDUserI user)
			throws FDResourceException {
		String errorMessage = "";
		FDCartModel cart = user.getShoppingCart();
		boolean isEbt = false;
		if (cart.getPaymentMethod() != null) {
			isEbt = EnumPaymentMethodType.EBT.equals(cart.getPaymentMethod()
					.getPaymentMethodType());
		}
		if (cart.containsUnlimitedPass())
			if (cart.containsDlvPassOnly()) {
				errorMessage = "Your cart contains only delivery pass item(s).";
			} else if (!user.isOrderMinimumMet()
					&& user.getMasqueradeContext() == null) {
				if (!isEbt) {
					errorMessage = "Because some items were unavailable for delivery at the time you selected, they've been removed from your cart."
							+ " Unfortunately this means that your order total has been adjusted and falls below our $"
							+ (int) user.getMinimumOrderAmount()
							+ " minimum. Please <a href='/index.jsp'>shop for replacement items</a> and then return to Checkout."
							+ " We apologize for the inconvenience.";
				} else {
					errorMessage = "Because some items were ineligible to be payed by EBT, they've been removed from your cart."
							+ " Unfortunately this means that your order total has been adjusted and falls below our $"
							+ (int) user.getMinimumOrderAmount()
							+ " minimum. Please <a href='/index.jsp'>shop for replacement items</a> and then return to Checkout."
							+ " We apologize for the inconvenience.";
				}
			} else if (cart.getSubTotal() < cart.getDeliveryReservation()
					.getMinOrderAmt()) {
				errorMessage = "Unfortunately your order total falls below our delivery $"
						+ (int) cart.getDeliveryReservation().getMinOrderAmt()
						+ " minimum. Please <a href='/index.jsp'>shop for replacement items</a> and then return to Checkout."
						+ " We apologize for the inconvenience.";
			}
		return errorMessage;
	}

	public Map<String, String> populateWarningMessages(FDUserI user) {
		Map<String, String> warningMessages = new HashMap<String, String>();
		warningMessages.put(GENERAL_UNDER_ORDER_MINIMUM_MESSAGE_KEY,
				SystemMessageList.MSG_GENERAL_UNDER_ORDER_MINIMUM_MESSAGE);
		warningMessages.put(DELIVERY_PASS_ONLY,
				SystemMessageList.MSG_CONTAINS_DLV_PASS_ONLY);
		warningMessages.put(DELIVERY_PASS_CANCELLED,
				SystemMessageList.MSG_UNLIMITED_PASS_CANCELLED);
		warningMessages.put(CART_IS_EMPTY,
				SystemMessageList.MSG_CHECKOUT_CART_EMPTY);
		warningMessages.put(DONATION_PRODUCTS_ONLY,
				SystemMessageList.MSG_CONTAINS_DLV_PASS_ONLY);
		return warningMessages;
	}

    public String translateWarningMessage(String warningMessageKey, FDUserI user) throws FDResourceException {
        String warningMessage = null;
        if (warningMessageKey != null && !StandingOrderHelper.isSO3StandingOrder(user)) {
            Map<String, String> populateWarningMessages = AvailabilityService.defaultService().populateWarningMessages(user);
            warningMessage = populateWarningMessages.get(warningMessageKey);
            if (AvailabilityService.GENERAL_UNDER_ORDER_MINIMUM_MESSAGE_KEY.equals(warningMessageKey)) {
                warningMessage = MessageFormat.format(warningMessage, JspMethods.formatPrice(user.getMinimumOrderAmount()));
            }
        }
        return warningMessage;
    }

	public String selectAlcoholicOrderMinimumType(FDUserI user)
			throws FDResourceException {
		// APPDEV-4552 - no minimum check in masquerade mode
		if (user != null && user.getMasqueradeContext() != null) {
			return null;
		}

		String warningType = null;

		if (!user.isOrderMinimumMet()) {
			warningType = GENERAL_UNDER_ORDER_MINIMUM_MESSAGE_KEY;
		}
		return warningType;
	}

	public String selectWarningType(FDUserI user) throws FDResourceException {
		String warningType = null;
		FDCartModel cart = user.getShoppingCart();
		ErpAddressModel deliveryAddress = cart.getDeliveryAddress();

		if (cart.containsDlvPassOnly()) {
			warningType = DELIVERY_PASS_ONLY;
		} else if (cart.containsDonationProductsOnly()) {
			warningType = DONATION_PRODUCTS_ONLY;
		} else if (user.getDlvPassInfo() != null
				&& user.getDlvPassInfo().isUnlimited()
				&& user.isDlvPassCancelled() && cart.isDlvPassAlreadyApplied()) {
			warningType = DELIVERY_PASS_CANCELLED;
		} else if (!user.isOrderMinimumMet() && deliveryAddress != null
				&& user.getMasqueradeContext() == null) {
			warningType = GENERAL_UNDER_ORDER_MINIMUM_MESSAGE_KEY;
		} else if (user.getShoppingCart().getOrderLines().isEmpty()) {
			warningType = CART_IS_EMPTY;
		}
		return warningType;
	}

	public void adjustCartAvailability(final HttpServletRequest request,
			List<String> removeCartLineIds, final FDUserI user)
			throws FDResourceException, FDInvalidConfigurationException {
		FDCartModel cart = user.getShoppingCart();
		if (cart.getPaymentMethod() != null
				&& EnumPaymentMethodType.EBT.equals(cart.getPaymentMethod()
						.getPaymentMethodType())) {
			for (FDCartLineI cartLine : cart.getEbtIneligibleOrderLines()) {
				cart.removeOrderLineById(cartLine.getRandomId());
				// Create FD remove cart event.
				FDEventUtil.logRemoveCartEvent(cartLine, request);
			}
		} else {
			// unavMap will be empty if ATC happened on step_2_unavail.jsp, but
			// amounts are already set to deliverable amounts
			// see ShoppingCartUtil.getSubTotal(clonedCart) in
			// UnavailabilityPopulator
			Map<String, FDAvailabilityInfo> unavMap = cart
					.getUnavailabilityMap();
			for (Iterator<Entry<String, FDAvailabilityInfo>> i = unavMap
					.entrySet().iterator(); i.hasNext();) {
				Entry<String, FDAvailabilityInfo> entry = i.next();
				String key = entry.getKey();
				FDAvailabilityInfo info = entry.getValue();
				int cartLineId = Integer.parseInt(key);
				FDCartLineI cartline = cart.getOrderLineById(cartLineId);
				int cartIndex = cart.getOrderLineIndex(cartLineId);
				if (info instanceof FDStockAvailabilityInfo) {
					FDStockAvailabilityInfo sInfo = (FDStockAvailabilityInfo) info;

					if (sInfo.getQuantity() == 0) {
						// remove
						cart.removeOrderLine(cartIndex);
						// Create FD remove cart event.
						FDEventUtil.logRemoveCartEvent(cartline, request);

					} else {
						cartline.setQuantity(sInfo.getQuantity());
						// Create FD Modify cart event.
						FDEventUtil.logEditCartEvent(cartline, request);
					}

				} else {
					// remove
					cart.removeOrderLine(cartIndex);
					FDEventUtil.logRemoveCartEvent(cartline, request);
				}
			}

			performDeliveryPassAvailabilityCheck(user);
			// Remove unavailable delivery passes.
			List<DlvPassAvailabilityInfo> unavailPasses = cart
					.getUnavailablePasses();
			if (unavailPasses != null && unavailPasses.size() > 0) {
				for (Iterator<DlvPassAvailabilityInfo> i = unavailPasses
						.iterator(); i.hasNext();) {
					DlvPassAvailabilityInfo info = i.next();
					Integer key = info.getKey();
					FDCartLineI cartline = cart
							.getOrderLineById(key.intValue());
					int cartIndex = cart.getOrderLineIndex(key.intValue());
					cart.removeOrderLine(cartIndex);
					FDEventUtil.logRemoveCartEvent(cartline, request);
				}
				unavailPasses.clear();
			}

			// remove those which were explicitly selected for removal and the
			// ones that are not available
			// (if unavMap is empty because of ATC this will remove unavailable
			// items)
			if (removeCartLineIds != null) {
				for (String removeCartLineId : removeCartLineIds) {
					int cartLineIdInt = Integer.parseInt(removeCartLineId);
					int cartIndex = cart.getOrderLineIndex(cartLineIdInt);
					if (cartIndex > -1) {
						FDCartLineI cartline = cart
								.getOrderLineById(cartLineIdInt);
						cart.removeOrderLine(cartIndex);
						FDEventUtil.logRemoveCartEvent(cartline, request);
					}
				}
			}
		}
		refreshCartAndUser(user, cart);
	}

	private void performDeliveryPassAvailabilityCheck(FDUserI user)
			throws FDResourceException {

		FDCartModel cart = user.getShoppingCart();

		int dlvPassCount = cart.getDeliveryPassCount();
		if (dlvPassCount == 0) {
			// return ;
		}
		Map<String, FDAvailabilityInfo> unavMap = cart.getUnavailabilityMap();
		// addedList - contains list of passes that was added to the cart during
		// this session.
		List<FDCartLineI> dlvPasses = new ArrayList<FDCartLineI>();
		boolean lastPurchasedPass = false;
		for (Iterator<FDCartLineI> i = cart.getOrderLines().iterator(); i
				.hasNext();) {
			FDCartLineI line = (FDCartLineI) i.next();
			String key = new Integer(line.getRandomId()).toString();
			if (line.lookupFDProduct().isDeliveryPass()
					&& !(unavMap.containsKey(key))) {
				dlvPasses.add(line);
				if (line instanceof FDModifyCartLineI)
					lastPurchasedPass = true;
			}
		}
		if (dlvPasses.size() == 1 && lastPurchasedPass) {
			// There is no new delivery passes added to cart during modify
			// order. So return
			return;
		}
		List<DlvPassAvailabilityInfo> unavailableList = new ArrayList<DlvPassAvailabilityInfo>();
		if (!user.isEligibleForDeliveryPass()) {
			addToUnavailableList(dlvPasses, unavailableList,
					REASON_NOT_ELIGIBLE);
		} else if (user.getDlvPassInfo().getUsablePassCount() >= FDStoreProperties
				.getMaxDlvPassPurchaseLimit()) {

			addToUnavailableList(dlvPasses, unavailableList, REASON_MAX_PASSES);
		} else {
			// User is eligible for delivery pass.
			EnumDlvPassStatus status = user.getDeliveryPassStatus();

			/*
			 * if(!DeliveryPassUtil.isEligibleStatus(status) &&
			 * !DeliveryPassUtil.isOriginalOrder(user)){
			 * addToUnavailableList(dlvPasses, unavailableList,
			 * REASON_PASS_EXISTS); }else {
			 */// --commented for DP1.1

			// User has eligible status to buy a pass. Pull out the eligible and
			// ineligible passes.
			List<FDCartLineI> eligibleList = new ArrayList<FDCartLineI>();
			List<FDCartLineI> ineligibleList = new ArrayList<FDCartLineI>();
			checkForEligiblePasses(user, dlvPasses, eligibleList,
					ineligibleList, unavailableList);
			// addToUnavailableList(ineligibleList, unavailableList,
			// REASON_NOT_ELIGIBLE);
			if (eligibleList.size() > 1) {
				/*
				 * There are more than one delivery pass. Keep the high priced
				 * delivery pass in the cart. Push the remaining passes into the
				 * unavailable list.
				 */
				List<FDCartLineI> lowPriceList = retainHighPricedDlvPass(eligibleList);
				addToUnavailableList(lowPriceList, unavailableList,
						REASON_TOO_MANY_PASSES);
			}
			// }--commented for DP1.1
		}
		cart.setUnavailablePasses(unavailableList);

	}

	private void refreshCartAndUser(final FDUserI user, FDCartModel cart)
			throws FDResourceException, FDInvalidConfigurationException {
		cart.refreshAll(true);
		// This method retains all product keys that are in the cart in the dcpd
		// promo product info.
		user.getDCPDPromoProductCache().retainAll(
				cart.getProductKeysForLineItems());
		// Revalidate the cart for deliverypass.
		cart.handleDeliveryPass();
		user.updateUserState();
		// Coupons have to re-evaluated.
		user.setCouponEvaluationRequired(true);

		cart.setAvailability(NullAvailability.AVAILABLE);
	}

	private void checkForEligiblePasses(FDUserI user,
			List<FDCartLineI> dlvPasses, List<FDCartLineI> eligibleList,
			List<FDCartLineI> inEligibleList,
			List<DlvPassAvailabilityInfo> unavailableList)
			throws FDResourceException {
		EnumDlvPassProfileType profileType = user.getEligibleDeliveryPass();
		Iterator<FDCartLineI> i = dlvPasses.iterator();
		FDCartLineI line = null;
		DeliveryPassType type = null;
		int autoRenewPassCount = user.getDlvPassInfo()
				.getAutoRenewUsablePassCount();
		while (i.hasNext()) {
			line = (FDCartLineI) i.next();
			type = DeliveryPassType
					.getEnum(line.lookupFDProduct().getSkuCode());

			if (autoRenewPassCount > 0 && type.isAutoRenewDP()) {
				inEligibleList.add(line);
				addToUnavailableList(line, unavailableList,
						REASON_MULTIPLE_AUTORENEW_PASSES);
			} else if (user.getDlvPassInfo().isFreeTrialRestricted()
					&& type.isFreeTrialDP()) {
				inEligibleList.add(line);
				addToUnavailableList(line, unavailableList, REASON_NOT_ELIGIBLE);
			} else if ((type.isUnlimited() && (EnumDlvPassProfileType.UNLIMITED
					.equals(profileType)
					|| EnumDlvPassProfileType.PROMOTIONAL_UNLIMITED
							.equals(profileType) || EnumDlvPassProfileType.AMAZON_PRIME
						.equals(profileType)))
					|| (!type.isUnlimited() && EnumDlvPassProfileType.BSGS
							.equals(profileType))) {
				if (EnumDlvPassProfileType.UNLIMITED.equals(profileType)
						&& (FDStoreProperties.getUnlimitedAmazonPrimeProfile()
								.equals(type.getProfileValue()) || FDStoreProperties
								.getUnlimitedPromotionalProfile().equals(
										type.getProfileValue()))) {
					inEligibleList.add(line);
					addToUnavailableList(line, unavailableList,
							formatPhoneMsg(REASON_PROMOTIONAL_PASS));
				} else {
					eligibleList.add(line);
				}
			} else if ((type.isUnlimited() && EnumDlvPassProfileType.BSGS
					.equals(profileType))
					|| (!type.isUnlimited() && (EnumDlvPassProfileType.UNLIMITED
							.equals(profileType)
							|| EnumDlvPassProfileType.PROMOTIONAL_UNLIMITED
									.equals(profileType) || EnumDlvPassProfileType.AMAZON_PRIME
								.equals(profileType)))) {
				inEligibleList.add(line);
				addToUnavailableList(line, unavailableList, REASON_NOT_ELIGIBLE);
			}

		}

	}

	private void addToUnavailableList(FDCartLineI line,
			List<DlvPassAvailabilityInfo> unavailableList, String reasonCode) {
		if (line != null) {
			DlvPassAvailabilityInfo info = new DlvPassAvailabilityInfo(
					new Integer(line.getRandomId()), reasonCode);
			unavailableList.add(info);
		}

	}

	private void addToUnavailableList(List<FDCartLineI> sourceList,
			List<DlvPassAvailabilityInfo> unavailableList, String reasonCode) {
		Iterator<FDCartLineI> i = sourceList.iterator();
		while (i.hasNext()) {
			FDCartLineI line = i.next();
			DlvPassAvailabilityInfo info = new DlvPassAvailabilityInfo(
					new Integer(line.getRandomId()), reasonCode);
			unavailableList.add(info);
		}
	}

	private String formatPhoneMsg(String pattern) {
		return MessageFormat.format(pattern, new Object[] {/*
															 * UserUtil.
															 * getCustomerServiceContact
															 * (request)
															 */"Number" });
	}

	private List<FDCartLineI> retainHighPricedDlvPass(
			List<FDCartLineI> eligibleList) {
		Collections.sort(eligibleList, PRICE_COMPARATOR);
		Iterator<FDCartLineI> i = eligibleList.iterator();
		// Skip the high priced delivery pass(first item) from the list.
		i.next();
		List<FDCartLineI> lowPriceList = new ArrayList<FDCartLineI>();
		while (i.hasNext()) {
			FDCartLineI line = (FDCartLineI) i.next();
			lowPriceList.add(line);
		}
		return lowPriceList;
	}

	public  static final String REASON_NOT_ELIGIBLE = "Not currently eligible for DeliveryPass. Please contact Customer Service.";
	public  static final String REASON_TOO_MANY_PASSES = "Too many DeliveryPasses added";
	public  static final String REASON_PASS_EXISTS = "Account contains an active or pending DeliveryPass.<br>Please see the Your Account section for more information";
	public  static final String REASON_MAX_PASSES = "Account already has the maximum number of allowable DeliveryPasses.";
	public  static final String REASON_PROMOTIONAL_PASS = "Not currently eligible for DeliveryPasses. Please contact Customer Service at {0}. ";
	public  static final String REASON_MULTIPLE_AUTORENEW_PASSES = "You already have a DeliveryPass scheduled to automatically renew.";

	private final static Comparator<FDCartLineI> PRICE_COMPARATOR = new Comparator<FDCartLineI>() {
		public int compare(FDCartLineI o1, FDCartLineI o2) {
			Double amt2 = new Double(o2.getPrice());
			Double amt1 = new Double(o1.getPrice());
			return amt2.compareTo(amt1);
		}
	};

}
