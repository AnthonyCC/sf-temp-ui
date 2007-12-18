package com.freshdirect.customer.adapter;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.address.BasicContactAddressI;
import com.freshdirect.common.address.ContactAddressAdapter;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.PaymentMethodI;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.sap.SapCustomerI;

public class CustomerAdapter implements SapCustomerI {

	private final String sapCustomerNumber;
	private final BasicContactAddressI shipToAddress;
	private final BasicContactAddressI billToAddress;
	private final PaymentMethodI paymentMethod;
	private final BasicContactAddressI alternateAddress;

	public CustomerAdapter(boolean phonePrivate, ErpCustomerModel erpCustomer, BasicContactAddressI shipToAddress, BasicContactAddressI billToAddress) {
		this.sapCustomerNumber = erpCustomer.getSapId();
		ErpCustomerInfoModel customerInfo = erpCustomer.getCustomerInfo();
		this.shipToAddress = shipToAddress==null ? null : new ContactAddressAdapter(shipToAddress, customerInfo.getFirstName(), customerInfo.getLastName(), filterPhone(phonePrivate, shipToAddress.getPhone()));
		this.billToAddress = billToAddress==null ? null : new ContactAddressAdapter(billToAddress, customerInfo.getFirstName(), customerInfo.getLastName(), filterPhone(phonePrivate, billToAddress.getPhone()));
		this.paymentMethod = null;
		this.alternateAddress = null;
	}

	public CustomerAdapter(boolean phonePrivate, ErpCustomerModel erpCustomer, BasicContactAddressI shipToAddress, ErpPaymentMethodI erpPaymentMethod, BasicContactAddressI alternateAddress) {
		this.sapCustomerNumber = erpCustomer.getSapId();
	
		this.alternateAddress = alternateAddress;
	
		ErpCustomerInfoModel customerInfo = erpCustomer.getCustomerInfo();
		this.shipToAddress = new ContactAddressAdapter(shipToAddress, shipToAddress.getFirstName(), shipToAddress.getLastName(), filterPhone(phonePrivate, shipToAddress.getPhone()));

		String name = erpPaymentMethod.getName();
		int pos = name.indexOf(' ');
		if (pos==-1) {
			this.billToAddress = new ContactAddressAdapter(erpPaymentMethod, "", name);
		} else {
			// cut up name on credit card to first/lastname
			this.billToAddress = new ContactAddressAdapter(erpPaymentMethod, name.substring(0,pos), name.substring(pos+1));
		}
	
		// JCN --  MUST DEFINE AN ECHECK ADAPTER AND CHANGE THIS
		this.paymentMethod = new PaymentMethodAdapter(erpPaymentMethod);
	}
	
	private static PhoneNumber filterPhone(boolean phonePrivate, PhoneNumber phone) {
		return phonePrivate ? ErpServicesProperties.getPhoneDispatch() : phone;
	}

	public BasicContactAddressI getShipToAddress() {
		return this.shipToAddress;
	}

	public BasicContactAddressI getBillToAddress() {
		return this.billToAddress;
	}

	public PaymentMethodI getPaymentMethod() {
		return this.paymentMethod;
	}
	
	public BasicContactAddressI getAlternateAddress() {
		return this.alternateAddress;	
	}

	public String getSapCustomerNumber() {
		return this.sapCustomerNumber;
	}

}

