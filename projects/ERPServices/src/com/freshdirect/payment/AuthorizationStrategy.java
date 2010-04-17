package com.freshdirect.payment;

import java.util.ArrayList;
import java.util.Collections;
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

public class AuthorizationStrategy extends PaymentStrategy {

	private static final long	serialVersionUID	= -193567950363428880L;
	
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

	public List<AuthorizationInfo> getOutstandingAuthorizations() {
		ErpAbstractOrderModel order = this.sale.getCurrentOrder();
		ErpInvoiceModel inv = this.sale.getLastInvoice();
		final ErpAffiliate bc = ErpAffiliate.getEnum(ErpAffiliate.CODE_BC);
		final ErpAffiliate usq = ErpAffiliate.getEnum(ErpAffiliate.CODE_USQ);

		for ( ErpOrderLineModel line : order.getOrderLines() ) {
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

		for ( ErpChargeLineModel cl : order.getCharges() ) {
			fdAuthInfo.addCharge(cl);
		}
					
		
		List<ErpDiscountLineModel> discounts = order.getDiscounts();
		if (discounts != null && !discounts.isEmpty()) {
			for ( ErpDiscountLineModel d  : discounts ) {
				this.addDeduction(d.getDiscount().getAmount());
			}
		}
		
		for ( ErpAppliedCreditModel c : order.getAppliedCredits() ) {
			this.addDeduction(c.getAmount());
		}
		
		//Apply GC Payments
		for ( ErpAppliedGiftCardModel agc : order.getAppliedGiftcards() ) {
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

	private List<AuthorizationInfo> getOutstandingAuthorizations(ErpPaymentMethodI pm) {
		if(pm.isGiftCard()) return Collections.emptyList();
		this.fdAuthInfo.setAuthorizations(this.sale.getApprovedAuthorizations(this.fdAuthInfo.affiliate, pm));
		this.bcAuthInfo.setAuthorizations(this.sale.getApprovedAuthorizations(this.bcAuthInfo.affiliate, pm));
		this.usqAuthInfo.setAuthorizations(this.sale.getApprovedAuthorizations(this.usqAuthInfo.affiliate, pm));
		
		List<AuthorizationInfo> auths = new ArrayList<AuthorizationInfo>();
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
		
		private final String				customerId;
		private List<ErpAuthorizationModel>	auths;

		public AuthInfo(String saleId, String customerId, ErpAffiliate affiliate) {
			super(saleId, affiliate);
			this.customerId = customerId;
		}

		public void setAuthorizations(List<ErpAuthorizationModel> auths) {
			this.auths = auths;
		}


		public AuthorizationInfo getAuthInfo(ErpPaymentMethodI pm) {
			double amount = getAmount();
			for ( ErpAuthorizationModel auth : auths ) {
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
