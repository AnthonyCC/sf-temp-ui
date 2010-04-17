package com.freshdirect.customer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.framework.collection.CollectionException;
import com.freshdirect.framework.collection.LocalObjectList;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * ErpCustomer model class.
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpCustomerModel extends ModelSupport implements ErpCustomerI {
	
	private static final long	serialVersionUID	= -8450312250626317852L;

	/**
	 * @link aggregationByValue
	 * @directed
	 * @associates <{ErpAddressModel}>
	 * @label ship to
	 */
	private LocalObjectList shipToAddress = new LocalObjectList();

	private LocalObjectList paymentMethodList = new LocalObjectList();

	private LocalObjectList customerCredits = new LocalObjectList();

	private LocalObjectList customerAlerts = new LocalObjectList();

	private ErpCustomerInfoModel customerInfo;
	
	//This field is required to capture default bill to address entered for GC customers only.
	//due to the fact GC customers do not require a delivery/ship to address. This is a 
	//non-persistent field.
	private ContactAddressModel sapBillToAddress;

	private String userId;
	private String passwordHash;
	private String sapId;
	private boolean active;

	public String getUserId() {
		return this.userId;
	}
	public void setUserId(String userId){
	    this.userId = userId;
	}

	public void setPasswordHash(String password) {
		this.passwordHash = password;
	}

	public String getPasswordHash(){
	    return this.passwordHash;
	}

	public String getSapId() {
		return this.sapId;
	}

	public void setSapId(String sapId) {
		this.sapId = sapId;
	}

	/**
	 * Get ShipToAddresss.
	 *
	 * @return collection of ShipToAddress model objects
	 */
	public List<ErpAddressModel> getShipToAddresses() {
		// shallow copy
		return (List<ErpAddressModel>)this.shipToAddress.clone();
	}

	/**
	 * Add a new ShipToAddress.
	 *
	 * @param element ShipToAddress model object
	 */
	public void addShipToAddress(ErpAddressModel element) {
		this.shipToAddress.add(element);
	}

	/**
	 * Update an existing ShipToAddress, based on PK.
	 *
	 * @param element ShipToAddress model object, with PK
	 *
	 * @throws CollectionException if the PK was not found.
	 */
	public void updateShipToAddress(ErpAddressModel element) {
		this.shipToAddress.update(element);
	}

	/**
	 * Remove an existing ShipToAddress by PK.
	 *
	 * @param id ShipToAddress PK to remove
	 *
	 * @return false if not found
	 */
	public boolean removeShipToAddress(PrimaryKey pk) {
		return null != this.shipToAddress.removeByPK(pk);
	}

	public List<ErpCustomerCreditModel> getCustomerCredits() {
		// shallow copy
		return (List<ErpCustomerCreditModel>) this.customerCredits.clone();
	}

	public void addCustomerCredit(ErpCustomerCreditModel element){
		this.customerCredits.add(element);
	}
	
	public List<ErpCustomerCreditModel> getCreditsForComplaint( String complaintId ){
		List<ErpCustomerCreditModel> complaintCredits = new ArrayList<ErpCustomerCreditModel>();
		for(Iterator<ErpCustomerCreditModel> i = this.customerCredits.iterator(); i.hasNext();){
			ErpCustomerCreditModel customerCredit = (ErpCustomerCreditModel)i.next();
			if(customerCredit.getComplaintPk().getId().equals(complaintId)){
				complaintCredits.add(customerCredit);
			}
		}
		return complaintCredits;
	}

	public void updateCustomerCredit(ErpCustomerCreditModel element){
		this.customerCredits.update(element);
	}

	public boolean removeCustomerCredit(PrimaryKey pk){
		return null != this.customerCredits.removeByPK(pk);
	}

	public double getCustomerCreditRemainingAmount() {
		double remainingCreditAmount = 0.0;
		for (Iterator<ErpCustomerCreditModel> it = this.customerCredits.iterator(); it.hasNext(); ) {
			ErpCustomerCreditModel credit = (ErpCustomerCreditModel) it.next();
			remainingCreditAmount += credit.getRemainingAmount();
		}
		return remainingCreditAmount;
	}

	public ErpCustomerInfoModel getCustomerInfo() {
		return this.customerInfo;
	}

	public void setCustomerInfo(ErpCustomerInfoModel customerInfo) {
		this.customerInfo = customerInfo;
	}

	public boolean isActive() {
	    return this.active;
	}

	public void setActive(boolean active) {
	    this.active = active;
	}

	public List<ErpPaymentMethodI> getPaymentMethods() {
	    return (List<ErpPaymentMethodI>)this.paymentMethodList.clone();
	}

	public void addPaymentMethod(ErpPaymentMethodI element){
		this.paymentMethodList.add(element);
	}

	public void updatePaymentMethod(ErpPaymentMethodI element) {
		this.paymentMethodList.update((ErpPaymentMethodModel)element);
	}

	public boolean removePaymentMethod(PrimaryKey pk) {
	    return null != this.paymentMethodList.removeByPK(pk);
	}

	public boolean isOnAlert() {
	    return this.customerAlerts == null || this.customerAlerts.size()> 0;
	}

	public List<ErpCustomerAlertModel> getCustomerAlerts() {
	    return (List<ErpCustomerAlertModel>) this.customerAlerts.clone();
	}

	public void addCustomerAlert(ErpCustomerAlertModel element){
		this.customerAlerts.add(element);
	}

	public void updateCustomerAlert(ErpCustomerAlertModel element) {
		this.customerAlerts.update(element);
	}

	public boolean removeCustomerAlert(PrimaryKey pk) {
	    return null != this.customerAlerts.removeByPK(pk);
	}
	public ContactAddressModel getSapBillToAddress() {
		return sapBillToAddress;
	}
	public void setSapBillToAddress(ContactAddressModel sapBillToAddress) {
		this.sapBillToAddress = sapBillToAddress;
	}
}
