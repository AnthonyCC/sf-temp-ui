package com.freshdirect.webapp.ajax.expresscheckout.promotion.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.promotion.Promotion;
import com.freshdirect.fdstore.promotion.PromotionApplicatorI;
import com.freshdirect.fdstore.promotion.PromotionErrorType;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.promotion.WaiveChargeApplicator;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewManager;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class PromotionService {

    private static final PromotionService INSTANCE = new PromotionService();

    public static final String PROMOTION_CODE_FIELD_ID = "promotionCode";

    private PromotionService() {
    }

    public static PromotionService defaultService() {
        return INSTANCE;
    }

    // TODO: review commented parts.
    public List<ValidationError> applyPromotionCode(FormDataRequest promotionRequestData, FDSessionUser user, HttpSession session) throws FDResourceException {
        List<ValidationError> result = new ArrayList<ValidationError>();
        String redemptionCode = FormDataService.defaultService().get(promotionRequestData, PROMOTION_CODE_FIELD_ID);
        String promoId = FDPromotionNewManager.getRedemptionPromotionId(redemptionCode);
        if (redemptionCode == null || redemptionCode.trim().isEmpty()) {
            result.add(new ValidationError(PROMOTION_CODE_FIELD_ID, SystemMessageList.MSG_EMPTY_USER_INPUT_AT_PROMOTION_CODE));
            return result;
        }
        if (promoId == null) {
            Object[] params = new Object[] { redemptionCode };
            result.add(new ValidationError(PROMOTION_CODE_FIELD_ID, MessageFormat.format(SystemMessageList.MSG_INVALID_CODE, params)));
        } else {
            // Get the redemption Promotion from the cache.
            Promotion promotion = (Promotion) PromotionFactory.getInstance().getRedemptionPromotion(promoId);
            if (promotion == null) {// This check is required for incomplete
                // promotions in DB.
                Object[] params = new Object[] { redemptionCode };
                result.add(new ValidationError(PROMOTION_CODE_FIELD_ID, MessageFormat.format(SystemMessageList.MSG_INVALID_CODE, params)));
            } else {
                user.setRedeemedPromotion(promotion);
                // Get the header discount if any applied to the cart before the
                // updateUserState call.
                FDCartI cart = user.getShoppingCart();
                List<ErpDiscountLineModel> prevdiscounts = new ArrayList<ErpDiscountLineModel>(cart.getDiscounts());
                user.updateUserState();
                String promoCode = promotion.getPromotionCode();
                boolean eligible = user.getPromotionEligibility().isEligible(promoCode);
                boolean isApplied = user.getPromotionEligibility().isApplied(promoCode);
                int errorCode = user.getPromoErrorCode(promoCode);
                // request.setAttribute("isEligible", eligible);
                Object[] params = new Object[] { redemptionCode };
                if (!eligible) {
                    if (user.isFraudulent() && promotion.isFraudCheckRequired()) {
                        result.add(new ValidationError(PROMOTION_CODE_FIELD_ID,
                                MessageFormat.format(SystemMessageList.MSG_PROMO_NOT_UNIQUE_INFO, new Object[] { user.getCustomerServiceContact() })));
                        result.add(new ValidationError(PROMOTION_CODE_FIELD_ID, MessageFormat.format(SystemMessageList.MSG_REDEMPTION_NOT_ELIGIBLE, params)));
                        user.setRedeemedPromotion(null);
                    } else if (promotion.getExpirationDate() != null && new Date().after(promotion.getExpirationDate())) {
                        result.add(new ValidationError(PROMOTION_CODE_FIELD_ID, MessageFormat.format(SystemMessageList.MSG_REDEMPTION_HAS_EXPIRED, params)));
                        user.setRedeemedPromotion(null);
                    } else if (errorCode == PromotionErrorType.ERROR_REDEMPTION_EXCEEDED.getErrorCode()) {
                        result.add(new ValidationError(PROMOTION_CODE_FIELD_ID, MessageFormat.format(SystemMessageList.MSG_CART_REDEMPTION_EXCEEDED, params)));
                        user.setRedeemedPromotion(null);
                    } else if (errorCode == PromotionErrorType.ERROR_USAGE_LIMIT_ONE_EXCEEDED.getErrorCode()) {
                        result.add(new ValidationError(PROMOTION_CODE_FIELD_ID, MessageFormat.format(SystemMessageList.MSG_CART_USAGE_LIMIT_ONE_EXCEEDED, params)));
                        user.setRedeemedPromotion(null);
                    } else if (errorCode == PromotionErrorType.ERROR_USAGE_LIMIT_MORE_EXCEEDED.getErrorCode()) {
                        result.add(new ValidationError(PROMOTION_CODE_FIELD_ID, MessageFormat.format(SystemMessageList.MSG_CART_USAGE_LIMIT_MORE_EXCEEDED, params)));
                        user.setRedeemedPromotion(null);
                    } else {
                        if (errorCode == PromotionErrorType.NO_ELIGIBLE_ADDRESS_SELECTED.getErrorCode()
                                || errorCode == PromotionErrorType.NO_DELIVERY_ADDRESS_SELECTED.getErrorCode()) {
                            result.add(new ValidationError(PROMOTION_CODE_FIELD_ID, SystemMessageList.MSG_REDEMPTION_NOTE_DLV_ADDRESS));
                            // request.setAttribute("promoError", "true");
                        } else if (errorCode == PromotionErrorType.NO_ELIGIBLE_PAYMENT_SELECTED.getErrorCode()
                                || errorCode == PromotionErrorType.NO_PAYMENT_METHOD_SELECTED.getErrorCode()) {
                            result.add(new ValidationError(PROMOTION_CODE_FIELD_ID, SystemMessageList.MSG_REDEMPTION_NOTE_PAYMENT));
                            // request.setAttribute("promoError", "true");
                        } else if (!isApplied) {
                            result.add(new ValidationError(PROMOTION_CODE_FIELD_ID, MessageFormat.format(SystemMessageList.MSG_INVALID_CODE, params)));
                            user.setRedeemedPromotion(null);
                        } else {
                            result.add(new ValidationError(PROMOTION_CODE_FIELD_ID, SystemMessageList.MSG_REDEMPTION_ALREADY_USED));
                        }
                    }
                } else if (!isApplied) {
                    // request.setAttribute("isEligible", eligible);
                    if (user.isFraudulent() && promotion.isFraudCheckRequired()) {
                        user.setRedeemedPromotion(null);
                        result.add(new ValidationError(PROMOTION_CODE_FIELD_ID,
                                MessageFormat.format(SystemMessageList.MSG_PROMO_NOT_UNIQUE_INFO, new Object[] { user.getCustomerServiceContact() })));
                        result.add(new ValidationError(PROMOTION_CODE_FIELD_ID, MessageFormat.format(SystemMessageList.MSG_REDEMPTION_NOT_ELIGIBLE, params)));
                    } else if (user.getShoppingCart().getSubTotal() < promotion.getMinSubtotal()) {
                        Object[] params1 = new Object[] { new Double(promotion.getMinSubtotal()) };
                        result.add(new ValidationError(PROMOTION_CODE_FIELD_ID, MessageFormat.format(SystemMessageList.MSG_REDEMPTION_MIN_NOT_MET, params1)));

                    } else if (promotion.isLineItemDiscount()) {
                        result.add(new ValidationError(PROMOTION_CODE_FIELD_ID, SystemMessageList.MSG_REDEMPTION_NO_ELIGIBLE_CARTLINES));
                    } else if (promotion.isSampleItem()) {
                        result.add(new ValidationError(PROMOTION_CODE_FIELD_ID, SystemMessageList.MSG_REDEMPTION_PRODUCT_UNAVAILABLE));
                    } else {
                        if (errorCode == PromotionErrorType.NO_ELIGIBLE_TIMESLOT_SELECTED.getErrorCode()) {
                            result.add(new ValidationError(PROMOTION_CODE_FIELD_ID, SystemMessageList.MSG_REDEMPTION_NOTE_TIMESLOT));
                        }
                    }
                } else {
                    /*
                     * The redemption promotion is applied. Check if there is any previously applied header discount-the automatice ones. If yes then throw an error message to the
                     * user.
                     */
                    if (prevdiscounts.size() > 0 && promotion.isHeaderDiscount() && !promotion.isCombineOffer()) {
                        result.add(new ValidationError(PROMOTION_CODE_FIELD_ID, SystemMessageList.MSG_REDEMPTION_OVERRIDE_AUTOMATIC));
                        // request.setAttribute("redeem_override_msg",
                        // SystemMessageList.MSG_REDEMPTION_OVERRIDE_AUTOMATIC);
                    }
                    user.setCouponEvaluationRequired(true);
                    // request.setAttribute("isEligible", eligible);

                }
                session.setAttribute(SessionName.USER, user);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public void removePromotion(HttpSession session, FDUserI user) {
        PromotionI promo = user.getRedeemedPromotion();
        if (promo != null) {
            // MNT - 144 Bug fix.
            // APPDEV-2850 - applicator are now combinable and come as a list
            for (Iterator<PromotionApplicatorI> i = ((Promotion) promo).getApplicatorList().iterator(); i.hasNext();) {
                PromotionApplicatorI _applicator = i.next();
                if (_applicator instanceof WaiveChargeApplicator) {
                    WaiveChargeApplicator waiveChargeApplicator = (WaiveChargeApplicator) _applicator;
                    if (waiveChargeApplicator.getChargeType() == EnumChargeType.DELIVERY) {
                        user.getShoppingCart().setDlvPromotionApplied(false);
                    }
                }
            }
            user.setRedeemedPromotion(null);
            user.updateUserState();
            user.setCouponEvaluationRequired(true);
            session.setAttribute(SessionName.USER, user);
        }
    }
}
