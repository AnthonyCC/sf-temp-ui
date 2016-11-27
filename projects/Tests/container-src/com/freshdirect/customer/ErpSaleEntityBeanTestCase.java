/*
 * $Workfile:ErpSaleEntityBeanTestCase.java$
 *
 * $Date:8/18/2003 3:40:26 PM$
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
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.TestCase;

import com.freshdirect.TestUtil;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.ejb.ErpSaleEB;
import com.freshdirect.customer.ejb.ErpSaleHome;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.framework.core.PrimaryKey;

/**
 *
 *
 * @version $Revision:7$
 * @author $Author:Kashif Nadeem$
 */
public class ErpSaleEntityBeanTestCase extends TestCase{

	protected Context ctx;

	protected ErpSaleHome home;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new ErpSaleEntityBeanTestCase("testErpSaleEB"));
	}

	/** Creates new ErpSaleEntityBeanTestCase */
    public ErpSaleEntityBeanTestCase(String testName) {
		super(testName);
    }

	protected void setUp() throws NamingException {
		ctx = TestUtil.getInitialContext();
		home = (ErpSaleHome) ctx.lookup("freshdirect.erp.Sale");
	}

	/** This method cleans up and closes all the resources used by this test
	 * @throws NamingException throws NamingException if there is a problem while
	 * closing the context.
	 */
	protected void tearDown() throws NamingException {
		ctx.close();
	}

	public void testErpSaleEB() throws RemoteException, CreateException, RemoveException, FinderException, ErpTransactionException, ErpComplaintException {
		
		ErpCreateOrderModel orderModel = new ErpCreateOrderModel();

		//
		// setup deliveryInfo
		//
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

		//
		// setup payment method
		//
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

		//
		// setup an orderline
		//
		ErpOrderLineModel orderLine = new ErpOrderLineModel();
		orderLine.setConfigurationDesc("TEST CONFIGURATION");
		HashMap conf = new HashMap();
		conf.put("PROPERTY1", "VALUE1");
		FDConfiguration config = new FDConfiguration(10.0, "lb", conf);
		FDSku sku = new FDSku("123Test", 1);
		orderLine.setSku(sku);
		orderLine.setConfiguration(config);
		orderLine.setDepartmentDesc("TEST DEPARTMENT");
		orderLine.setDescription("TEST DESCRIPTION");
		orderLine.setOrderLineNumber("000100");
		orderLine.setMaterialNumber("SAP12345");
		orderLine.setPrice(100.00);
		orderLine.setDiscount(null);
        orderLine.setDeliveryGroup(ErpOrderLineModel.DEFAULT_DELIVERY_GROUP);
		List orderLines = new ArrayList();
		orderLines.add(orderLine);
		orderModel.setOrderLines(orderLines);

		//
		// setup applied credits
		//
		ErpAppliedCreditModel appliedCredit = new ErpAppliedCreditModel();
		appliedCredit.setAmount(10.0);
		appliedCredit.setDepartment("FRUIT");
		//appliedCredit.setDescription("Customer not happy");
		//appliedCredit.setReasonCode("100");
		appliedCredit.setCustomerCreditPk(new PrimaryKey("2096"));

		List appliedCredits = new ArrayList();
		appliedCredits.add(appliedCredit);
		orderModel.setAppliedCredits(appliedCredits);

		//
		// setup various dates
		//
		orderModel.setPricingDate(Calendar.getInstance().getTime());
		orderModel.setRequestedDate(Calendar.getInstance().getTime());
		orderModel.setTransactionDate(Calendar.getInstance().getTime());
		orderModel.setTransactionSource(EnumTransactionSource.WEBSITE);

		//
		// create the bean
		//

		// !!! customer number should not be hardcoded
		ErpSaleEB eb = (ErpSaleEB)home.create(new PrimaryKey("394"), orderModel, new HashSet());

		PrimaryKey pk = (PrimaryKey) eb.getPrimaryKey();

		try {
			eb.invalidate();
			fail("Couldn't invalidate entity");
		} catch (EJBException ejbe) {
		} catch (RemoteException re) {
		}

		//
		// test findByPrimaryKey
		//
		eb = home.findByPrimaryKey(pk);
		assertEquals("Primary key id", pk.getId(), ((PrimaryKey)eb.getPrimaryKey()).getId());

		ErpSaleModel saleModel = (ErpSaleModel)eb.getModel();
		Iterator i = saleModel.getTransactions().iterator();
		ErpCreateOrderModel storedOrderModel = (ErpCreateOrderModel)i.next();
		ErpDeliveryInfoModel storedDlvInfo = storedOrderModel.getDeliveryInfo();

		ErpAddressModel storedResult = storedDlvInfo.getDeliveryAddress();
		assertEquals("Address check", address.getAddress1(), storedResult.getAddress1());
		assertEquals("Address check", address.getAddress2(), storedResult.getAddress2());
		assertEquals("Address check", address.getApartment(), storedResult.getApartment());
		assertEquals("Address check", address.getCity(), storedResult.getCity());
		assertEquals("Address check", address.getState(), storedResult.getState());
		assertEquals("Address check", address.getZipCode(), storedResult.getZipCode());
		assertEquals("Address check", address.getCountry(), storedResult.getCountry());

		ErpCreditCardModel storedCcd = (ErpCreditCardModel)storedOrderModel.getPaymentMethod();

		assertEquals("Credit Card Check", creditCard.getName(), storedCcd.getName());
		assertEquals("Credit Card Check", creditCard.getAccountNumber(), storedCcd.getAccountNumber());
		//assertEquals("Credit Card Check", creditCard.getExpirationDate(), storedCcd.getExpirationDate());
		assertEquals("Credit Card Check", creditCard.getAddress1(), storedCcd.getAddress1());
		assertEquals("Credit Card Check", creditCard.getAddress2(), storedCcd.getAddress2());
		assertEquals("Credit Card Check", creditCard.getApartment(), storedCcd.getApartment());
		assertEquals("Credit Card Check", creditCard.getCity(), storedCcd.getCity());
		assertEquals("Credit Card Check", creditCard.getState(), storedCcd.getState());
		assertEquals("Credit Card Check", creditCard.getZipCode(), storedCcd.getZipCode());
		assertEquals("Credit Card Check", creditCard.getCountry(), storedCcd.getCountry());

		List storedOrderLines = storedOrderModel.getOrderLines();
		ErpOrderLineModel storedOrderLine = (ErpOrderLineModel)storedOrderLines.get(0);

		assertEquals("OrderLine Check", orderLine.getConfigurationDesc(), storedOrderLine.getConfigurationDesc());
		assertEquals("OrderLine Check", orderLine.getDepartmentDesc(), storedOrderLine.getDepartmentDesc());
		assertEquals("OrderLine Check", orderLine.getDescription(), storedOrderLine.getDescription());
		assertEquals("OrderLine Check", orderLine.getOrderLineNumber(), storedOrderLine.getOrderLineNumber());
		assertEquals("OrderLine Check", orderLine.getMaterialNumber(), storedOrderLine.getMaterialNumber());
		assertEquals("OrderLine Check", orderLine.getPrice(), storedOrderLine.getPrice(), 0.001);
		assertEquals("OrderLine Check", orderLine.getQuantity(), storedOrderLine.getQuantity(), 0.001);
		assertEquals("OrderLine Check", orderLine.getSalesUnit(), storedOrderLine.getSalesUnit());
		assertEquals("OrderLine Check", orderLine.getSku().getSkuCode(), storedOrderLine.getSku().getSkuCode());
		assertEquals("OrderLine Check", orderLine.getSku().getVersion(), storedOrderLine.getSku().getVersion());
        assertEquals("OrderLine Check", orderLine.getDeliveryGroup(), storedOrderLine.getDeliveryGroup());

		eb.createOrderComplete("SAP123987");
		try {
			eb.invalidate();
			fail("Couldn't invalidate entity");
		} catch (EJBException ejbe) {
		} catch (RemoteException re) {
		}

		// find entity now by primary key
		eb = home.findByPrimaryKey(pk);
		saleModel = (ErpSaleModel)eb.getModel();

		assertEquals("SAP Check", "SAP123987", saleModel.getSapOrderNumber());

		eb.cutoff();

		ErpInvoiceModel invoiceModel = new ErpInvoiceModel();
		invoiceModel.setTransactionDate(Calendar.getInstance().getTime());
		invoiceModel.setTransactionSource(EnumTransactionSource.SYSTEM);
		invoiceModel.setSubTotal(109.99);
		invoiceModel.setTax(9.00);
		ErpInvoiceLineModel invoiceLine = new ErpInvoiceLineModel();
		invoiceLine.setPrice(100);
		invoiceLine.setQuantity(10);
		invoiceLine.setTaxValue(.8);
		invoiceLine.setMaterialNumber("SAP12345");
		invoiceLine.setOrderLineNumber("000100");
		List invoiceLines = new ArrayList();
		invoiceLines.add(invoiceLine);
		invoiceModel.setInvoiceLines(invoiceLines);

		eb.addInvoice(invoiceModel);

//		ErpPaymentModel paymentModel = new ErpPaymentModel();
//		paymentModel.setAmount(109.99);
//		paymentModel.setPaymentMethod(creditCard);
//		paymentModel.setTransactionSource(EnumTransactionSource.SYSTEM);
//
//		eb.addPayment(paymentModel);

		ErpComplaintModel complaintModel = new ErpComplaintModel();
		complaintModel.setDescription("TEST_DESC");
		complaintModel.setCreatedBy("TEST_CSR");
		complaintModel.setCreateDate(new java.util.Date());
		complaintModel.setStatus(EnumComplaintStatus.PENDING);

		ErpComplaintLineModel lineModel = new ErpComplaintLineModel();
		lineModel.setComplaintLineNumber("0");
		lineModel.setQuantity(5.0);
		lineModel.setAmount(10.0);
		//lineModel.setDepartment("TEST_DEPARTMENT");
		//lineModel.setReasonCode("TEST_CODE");
		lineModel.setType(EnumComplaintLineType.ORDER_LINE);
		lineModel.setMethod(EnumComplaintLineMethod.STORE_CREDIT);

		List complaintLines = new ArrayList();
		complaintLines.add(lineModel);
		complaintModel.setComplaintLines(complaintLines);

		eb.addComplaint(complaintModel);

		try {
			eb.invalidate();
			fail("Couldn't invalidate entity");
		} catch (EJBException ejbe) {
		} catch (RemoteException re) {
		}

		// find entity now by primary key
		eb = home.findByPrimaryKey(pk);

		ErpAbstractOrderModel absOrder = eb.getCurrentOrder();
	}


}
