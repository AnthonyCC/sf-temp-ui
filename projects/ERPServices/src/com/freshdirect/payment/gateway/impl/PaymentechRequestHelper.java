package com.freshdirect.payment.gateway.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.payment.gateway.CreditCard;
import com.freshdirect.payment.gateway.CreditCardType;
import com.freshdirect.payment.gateway.Currency;
import com.freshdirect.payment.gateway.ECheck;
import com.freshdirect.payment.gateway.Merchant;
import com.freshdirect.payment.gateway.PaymentMethod;
import com.freshdirect.payment.gateway.PaymentMethodType;
import com.freshdirect.payment.gateway.TransactionType;
import com.freshdirect.payment.gateway.impl.PaymentechConstants.CardType;
import com.paymentech.orbital.sdk.interfaces.RequestIF;
import com.paymentech.orbital.sdk.request.FieldNotFoundException;


final class PaymentechRequestHelper {
	
	final static List<String> validCountryCodes= Arrays.asList(PaymentechConstants.COUNTRY_CODES);
	
	private  static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat> () {
		
		  @Override
		  public DateFormat get() {
		   return super.get();
		  }
		  @Override
		  protected DateFormat initialValue() {
		   return new SimpleDateFormat("MMyy");
		  }
		  
		  @Override
		  public void remove() {
		   super.remove();
		  }
		  @Override
		  public void set(DateFormat value) {
		   super.set(value);
		  }
		 };

	
	 static void setMerchantID(String transaction,RequestIF request,Merchant merchant) throws FieldNotFoundException {
		if(merchant!=null) {
			if(RequestIF.PROFILE_TRANSACTION.equals(transaction)) {
				request.setFieldValue(PaymentechFields.ProfileRequest.CustomerMerchantID.name(),
									  PaymentechConstants.MerchantID.get(merchant).getValue());
			} else if(RequestIF.NEW_ORDER_TRANSACTION.equals(transaction)) {
				request.setFieldValue(PaymentechFields.NewOrderRequest.MerchantID.name(),
						              PaymentechConstants.MerchantID.get(merchant).getValue());
						//"700000000413");
			}
		}
	 }

	static void setBIN(String transaction, RequestIF request) throws FieldNotFoundException {
		if(RequestIF.PROFILE_TRANSACTION.equals(transaction)) {
			request.setFieldValue(PaymentechFields.ProfileRequest.CustomerBin.name(),
					              PaymentechConstants.BIN.SALEM.getCode());
		} else if(RequestIF.NEW_ORDER_TRANSACTION.equals(transaction)) {
			request.setFieldValue(PaymentechFields.NewOrderRequest.BIN.name(),
		                          PaymentechConstants.BIN.SALEM.getCode());
		}
	}
    
	static void setCCInfo(String transaction, RequestIF request, CreditCard creditCard) throws FieldNotFoundException {
		
		if(RequestIF.PROFILE_TRANSACTION.equals(transaction)) {
			request.setFieldValue(PaymentechFields.ProfileRequest.CustomerAccountType.name(),
					PaymentechConstants.AccountType.CREDIT_CARD.getCode());
			if(!StringUtil.isEmpty(creditCard.getAccountNumber()) && StringUtil.isEmpty(creditCard.getBillingProfileID())) {
				request.setFieldValue(PaymentechFields.ProfileRequest.CCAccountNum.name(),
						              creditCard.getAccountNumber());
			}
			if(creditCard.getExpirationDate()!=null) {
				request.setFieldValue(PaymentechFields.ProfileRequest.CCExpireDate.name(),
									 // SF.format(creditCard.getExpirationDate()));
								df.get().format(creditCard.getExpirationDate()));
			}
		} else if(RequestIF.NEW_ORDER_TRANSACTION.equals(transaction)) {
			if(!StringUtil.isEmpty(creditCard.getAccountNumber()) && StringUtil.isEmpty(creditCard.getBillingProfileID())) {
				request.setFieldValue(PaymentechFields.NewOrderRequest.AccountNum.name(),
						              creditCard.getAccountNumber());
				request.setFieldValue(PaymentechFields.NewOrderRequest.CardBrand.name(),
			                          PaymentechConstants.CardType.get(creditCard.getCreditCardType()).getCode());
			}
			if(creditCard.getExpirationDate()!=null) {
				request.setFieldValue(PaymentechFields.NewOrderRequest.Exp.name(),
									  //SF.format(creditCard.getExpirationDate()));
									df.get().format(creditCard.getExpirationDate()));
			}
			if(!StringUtil.isEmpty(creditCard.getCVV())) {
				if( CreditCardType.VISA.equals(creditCard.getType())|| 
					CreditCardType.DISCOVER .equals(creditCard.getType())
					) {
            		request.setFieldValue(PaymentechFields.NewOrderRequest.CardSecValInd.name(), "1");
				}
            	request.setFieldValue(PaymentechFields.NewOrderRequest.CardSecVal.name(), creditCard.getCVV());
			}
		}	
	}
	 
	static void setECheckInfo(String transaction, RequestIF request, ECheck echeck) throws FieldNotFoundException {
		
		if(RequestIF.PROFILE_TRANSACTION.equals(transaction)) {
			request.setFieldValue(PaymentechFields.ProfileRequest.CustomerAccountType.name(),
					  PaymentechConstants.AccountType.ECHECK.getCode());
			if(!StringUtil.isEmpty(echeck.getAccountNumber()) && StringUtil.isEmpty(echeck.getBillingProfileID())) {
				request.setFieldValue(PaymentechFields.ProfileRequest.ECPAccountDDA.name(),
								  	  echeck.getAccountNumber());
			}
			if(echeck.getBankAccountType()!=null) {
				request.setFieldValue(PaymentechFields.ProfileRequest.ECPAccountType.name(),
						              PaymentechConstants.BankAccountType.get(echeck.getBankAccountType()).getCode());// fix
			}
			if(!StringUtil.isEmpty(echeck.getRoutingNumber())) {
				request.setFieldValue(PaymentechFields.ProfileRequest.ECPAccountRT.name(),
						              echeck.getRoutingNumber());
			}
			request.setFieldValue(PaymentechFields.ProfileRequest.ECPBankPmtDlv.name(),
								  PaymentechConstants.ECheckPymtDelivery.ACH.getCode());
						
		} else if(RequestIF.NEW_ORDER_TRANSACTION.equals(transaction)) {

			request.setFieldValue(PaymentechFields.NewOrderRequest.CardBrand.name(),
					  PaymentechConstants.AccountType.ECHECK.getCode());
			if(!StringUtil.isEmpty(echeck.getAccountNumber()) && StringUtil.isEmpty(echeck.getBillingProfileID())) {
				request.setFieldValue(PaymentechFields.NewOrderRequest.CheckDDA.name(),
								  	  echeck.getAccountNumber());
			}
			if(echeck.getBankAccountType()!=null) {
				request.setFieldValue(PaymentechFields.NewOrderRequest.BankAccountType.name(),
						              PaymentechConstants.BankAccountType.get(echeck.getBankAccountType()).getCode());// fix
			}
			request.setFieldValue(PaymentechFields.NewOrderRequest.ECPAuthMethod.name(),
					  PaymentechConstants.ECPAUTHMETHOD);
			request.setFieldValue(PaymentechFields.NewOrderRequest.BankPmtDelv.name(),
					PaymentechConstants.BANKPMTDELV);
			
			if(!StringUtil.isEmpty(echeck.getRoutingNumber())) {
				request.setFieldValue(PaymentechFields.NewOrderRequest.BCRtNum.name(),
						              echeck.getRoutingNumber());
			}
						
		}	
	}

	static void setAddressInfo(String transaction,
			                          RequestIF request,
			                          PaymentMethod paymentMethod)
	                                  throws FieldNotFoundException {
		if(RequestIF.PROFILE_TRANSACTION.equals(transaction)) {
			if(!StringUtil.isEmpty(paymentMethod.getCustomerName())) {
				request.setFieldValue(PaymentechFields.ProfileRequest.CustomerName.name(),
						StringUtils.substring(paymentMethod.getCustomerName(), 0, 30) );
			}
			if(!StringUtil.isEmpty(paymentMethod.getAddressLine1())) {
				request.setFieldValue(PaymentechFields.ProfileRequest.CustomerAddress1.name(),
						StringUtils.substring(paymentMethod.getAddressLine1(), 0, 30));
			}
			if(!StringUtil.isEmpty(paymentMethod.getAddressLine2())) {
				request.setFieldValue(PaymentechFields.ProfileRequest.CustomerAddress2.name(),
						StringUtils.substring(paymentMethod.getAddressLine2(), 0, 30));
			}
			if(!StringUtil.isEmpty(paymentMethod.getCity())) {
				request.setFieldValue(PaymentechFields.ProfileRequest.CustomerCity.name(),
						StringUtils.substring(paymentMethod.getCity(), 0, 20));
			}
			if(!StringUtil.isEmpty(paymentMethod.getState())) {
				
				String state="";
				if(paymentMethod.getState().length()!=2) {
					if(paymentMethod.getState().length()<2)
						state=StringUtils.leftPad(paymentMethod.getState(), 2, '0');
					else 
						state=StringUtils.substring(paymentMethod.getState(), 0, 2);
				} else {
					state=paymentMethod.getState();
				}
				request.setFieldValue(PaymentechFields.ProfileRequest.CustomerState.name(),
						state);
			}
			if(!StringUtil.isEmpty(paymentMethod.getZipCode())) {
				request.setFieldValue(PaymentechFields.ProfileRequest.CustomerZIP.name(),
						StringUtils.substring(paymentMethod.getZipCode(), 0, 10));
			}
			if(!StringUtil.isEmpty(paymentMethod.getCountry())) {
				request.setFieldValue(PaymentechFields.ProfileRequest.CustomerCountryCode .name(),
						StringUtils.substring(paymentMethod.getCountry(), 0, 2));
			}
		} else if(RequestIF.NEW_ORDER_TRANSACTION.equals(transaction)) {
			if(!StringUtil.isEmpty(paymentMethod.getCustomerName())) {
				request.setFieldValue(PaymentechFields.NewOrderRequest.AVSname.name(),
						StringUtils.substring(paymentMethod.getCustomerName(), 0, 30));
			}
			
			
			if(!StringUtil.isEmpty(paymentMethod.getAddressLine1())) {
				request.setFieldValue(PaymentechFields.NewOrderRequest.AVSaddress1.name(),
						StringUtils.substring(paymentMethod.getAddressLine1(), 0, 30));
			}
			if(!StringUtil.isEmpty(paymentMethod.getAddressLine2())) {
				request.setFieldValue(PaymentechFields.NewOrderRequest.AVSaddress2.name(),
						StringUtils.substring(paymentMethod.getAddressLine2(), 0, 30));
			}
			if(!StringUtil.isEmpty(paymentMethod.getCity())) {
				request.setFieldValue(PaymentechFields.NewOrderRequest.AVScity.name(),
						StringUtils.substring(paymentMethod.getCity(), 0, 20));
			}
			if(!StringUtil.isEmpty(paymentMethod.getState())) {
				request.setFieldValue(PaymentechFields.NewOrderRequest.AVSstate.name(),
						StringUtils.substring( paymentMethod.getState(), 0, 2));
			}
			if(!StringUtil.isEmpty(paymentMethod.getZipCode())) {
				request.setFieldValue(PaymentechFields.NewOrderRequest.AVSzip.name(),
						StringUtils.substring(paymentMethod.getZipCode(), 0, 10));
			}
			if(!StringUtil.isEmpty(paymentMethod.getCountry())) {
				String countryCode=StringUtils.substring(paymentMethod.getCountry(),0,2);
				request.setFieldValue(PaymentechFields.NewOrderRequest.AVScountryCode.name(),
						validCountryCodes.contains(countryCode)?countryCode:" ");
			} else {
				request.setFieldValue(PaymentechFields.NewOrderRequest.AVScountryCode.name()," ");
			}
		}
		
	}

	static void setProfileTxInfo(TransactionType transType, RequestIF request,String profileID) throws FieldNotFoundException {
		if(TransactionType.ADD_PROFILE.equals(transType)) {
			
			
			request.setFieldValue(
					PaymentechFields.ProfileRequest.CustomerProfileAction.name(),
					PaymentechConstants.ProfileAction.CREATE.getCode());
			request.setFieldValue(
					PaymentechFields.ProfileRequest.CustomerProfileOrderOverrideInd.name(), "NO");
			request.setFieldValue(
					PaymentechFields.ProfileRequest.CustomerProfileFromOrderInd.name(), "A");
			request.setFieldValue(
					PaymentechFields.ProfileRequest.OrderDefaultDescription.name(),
					"Profile Tx");
	    } else if(TransactionType.GET_PROFILE.equals(transType)) {
	    	request.setFieldValue(PaymentechFields.ProfileRequest.CustomerProfileAction.name(),
                                  PaymentechConstants.ProfileAction.RETRIEVE.getCode());
	    } else if(TransactionType.UPDATE_PROFILE.equals(transType)) {
	    	request.setFieldValue(PaymentechFields.ProfileRequest.CustomerProfileAction.name(),
	                              PaymentechConstants.ProfileAction.UPDATE.getCode());
	    	request.setFieldValue(
					PaymentechFields.ProfileRequest.OrderDefaultDescription.name(),
					"Profile Update");
	    }else if(TransactionType.DELETE_PROFILE.equals(transType)) {
	    	request.setFieldValue(PaymentechFields.ProfileRequest.CustomerProfileAction.name(),
                                  PaymentechConstants.ProfileAction.DELETE.getCode());
	    } 
		if(!StringUtil.isEmpty(profileID)) {
			request.setFieldValue(PaymentechFields.ProfileRequest.CustomerRefNum.name(),
					              profileID);
		}
	}

	static void setProfileID(TransactionType transType, RequestIF request,String profileID) throws FieldNotFoundException {
		if(!StringUtil.isEmpty( profileID)) {
			request.setFieldValue(PaymentechFields.NewOrderRequest.CustomerRefNum.name(),
					              profileID);
		}
	}
	public static void setTerminalID(String transaction,RequestIF request) throws FieldNotFoundException {
		if(RequestIF.NEW_ORDER_TRANSACTION.equals(transaction)) { 
			request.setFieldValue(PaymentechFields.NewOrderRequest.TerminalID.name(),
	                          	  PaymentechConstants.TERMINAL_ID);
		}
	}
	public static void setMessageType(TransactionType transactionType,RequestIF request) throws FieldNotFoundException {
		if(TransactionType.CC_VERIFY.equals(transactionType)||
		   TransactionType.AUTHORIZE.equals(transactionType)||
		   TransactionType.ACH_VERIFY.equals(transactionType)
		   ) {
			
				request.setFieldValue(PaymentechFields.NewOrderRequest.MessageType.name(),
				                      PaymentechConstants.MessageType.AUTH.getCode());
				//request.setFieldValue("ECPActionCode", "VD"); 
		} else if(TransactionType.CASHBACK.equals(transactionType)) {
			request.setFieldValue(PaymentechFields.NewOrderRequest.MessageType.name(),
                    PaymentechConstants.MessageType.REFUND.getCode());
		}
	}

	public static void setMessageTypeForECheckCapture(RequestIF request) throws FieldNotFoundException {
		request.setFieldValue(PaymentechFields.NewOrderRequest.MessageType.name(),
                              PaymentechConstants.MessageType.FORCE_CAPTURE.getCode());
	}
	
	public static void setOrderID(String transaction,String transactionID,RequestIF request) throws FieldNotFoundException {
		if(RequestIF.NEW_ORDER_TRANSACTION.equals(transaction)) { 
			if(transactionID!=null) {
            	request.setFieldValue(PaymentechFields.NewOrderRequest.OrderID.name(),
            			              transactionID);
            }
		}
	}
	public static void setCurrencyCode(String transaction,Currency currency, RequestIF request) throws FieldNotFoundException {
		if(RequestIF.NEW_ORDER_TRANSACTION.equals(transaction)) { 
			request.setFieldValue(PaymentechFields.NewOrderRequest.CurrencyCode.name(),
		               PaymentechConstants.CurrencyCode.get(currency).getValue());
		}
	}

	public static void setCurrencyExponent(RequestIF request) throws FieldNotFoundException {
		request.setFieldValue(PaymentechFields.NewOrderRequest.CurrencyExponent.name(),
							  PaymentechConstants.CURRENCY_EXPONENT);		
	}

	public static void setAmount(double amount,	RequestIF request) throws FieldNotFoundException {
		request.setFieldValue(PaymentechFields.NewOrderRequest.Amount.name(), String.valueOf(Math.round(amount*100.0)));
	}
	public static void setTxRefNum(String  val,	RequestIF request) throws FieldNotFoundException {
		request.setFieldValue(PaymentechFields.NewOrderRequest.TxRefNum.name(), val);
	}
	public static void setTxRefIdx(String  val,	RequestIF request) throws FieldNotFoundException {
		request.setFieldValue(PaymentechFields.NewOrderRequest.TxRefIdx.name(), val);
	}
	public static void setOnlineReverseIndicator(String  val,	RequestIF request) throws FieldNotFoundException {
		request.setFieldValue(PaymentechFields.NewOrderRequest.OnlineReversalInd.name(), val);
	}
	
	public static void setEWalletInfo(String transaction, RequestIF request, PaymentMethod paymentMethod) throws FieldNotFoundException {
		/*
		 * Commenting this for now till Paymentech resolves MasterCard issue.
		if (PaymentMethodType.CREDIT_CARD.equals(paymentMethod.getType())) {	
			CreditCard creditCard = (CreditCard) paymentMethod;						
			if (creditCard.getEwalletId() != null && creditCard.getEwalletId().equals(PaymentechConstants.MPWALLETID) && creditCard.getCreditCardType().equals(CreditCardType.MASTERCARD)){
				request.setFieldValue(PaymentechFields.NewOrderRequest.DWWalletID.name(),
						creditCard.getVendorEWalletID());
				request.setFieldValue(PaymentechFields.NewOrderRequest.DWSLI.name(),
			               PaymentechConstants.DWSLI );
				request.setFieldValue(PaymentechFields.NewOrderRequest.DigitalWalletType.name(),
						PaymentechConstants.MASTERPASS);
				}
		}*/
			
		}
	
	public static void main(String[] a) {
		Date d=Calendar.getInstance().getTime();
		String z=df.get().format(d);
		System.out.println(z);
	}

}
