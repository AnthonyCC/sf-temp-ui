package com.freshdirect.payment;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAppliedCreditModel;
import com.freshdirect.customer.ErpCaptureModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpInvoiceLineModel;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.util.MathUtil;
import com.freshdirect.giftcard.ErpAppliedGiftCardModel;

public class CaptureStrategy extends PaymentStrategy {
	
	private final CapInfo fdInfo;
	private final CapInfo bcInfo;
	private final CapInfo usqInfo;
	
	public CaptureStrategy (ErpSaleModel sale) {
		super(sale);
		ErpAffiliate fd = ErpAffiliate.getPrimaryAffiliate();
		this.fdInfo = new CapInfo(sale.getPK().getId(), fd, sale.getCaptures(fd));
		ErpAffiliate bc = ErpAffiliate.getEnum(ErpAffiliate.CODE_BC);
		this.bcInfo = new CapInfo(sale.getPK().getId(), bc, sale.getCaptures(bc));
		ErpAffiliate usq = ErpAffiliate.getEnum(ErpAffiliate.CODE_USQ);
		this.usqInfo = new CapInfo(sale.getPK().getId(), usq, sale.getCaptures(usq));

	}
	
	public Map getOutstandingCaptureAmounts(){
		ErpAbstractOrderModel order = this.sale.getCurrentOrder();
		ErpInvoiceModel invoice = this.sale.getLastInvoice();
		
		if(invoice == null) {
			throw new FDRuntimeException("Cannot capture an order without an INVOICE: " + this.sale.getPK().getId());
		}
		
		if(MathUtil.roundDecimal(invoice.getAmount()) <= 0) {
			return Collections.EMPTY_MAP;
		}
		
		final ErpAffiliate bc = ErpAffiliate.getEnum(ErpAffiliate.CODE_BC);
		final ErpAffiliate usq = ErpAffiliate.getEnum(ErpAffiliate.CODE_USQ);
		
		for (Iterator i = order.getOrderLines().iterator(); i.hasNext(); ) {
			ErpOrderLineModel line = (ErpOrderLineModel) i.next();
			ErpInvoiceLineModel invLine = invoice.getInvoiceLine(line.getOrderLineNumber());
			if(usq.equals(line.getAffiliate())) {				
				this.usqInfo.addInvoiceLine(invLine);
			}else if(bc.equals(line.getAffiliate())) {				
				this.bcInfo.addInvoiceLine(invLine);
			} else {		
				this.fdInfo.addInvoiceLine(invLine);
			}			
		}
		
		for (Iterator i = invoice.getCharges().iterator(); i.hasNext();) {
			ErpChargeLineModel cl = (ErpChargeLineModel) i.next();
			fdInfo.addCharge(cl);
		}
															
		List discounts = invoice.getDiscounts();
		if (discounts != null && !discounts.isEmpty()) {
			for (Iterator i = discounts.iterator(); i.hasNext();) {
				ErpDiscountLineModel d = (ErpDiscountLineModel) i.next();
				this.addDeduction(d.getDiscount().getAmount());
			}
		}
		
		for (Iterator i = invoice.getAppliedCredits().iterator(); i.hasNext();) {
			ErpAppliedCreditModel c = (ErpAppliedCreditModel) i.next();
			this.addDeduction(c.getAmount());
		}
				
		//Apply GC Payments
		for (Iterator i = invoice.getAppliedGiftCards().iterator(); i.hasNext();) {
			ErpAppliedGiftCardModel agc = (ErpAppliedGiftCardModel) i.next();
			if(usq.equals(agc.getAffiliate())) {
				usqInfo.addGCPayment(agc.getAmount());
			} else {
				fdInfo.addGCPayment(agc.getAmount());
			}
		}
		if(order.getPaymentMethod().isGiftCard()) return Collections.emptyMap();
		
		List fdAuths = this.sale.getApprovedAuthorizations(fdInfo.affiliate, order.getPaymentMethod());
		List bcAuths = this.sale.getApprovedAuthorizations(bcInfo.affiliate, order.getPaymentMethod());
		List usqAuths = this.sale.getApprovedAuthorizations(usqInfo.affiliate, order.getPaymentMethod());
		
		Map captures = new LinkedHashMap();
		
		if(usqInfo.getAmount() > 0 && usqAuths.isEmpty()) {
			if(!fdAuths.isEmpty()) {
				fdInfo.merge(usqInfo);
				captures.put(fdInfo.affiliate, new Double(fdInfo.getAmount()));
			}
		}else if(bcInfo.getAmount() > 0 && bcAuths.isEmpty()) {
			if(!fdAuths.isEmpty()) {
				fdInfo.merge(bcInfo);
				captures.put(fdInfo.affiliate, new Double(fdInfo.getAmount()));
			}
		}else{
			if(fdInfo.getAmount() > 0) {
				captures.put(fdInfo.affiliate, new Double(fdInfo.getAmount()));
			}
			if(bcInfo.getAmount() > 0) {
				captures.put(bcInfo.affiliate, new Double(bcInfo.getAmount()));
			}
			if(usqInfo.getAmount() > 0) {
				captures.put(usqInfo.affiliate, new Double(usqInfo.getAmount()));
			}

		}
		
		return captures;
	}
	
	private void addDeduction(double amount) {
		double remainder = fdInfo.addDeduction(amount);
		if(remainder > 0) {
			remainder = usqInfo.addDeduction(remainder);
		}
		if(remainder > 0) {
			remainder = bcInfo.addDeduction(remainder);
		}
		if (remainder > 0) {
			throw new FDRuntimeException("Applied more discount than order subtotal");
		}
	}
	
	private static class CapInfo extends PaymentStrategy.PaymentInfo {
		
		private final List captures;
		
		public CapInfo(String saleId, ErpAffiliate affiliate, List captures) {
			super(saleId, affiliate);
			this.captures = captures;
		}

		public double getAmount() {
			double amount = super.getAmount();
			for(Iterator i = captures.iterator(); i.hasNext(); ) {
				ErpCaptureModel capture = (ErpCaptureModel) i.next();
				amount = MathUtil.roundDecimal(amount - capture.getAmount());
			}
			return amount;
		}	 
	}

}
