package com.freshdirect.payment.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.framework.core.PrimaryKey;

public class CaptureAmountTestCase extends TestCase {
	
	public void testCapture () {
		
		List auths = new ArrayList();
		auths.add(this.makeAuth("200", 100));
		auths.add(this.makeAuth("101", 5));
		auths.add(this.makeAuth("102", 5));
		
		double amount = 105.99;
		Map result = PaymentSessionBean.getCaptureAmounts(auths, amount);
		
		assertFalse(result.containsKey("102"));
		assertEquals(result.get("200"), new Double(100.99));
		assertEquals(result.get("101"), new Double(5.0));
		
		auths.clear();
		
		auths.add(this.makeAuth("100", 95));
		auths.add(this.makeAuth("101", 2.5));
		auths.add(this.makeAuth("102", 10));
		
		amount = 94;
		result = PaymentSessionBean.getCaptureAmounts(auths, amount);
		assertFalse(result.containsKey("101"));
		assertFalse(result.containsKey("102"));
		assertEquals(result.get("100"), new Double(94.0));
		
		auths.clear();
		
		amount = 100.0;
		result = PaymentSessionBean.getCaptureAmounts(auths, amount);
		assertTrue(result.isEmpty());
		
		auths.clear();
		
		auths.add(this.makeAuth("100", 50));
		
		amount = 100.0;
		result = PaymentSessionBean.getCaptureAmounts(auths, amount);
		assertEquals(result.get("100"), new Double(50.0));
		
		auths.clear();
		auths.add(this.makeAuth("100", 123.8));
		auths.add(this.makeAuth("101", 10.20));
		
		amount = 140.80;
		result = PaymentSessionBean.getCaptureAmounts(auths, amount);
		assertEquals(result.size(), 2);
		assertEquals(result.get("100"), new Double(123.8));
		assertEquals(result.get("101"), new Double(10.20));
		
		auths.clear();
		
		auths.add(this.makeAuth("100", 51.63));
		auths.add(this.makeAuth("101", 2.32));
		
		amount = 51.98;
		result = PaymentSessionBean.getCaptureAmounts(auths, amount);
		assertEquals(result.size(), 1);
		assertEquals(result.get("100"), new Double(51.98));
		
		auths.clear();
		auths.add(this.makeAuth("100", 51.63));
		amount = 51.98;
		result = PaymentSessionBean.getCaptureAmounts(auths, amount);
		assertEquals(result.size(), 1);
		assertEquals(result.get("100"), new Double(51.63));
		
		auths.clear();
		auths.add(this.makeAuth("100", 2.65));
		auths.add(this.makeAuth("101", 2.20));
		amount = 5;
		result = PaymentSessionBean.getCaptureAmounts(auths, amount);
		assertEquals(result.get("100"), new Double(2.65));
		assertEquals(result.get("101"), new Double(2.20));
		
		auths.clear();
		auths.add(this.makeAuth("900", 2.50));
		auths.add(this.makeAuth("10", 1.00));
		amount = 3.00;
		result = PaymentSessionBean.getCaptureAmounts(auths, amount);
		assertEquals(result.size(), 1);
		assertFalse(result.containsKey("10"));
		assertEquals(result.get("900"), new Double(3.00));
	}
	
	private ErpAuthorizationModel makeAuth(String id, double amount) {
		ErpAuthorizationModel a = new ErpAuthorizationModel();
		
		a.setPK(new PrimaryKey(id));
		a.setAmount(amount);
		a.setAuthCode("Approved-100");
		a.setAvs("Y");
		a.setCardType(EnumCardType.VISA);
		a.setCcNumLast4("111");
		a.setDescription("Approved");
		a.setMerchantId("Freshdirect");
		a.setSequenceNumber("00000000"+id);
		a.setTax(0);
		a.setTransactionDate(new Date());
		a.setTransactionSource(EnumTransactionSource.SYSTEM);
		
		return a;
	}
}
