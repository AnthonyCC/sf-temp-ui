package com.freshdirect.payment;

import com.braintreegateway.Customer;
import com.braintreegateway.PayPalAccount;
import com.braintreegateway.PaymentMethod;
import com.braintreegateway.Transaction;
import com.freshdirect.fdstore.FDPayPalServiceException;

public class PayPalResponse extends Result{

	
   private FDPayPalServiceException exception;

   private  PayPalAccount payPalAccount;
   private  Customer customer;
   private  PaymentMethod paymentResult;
   private  Transaction  transactions;
   private  com.braintreegateway.Result<Customer> customerResults;
   private  com.braintreegateway.Result<PaymentMethod> pamentMethodResults;
   private TransactionModel TransactionModel;
   private PayPalAccountModel payPalAccountModel;
   
	private String orderNumber;
	private String merchantId;
	private String merchentEmailId;
	private String token; 
	private String customerId;
	private String taxId;
	private String customerFirstName;
	private String customerLastName;
	private String paymentMethodNonce;
	private String deviceId;
	private String profileId;
    
	
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchentEmailId() {
		return merchentEmailId;
	}
	public void setMerchentEmailId(String merchentEmailId) {
		this.merchentEmailId = merchentEmailId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getTaxId() {
		return taxId;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	public String getCustomerFirstName() {
		return customerFirstName;
	}
	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}
	public String getCustomerLastName() {
		return customerLastName;
	}
	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}
	public String getPaymentMethodNonce() {
		return paymentMethodNonce;
	}
	public void setPaymentMethodNonce(String paymentMethodNonce) {
		this.paymentMethodNonce = paymentMethodNonce;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getProfileId() {
		return profileId;
	}
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}


	public com.braintreegateway.Result<PaymentMethod> getPamentMethodResults() {
	return pamentMethodResults;
}
public void setPamentMethodResults(
		com.braintreegateway.Result<PaymentMethod> pamentMethodResults) {
	this.pamentMethodResults = pamentMethodResults;
}
	public FDPayPalServiceException getException() {
		return exception;
	}
	public void setException(FDPayPalServiceException exception) {
		this.exception = exception;
	}
	public PayPalAccount getPayPalAccount() {
		return payPalAccount;
	}
	public void setPayPalAccount(PayPalAccount payPalAccount) {
		this.payPalAccount = payPalAccount;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public  PaymentMethod getPaymentResult() {
		return paymentResult;
	}
	public void setPaymentResult( PaymentMethod paymentResult) {
		this.paymentResult = paymentResult;
	}
	public Transaction getTransactions() {
	return transactions;
	}
	public void setTransactions(
			Transaction transactions) {
		this.transactions = transactions;
	}
	public com.braintreegateway.Result<Customer> getCustomerResults() {
		return customerResults;
	}
	public void setCustomerResults(com.braintreegateway.Result<Customer> customerResults) {
		this.customerResults = customerResults;
	}
	public TransactionModel getTransactionModel() {
		return TransactionModel;
	}
	public void setTransactionModel(TransactionModel transactionModel) {
		TransactionModel = transactionModel;
	}
	public PayPalAccountModel getPayPalAccountModel() {
		return payPalAccountModel;
	}
	public void setPayPalAccountModel(PayPalAccountModel payPalAccountModel) {
		this.payPalAccountModel = payPalAccountModel;
	}

}
