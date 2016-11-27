package com.freshdirect.payment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
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
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.ErpAppliedGiftCardModel;

public class AuthorizationStrategy extends PaymentStrategy {

	private static final long	serialVersionUID	= -193567950363428880L;
	private static final Category LOGGER = LoggerFactory.getInstance(AuthorizationStrategy.class);
	
	private final AuthInfo fdAuthInfo;
	private final AuthInfo bcAuthInfo;
	private final AuthInfo usqAuthInfo;
	private final AuthInfo fdwAuthInfo;

	public AuthorizationStrategy(ErpSaleModel sale) {
		super(sale);
		double perishableBuffer = getPerishableBuffer(sale);
		this.fdAuthInfo = new AuthInfo(
			sale.getPK().getId(), 
			sale.getCustomerPk().getId(),
			ErpAffiliate.getPrimaryAffiliate(sale.geteStoreId()),perishableBuffer);
		this.bcAuthInfo = new AuthInfo(
			sale.getPK().getId(),
			sale.getCustomerPk().getId(),
			ErpAffiliate.getEnum(ErpAffiliate.CODE_BC),perishableBuffer);
		this.usqAuthInfo = new AuthInfo(
				sale.getPK().getId(),
				sale.getCustomerPk().getId(),
				ErpAffiliate.getEnum(ErpAffiliate.CODE_USQ),perishableBuffer);
		this.fdwAuthInfo = new AuthInfo(
				sale.getPK().getId(),
				sale.getCustomerPk().getId(),
				ErpAffiliate.getEnum(ErpAffiliate.CODE_FDW),perishableBuffer);
	}

	public List<AuthorizationInfo> getOutstandingAuthorizations() {
		ErpAbstractOrderModel order = this.sale.getCurrentOrder();
		ErpInvoiceModel inv = this.sale.getLastInvoice();
		final ErpAffiliate bc = ErpAffiliate.getEnum(ErpAffiliate.CODE_BC);
		final ErpAffiliate usq = ErpAffiliate.getEnum(ErpAffiliate.CODE_USQ);
		final ErpAffiliate fdw = ErpAffiliate.getEnum(ErpAffiliate.CODE_FDW);

		for ( ErpOrderLineModel line : order.getOrderLines() ) {
			if (usq.equals(line.getAffiliate())) {
				if (inv != null) {
					usqAuthInfo.addInvoiceLine(inv.getInvoiceLine(line.getOrderLineNumber()));
				} else {
					usqAuthInfo.addOrderline(line);
				}
			} else if (fdw.equals(line.getAffiliate())) {
				if (inv != null) {
					fdwAuthInfo.addInvoiceLine(inv.getInvoiceLine(line.getOrderLineNumber()));
				} else {
					fdwAuthInfo.addOrderline(line);
				}
			} else if (bc.equals(line.getAffiliate())) {
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
			} else if(fdw.equals(agc.getAffiliate())) {
				fdwAuthInfo.addGCPayment(agc.getAmount());
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
		remainder = fdwAuthInfo.addDeduction(remainder);
		
		if (remainder > 0) {
			throw new FDRuntimeException("Applied more discount than order pre deduction total");
		}
	}

	private List<AuthorizationInfo> getOutstandingAuthorizations(ErpPaymentMethodI pm) {
		if(pm.isGiftCard()) return Collections.emptyList();
		this.fdAuthInfo.setAuthorizations(this.sale.getApprovedAuthorizations(this.fdAuthInfo.affiliate, pm));
		this.bcAuthInfo.setAuthorizations(this.sale.getApprovedAuthorizations(this.bcAuthInfo.affiliate, pm));
		this.usqAuthInfo.setAuthorizations(this.sale.getApprovedAuthorizations(this.usqAuthInfo.affiliate, pm));
		this.fdwAuthInfo.setAuthorizations(this.sale.getApprovedAuthorizations(this.fdwAuthInfo.affiliate, pm));
		
		List<AuthorizationInfo> auths = new ArrayList<AuthorizationInfo>();
		
		
		AuthorizationInfo bcInfo = bcAuthInfo.getAuthInfo(pm);		
		if(bcInfo.getAmount() > 0) {
			auths.add(bcInfo);
		}

		AuthorizationInfo usqInfo = usqAuthInfo.getAuthInfo(pm);		
		if(usqInfo.getAmount() > 0) {
			auths.add(usqInfo);
		}
		
		AuthorizationInfo fdwInfo = fdwAuthInfo.getAuthInfo(pm);		
		if(fdwInfo.getAmount() > 0) {
			auths.add(fdwInfo);
		}
		
		/** Move fdAuth to the end to prevent auth holds when usq auths fail */
		AuthorizationInfo fdInfo = fdAuthInfo.getAuthInfo(pm);				
		if(fdInfo.getAmount() > 0) {
			auths.add(fdInfo);
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

		public AuthInfo(String saleId, String customerId, ErpAffiliate affiliate, double perishableBuffer) {
			super(saleId, affiliate,perishableBuffer);
			this.customerId = customerId;
		}

		public void setAuthorizations(List<ErpAuthorizationModel> auths) {
			this.auths = auths;
		}


		public AuthorizationInfo getAuthInfo(ErpPaymentMethodI pm) {
			double amount = getAmount();
			for ( ErpAuthorizationModel auth : auths ) {
				if (EnumPaymentType.MAKE_GOOD.equals(pm.getPaymentType())
					|| EnumPaymentType.ON_FD_ACCOUNT.equals(pm.getPaymentType()) ||(EnumPaymentMethodType.EBT.equals(pm.getPaymentMethodType()) && pm.getPaymentMethodType().equals(auth.getPaymentMethodType()))) {
					amount = MathUtil.roundDecimal(amount - auth.getAmount());
				} else {
					
					if(!StringUtil.isEmpty(pm.getProfileID())) {
						
						if(pm.getProfileID().equals(auth.getProfileID())) {
							amount = MathUtil.roundDecimal(amount - auth.getAmount());
						}
						
					}
				 else {
						if ( (EnumCardType.GCP.equals(pm.getCardType())||EnumCardType.EBT.equals(pm.getCardType())) &&
							 pm.getCardType().equals(auth.getCardType()) && 
							 pm.getAccountNumber().endsWith(auth.getCcNumLast4())
							 ) {
							
							amount = MathUtil.roundDecimal(amount - auth.getAmount());
						}
					}
				}

			}
			if(pm!=null && affiliate!=null )
				LOGGER.info(new StringBuilder(200).append(" Needed authorization amount for order: ")
					                          .append(saleId).append(" and affiliate :")
					                          .append(affiliate.getName()).append(" is $")
					                          .append(amount).append(" for paymentMethodType: ").append(pm.getPaymentMethodType()).append(" with last 4 digits ").append(pm.getMaskedAccountNumber())
					                          .append(" and profileID: ").append(pm.getProfileID())
					                          .toString());
			return new AuthorizationInfo(saleId, customerId, affiliate, amount, pm, false);
		}

	}

}
