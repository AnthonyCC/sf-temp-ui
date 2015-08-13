package com.freshdirect.webapp.ajax.expresscheckout.giftcard.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.giftcard.FDGiftCardInfoList;
import com.freshdirect.fdstore.giftcard.FDGiftCardModel;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.giftcard.CardInUseException;
import com.freshdirect.giftcard.CardOnHoldException;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.InvalidCardException;
import com.freshdirect.giftcard.ServiceUnavailableException;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class GiftCardService {

	public static final String GIVEX_NUM_FIELD_ID = "givexNum";

	public static GiftCardService defaultService() {
		return INSTANCE;
	}

	private static final GiftCardService INSTANCE = new GiftCardService();

	private final int GC_RETRY_COUNT = 4;

	private final int GC_RETRY_WARNING_COUNT = 3;

	private GiftCardService() {
	}

	public List<ValidationError> applyGiftCard(FormDataRequest requestData, FDSessionUser user, FDActionInfo info, String customerServiceContact) {
		List<ValidationError> validationResult = new ArrayList<ValidationError>();
		try {
			if (!user.getFDCustomer().getProfile().allowApplyGC()) {
				validationResult.add(new ValidationError(GIVEX_NUM_FIELD_ID, formatMessage(SystemMessageList.ACCOUNT_LOCKED_FOR_GC, customerServiceContact)));
			} else {
				String givexNum = FormDataService.defaultService().get(requestData, GIVEX_NUM_FIELD_ID);
                if (givexNum != null && !givexNum.trim().isEmpty()) {
					try {
						ErpGiftCardModel gcModel = FDCustomerManager.applyGiftCard(user.getIdentity(), givexNum.trim(), info);
						if (gcModel.getBalance() == 0) {
							validationResult.add(new ValidationError(GIVEX_NUM_FIELD_ID, SystemMessageList.APPLY_GC_WITH_ZERO_BALANCE));
						}
						user.getGiftCardList().addGiftCard(new FDGiftCardModel(gcModel));
						user.resetGCRetryCount();
					} catch (ServiceUnavailableException se) {
						validationResult.add(new ValidationError(GIVEX_NUM_FIELD_ID, SystemMessageList.MSG_GC_SERVICE_UNAVAILABLE));
					} catch (InvalidCardException e) {
						user.incrementGCRetryCount();
						if (user.getGCRetryCount() >= GC_RETRY_COUNT) {
							// Lock the customer account from applying gift
							// card.
							user.getFDCustomer().getProfile().setAttribute("allow_apply_gc", "false");
							FDCustomerManager.setProfileAttribute(user.getIdentity(), "allow_apply_gc", "false", info);
							validationResult.add(new ValidationError(GIVEX_NUM_FIELD_ID, formatMessage(SystemMessageList.ACCOUNT_LOCKED_FOR_GC, customerServiceContact)));
						} else {
							if (user.getGCRetryCount() >= GC_RETRY_WARNING_COUNT) {
								validationResult.add(new ValidationError(GIVEX_NUM_FIELD_ID, formatMessage(SystemMessageList.APPLY_GC_WARNING, customerServiceContact)));
							}
							validationResult.add(new ValidationError(GIVEX_NUM_FIELD_ID, SystemMessageList.MSG_GC_INVALID));
						}
					} catch (CardOnHoldException e) {
						validationResult.add(new ValidationError(GIVEX_NUM_FIELD_ID, formatMessage(SystemMessageList.MSG_GC_ON_HOLD, customerServiceContact)));
					} catch (CardInUseException ce) {
						if (ce.getCardOwner().equals(user.getUserId())) {
							validationResult.add(new ValidationError(GIVEX_NUM_FIELD_ID, formatMessage(SystemMessageList.MSG_GC_ALREADY_ADDED, customerServiceContact)));
						} else {
							validationResult.add(new ValidationError(GIVEX_NUM_FIELD_ID, formatMessage(SystemMessageList.MSG_GC_IN_USE, customerServiceContact)));
						}
					}
				} else {
					validationResult.add(new ValidationError(GIVEX_NUM_FIELD_ID, SystemMessageList.MSG_GC_INVALID));
				}
			}
		} catch (FDResourceException e) {
			validationResult.add(new ValidationError(GIVEX_NUM_FIELD_ID, "Unable to process your request due to technical difficulty."));
		}
		return validationResult;
	}

	public void removeGiftCard(FDUserI user) {
		FDGiftCardInfoList giftCards = user.getGiftCardList();
		if (giftCards != null) {
			giftCards.clearAllSelection();
		}
	}

	private String formatMessage(String pattern, String customerServiceContact) {
		return MessageFormat.format(pattern, new Object[] { customerServiceContact });
	}

	public void setAllowGcUsage(FDSessionUser user, FDActionInfo info, boolean allowGcUsage, ActionResult actionResult) {
		try {
			// System.out.println("setAllowGCUsage()=================");
			/*
			 * Toggle profile value for allow_apply_gc true = customer allowed
			 * to use gift card false = customer NOT allowed to use gift card
			 */
			if (allowGcUsage) {
				boolean allowGCUsageValue = user.getFDCustomer().getProfile().allowApplyGC();
				if (allowGCUsageValue) {
					user.getFDCustomer().getProfile().setAttribute("allow_apply_gc", "false");
					FDCustomerManager.setProfileAttribute(user.getIdentity(), "allow_apply_gc", "false", info);
				} else {
					user.getFDCustomer().getProfile().setAttribute("allow_apply_gc", "true");
					FDCustomerManager.setProfileAttribute(user.getIdentity(), "allow_apply_gc", "true", info);
					user.resetGCRetryCount();
				}
			}
		} catch (FDResourceException e) {
			actionResult.addError(new ActionError("technical_difficulty", "Unable to process your request due to technical difficulty."));
		}
	}

}
