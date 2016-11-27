/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.customer;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.TestCase;

import com.freshdirect.TestUtil;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.ejb.ErpCustomerManagerHome;
import com.freshdirect.customer.ejb.ErpCustomerManagerSB;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.framework.core.PrimaryKey;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class ErpCustomerManagerTestCase extends TestCase{

	protected Context ctx;

	protected ErpCustomerManagerHome home;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new ErpCustomerManagerTestCase("testErpCustomerManager"));
	}

	/** Creates new SapOrderAdapterTestCase */
    public ErpCustomerManagerTestCase(String testName) {
		super(testName);
    }

	protected void setUp() throws NamingException{
		ctx = TestUtil.getInitialContext();
		home = (ErpCustomerManagerHome) ctx.lookup("freshdirect.erp.CustomerManager");

	}

	protected void tearDown() throws NamingException{
		ctx.close();
	}

	public void testErpCustomerManager() throws CreateException, RemoteException, ErpAuthorizationException, ErpFraudException {

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
		//conf.put("PROPERTY1", "VALUE1");
		FDConfiguration config = new FDConfiguration(40.0, "EA", conf);
		FDSku sku = new FDSku("FRU0004980", 273);
		orderLine.setSku(sku);
		orderLine.setConfiguration(config);
		orderLine.setDepartmentDesc("FRUIT");
		orderLine.setDescription("Breaburn Apple Medium");
		orderLine.setOrderLineNumber("000100");
		orderLine.setMaterialNumber("000000000200200201");
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
		appliedCredit.setCustomerCreditPk(new PrimaryKey("1812"));

		List appliedCredits = new ArrayList();
		appliedCredits.add(appliedCredit);
		orderModel.setAppliedCredits(appliedCredits);

		orderModel.setPricingDate(Calendar.getInstance().getTime());
		orderModel.setRequestedDate(Calendar.getInstance().getTime());
		orderModel.setTransactionDate(Calendar.getInstance().getTime());
		orderModel.setTransactionSource(EnumTransactionSource.WEBSITE);

		ErpCustomerManagerSB sb = home.create();
		sb.placeOrder(new PrimaryKey("1812"), orderModel, new HashSet());


	}

}

