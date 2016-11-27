package com.freshdirect.dataloader.payment.reconciliation.paypal;

import java.math.BigDecimal;

public class Money {
	BigDecimal dollar = null;
	BigDecimal cents = null;
	BigDecimal orig = null;
	
	public Money(long actual) {
		this.cents = BigDecimal.valueOf(actual);
		this.dollar = cents.movePointLeft(2);
		this.orig = cents;
	}
	
	public Money(double actual) {
		this.dollar = BigDecimal.valueOf(actual);
		this.cents = dollar.movePointRight(2);
		this.orig = dollar;
	}
	
	public Money(BigDecimal actual) {
		
		this.orig = actual;
	}
	
	public double getDollar() {
		return dollar.doubleValue();
	}
	
	public long getCents() {
		return cents.longValue();
	}
}
