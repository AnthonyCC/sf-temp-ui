/**
 *
 */
package com.freshdirect.deliverypass;

/**
 * @author skrishnasamy
 *
 */
public class DlvPassConstants {
	public static final int INCREMENT_FLAG = 0;
	public static final int DECREMENT_FLAG = 1;
	public static final String PROMO_CODE = "DELIVERYPASS";
	public static final String BSGS_APPLIED_MSG = "1 DeliveryPass";
	public static final String UNLIMITED_APPLIED_MSG = "FREE with DeliveryPass";
	public static final String ACTIVE_ITEM = "ActiveItem";
	public static final String REFUND_AMOUNT = "RefundAmount";
	public static final String PASS_HISTORY = "PassHistory";
	public static final String DLV_PASS_SESSION_ID = "DlvPassSessionId";
	public static final String RETURN_NOTE = "One delivery was credited due to a returned order.";
	public static final String CANCEL_NOTE = "Delivery Pass was cancelled due to a returned order.";
	public static final String IS_FREE_TRIAL_RESTRICTED="isFreeTrialRestricted";
	public static final String AUTORENEW_USABLE_PASS_COUNT="autoRenewUsableDPCount";
	public static final String USABLE_PASS_COUNT="UsablePassCount";
	public static final String AUTORENEW_DP_TYPE="AutoRenewDPType";
	public static final String AUTORENEW_DP_PRICE="AutoRenewDPPrice";
	public static final String UNLIMITED="Unlimited";
	public final static String AUTORENEW_PYMT_METHOD_INVALID="Default payment method invalid";
	public final static String AUTORENEW_PYMT_METHOD_UNKNOWN="Default payment method unknown";
	public final static String AUTORENEW_PYMT_METHOD_CC_EXPIRED="Credit card selected is expired.";
	
	public  static final String REASON_NOT_ELIGIBLE = "reasonNotEligible";
	public  static final String REASON_TOO_MANY_PASSES = "Too many DeliveryPasses added";
	public  static final String REASON_PASS_EXISTS = "Account contains an active or pending DeliveryPass.<br>Please see the Your Account section for more information";
	public  static final String REASON_MAX_PASSES = "Account already has the maximum number of allowable DeliveryPasses.";
	public  static final String REASON_PROMOTIONAL_PASS = "Not currently eligible for DeliveryPasses. Please contact Customer Service at {0}. ";

}
