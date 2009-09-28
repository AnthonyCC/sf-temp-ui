package com.freshdirect.payment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.customer.EnumPaymentType;
import com.freshdirect.customer.EnumSaleType;
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
import com.freshdirect.giftcard.ErpAppliedGiftCardModel;

import java.util.Collections;

public class AuthorizationStrategy extends PaymentStrategy {

	private final AuthInfo fdAuthInfo;
	private final AuthInfo bcAuthInfo;
	private final AuthInfo usqAuthInfo;

	public AuthorizationStrategy(ErpSaleModel sale) {
		super(sale);
		this.fdAuthInfo = new AuthInfo(
			sale.getPK().getId(), 
			sale.getCustomerPk().getId(),
			ErpAffiliate.getPrimaryAffiliate());
		this.bcAuthInfo = new AuthInfo(
			sale.getPK().getId(),
			sale.getCustomerPk().getId(),
			ErpAffiliate.getEnum(ErpAffiliate.CODE_BC));
		this.usqAuthInfo = new AuthInfo(
				sale.getPK().getId(),
				sale.getCustomerPk().getId(),
				ErpAffiliate.getEnum(ErpAffiliate.CODE_USQ));
	}

	public List getOutstandingAuthorizations() {
		ErpAbstractOrderModel order = this.sale.getCurrentOrder();
		ErpInvoiceModel inv = this.sale.getLastInvoice();
		final ErpAffiliate bc = ErpAffiliate.getEnum(ErpAffiliate.CODE_BC);
		final ErpAffiliate usq = ErpAffiliate.getEnum(ErpAffiliate.CODE_USQ);

		for (Iterator i = order.getOrderLines().iterator(); i.hasNext();) {
			ErpOrderLineModel line = (ErpOrderLineModel) i.next();
			if (usq.equals(line.getAffiliate())) {
				if (inv != null) {
					usqAuthInfo.addInvoiceLine(inv.getInvoiceLine(line.getOrderLineNumber()));
				} else {
					usqAuthInfo.addOrderline(line);
				}
			}else if (bc.equals(line.getAffiliate())) {
				if (inv != null) {
					bcAuthInfo.addInvoiceLine(inv.getInvoiceLine(line.getOrderLineNumber()));
				} else {
					bcAuthInfo.addOrderline(line);
				}
			} else {
				if (inv != null) {
					fdAuthInfo.addInvoiceLine(inv.getInvoiceLine(line.getOrderLineNumber()));
				} else {
					fdAuthInfo.addOrderline(line);
				}
			}
		}
		
		
		if (fdAuthInfo.getSubtotal() <= 0 && bcAuthInfo.getSubtotal() <= 0 && usqAuthInfo.getSubtotal() <= 0) {
			//  new gift card changes 
			if(!this.sale.getType().equals(EnumSaleType.GIFTCARD) && !this.sale.getType().equals(EnumSaleType.DONATION))
			    throw new FDRuntimeException("Order with not orderlines");
		}

		for (Iterator i = order.getCharges().iterator(); i.hasNext();) {
			ErpChargeLineModel cl = (ErpChargeLineModel) i.next();
			fdAuthInfo.addCharge(cl);
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
		
		//Apply GC Payments
		for (Iterator i = order.getAppliedGiftcards().iterator(); i.hasNext();) {
			ErpAppliedGiftCardModel agc = (ErpAppliedGiftCardModel) i.next();
			if(usq.equals(agc.getAffiliate())) {
				usqAuthInfo.addGCPayment(agc.getAmount());
			} else {
				fdAuthInfo.addGCPayment(agc.getAmount());
			}
		}
		
		return this.getOutstandingAuthorizations(order.getPaymentMethod());
	}
	
	private void addDeduction(double amount) {
		double remainder = fdAuthInfo.addDeduction(amount);
		remainder = bcAuthInfo.addDeduction(remainder);
		remainder = usqAuthInfo.addDeduction(remainder);
		
		if (remainder > 0) {
			throw new FDRuntimeException("Applied more discount than order pre deduction total");
		}
	}

	private List getOutstandingAuthorizations(ErpPaymentMethodI pm) {
		if(pm.isGiftCard()) return Collections.emptyList();
		this.fdAuthInfo.setAuthorizations(this.sale.getApprovedAuthorizations(this.fdAuthInfo.affiliate, pm));
		this.bcAuthInfo.setAuthorizations(this.sale.getApprovedAuthorizations(this.bcAuthInfo.affiliate, pm));
		this.usqAuthInfo.setAuthorizations(this.sale.getApprovedAuthorizations(this.usqAuthInfo.affiliate, pm));
		
		List auths = new ArrayList();
		AuthorizationInfo fdInfo = fdAuthInfo.getAuthInfo(pm);				
		if(fdInfo.getAmount() > 0) {
			auths.add(fdInfo);
		}
		
		AuthorizationInfo bcInfo = bcAuthInfo.getAuthInfo(pm);		
		if(bcInfo.getAmount() > 0) {
			auths.add(bcInfo);
		}

		AuthorizationInfo usqInfo = usqAuthInfo.getAuthInfo(pm);		
		if(usqInfo.getAmount() > 0) {
			auths.add(usqInfo);
		}

		ErpChargeInvoiceModel chargeInvoice = this.sale.getLastChargeInvoice();
		if (chargeInvoice != null && chargeInvoice.getAmount() > 0) {
			auths.add(new AuthorizationInfo(sale.getPK().getId(), sale.getCustomerPk().getId(), fdAuthInfo.affiliate, chargeInvoice.getAmount(), pm, true));
		}

		return auths;
	}

	private static class AuthInfo extends PaymentStrategy.PaymentInfo {
		
		private final String customerId;
		private List auths;

		public AuthInfo(String saleId, String customerId, ErpAffiliate affiliate) {
			super(saleId, affiliate);
			this.customerId = customerId;
		}

		public void setAuthorizations(List auths) {
			this.auths = auths;
		}


		public AuthorizationInfo getAuthInfo(ErpPaymentMethodI pm) {
			double amount = this.getAmount();
			for (Iterator i = this.auths.iterator(); i.hasNext();) {
				ErpAuthorizationModel auth = (ErpAuthorizationModel) i.next();
				if (EnumPaymentType.MAKE_GOOD.equals(pm.getPaymentType())
					|| EnumPaymentType.ON_FD_ACCOUNT.equals(pm.getPaymentType())) {
					amount = MathUtil.roundDecimal(amount - auth.getAmount());
				} else {
					if (EnumPaymentMethodType.ECHECK.equals(pm.getPaymentMethodType())
						&& pm.getPaymentMethodType().equals(auth.getPaymentMethodType())) {
						
						if (pm.getAbaRouteNumber().equalsIgnoreCase(auth.getAbaRouteNumber())
							&& pm.getAccountNumber().endsWith(auth.getCcNumLast4())
							&& pm.getBankAccountType().equals(auth.getBankAccountType())) {
							amount = MathUtil.roundDecimal(amount - auth.getAmount());
						}
					} else {
						if (pm.getCardType().equals(auth.getCardType()) && pm.getAccountNumber().endsWith(auth.getCcNumLast4())) {
							amount = MathUtil.roundDecimal(amount - auth.getAmount());
						}
					}
				}

			}
			
			return new AuthorizationInfo(saleId, customerId, affiliate, amount, pm, false);
		}

	}

}
