package com.freshdirect.giftcard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAppliedCreditModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.payment.PaymentStrategy;

public class GiftCardApplicationStrategy extends PaymentStrategy {

	private final ApplicationInfo fdAppInfo;
	private final ApplicationInfo usqAppInfo;
	private final ApplicationInfo fdwAppInfo;
	private final ErpAbstractOrderModel order;
	private final ErpAffiliate usqAffiliate;
	private final ErpAffiliate fdwAffiliate;
	
	private final ErpInvoiceModel inv;
	private List<ErpAppliedGiftCardModel> appGiftCardInfo = new ArrayList<ErpAppliedGiftCardModel>();
	
	public GiftCardApplicationStrategy(ErpAbstractOrderModel order, ErpInvoiceModel invoice, ErpAffiliate fd,
			ErpAffiliate usq, ErpAffiliate fdw) {
		super(null);
		this.order = order;
		this.inv = invoice;
		double perishableBuffer = getPerishableBuffer(order);
		this.fdAppInfo = new ApplicationInfo(null, null, fd, perishableBuffer);
		this.usqAppInfo = new ApplicationInfo(null, null, usq, perishableBuffer);
		this.fdwAppInfo = new ApplicationInfo(null, null, fdw, perishableBuffer);
		this.usqAffiliate = usq;
		this.fdwAffiliate = fdw;
	}

	public GiftCardApplicationStrategy(ErpAbstractOrderModel order, ErpInvoiceModel invoice) {
		this(order, invoice, ErpAffiliate.getPrimaryAffiliate(), ErpAffiliate.getEnum(ErpAffiliate.CODE_USQ),
				ErpAffiliate.getEnum(ErpAffiliate.CODE_FDW));

	}

	public void generateAppliedGiftCardsInfo() {

		for (Iterator i = order.getOrderLines().iterator(); i.hasNext();) {
			ErpOrderLineModel line = (ErpOrderLineModel) i.next();
			if (this.usqAffiliate.getCode().equals(line.getAffiliateCode())) {
				if (inv != null) {
					usqAppInfo.addInvoiceLine(inv.getInvoiceLine(line.getOrderLineNumber()));
				} else {
					usqAppInfo.addOrderline(line);
				}
			} else if (this.fdwAffiliate.getCode().equals(line.getAffiliateCode())) {
				if (inv != null) {
					fdwAppInfo.addInvoiceLine(inv.getInvoiceLine(line.getOrderLineNumber()));
				} else {
					fdwAppInfo.addOrderline(line);
				}
			} else {
				if (inv != null) {
					fdAppInfo.addInvoiceLine(inv.getInvoiceLine(line.getOrderLineNumber()));
				} else {
					fdAppInfo.addOrderline(line);
				}
			}
		}
		
		
		if (inv != null) {
			for (Iterator<ErpChargeLineModel> i = inv.getCharges().iterator(); i.hasNext();) {
				ErpChargeLineModel cl = i.next();
				fdAppInfo.addCharge(cl);
			}
		} else {
			for (Iterator<ErpChargeLineModel> i = order.getCharges().iterator(); i.hasNext();) {
				ErpChargeLineModel cl = i.next();
				fdAppInfo.addCharge(cl);
			}
		}
					
		List<ErpDiscountLineModel> discounts = null;
		if(inv != null){
			discounts = inv.getDiscounts();
		} else {
			discounts = order.getDiscounts();
		}
		if (discounts != null && !discounts.isEmpty()) {
			for (Iterator<ErpDiscountLineModel> i = discounts.iterator(); i.hasNext();) {
				ErpDiscountLineModel d = i.next();
				this.addDeduction(d.getDiscount().getAmount());
			}
		}
		if(inv != null){
			for (Iterator<ErpAppliedCreditModel> i = inv.getAppliedCredits().iterator(); i.hasNext();) {
				ErpAppliedCreditModel c =  i.next();
				this.addDeduction(c.getAmount());
			}
		} else {
			for (Iterator<ErpAppliedCreditModel> i = order.getAppliedCredits().iterator(); i.hasNext();) {
				ErpAppliedCreditModel c = i.next();
				this.addDeduction(c.getAmount());
			}
		}

		List<ErpGiftCardModel> gcPaymentMethods = order.getSelectedGiftCards();
		if(null != gcPaymentMethods) {
			for(Iterator<ErpGiftCardModel> it = gcPaymentMethods.iterator(); it.hasNext();){
				ErpPaymentMethodI giftcard = (ErpPaymentMethodI) it.next();
				this.appGiftCardInfo.addAll(this.getAppliedList(giftcard));
				
			}
		}
	}
	
	public double getRemainingBalance() {
		return (fdAppInfo.getAmount() + usqAppInfo.getAmount() + fdwAppInfo.getAmount());
	}
	
	public double getPerishableBufferAmount(){
		return (fdAppInfo.getPerishableBufferAmount() + usqAppInfo.getPerishableBufferAmount() + fdwAppInfo.getPerishableBufferAmount());
	}
	
	private void addDeduction(double amount) {
		double remainder = fdAppInfo.addDeduction(amount);
		remainder = usqAppInfo.addDeduction(remainder);
		remainder = fdwAppInfo.addDeduction(remainder);
		
		if (remainder > 0) {
			throw new FDRuntimeException("Applied more discount "+remainder+" than order pre deduction total");
		}
	}

	private List<ErpAppliedGiftCardModel> getAppliedList(ErpPaymentMethodI pm) {
		
		List<ErpAppliedGiftCardModel> appList = new ArrayList<ErpAppliedGiftCardModel>();
		double gcBalance = pm.getBalance();
		ErpAppliedGiftCardModel fdAppModel = fdAppInfo.getApplicationInfo(pm, gcBalance);				
		if(fdAppModel != null && fdAppModel.getAmount() > 0) {
			appList.add(fdAppModel);
			gcBalance -= fdAppModel.getAmount();
		}
		if(gcBalance == 0) return appList;
		ErpAppliedGiftCardModel usqAppModel = usqAppInfo.getApplicationInfo(pm, gcBalance);		
		if(usqAppModel != null && usqAppModel.getAmount() > 0) {
			appList.add(usqAppModel);
		}
		
		if(gcBalance == 0) return appList;
		ErpAppliedGiftCardModel fdwAppModel = fdwAppInfo.getApplicationInfo(pm, gcBalance);		
		if(fdwAppModel != null && fdwAppModel.getAmount() > 0) {
			appList.add(fdwAppModel);
		}

		return appList;
	}

	private static class ApplicationInfo extends PaymentStrategy.PaymentInfo {
		
		private final String customerId;
		private double gcPaymentAmount;
		
		public ApplicationInfo(String saleId, String customerId, ErpAffiliate affiliate,double perishableBuffer) {
			super(saleId, affiliate,perishableBuffer);
			this.customerId = customerId;
		}

		public ErpAppliedGiftCardModel getApplicationInfo(ErpPaymentMethodI pm, double gcBalance) {
			double amount = this.getAmount();
			if(amount > 0){
				double appliedAmount = Math.min(amount, gcBalance);
				ErpAppliedGiftCardModel gcModel = new ErpAppliedGiftCardModel();
				gcModel.setAffiliate(affiliate);
				gcModel.setAmount(appliedAmount);
				gcModel.setCertificateNum(ErpGiftCardUtil.getCertificateNumber(pm.getAccountNumber()));
				this.updateGCPayment(appliedAmount);
				return gcModel;  
			}
			return null;
		}
		
		public double getAmount() {
			return (super.getAmount() - this.gcPaymentAmount); 
		}
		
		public void updateGCPayment(double amount) {
			this.gcPaymentAmount += amount;
		}	
	}

	public List<ErpAppliedGiftCardModel> getAppGiftCardInfo() {
		return appGiftCardInfo;
	}

}
