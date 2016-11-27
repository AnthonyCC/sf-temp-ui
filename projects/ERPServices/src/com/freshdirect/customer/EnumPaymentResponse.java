/*
 * EnumPaymentResponse.java
 *
 * Created on November 15, 2001, 10:38 PM
 */

package com.freshdirect.customer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;



/**
 *
 * @author  jmccarter
 * @version
 */
public class EnumPaymentResponse extends ValuedEnum { 
//implements java.io.Serializable {

    public final static EnumPaymentResponse APPROVED		= new EnumPaymentResponse(0, "A", "Approved");
    public final static EnumPaymentResponse DECLINED		= new EnumPaymentResponse(1, "D", "Payment Method Declined");
    public final static EnumPaymentResponse CALL			= new EnumPaymentResponse(2, "C", "Please Call");
    public final static EnumPaymentResponse PICK_UP_CARD	= new EnumPaymentResponse(3, "P", "Ther is a problem with the card take it from cardholder");
    public final static EnumPaymentResponse EXPIRED_CARD	= new EnumPaymentResponse(4, "X", "Expired Card");
    public final static EnumPaymentResponse ERROR			= new EnumPaymentResponse(5, "E", "A processing error has occured");
    
    public final static EnumPaymentResponse INSUFFIENT_FUNDS_R			= new EnumPaymentResponse(6, "R01", "Insufficient Funds");
    public final static EnumPaymentResponse CLOSED_ACCOUNT_R			= new EnumPaymentResponse(7, "R02", "Closed Account");
    public final static EnumPaymentResponse UNKNOWN_ACCOUNT_R			= new EnumPaymentResponse(8, "R03", "Unknown Account");
    public final static EnumPaymentResponse INVALID_ACCOUNT_NUMBER_R	= new EnumPaymentResponse(9, "R04", "Invalid Account Number");
    public final static EnumPaymentResponse RETURNED_BY_OFDI_R			= new EnumPaymentResponse(10, "R06", "Returned by OFDI");
    public final static EnumPaymentResponse AUTH_REVOKED_BY_CUST_R		= new EnumPaymentResponse(11, "R07", "Authorization Revoked By Customer");
    public final static EnumPaymentResponse PAYMENT_STOPPED_R			= new EnumPaymentResponse(12, "R08", "Payment Stopped");
    public final static EnumPaymentResponse UNCOLLECTED_FUNDS_R			= new EnumPaymentResponse(13, "R09", "Uncollected Funds");
    public final static EnumPaymentResponse NOT_AUTHORIZED_BY_CUST_R	= new EnumPaymentResponse(14, "R10", "Not Authorization By Customer");
    public final static EnumPaymentResponse BRANCH_SOLD_R				= new EnumPaymentResponse(15, "R12", "Branch Sold To Another DFI");
    public final static EnumPaymentResponse RFDI_NOT_QUALIFIED_R		= new EnumPaymentResponse(16, "R13", "RFDI Not Qualified To Participate");
    public final static EnumPaymentResponse BENEFICIARY_DECEASED_R		= new EnumPaymentResponse(17, "R14", "Beneficiary Deceased");
    public final static EnumPaymentResponse ACCOUNT_HOLDER_DECEASED_R	= new EnumPaymentResponse(18, "R15", "Account Holder Deceased");    
    public final static EnumPaymentResponse FROZEN_ACCOUNT_R			= new EnumPaymentResponse(19, "R16", "Frozen Account");
    public final static EnumPaymentResponse REFER_TO_PAYER_R			= new EnumPaymentResponse(20, "R17", "Refer To Payer");
    public final static EnumPaymentResponse NOT_TRANSACTION_ACCOUNT_R	= new EnumPaymentResponse(21, "R20", "Non-Transaction Account");
    public final static EnumPaymentResponse NOT_AUTHORIZED_BY_CORP_CUST_R= new EnumPaymentResponse(22, "R29", "Corporate Customer Advises Not Authorized");
    public final static EnumPaymentResponse MISROUTED_DISHONOR_R		= new EnumPaymentResponse(23, "R71", "Misrouted Dishonored");
    public final static EnumPaymentResponse UNTIMELY_DISHONOR_R			= new EnumPaymentResponse(24, "R72", "Untimely Dishonor");
    public final static EnumPaymentResponse TIMELY_ORIGINAL_RETURN_R	= new EnumPaymentResponse(25, "R73", "Timely Original Return");
    public final static EnumPaymentResponse CORRECT_RETURN_R			= new EnumPaymentResponse(26, "R74", "Corrected Return");

    public final static EnumPaymentResponse INSUFFIENT_FUNDS_D			= new EnumPaymentResponse(27, "D01", "Insufficient Funds");
    public final static EnumPaymentResponse CLOSED_ACCOUNT_D			= new EnumPaymentResponse(28, "D02", "Closed Account");
    public final static EnumPaymentResponse UNKNOWN_ACCOUNT_D			= new EnumPaymentResponse(29, "D03", "Unknown Account");
    public final static EnumPaymentResponse INVALID_ACCOUNT_NUMBER_D	= new EnumPaymentResponse(30, "D04", "Invalid Account Number");
    public final static EnumPaymentResponse RETURNED_BY_OFDI_D			= new EnumPaymentResponse(31, "D06", "Returned by OFDI");
    public final static EnumPaymentResponse AUTH_REVOKED_BY_CUST_D		= new EnumPaymentResponse(32, "D07", "Authorization Revoked By Customer");
    public final static EnumPaymentResponse PAYMENT_STOPPED_D			= new EnumPaymentResponse(33, "D08", "Payment Stopped");
    public final static EnumPaymentResponse UNCOLLECTED_FUNDS_D			= new EnumPaymentResponse(34, "D09", "Uncollected Funds");
    public final static EnumPaymentResponse NOT_AUTHORIZED_BY_CUST_D	= new EnumPaymentResponse(35, "D10", "Not Authorization By Customer");
    public final static EnumPaymentResponse BRANCH_SOLD_D				= new EnumPaymentResponse(36, "D12", "Branch Sold To Another DFI");
    public final static EnumPaymentResponse RFDI_NOT_QUALIFIED_D		= new EnumPaymentResponse(37, "D13", "RFDI Not Qualified To Participate");
    public final static EnumPaymentResponse BENEFICIARY_DECEASED_D		= new EnumPaymentResponse(38, "D14", "Beneficiary Deceased");
    public final static EnumPaymentResponse ACCOUNT_HOLDER_DECEASED_D	= new EnumPaymentResponse(39, "D15", "Account Holder Deceased");    
    public final static EnumPaymentResponse FROZEN_ACCOUNT_D			= new EnumPaymentResponse(40, "D16", "Frozen Account");
    public final static EnumPaymentResponse REFER_TO_PAYER_D			= new EnumPaymentResponse(41, "D17", "Refer To Payer");
    public final static EnumPaymentResponse NOT_TRANSACTION_ACCOUNT_D	= new EnumPaymentResponse(42, "D20", "Non-Transaction Account");
    public final static EnumPaymentResponse NOT_AUTHORIZED_BY_COPR_CUST_D= new EnumPaymentResponse(43, "D29", "Corporate Customer Advises Not Authorized");
    public final static EnumPaymentResponse MISROUTED_DISHONOR_D		= new EnumPaymentResponse(44, "D71", "Misrouted Dishonored");
    public final static EnumPaymentResponse UNTIMELY_DISHONOR_D			= new EnumPaymentResponse(45, "D72", "Untimely Dishonor");
    public final static EnumPaymentResponse TIMELY_ORIGINAL_RETURN_D	= new EnumPaymentResponse(46, "D73", "Timely Original Return");
    public final static EnumPaymentResponse CORRECT_RETURN_D			= new EnumPaymentResponse(47, "D74", "Corrected Return");

    // from payment tech for EChecks
    public final static EnumPaymentResponse APPROVED_PT					= new EnumPaymentResponse(100, "100", "Approved");
    public final static EnumPaymentResponse VALIDATED_PT				= new EnumPaymentResponse(101, "101", "Validated");
    public final static EnumPaymentResponse VERIFIED_PT					= new EnumPaymentResponse(102, "102", "Verified");
    public final static EnumPaymentResponse PRE_NOTE_PT					= new EnumPaymentResponse(103, "103", "Passed Pre-Note");
    public final static EnumPaymentResponse NO_REASON_TO_DECLINE_PT		= new EnumPaymentResponse(104, "104", "No Reason To Decline");
    public final static EnumPaymentResponse BAD_AMOUNT_PT				= new EnumPaymentResponse(202, "202", "Bad Amount");
    public final static EnumPaymentResponse ZERO_AMOUNT_PT				= new EnumPaymentResponse(203, "203", "Zero Amount");
    public final static EnumPaymentResponse OTHER_ERROR_PT				= new EnumPaymentResponse(204, "204", "Other Error");
    public final static EnumPaymentResponse INVALID_CURRENCY_PT			= new EnumPaymentResponse(238, "238", "Invalid Currency");
    public final static EnumPaymentResponse INVALID_MOP_PT				= new EnumPaymentResponse(239, "239", "Invalid MOP");
    public final static EnumPaymentResponse CHECK_CONVERSION_ERROR_PT	= new EnumPaymentResponse(247, "247", "Check Conversion Error");
    public final static EnumPaymentResponse INVALID_TRANSACTION_TYPE_PT	= new EnumPaymentResponse(253, "253", "Invalid Transaction Type");
    public final static EnumPaymentResponse ON_NEGATIVE_FILE_PT			= new EnumPaymentResponse(519, "519", "On Negative File");
    public final static EnumPaymentResponse INVALID_TRANSIT_NUMBER_PT	= new EnumPaymentResponse(750, "750", "Invalid Transit Number");
    public final static EnumPaymentResponse UNKNOWN_TRANSIT_NUMBER_PT	= new EnumPaymentResponse(751, "751", "Unknown Transit Number");
    public final static EnumPaymentResponse MISSING_NAME_PT				= new EnumPaymentResponse(752, "752", "Missing Name");
    public final static EnumPaymentResponse INVALID_ACCOUNT_TYPE_PT		= new EnumPaymentResponse(753, "753", "Invalid Account Type");
    public final static EnumPaymentResponse CLOSED_ACCOUNT_PT			= new EnumPaymentResponse(754, "754", "Closed Account");
    public final static EnumPaymentResponse UNKNOWN_ACCOUNT_PT			= new EnumPaymentResponse(755, "755", "Unknown Account At Bank");
    public final static EnumPaymentResponse ACCOUNT_HOLDER_DECEASED_PT	= new EnumPaymentResponse(756, "756", "Account Holder Deceased");    
    public final static EnumPaymentResponse BENEFICIARY_DECEASE_PT		= new EnumPaymentResponse(757, "757", "Beneficiary Deceased");
    public final static EnumPaymentResponse FROZEN_ACCOUNT_PT			= new EnumPaymentResponse(758, "758", "Frozen Account");
    public final static EnumPaymentResponse CUSTOMER_OPT_OUT_PT			= new EnumPaymentResponse(759, "759", "Customer has refused transaction");
    public final static EnumPaymentResponse ACH_NON_PARTICIPANT_PT		= new EnumPaymentResponse(760, "760", "Bank is non participant");
    public final static EnumPaymentResponse NO_ADDRESS_PT				= new EnumPaymentResponse(762, "762", "Address is missing on deposit");
    public final static EnumPaymentResponse INVALID_ACCOUNT_NUMBER_PT	= new EnumPaymentResponse(763, "763", "Invalid Account Number");
    public final static EnumPaymentResponse AUTH_REVOKED_BY_CUST_PT		= new EnumPaymentResponse(764, "764", "Authorization Revoked By Customer");
    public final static EnumPaymentResponse CUST_ADVISES_NON_AUTH_PT	= new EnumPaymentResponse(765, "765", "Customer has not authorized bank to accept transaction");
    public final static EnumPaymentResponse INVALID_ACCOUNT_NUM_FORMAT_PT= new EnumPaymentResponse(767, "767", "Invalid Account Number Format");
    public final static EnumPaymentResponse BAD_ACCOUNT_NUMBER_DATA_PT	= new EnumPaymentResponse(768, "768", "Bad Account Number Format");
    public final static EnumPaymentResponse NON_CONVERTIBLE_ACCOUNT_PT	= new EnumPaymentResponse(769, "769", "Account is ineligible for check conversion");
    public final static EnumPaymentResponse INVALID_MERCHANT_PT			= new EnumPaymentResponse(833, "833", "Invalid Merchant");
    public final static EnumPaymentResponse UNAUTHORIZED_USER_PT		= new EnumPaymentResponse(834, "834", "Unauthorized User");

    public final static EnumPaymentResponse PAYMENTECH_NOT_AUTHORIZE	= new EnumPaymentResponse(900, "0", "Paymentech Did Not Authorize");
    

    private String description;
		
	public static EnumPaymentResponse getEnum(String code) {
		return (EnumPaymentResponse) getEnum(EnumPaymentResponse.class, code);
	}

	public static EnumPaymentResponse getEnum(int id) {
		return (EnumPaymentResponse) getEnum(EnumPaymentResponse.class, id);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumPaymentResponse.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumPaymentResponse.class);
	}

	public static Iterator iterator() {
		return iterator(EnumPaymentResponse.class);
	}
	
	public String getDescription(){
		return this.description;
	}

	public String toString() {
		return this.description;		
	}
    
    private EnumPaymentResponse(int id, String name, String description) {
		super(name, id);
        this.description = description;
    }

    public String getCode() {
		return getName();
	}	
}
