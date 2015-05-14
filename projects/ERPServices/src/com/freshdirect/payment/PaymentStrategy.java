package com.freshdirect.payment;

import java.io.Serializable;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpInvoiceLineModel;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.TaxCalculatorUtil;
import com.freshdirect.framework.util.MathUtil;

public abstract class PaymentStrategy implements Serializable {
	
	protected final ErpSaleModel sale;
	
	public PaymentStrategy (ErpSaleModel sale) {
		this.sale = sale;
	}

	
	
	protected static abstract class PaymentInfo {
		protected final String saleId;
		protected final ErpAffiliate affiliate;
		private double perishableBuffer;
		private double subtotal;
		private double tax;
		private double depositValue;
		private double deductionAmount;
		private double chargeAmount;
		private double perishableBufferAmount;
		//Payment made through Gift cards.
		private double gcPaymentAmount;
		
		@Deprecated
		public PaymentInfo(String saleId, ErpAffiliate affiliate) {
			this.saleId = saleId;
			this.affiliate = affiliate;
			this.perishableBuffer = MathUtil.roundDecimal(ErpServicesProperties.getPerishableAuthBuffer());
			this.subtotal = 0.0;
			this.tax = 0.0;
			this.depositValue = 0.0;
			this.deductionAmount = 0.0;
			this.chargeAmount = 0.0;
			this.perishableBufferAmount = 0.0;
			this.gcPaymentAmount = 0.0;
		}
		
		public PaymentInfo(String saleId, ErpAffiliate affiliate,double perishableBuffer) {
			this.saleId = saleId;
			this.affiliate = affiliate;
			this.perishableBuffer = perishableBuffer;
			this.subtotal = 0.0;
			this.tax = 0.0;
			this.depositValue = 0.0;
			this.deductionAmount = 0.0;
			this.chargeAmount = 0.0;
			this.perishableBufferAmount = 0.0;
			this.gcPaymentAmount = 0.0;
		}

		public ErpAffiliate getAffiliate() {
			return this.affiliate;
		}

		public double getSubtotal() {
			return this.subtotal;
		}


		
		public double addDeduction(double deduction) {			
			double diff = MathUtil.roundDecimal(this.getAmount());
			if (diff <= 0) {
				return deduction;
			}
			
			double appliedAmount = MathUtil.roundDecimal(Math.min(deduction, diff));
			this.deductionAmount = MathUtil.roundDecimal(this.deductionAmount + appliedAmount);
			return MathUtil.roundDecimal(deduction - appliedAmount);
		}

		public void addGCPayment(double payment) {			
			this.gcPaymentAmount = MathUtil.roundDecimal(this.gcPaymentAmount + payment);
		}
		
		public void addOrderline(ErpOrderLineModel line) {
			double price = line.getPrice();
			this.subtotal = MathUtil.roundDecimal(this.subtotal + price);
			
			if (line.isPerishable()) {
				double thisBuffer = MathUtil.roundDecimal(price * perishableBuffer);
				perishableBufferAmount = MathUtil.roundDecimal(perishableBufferAmount + thisBuffer);	
			} 
			
//			double thisTax = MathUtil.roundDecimal(line.getTaxRate() * price);
			double thisTax = TaxCalculatorUtil.getTaxValue(line);
			this.tax = MathUtil.roundDecimal(this.tax + thisTax);
			this.depositValue = MathUtil.roundDecimal(this.depositValue + line.getDepositValue());
		}

		public void addInvoiceLine(ErpInvoiceLineModel line) {
			this.subtotal = MathUtil.roundDecimal(this.subtotal + line.getPrice());
			this.tax = MathUtil.roundDecimal(this.tax + line.getTaxValue());
			this.depositValue = MathUtil.roundDecimal(this.depositValue + line.getDepositValue());
		}

		public void addCharge(ErpChargeLineModel cl) {
			this.chargeAmount = MathUtil.roundDecimal(this.chargeAmount + cl.getTotalAmount());			
			double t = MathUtil.roundDecimal(cl.getTotalAmount() * cl.getTaxRate());
			this.tax = MathUtil.roundDecimal(this.tax + t);
		}
		
		public void merge(PaymentInfo info) {
			this.subtotal = MathUtil.roundDecimal(this.subtotal + info.subtotal);
			this.deductionAmount = MathUtil.roundDecimal(this.deductionAmount + info.deductionAmount);
			this.chargeAmount = MathUtil.roundDecimal(this.chargeAmount + info.chargeAmount);
			this.tax = MathUtil.roundDecimal(this.tax + info.tax);
			this.depositValue = MathUtil.roundDecimal(this.depositValue + info.depositValue);
			this.perishableBufferAmount = MathUtil.roundDecimal(this.perishableBufferAmount + info.perishableBufferAmount);
			this.gcPaymentAmount = MathUtil.roundDecimal(this.gcPaymentAmount + info.gcPaymentAmount);
		}

		public double getAmount() {
			double amount = 0.0;
			//add orderline total
			amount = MathUtil.roundDecimal(amount + this.subtotal);
			//add the perishable buffer
			amount = MathUtil.roundDecimal(amount + this.perishableBufferAmount);
			//subtract applied deductions credit + promotions
			amount = MathUtil.roundDecimal(amount - this.deductionAmount);
			//add charges
			amount = MathUtil.roundDecimal(amount + this.chargeAmount);
			//add tax
			amount = MathUtil.roundDecimal(amount + this.tax);
			//add deposit value
			amount = MathUtil.roundDecimal(amount + this.depositValue);
			//apply GC payment
			amount = MathUtil.roundDecimal(amount - this.gcPaymentAmount);
			
			return amount;
		}
		
		public double getPerishableBufferAmount(){
			return this.perishableBufferAmount;
		}

	}
	
	/**
	 * Method to get the perishable buffer value based on the payment method of the order.
	 * @param sale
	 * @return perishable buffer.
	 */
	protected static double getPerishableBuffer(ErpSaleModel sale){		
		double perishableBuffer=ErpServicesProperties.getPerishableAuthBuffer();
		if(null !=sale){
			ErpAbstractOrderModel order = sale.getCurrentOrder();
			ErpPaymentMethodI paymentMethod =order.getPaymentMethod();
			perishableBuffer = getPerishableBufferForEBT(perishableBuffer,paymentMethod);
		}
		return perishableBuffer;
	}	

	protected static double getPerishableBuffer(ErpAbstractOrderModel order){		
		double perishableBuffer=ErpServicesProperties.getPerishableAuthBuffer();
		if(null !=order){
			ErpPaymentMethodI paymentMethod =order.getPaymentMethod();
			perishableBuffer = getPerishableBufferForEBT(perishableBuffer,paymentMethod);
		}
		return perishableBuffer;
	}
	
	private static double getPerishableBufferForEBT(double perishableBuffer,
			ErpPaymentMethodI paymentMethod) {
		if(null !=paymentMethod && EnumPaymentMethodType.EBT.equals(paymentMethod.getPaymentMethodType())){
			perishableBuffer = 0.0;
		}
		return perishableBuffer;
	}
}
