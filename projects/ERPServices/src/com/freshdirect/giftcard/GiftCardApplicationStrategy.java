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
	private final ApplicationInfo fdwAppInfo;
	private final ErpAbstractOrderModel order;
	private final ErpInvoiceModel inv;
	private List appGiftCardInfo = new ArrayList();
	
	public GiftCardApplicationStrategy(ErpAbstractOrderModel order, ErpInvoiceModel invoice) {
		super(null);
		this.order = order;
		this.inv = invoice;
		double perishableBuffer=getPerishableBuffer(order);
		this.fdAppInfo = new ApplicationInfo(
			null, 
			null,
			ErpAffiliate.getPrimaryAffiliate(),perishableBuffer);
		this.usqAppInfo = new ApplicationInfo(
				null,
				null,
				ErpAffiliate.getEnum(ErpAffiliate.CODE_USQ),perishableBuffer);
		this.fdwAppInfo = new ApplicationInfo(
				null,
				null,
				ErpAffiliate.getEnum(ErpAffiliate.CODE_FDW),perishableBuffer);
	}

	public void generateAppliedGiftCardsInfo() {
		final ErpAffiliate bc = ErpAffiliate.getEnum(ErpAffiliate.CODE_BC);
		final ErpAffiliate usq = ErpAffiliate.getEnum(ErpAffiliate.CODE_USQ);
		final ErpAffiliate fdw = ErpAffiliate.getEnum(ErpAffiliate.CODE_FDW);

		for (Iterator i = order.getOrderLines().iterator(); i.hasNext();) {
			ErpOrderLineModel line = (ErpOrderLineModel) i.next();
			if (usq.equals(line.getAffiliate())) {
				if (inv != null) {
					usqAppInfo.addInvoiceLine(inv.getInvoiceLine(line.getOrderLineNumber()));
				} else {
					usqAppInfo.addOrderline(line);
				}
			} else if (fdw.equals(line.getAffiliate())) {
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
		
		
//		if (fdAppInfo.getSubtotal() <= 0 && usqAppInfo.getSubtotal() <= 0) {
//			throw new FDRuntimeException("Order with not orderlines");
//		}
		if (inv != null) {
			for (Iterator i = inv.getCharges().iterator(); i.hasNext();) {
				ErpChargeLineModel cl = (ErpChargeLineModel) i.next();
				fdAppInfo.addCharge(cl);
			}
		} else {
			for (Iterator i = order.getCharges().iterator(); i.hasNext();) {
				ErpChargeLineModel cl = (ErpChargeLineModel) i.next();
				fdAppInfo.addCharge(cl);
			}
		}
					
		List discounts = null;
		if(inv != null){
			discounts = inv.getDiscounts();
		} else {
			discounts = order.getDiscounts();
		}
		if (discounts != null && !discounts.isEmpty()) {
			for (Iterator i = discounts.iterator(); i.hasNext();) {
				ErpDiscountLineModel d = (ErpDiscountLineModel) i.next();
				this.addDeduction(d.getDiscount().getAmount());
			}
		}
		if(inv != null){
			for (Iterator i = inv.getAppliedCredits().iterator(); i.hasNext();) {
				ErpAppliedCreditModel c = (ErpAppliedCreditModel) i.next();
				this.addDeduction(c.getAmount());
			}
		} else {
			for (Iterator i = order.getAppliedCredits().iterator(); i.hasNext();) {
				ErpAppliedCreditModel c = (ErpAppliedCreditModel) i.next();
				this.addDeduction(c.getAmount());
			}
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

	public List getAppGiftCardInfo() {
		return appGiftCardInfo;
	}

}
