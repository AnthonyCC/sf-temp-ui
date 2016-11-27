package com.freshdirect.fdstore.ewallet;

/**
 * @author Aniwesh Vatsal
 *
 */
public class EwalletConstants {

	// MasterPass Express Checkout 
	/*	public static final String EWALLET_MP_PAIRING_START = "MP_Pairing_Start";
	public static final String EWALLET_MP_PAIRING_END = "MP_Pairing_End";
	public static final String EWALLET_MP_CONNECT_START="MP_Connect_Start";
	public static final String EWALLET_MP_CONNECT_END="MP_Connect_End";
	public static final String EWALLET_MP_EXPRESS_CHECKOUT = "MP_Express_Checkout";
	public static final String EWALLET_MP_GET_ALL_PAYMETHOD_IN_EWALLET="MP_All_PayMethod_In_Ewallet";
	public static final String EWALLET_MP_DISCONNECT_EWALLET="MP_Disconnect_Ewallet";
	public static final String PARAM_PAIRING_TOKEN = "pairing_token";
	public static final String PARAM_PAIRING_VERIFIER = "pairing_verifier";
	public final String EWALLET_PAIRED_PAYMENT_METHOD = "mpPairedPaymentMethod";
	public final String EWALLET_PRECHECKOT_CARDID = "precheckout_CardId";
	public static final String ACTION = "action";
	
	private static final String EWALLET_MP_EXPRESS_CHECKOUT="MP_Express_Checkout";
	private static final String EWALLET_MP_PAIRING_CHECKOUT="MP_Pairing_Start";
	private static final String EWALLET_MP_PAIRING_END="MP_Pairing_End";
	private static final String EWALLET_MP_GET_ALL_PAYMETRHOD_IN_EWALLET = "MP_All_PayMethod_In_Ewallet";
	private static final String EWALLET_MP_CONNECT_START="MP_Connect_Start";
	private static final String EWALLET_MP_CONNECT_END="MP_Connect_End";
	private static final String EWALLET_MP_DISCONNECT="MP_Disconnect_Ewallet";
	private static final String MASTERPASS_REQ_ATTR_ACTION_COMPLETED_VALUE="Pairing_End";
	private static final String MASTERPASS_BOOLEAN_TRUE="true";
	private static final String MASTERPASS_REQ_ATTR_INVALID_PAYMENT="invalidPaymentMethod";
	private static final String MASTERPASS_REQ_ATTR_PAYMENT="paymentData";
	private static final String MASTERPASS_REQ_ATTR_MPPAYMENTS="mpEwalletPaymentData";
	private static final String MASTERPASS_REQ_ATTR_MPPREFERREDCARD="mpEwalletPreferredCard";

*/	
	public static final String MP_EWALLET_RESPONSE_STATUS = "mpstatus";
	public static final String EWALLET_MP_STANDARD_CHECKOUT="MP_Standard_Checkout";
	public static final String EWALLET_MP_STANDARD_CHECKOUT_DATA="MP_Standard_CheckoutData";
	public static final String MASTERPASS_CANCEL_STATUS="cancel";
	public static final String MASTERPASS_FAILURE_STATUS="failure";
	public static final String MASTERPASS_TRANSACTIONID="transactionId";
	public static final String MASTERPASS_REQ_ATTR_ACTION_COMPLETED="actionCompleted";
	public static final String MP_REQ_ATTR_ACTION_COMPLETED_VALUE="Standard_Checkout";
	public static final String PARAM_REQUEST_TOKEN = "oauth_token";
	public static final String PARAM_OAUTH_VERIFIER = "oauth_verifier";
	public static final String PARAM_CHECKOUT_URL = "checkout_resource_url";
	public static final String EWALLET_TYPE = "ewalletType";
	public static final String MP_EWALLET_NAME = "MP";
	public static final String EWALLET_ERROR_CODE = "WALLET_ERROR";
	
	public static final String EWALLET_PP_START_PAIRING="PP_Start_Pairing";
	public static final String PP_EWALLET_NAME = "PP";

	public static final String EWALLET_PP_END_PAIRING = "PP_End_Pairing";
	public static final String EWALLET_PP_START_CONNECTING = "PP_Start_Connecting";
	public static final String EWALLET_PP_END_CONNECTING = "PP_End_Connecting";
	
	public static final String EWALLET_PP_DISCONNECT = "PP_Wallet_Disconnect";
	public static final String EWALLET_SESSION_ATTRIBUTE_NAME="EWALLET_CARD_TYPE";
	
	public static final String PARAM_PP_PAYMENMETHOD_NONCE ="paymentMethodNonce";
	public static final String PARAM_FIRST_NAME ="firstName";
	public static final String PARAM_LAST_NAME ="lastName";
	public static final String PARAM_EMAILID ="email";
	public static final String PARAM_ORIGIN ="origin";
	public static final String PARAM_DEVICEID ="deviceId";
	
	public static final String PAYPAL_CLIENT_TOKEN_TXN="ClientTokenTxn";
	public static final String PAYPAL_CREATE_CUSTOMER_TXN="CreateCustomerTxn";
	public static final String PAYPAL_GET_VAULT_TOKEN_TXN="GetVaultTokenTxn";
	
	public static final String PAYPAL_TXN_SUCCESS="SUCCESS";
	public static final String PAYPAL_TXN_FAIL="FAIL";
	public static final String PAYPAL_ORIGIN_YOUR_ACCOUNT = "your_account";
	
	public static final String EWALLET_PP_WALLET_DISCONNECT="PP_Wallet_Disconnect";
	public static final String GET_PP_DEVICE_DATA="get_pp_device_data";
}
