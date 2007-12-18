/*
 * SapOrderAdapterTestCase.java
 *
 * Created on December 12, 2001, 8:59 PM
 */

package com.freshdirect.customer;

/**
 *
 * @author  knadeem
 * @version
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.naming.NamingException;

import junit.framework.TestCase;

import com.freshdirect.common.address.BasicContactAddressI;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.CreditCardI;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.customer.adapter.SapOrderAdapter;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.sap.SapCustomerI;


public class SapOrderAdapterTestCase extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new SapOrderAdapterTestCase("testSapOrderAdapter"));
	}

	/** Creates new SapOrderAdapterTestCase */
    public SapOrderAdapterTestCase(String testName) {
		super(testName);
    }

	protected void setUp() throws NamingException {
	}

	protected void tearDown() throws NamingException{
	}

	public void testSapOrderAdapter() throws PricingException, FDResourceException {

		ErpCreateOrderModel orderModel = new ErpCreateOrderModel();

		ErpDeliveryInfoModel deliveryInfo = new ErpDeliveryInfoModel();
		deliveryInfo.setDeliveryStartTime(Calendar.getInstance().getTime());
		deliveryInfo.setDeliveryEndTime(Calendar.getInstance().getTime());
		//deliveryInfo.setDeliverySurcharge(9.99);
		deliveryInfo.setDeliveryZone("146");
		ErpAddressModel address = new ErpAddressModel();
		address.setAddress1("112 Fulton St");
		address.setAddress2("test");
		address.setApartment("3C");
		address.setCity("New York");
		address.setState("NY");
		address.setZipCode("10038");
		address.setCountry("US");
		deliveryInfo.setDeliveryAddress(address);
		orderModel.setDeliveryInfo(deliveryInfo);

		ErpCreditCardModel creditCard = new ErpCreditCardModel();
		Calendar expiration = new GregorianCalendar();
		expiration.add(Calendar.YEAR, 2);

		creditCard.setName("Python martini");
		creditCard.setAccountNumber("41111111111111111");
		creditCard.setExpirationDate(expiration.getTime());
		creditCard.setCardType(EnumCardType.getCardType("VISA"));
		creditCard.setAddress1("112 Fulton St");
		creditCard.setAddress2("test");
		creditCard.setApartment("3C");
		creditCard.setCity("New York");
		creditCard.setState("NY");
		creditCard.setZipCode("10038");
		creditCard.setCountry("US");
		orderModel.setPaymentMethod(creditCard);

		ErpOrderLineModel orderLine = new ErpOrderLineModel();
		orderLine.setConfigurationDesc("TEST CONFIGURATION");
		HashMap conf = new HashMap();
		conf.put("PROPERTY1", "VALUE1");
		FDConfiguration config = new FDConfiguration(10.0, "lb", conf);
		FDSku sku = new FDSku("FRU0005007", 40);
		orderLine.setSku(sku);
		orderLine.setConfiguration(config);
		orderLine.setDepartmentDesc("TEST DEPARTMENT");
		orderLine.setDescription("TEST DESCRIPTION");
		orderLine.setOrderLineNumber("1234");
		orderLine.setMaterialNumber("SAP12345");
		orderLine.setPrice(100.00);
		orderLine.setDiscount(null);
		List orderLines = new ArrayList();
		orderLines.add(orderLine);
		orderModel.setOrderLines(orderLines);

		ErpAppliedCreditModel appliedCredit = new ErpAppliedCreditModel();
		appliedCredit.setAmount(10.0);
		appliedCredit.setDepartment("FRUIT");
		//appliedCredit.setDescription("Customer not happy");
		//appliedCredit.setReasonCode("100");
		appliedCredit.setCustomerCreditPk(new PrimaryKey("2096"));

		List appliedCredits = new ArrayList();
		appliedCredits.add(appliedCredit);
		orderModel.setAppliedCredits(appliedCredits);

		orderModel.setPricingDate(Calendar.getInstance().getTime());
		orderModel.setRequestedDate(Calendar.getInstance().getTime());
		orderModel.setTransactionDate(Calendar.getInstance().getTime());
		orderModel.setTransactionSource(EnumTransactionSource.WEBSITE);

		ErpCustomerModel customerModel = new ErpCustomerModel();

		customerModel.setUserId("python@fd.com");
		customerModel.setPasswordHash("martini");

		ErpCustomerInfoModel infoModel = new ErpCustomerInfoModel();
		infoModel.setTitle("Mr");
		infoModel.setFirstName("Python");
		infoModel.setMiddleName("Test");
		infoModel.setLastName("Martini");
		infoModel.setEmail("python@fd.com");
		infoModel.setEmailPlaintext(true);
		//set Home phone number
		PhoneNumber homePhone = new PhoneNumber("212-555-1212");
		infoModel.setHomePhone(homePhone);
		//set business phone number
		PhoneNumber busPhone = new PhoneNumber("212-555-1212", "1149");
		infoModel.setBusinessPhone(busPhone);
		//set cell phone number
		PhoneNumber cellPhone = new PhoneNumber("212-555-1212");
		infoModel.setCellPhone(cellPhone);
		//set other phone number
		PhoneNumber otherPhone = new PhoneNumber("212-555-1212");
		infoModel.setOtherPhone(otherPhone);
		//set fax number
		PhoneNumber faxNumber = new PhoneNumber("212-555-1212");
		infoModel.setFax(faxNumber);
		infoModel.setWorkDepartment("Test");

		customerModel.setCustomerInfo(infoModel);
		SapOrderAdapter orderAdapter = new SapOrderAdapter(orderModel, customerModel);
		
		SapCustomerI customer = orderAdapter.getCustomer();
		BasicContactAddressI shipTo = customer.getShipToAddress();
		ErpPaymentMethodI ccd = customer.getPaymentMethod();
		BasicContactAddressI billTo = customer.getBillToAddress();

		assertEquals("Checking name", infoModel.getFirstName(), shipTo.getFirstName());
		assertEquals("Checking name", infoModel.getLastName(), shipTo.getLastName());
		assertEquals("Checking shipTo address",	address.getAddress1(), shipTo.getAddress1());
		assertEquals("Checking shipTo address",	address.getAddress2(), shipTo.getAddress2());
		assertEquals("Checking shipTo address",	address.getApartment(), shipTo.getApartment());
		assertEquals("Checking shipTo address", address.getCity(), shipTo.getCity());
		assertEquals("Checking shipTo address", address.getState(), shipTo.getState());
		assertEquals("Checking shipTo address", address.getCountry(), shipTo.getCountry());

		assertEquals("Checking Credit Card", creditCard.getAccountNumber(), ccd.getAccountNumber());

		assertEquals("Checking Credit Card", creditCard.getAddress1(), billTo.getAddress1());
		assertEquals("Checking Credit Card", creditCard.getAddress2(), billTo.getAddress2());
		assertEquals("Checking Credit Card", creditCard.getApartment(), billTo.getApartment());
		assertEquals("Checking Credit Card", creditCard.getCity(), billTo.getCity());
		assertEquals("Checking Credit Card", creditCard.getState(), billTo.getState());
		assertEquals("Checking Credit Card", creditCard.getCountry(), billTo.getCountry());

	}

}

