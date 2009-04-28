
package com.freshdirect.webapp.taglib.fdstore;

public interface SystemMessageList {
	//
	// Some single quotes need to be escaped like so ''
	//
	public final static String CUSTOMER_SERVICE_CONTACT	= "1-212-796-8002";

	public final static String MSG_REQUIRED				= "This information is required.";
    public final static String MSG_TECHNICAL_ERROR    	= "Sorry, we're experiencing technical difficulties. Please try again later.";
    public final static String MSG_MISSING_INFO       	= "We're sorry; some of the information below is missing or was filled out incorrectly. Please check:";
	public final static String MSG_MISSING_SURVEY_INFO  = "We're sorry; some required information is missing. Please make sure you've answered the questions marked in red below.";
    public final static String MSG_CONTACT_INFO       	= "If you are still unable to proceed, please call us at {0}, Monday through Thursday from 6.30 a.m. to 1 a.m., Friday from 6.30 a.m. to 10 p.m., Saturday from 7:30 a.m. to 10 p.m., and Sunday from 7:30 a.m. to 1 a.m.";

    public final static String MSG_AUTHENTICATION     	=
			"Sorry, we''re unable to find an account that matches this information. Please double-check your user name and password. If you forgot your password <a href=\"/login/forget_password_main.jsp\">click here</a> for help." +
			"<br><br>NOTE: If you are certain that you''ve entered your password correctly, it may need to be reset. To reset it, <a href=\"/login/forget_password_main.jsp\">click here</a> and follow the instructions. "+
			"If you have any questions, please contact us toll-free at {0}.";
	public final static String MSG_PASSWORD_TOO_SHORT	= "Please enter a password that is at least four characters long.";
    public final static String MSG_NUMBER_OF_ATTEMPTS 	= "We are not able to recognize that security word. Please contact Customer Service for help at {0}. <a href=\"/index.jsp\">Click here</a> to browse the site. Just remember that you''ll need to sign in to view account information or to checkout.";
    public final static String MSG_AGREEMENT_CHECK 		= "You must agree to the Customer Agreement to create an account with FreshDirect. Please check the box \"I have read and agree to the Customer Agreement\" to continue.";

	public final static String MSG_EMAIL_FORMAT 		= "Please make sure your email address is in the format \"you@isp.com\"";
	public final static String MSG_EMAIL_REPEAT			= "Please enter your email again - it doesn't match what you entered above.";

	// Depot
	public final static String MSG_DEPOT_WRONGCODE		= "We''re unable to recognize this code. Try entering it again, or for assistance, contact us at {0}.";

	// Address
	public final static String MSG_UNRECOGNIZE_ADDRESS					= "Sorry, we're unable to recognize this <b>address</b>. Please make sure it's entered correctly.";
	public final static String MSG_UNRECOGNIZE_STREET_NUMBER			= "Sorry, we''re unable to recognize this address. Please make sure the <b>building number</b> is correct.";
	public final static String MSG_UNRECOGNIZE_APARTMENT_NUMBER			= "Sorry, we''re unable to recognize this apartment #. Please make sure it''s entered correctly.";
	public final static String MSG_UNRECOGNIZE_STATE					= "Sorry, we''re unable to recognize this state - please make sure it''s entered correctly.";
	public final static String MSG_APARTMENT_REQUIRED					= "An apartment # is required for this address.";
	public final static String MSG_UNRECOGNIZE_ADDRESS_POSSIBLE_MATCHES	= "Sorry, we're unable to recognize this address. Did you mean to enter one of the following instead?";
	public final static String MSG_ZIP_CODE								= "Sorry, we''re unable to recognize this zip code. Please make sure it''s entered correctly.";
	public final static String MSG_DEPOT_CODE							= "Sorry, we''re unable to recognize this code.  Please make sure it''s entered correctly.";
	public final static String MSG_DONT_DELIVER_TO_ADDRESS 				= "We're sorry; FreshDirect does not deliver to this address.  To see where we deliver, <a href=\"javascript:popup('/help/delivery_zones.jsp','large')\">click here</a>.";
	public final static String MSG_CANT_GEOCODE 						= "We''re sorry! You cannot continue at this time because your address isn''t set up in our system. We''re working to enter it as quickly as possible. Please call us toll free at {0} and we''ll inform you when the problem is corrected.<br><br>Meanwhile, feel free to continue browsing our site. We''ll store everything in your cart until you''re able to check out.";
	public final static String MSG_CANT_GEOCODE_ZIP_CHECK				= "We''re sorry! Your address isn''t set up in our system. We''re working to enter it as quickly as possible. Please call us toll free at {0} and we''ll inform you when the problem is corrected.";
	public final static String MSG_RESTRICTED_ADDRESS 					= "We're sorry; FreshDirect does not deliver to this address because it is a commercial building. Unfortunately we are only able to make deliveries to residential buildings. You may enter another address <a href=\"/checkout/step_1_enter.jsp\">here</a> or choose the Pickup option below. To see where we deliver, <a href=\"javascript:popup('/help/delivery_zones.jsp','large')\">click here</a>.";
	public final static String MSG_CORP_ADDR_OUT_OF_ZONE				= "We're sorry; FreshDirect does not offer Corporate Office Service delivery at this location.";
	

	// checkout
	public final static String MSG_CHECKOUT_AMOUNT_TOO_LARGE	= "For your protection, FreshDirect will not accept orders over $750 online. Please call FreshDirect at {0} and a customer service representative will assist you in placing this order.";
	public final static String MSG_CHECKOUT_BELOW_MINIMUM		= "We''re sorry; you cannot check out because your pretax order total of {0,number,$0.00} is under the FreshDirect {1,number,$0} minimum order requirement. <a href=\"/index.jsp\">Continue Shopping.</a>";
	public final static String MSG_CHECKOUT_BELOW_MINIMUM_PICKUP	= "We''re sorry, you cannot check out because there is a {1,number,$0} pre-tax cart minimum for all FreshDirect pickup orders. To check out with a pickup location, please add more items to your cart first. <a href=\"/index.jsp\">Continue Shopping.</a>";
	public final static String MSG_CHECKOUT_NOT_ELIGIBLE		= "Billing and Delivery addresses must be identical, so we are unable to extend the {0,number,$0} free food promotion to you (one per household). If you believe you are eligible for the promotion, please contact us at {1} before completing Checkout.";
	public final static String MSG_CHECKOUT_CART_EMPTY		= "Please add at least one item to your cart to continue checkout.";
	public final static String MSG_CHECKOUT_DUP_ORDER		= "Your order has already been placed. To see the details of this order, <a href=\"/your_account/order_history.jsp\">click here</a>. If you have any additional questions, please call us at {0}, Monday through Thursday from 6.30 a.m. to 1 a.m., Friday from 6.30 a.m. to 10 p.m., Saturday from 7:30 a.m. to 10 p.m., and Sunday from 7:30 a.m. to 1 a.m.";
	public final static String MSG_CHECKOUT_PAST_CUTOFF		= "Because the cutoff time ({0,time,short}) has passed, delivery is no longer available for tomorrow. Please select another time slot for delivery.";
	public final static String MSG_CHECKOUT_PAST_CUTOFF_MODIFY	= "Because the cutoff time ({0,time,short}) has passed, this order can no longer be modified.";
	public final static String MSG_CHECKOUT_EXPIRED_RESERVATION = "Your timeslot reservation has expired, please go back and choose another timeslot.";
	public final static String MSG_CHECKOUT_MISMATCHED_RESERVATION = "The timeslot you selected is no longer available for your address. <a href=\"/checkout/step_1_choose.jsp\">Click here to verify your Delivery Address</a> and continue checkout to select an available time slot.";
	public final static String MSG_CHECKOUT_PICKUP_DIDNOT_AGREE = "To complete Checkout, you must agree to the FreshDirect unclaimed order policy by clicking the checkbox at the bottom of the page.";
	public final static String MSG_CHECKOUT_PICKUP_CONTACT_NUMBER = "Local Contact Number is required for Fire Island orders.";
	public final static String MSG_CHECKOUT_GENERIC_FRAUD		= "Please call FreshDirect at {0} and a customer service representative will assist you in placing this order.";
	public final static String MSG_CHECKOUT_MAKEGOOD_TOO_LARGE = "Make-good order amount too large. Please contact a supervisor to process the order.";
	public final static String MSG_CHECKOUT_TIMESLOT_NA			= "Sorry, the selected timeslot is no longer available. Please try again.";
	// Credit Card
	public final static String MSG_INVALID_ACCOUNT_NUMBER 	= "This number is invalid, please double-check your account number and enter the information again.";
	public final static String MSG_CARD_EXPIRATION_DATE			= "Please double-check your credit card's expiration date and enter it again.";
	public final static String MSG_DUPLICATE_ACCOUNT_NUMBER 	= "This is a duplicate account. Please double check and make any changes or enter a different account.";

	// eCheck
	public final static String MSG_INVALID_ABA_ROUTE_NUMBER 	= "This ABA route number is invalid.";
	public final static String MSG_INVALID_BANK_ACCOUNT_TYPE 	= "This bank account type is invalid.";
	public final static String MSG_INVALID_BANK_NAME 			= "This bank name is invalid.";
	public final static String MSG_INVALID_ACCOUNT_NUMBER_VERIFY= "Account numbers don''t match.";
	public final static String MSG_ACCOUNT_NUMBER_LENGTH 		= "Account number must have at least 5 digits.";
	
	public final static String MSG_NOT_UNIQUE_INFO				= "Some of the information you provided matches an existing account, so we are unable to extend our current promotions to you (one per household). If you believe you should be eligible for promotions, please contact our Customer Service Team at {0} before completing Checkout.";

	public final static String MSG_PASSWORD_REPEAT				= "Please enter your password again - it doesn't match what you entered above.";
	public final static String MSG_PASSWORD_LENGTH 				= "Please enter a password that is at least four characters long.";
	public final static String MSG_PASSWORDLINKEXPIRED 			= "An e-mail has been sent to this account. Please check your mailbox for a message from FreshDirect.";

	public final static String MSG_UNIQUE_USERNAME				= "An account already exists with this email address.  Please enter a different one or <a href=\"/login/forget_password.jsp\">click here</a> if you''ve forgotten your password.";
	public final static String MSG_INVALID_ADDRESS				= "Sorry, we're unable to recognize this address - please make sure it's entered correctly.";
	public final static String MSG_OUTERSPACE_ADDRESS			= "We're sorry, but you must have a valid home address in the Tri-State area (New York, New Jersey and Connecticut) in order to register.";

	// cart
	public final static String MSG_QUANTITY_REQUIRED			= "Please select a quantity before adding items to your cart.";
	// CCL
	public final static String MSG_CCL_COPY_QTY_REQUIRED		= "Please select a quantity before copying items to another List.";
	public final static String MSG_IDENTIFY_CARTLINE			= "Unable to identify which cartLine to change.";
    public final static String MSG_CCL_QUANTITY_REQUIRED        = "Please select a quantity before adding items to a list";
	
	//common
	public final static String MSG_SYSTEM_ERROR			= "There has been a problem in the system.";
	public final static String MSG_NUM_REQ			= "Please enter a valid number";
	
	// password assistance (siteaccess)
	public final static String MSG_INVALID_HINT			= "Invalid or missing hint.";
	
	// redemption promo messages
	public final static String MSG_INVALID_CODE = "Sorry, this promotion code ({0}) is not valid.";
	public final static String MSG_REDEMPTION_MIN_NOT_MET = "PLEASE NOTE: offer will be applied when cart subtotal is {0,number,$0.00} or more.";
	public final static String MSG_REDEMPTION_ALREADY_USED = "The promotion code you entered has been applied to a previous order placed with this account.";
	public final static String MSG_REDEMPTION_HAS_EXPIRED = "The promotion code that you entered has expired. This promotion is no longer being offered.";
	public final static String MSG_REDEMPTION_PRODUCT_UNAVAILABLE = "The product offered is temporarily unavailable.";
	public final static String MSG_REDEMPTION_OVERRIDE_AUTOMATIC = "PLEASE NOTE: The discount applied to your order has changed because the promotion code you entered cannot be combined with other offers. To restore the previous discount, click the \"Remove\" link.";
	public final static String MSG_REDEMPTION_NO_ELIGIBLE_CARTLINES = "PLEASE NOTE: Discount will be applied when qualifying items are added to your cart.";
	public final static String MSG_INVALID_ACCESS_CODE = "Sorry, this access code ({0}) is not valid.";
	public final static String MSG_INVALID_CORP_LOCATION = "Sorry, we''re unable to recognize this Corp Location - please make sure it''s entered correctly.";
	public final static String MSG_COMMERCIAL_ADDRESS = "Sorry, according to our data this is a commercial address, eligible for our corporate delivery service, FreshDirect At The Office.";
	public final static String MSG_HOME_NO_COS_DLV_ADDRESS = "Sorry, according to our data this is a residential address, eligible for our Home Delivery service.";

	public final static String MSG_EMAIL_TO_SELF			= "Sorry, you cannot enter your own email address.";
	  
	public final static String MSG_CONTAINS_DLV_PASS		= "We''re sorry! Your cart already contains a delivery pass. Please remove it to add a different one.";
	public final static String MSG_INVALID_DLV_PASS = "The delivery pass is invalid. Please contact customer service at {0} to process the order.";
	public final static String MSG_1_UNLIMITED_PASS_EXPIRED = "We''re sorry! Your unlimited delivery pass expired on ";
	public final static String MSG_2_UNLIMITED_PASS_EXPIRED = ". Delivery charges will apply if you resubmit the order.";
	public final static String MSG_DLV_PASS_ADDED = "One Delivery was added successfully.";
	public final static String MSG_DLV_PASS_EXTENDED = "Delivery pass was extended successfully by one week.";
	public final static String MSG_DLV_PASS_SINGLE_CREDIT = "One credit added to your Delivery pass.";
	public final static String MSG_DLV_PASS_MULTIPLE_CREDIT = " credits added to your Delivery pass.";
	public final static String MSG_DLV_PASS_EXTENDED_SINGLE_WEEK = "Delivery pass extended successfully by one week.";
	public final static String MSG_DLV_PASS_EXTENDED_MULTIPLE_WEEKS = " weeks added to Delivery pass.";
	public final static String MSG_DLV_PASS_CANCELLED = "Delivery pass was cancelled successfully.";
	public final static String MSG_DLV_PASS_NOT_CANCELLED="Delivery pass could not be cancelled";
	public final static String MSG_CONTAINS_DLV_PASS_ONLY = "We're sorry, you need to have one or more deliverable item(s) in the cart to buy a DeliveryPass.";
	public final static String MSG_UNLIMITED_PASS_CANCELLED = "We''re sorry! Your unlimited delivery pass is already cancelled. Delivery charges will apply if you resubmit the order.";
	public final static String MSG_PASS_DISCONTINUED = "We're sorry! this product is no longer available.";
	// SORI MESSGAES
	public final static String MSG_PROMOTION_APPLIED_VARY1 = "PLEASE NOTE: You are currently eligible for multiple promotional offers which cannot be combined. <BR>When you check out, we will automatically choose the offer that gives you the greatest discount";
	public final static String MSG_PROMOTION_APPLIED_VARY2 = "PLEASE NOTE: You are currently eligible for multiple promotional offers which cannot be combined. We have applied the offer which gives you greater discount.";
	
}
