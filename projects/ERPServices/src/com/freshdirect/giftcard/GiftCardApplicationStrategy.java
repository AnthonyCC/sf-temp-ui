package com.freshdirect.giftcard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.customer.EnumPaymentType;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAppliedCreditModel;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpChargeInvoiceModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.util.MathUtil;
import com.freshdirect.payment.PaymentStrategy;

public class GiftCardApplicationStrategy extends PaymentStrategy {

	private final ApplicationInfo fdAppInfo;
	private final ApplicationInfo usqAppInfo;
	private final ErpAbstractOrderModel order;
	private final ErpInvoiceModel inv;
	private List appGiftCardInfo = new ArrayList();
	
	public GiftCardApplicationStrategy(ErpAbstractOrderModel order, ErpInvoiceModel invoice) {
		super(null);
		this.order = order;
		this.inv = invoice;
		this.fdAppInfo = new ApplicationInfo(
			null, 
			null,
			ErpAffiliate.getPrimaryAffiliate());
		this.usqAppInfo = new ApplicationInfo(
				null,
				null,
				ErpAffiliate.getEnum(ErpAffiliate.CODE_USQ));
	}

	public void generateAppliedGiftCardsInfo() {
		final ErpAffiliate bc = ErpAffiliate.getEnum(ErpAffiliate.CODE_BC);
		final ErpAffiliate usq = ErpAffiliate.getEnum(ErpAffiliate.CODE_USQ);

		for (Iterator i = order.getOrderLines().iterator(); i.hasNext();) {
			ErpOrderLineModel line = (ErpOrderLineModel) i.next();
			if (usq.equals(line.getAffiliate())) {
				if (inv != null) {
					usqAppInfo.addInvoiceLine(inv.getInvoiceLine(line.getOrderLineNumber()));
				} else {
					usqAppInfo.addOrderline(line);
				}
			} else {
				if (inv != null) {
					fdAppInfo.addInvoiceLine(inv.getInvoiceLine(line.getOrderLineNumber()));
				} else {
					fdAppInfo.addOrderline(line);
				}
			}
		}
		
		
//		if (fdAppInfo.getSubtotal() <= 0 && usqAppInfo.getSubtotal() <= 0) {
//			throw new FDRuntimeException("Order with not orderlines");
//		}

		for (Iterator i = order.getCharges().iterator(); i.hasNext();) {
			ErpChargeLineModel cl = (ErpChargeLineModel) i.next();
			fdAppInfo.addCharge(cl);
		}
					
		
		List discounts = order.getDiscounts();
		if (discounts != null && !discounts.isEmpty()) {
			for (Iterator i = discounts.iterator(); i.hasNext();) {
				ErpDiscountLineModel d = (ErpDiscountLineModel) i.next();
				this.addDeduction(d.getDiscount().getAmount());
			}
		}
		
		for (Iterator i = order.getAppliedCredits().iterator(); i.hasNext();) {
			ErpAppliedCreditModel c = (ErpAppliedCreditModel) i.next();
			this.addDeduction(c.getAmount());
		}
		
		List gcPaymentMethods = order.getSelectedGiftCards();
		if(null != gcPaymentMethods) {
			for(Iterator it = gcPaymentMethods.iterator(); it.hasNext();){
				ErpPaymentMethodI giftcard = (ErpPaymentMethodI) it.next();
				this.appGiftCardInfo.addAll(this.getAppliedList(giftcard));
				
			}
		}
	}
	
	public double getRemainingBalance() {
		return (fdAppInfo.getAmount() + usqAppInfo.getAmount());
	}
	
	public double getPerishableBufferAmount(){
		return (fdAppInfo.getPerishableBufferAmount() + usqAppInfo.getPerishableBufferAmount());
	}
	
	private void addDeduction(double amount) {
		double remainder = fdAppInfo.addDeduction(amount);
		remainder = usqAppInfo.addDeduction(remainder);
		
		if (remainder > 0) {
			throw new FDRuntimeException("Applied more discount than order pre deduction total");
		}
	}

	private List getAppliedList(ErpPaymentMethodI pm) {
		
		List appList = new ArrayList();
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

		return appList;
	}

	private static class ApplicationInfo extends PaymentStrategy.PaymentInfo {
		
		private final String customerId;
		private double gcPaymentAmount;
		
		public ApplicationInfo(String saleId, String customerId, ErpAffiliate affiliate) {
			super(saleId, affiliate);
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

	public List getAppGiftCardInfo() {
		return appGiftCardInfo;
	}

}
