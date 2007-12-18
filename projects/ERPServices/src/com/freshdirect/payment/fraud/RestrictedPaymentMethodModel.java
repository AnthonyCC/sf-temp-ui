package com.freshdirect.payment.fraud;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumTransactionSource;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;

import java.util.Date;

public class RestrictedPaymentMethodModel extends ModelSupport {

	private String id;
	private String customerId;
	private String paymentMethodId;
	private String lastName;
	private String firstName;
	private EnumPaymentMethodType paymentMethodType;

	// credit card specific fields 
	private EnumCardType cardType;
	private Date expirationDate;
	
	// eChecks specific fields
	private String bankName;
	private EnumRestrictedPatternType abaRoutePatternType;
	private EnumBankAccountType bankAccountType;
	private String abaRouteNumber;
	
	private EnumRestrictedPatternType accountPatternType;
	private String accountNumber;
	
	private EnumRestrictedPaymentMethodStatus status;
	private EnumTransactionSource source;
	private String createUser;
	private String lastModifyUser;
	private Date createDate;
	private Date lastModifyDate;
	private String caseId;
	private EnumRestrictionReason reason;
	private String note;
	
	public RestrictedPaymentMethodModel () {
		super();
	}
		
	public RestrictedPaymentMethodModel (PrimaryKey pk){
		this();
		setPK(pk);
		setId(pk.getId());
	}
	
	public RestrictedPaymentMethodModel(RestrictedPaymentMethodModel model){
		this(new PrimaryKey(model.getId()));
		setId(model.getId());		
		setCustomerId(model.getCustomerId());
		setFirstName(model.getFirstName());
		setLastName(model.getLastName());
		setPaymentMethodType(model.getPaymentMethodType());
		setCardType(model.getCardType());
		setExpirationDate(model.getExpirationDate());
		setBankName(model.getBankName());
		setAbaRoutePatternType(model.getAbaRoutePatternType());
		setAbaRouteNumber(model.getAbaRouteNumber());
		setAccountPatternType(model.getAccountPatternType());
		setAccountNumber(model.getAccountNumber());
		setBankAccountType(model.getBankAccountType());
		setStatus(model.getStatus());
		setSource(model.getSource());
		setCreateUser(model.getCreateUser());
		setLastModifyUser(model.getLastModifyUser());
		setCreateDate(model.getCreateDate());
		setLastModifyDate(model.getLastModifyDate());
		setCaseId(model.getCaseId());
	}

	public String getId() {
		return this.id;
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public String getPaymentMethodId() {
		return this.paymentMethodId;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public EnumPaymentMethodType getPaymentMethodType() {
		return this.paymentMethodType;
	}
	
	public EnumCardType getCardType() {
		return this.cardType;
	}

	public Date getExpirationDate() {
		return this.expirationDate;
	}

	public String getBankName() {
		return this.bankName;
	}
	
	public EnumRestrictedPatternType getAbaRoutePatternType() {
		return this.abaRoutePatternType;
	}
	
	public String getAbaRouteNumber() {
		return this.abaRouteNumber;
	}

	public EnumRestrictedPatternType getAccountPatternType() {
		return this.accountPatternType;
	}

	public EnumBankAccountType getBankAccountType() {
		return this.bankAccountType;
	}

	public String getAccountNumber() {
		return this.accountNumber;
	}
	
	public EnumRestrictedPaymentMethodStatus getStatus() {
		return this.status;
	}
		
	public EnumTransactionSource getSource() {
		return this.source;
	}
	
	public String getCreateUser() {
		return this.createUser;
	}
	
	public String getLastModifyUser() {
		return this.lastModifyUser;
	}

	public Date getCreateDate() {
		return this.createDate;
	}
	
	public Date getLastModifyDate() {
		return this.lastModifyDate;
	}

	public String getCaseId() {
		return this.caseId;
	}

	public EnumRestrictionReason getReason() {
		return this.reason;
	}
	
	public String getNote() {
		return this.note;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public void setPaymentMethodId(String paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPaymentMethodType(EnumPaymentMethodType paymentMethodType) {
		this.paymentMethodType = paymentMethodType;
	}
	
	public void setCardType(EnumCardType cardType) {
		this.cardType = cardType;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	public void setAbaRoutePatternType(EnumRestrictedPatternType abaRoutePatternType) {
		this.abaRoutePatternType = abaRoutePatternType;
	}
	
	public void setAbaRouteNumber(String abaRouteNumber) {
		this.abaRouteNumber = abaRouteNumber;
	}

	public void setAccountPatternType(EnumRestrictedPatternType accountPatternType) {
		this.accountPatternType = accountPatternType;
	}

	public void setBankAccountType(EnumBankAccountType bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public void setStatus(EnumRestrictedPaymentMethodStatus status) {
		this.status = status;
	}
		
	public void setSource(EnumTransactionSource source) {
		this.source = source;
	}
	
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	public void setLastModifyUser(String lastModifyUser) {
		this.lastModifyUser = lastModifyUser;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	
	public void setReason(EnumRestrictionReason reason) {
		this.reason = reason;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
