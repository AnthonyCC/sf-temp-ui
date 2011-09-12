/*
 * Created on Jun 20, 2003
 */
package com.freshdirect.customer.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.common.pricing.Price;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.common.pricing.PricingEngine;
import com.freshdirect.common.pricing.PricingException;

import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumPaymentType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAppliedCreditModel;
import com.freshdirect.customer.ErpChargeInvoiceModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpInvoiceLineModel;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.customer.ErpInvoicedCreditModel;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpReturnLineModel;
import com.freshdirect.customer.ErpReturnOrderModel;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDConfiguredPrice;
import com.freshdirect.fdstore.FDPricingEngine;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.framework.util.MathUtil;
import com.freshdirect.customer.ErpDiscountLineModel;

/**
 * @author knadeem
 */

public class ErpGenerateInvoiceCommand {

	private final double DRY_GOODS_RESTOCKING_PERC = .25;
	private final double ALCOHOL_RESTOCKING_PERC = .5;
	private final double PERISHABLE_RESTOCKING_PERC = 1;

	private double fdRestocking = 0;
	private double wblRestocking = 0;
	private double bcRestocking = 0;
	private double usqRestocking = 0;

	public ErpInvoiceModel generateNewInvoice(ErpAbstractOrderModel order, ErpInvoiceModel invoice, ErpReturnOrderModel returnOrder) {

		HashMap lineMap = new HashMap();
		for (Iterator i = returnOrder.getInvoiceLines().iterator(); i.hasNext();) {
			ErpReturnLineModel returnLine = (ErpReturnLineModel)i.next();
			lineMap.put(returnLine.getLineNumber(), returnLine);
		}

		List newInvoiceLines = new ArrayList();

		for (Iterator i = invoice.getInvoiceLines().iterator(); i.hasNext();) {

			ErpInvoiceLineModel invoiceLine = (ErpInvoiceLineModel) i.next();
			ErpOrderLineModel orderLine = order.getOrderLine(invoiceLine.getOrderLineNumber());
			ErpReturnLineModel returnLine = (ErpReturnLineModel) lineMap.get(invoiceLine.getOrderLineNumber());

			ErpInvoiceLineModel newInvoiceLine =
				returnLine == null
					? new ErpInvoiceLineModel(invoiceLine)
					: createNewInvoiceLine(orderLine, invoiceLine, returnLine);

			newInvoiceLines.add(newInvoiceLine);
		}
		
		return this.makeNewInvoice(order, invoice, newInvoiceLines, returnOrder.getCharges());
	}
	private ErpInvoiceLineModel createNewInvoiceLine(
		ErpOrderLineModel orderLine,
		ErpInvoiceLineModel invoiceLine,
		ErpReturnLineModel returnLine) {

		if (((int) Math.round(returnLine.getQuantity() * 100)) > ((int) Math.round(invoiceLine.getQuantity() * 100))) {
			throw new EJBException("Cannot return more than ordered");
		}

		ErpInvoiceLineModel newInvoiceLine = new ErpInvoiceLineModel();
		newInvoiceLine.setOrderLineNumber(invoiceLine.getOrderLineNumber());
		newInvoiceLine.setMaterialNumber(invoiceLine.getMaterialNumber());
        
		if (returnLine.isRestockingOnly()) {
			double returnPrice = this.getDiscountedPrice(orderLine, returnLine.getQuantity()).getPrice();			
			if(ErpAffiliate.getEnum(ErpAffiliate.CODE_WBL).equals(orderLine.getAffiliate())){
				wblRestocking += returnPrice * ALCOHOL_RESTOCKING_PERC;
			} else if(ErpAffiliate.getEnum(ErpAffiliate.CODE_BC).equals(orderLine.getAffiliate())){
				bcRestocking += returnPrice * ALCOHOL_RESTOCKING_PERC;
			}else if(ErpAffiliate.getEnum(ErpAffiliate.CODE_USQ).equals(orderLine.getAffiliate())){
				usqRestocking += returnPrice * ALCOHOL_RESTOCKING_PERC;
			} else {
				double restockingPerc = orderLine.isPerishable() ? PERISHABLE_RESTOCKING_PERC : DRY_GOODS_RESTOCKING_PERC;
				fdRestocking += returnPrice * restockingPerc;
			}
		}

		double finalQuantity = invoiceLine.getQuantity() - returnLine.getQuantity();

		newInvoiceLine.setQuantity(finalQuantity);
		this.priceInvoiceLine(newInvoiceLine, orderLine);
		
		//System.out.println("newInvoiceLine.getPrice():"+newInvoiceLine.getPrice());
		
		if (invoiceLine.getQuantity()!=0) {
			double finalRatio = returnLine.getQuantity() / invoiceLine.getQuantity();
			newInvoiceLine.setWeight(invoiceLine.getWeight() * finalRatio);
		} else {
			newInvoiceLine.setWeight(0);
		}
		return newInvoiceLine;
	}

	private FDProduct getFDProduct(ErpOrderLineModel orderLine) {
		try {
			return FDCachedFactory.getProduct(orderLine.getSku());
		} catch (FDSkuNotFoundException e) {
			throw new EJBException(e);
		} catch (FDResourceException e) {
			throw new EJBException(e);
		}
	}
	
	
	private Price getDiscountedPrice(ErpOrderLineModel orderLine, double quantity) {
		if (((int) Math.round(quantity * 100)) == 0) {
			return new Price(0, 0);
		}
		FDProduct fdProduct = this.getFDProduct(orderLine);
		Pricing pricing = fdProduct.getPricing();
		FDConfiguration prConf = new FDConfiguration(quantity, orderLine.getSalesUnit(), orderLine.getOptions());
		try {
			FDConfiguredPrice price = FDPricingEngine.doPricing(fdProduct, prConf, orderLine.getDiscount(), orderLine.getPricingContext(), orderLine.getFDGroup(), orderLine.getGroupQuantity());
			orderLine.setPrice(price.getConfiguredPrice() - price.getPromotionValue());
			orderLine.setDiscountAmount(price.getPromotionValue());			
			Price oldPrice=PricingEngine.getConfiguredPrice(pricing, prConf, orderLine.getPricingContext(),orderLine.getFDGroup(), orderLine.getGroupQuantity()).getPrice();
			Price pr = new Price(MathUtil.roundDecimal(oldPrice.getBasePrice()-price.getPromotionValue()), MathUtil.roundDecimal(oldPrice.getSurcharge()));
			return pr;
		} catch (PricingException e) {
			throw new EJBException(e);
		}					
	}
	
	

	private Price getPrice(ErpOrderLineModel orderLine, double quantity) {
		if (((int) Math.round(quantity * 100)) == 0) {
			return new Price(0, 0);
		}
		FDProduct fdProduct = this.getFDProduct(orderLine);
		Pricing pricing = fdProduct.getPricing();
		FDConfiguration prConf = new FDConfiguration(quantity, orderLine.getSalesUnit(), orderLine.getOptions());
		try {
			return PricingEngine.getConfiguredPrice(pricing, prConf, orderLine.getPricingContext(),orderLine.getFDGroup(), orderLine.getGroupQuantity()).getPrice();
		} catch (PricingException e) {
			throw new EJBException(e);
		}						
	}

	private void priceInvoiceLine(ErpInvoiceLineModel invoiceLine, ErpOrderLineModel orderLine) {
		
		Price configuredPrice = this.getPrice(orderLine, invoiceLine.getQuantity());
      
		invoiceLine.setPrice(configuredPrice.getPrice());
		invoiceLine.setCustomizationPrice(configuredPrice.getSurcharge());
		
		FDProduct fdProduct = this.getFDProduct(orderLine);
		double tax = fdProduct.isTaxable() ? invoiceLine.getPrice() * orderLine.getTaxRate() : 0;
		double depositValue = calculateDepositValue(fdProduct.getDepositsPerEach(), orderLine.getDepositValue(), orderLine.getQuantity(), invoiceLine.getQuantity());
		double discountAmount=0;
		try {
			Price afterDiscountPrice=PricingEngine.applyDiscount(configuredPrice,invoiceLine.getQuantity(),orderLine.getDiscount());
			if(afterDiscountPrice!=null){
				double afterDiscountAmount=afterDiscountPrice.getBasePrice();
				discountAmount=invoiceLine.getPrice()-afterDiscountAmount;
			}
		} catch (PricingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("actual discount amount :"+discountAmount);
		//System.out.println("actual amount before discount:"+configuredPrice.getPrice());		
		invoiceLine.setActualDiscountAmount(discountAmount);
		invoiceLine.setPrice(configuredPrice.getPrice()-discountAmount);
		//System.out.println("actual amount after discount:"+invoiceLine.getPrice());
		invoiceLine.setTaxValue(tax);
		invoiceLine.setDepositValue(depositValue);
	}
	
	protected double calculateDepositValue(int depositsPerEach, double origDepositValue, double origQuantity, double retQuantity){
		double deposit_price = depositsPerEach == 0 ? 0 : origDepositValue / (origQuantity * depositsPerEach);
		return MathUtil.roundDecimal(retQuantity * depositsPerEach * deposit_price);
	}

	private ErpInvoiceModel makeNewInvoice(ErpAbstractOrderModel order, ErpInvoiceModel oldInvoice, List invoiceLines, List charges) {
		ErpInvoiceModel invoice = new ErpInvoiceModel(); 
		invoice.setTransactionDate(new Date());
		invoice.setTransactionSource(EnumTransactionSource.SYSTEM);
		invoice.setInvoiceNumber(oldInvoice.getInvoiceNumber());

		double total = 0.0;
		double tax = 0.0;
		double deposit = 0.0;
		for (Iterator i = invoiceLines.iterator(); i.hasNext();) {
			ErpInvoiceLineModel invoiceLine = (ErpInvoiceLineModel) i.next();
			total += invoiceLine.getPrice();
			tax += invoiceLine.getTaxValue();
			deposit += invoiceLine.getDepositValue();
		}

		invoice.setSubTotal(total);

		List newCharges = new ArrayList();
		for (Iterator i = charges.iterator(); i.hasNext();) {
			ErpChargeLineModel charge = (ErpChargeLineModel) i.next();
			if(EnumChargeType.DELIVERY.equals(charge.getType()) && tax > 0){
				charge.setTaxRate(getDeliveryTaxRate(order.getCharges()));
			}
			
			if (charge.getTotalAmount() > 0) {
				total += charge.getTotalAmount();
				tax += charge.getTotalAmount() * charge.getTaxRate();
				newCharges.add(new ErpChargeLineModel(charge));
			}
		}
		
		invoice.setTax(tax);
		total += tax;
		total += deposit;

		if (this.fdRestocking > 0) {
			newCharges.add(new ErpChargeLineModel(EnumChargeType.FD_RESTOCKING_FEE, "", this.fdRestocking, null, 0.0));
			total += this.fdRestocking;
		}

		if (this.wblRestocking > 0) {
			newCharges.add(new ErpChargeLineModel(EnumChargeType.WBL_RESTOCKING_FEE, "", this.wblRestocking, null, 0.0));
			total += this.wblRestocking;
		}
		
		if(this.bcRestocking > 0) {
			newCharges.add(new ErpChargeLineModel(EnumChargeType.BC_RESTOCKING_FEE, "", this.bcRestocking, null, 0.0));
			total += this.bcRestocking;
		}
		if(this.usqRestocking > 0) {
			newCharges.add(new ErpChargeLineModel(EnumChargeType.USQ_RESTOCKING_FEE, "", this.usqRestocking, null, 0.0));
			total += this.usqRestocking;
		}

		invoice.setInvoiceLines(invoiceLines);
		invoice.setCharges(newCharges);

		if (oldInvoice.getAppliedCredits().size() > 0) {
			List appliedCredits = new ArrayList();
			for (Iterator i = oldInvoice.getAppliedCredits().iterator(); i.hasNext();) {
				
				ErpAppliedCreditModel credit = (ErpAppliedCreditModel) i.next();
				ErpInvoicedCreditModel newCredit = new ErpInvoicedCreditModel();

				double creditAmount = Math.min(total, credit.getAmount());
				
				if(total > 0 ){
					total -= creditAmount;
				}
				
				newCredit.setAmount(creditAmount);
				newCredit.setCustomerCreditPk(credit.getCustomerCreditPk());
				newCredit.setAffiliate(credit.getAffiliate());
				newCredit.setDepartment(credit.getDepartment());
				newCredit.setSapNumber(credit.getSapNumber());
				newCredit.setOriginalCreditId(credit.getPK().getId());

				appliedCredits.add(newCredit);
			}
			
			invoice.setAppliedCredits(appliedCredits);
		}
		
		double newAmount = 0.0;

		if (oldInvoice.getDiscounts() != null && oldInvoice.getDiscounts().size() > 0) {
			double totalDiscountAmount = 0.0;
			// see if discounts need to be reduced
			for (Iterator iter = oldInvoice.getDiscounts().iterator(); iter.hasNext();) {
				ErpDiscountLineModel oldDiscountLine = (ErpDiscountLineModel) iter.next();
				Discount oldDiscount = oldDiscountLine.getDiscount();
				if (EnumDiscountType.DOLLAR_OFF.equals(oldDiscount.getDiscountType())) {
					totalDiscountAmount += oldDiscount.getAmount();
				}				
			}			
			newAmount = Math.min(totalDiscountAmount, invoice.getSubTotal());
			if (newAmount != totalDiscountAmount) {
				// discount amount needs to be reduced  --> reduce amount by order of discounts  
				double discDiff = totalDiscountAmount - invoice.getSubTotal();
				for (Iterator iter = oldInvoice.getDiscounts().iterator(); iter.hasNext();) {
					ErpDiscountLineModel oldDiscountLine = (ErpDiscountLineModel) iter.next();
					Discount oldDiscount = oldDiscountLine.getDiscount();					
					if (discDiff > 0.0 && EnumDiscountType.DOLLAR_OFF.equals(oldDiscount.getDiscountType())) {
						double reducedAmount = Math.min(discDiff, oldDiscount.getAmount());
						double newDiscountAmount = oldDiscount.getAmount() - reducedAmount;
						Discount newDiscount = new Discount(oldDiscount.getPromotionCode(), EnumDiscountType.DOLLAR_OFF, newDiscountAmount);
						invoice.addDiscount(new ErpDiscountLineModel(newDiscount));
						discDiff -= reducedAmount;
					}
					else {
						invoice.addDiscount(new ErpDiscountLineModel(oldDiscount));						
					}
				}			
			} else {  // discounts don't need to be modified
				//invoice.setDiscounts(oldInvoice.getDiscounts());
				ErpDiscountLineModel oldDiscountLine =null;
				Discount oldDiscount =null;
				for (Iterator iter = oldInvoice.getDiscounts().iterator(); iter.hasNext();) {
					oldDiscountLine = (ErpDiscountLineModel) iter.next();
					oldDiscount = oldDiscountLine.getDiscount();					
					invoice.addDiscount(new ErpDiscountLineModel(oldDiscount));
				} 
			}
			total -= newAmount;
		}
		

		if (EnumPaymentType.MAKE_GOOD.equals(order.getPaymentMethod().getPaymentType())) {
			total = 0;
		}
		invoice.setAmount(total);

		return invoice;
	}
	
	public ErpChargeInvoiceModel makeChargeInvoice(String saleId, double charge) {
		ErpChargeInvoiceModel chargeInvoice = new ErpChargeInvoiceModel();
		chargeInvoice.setTransactionDate(new Date());
		chargeInvoice.setTransactionSource(EnumTransactionSource.SYSTEM);
		chargeInvoice.setInvoiceNumber(saleId);
		chargeInvoice.setSubTotal(charge);
		
		List newCharges = new ArrayList();
		ErpChargeLineModel chargeLineModel = new ErpChargeLineModel();
		chargeLineModel.setAmount(charge);
		chargeLineModel.setType(EnumChargeType.BOUNCED_CHECK);
		newCharges.add(chargeLineModel);
		
		chargeInvoice.setTax(0);
		chargeInvoice.setCharges(newCharges);
		chargeInvoice.setAmount(charge);

		return chargeInvoice;
	}
	
	private double getDeliveryTaxRate(List oldInvoiceCharges){
		for(Iterator i = oldInvoiceCharges.iterator(); i.hasNext();){
			ErpChargeLineModel cl = (ErpChargeLineModel) i.next();
			if(EnumChargeType.DELIVERY.equals(cl.getType())){
				return cl.getTaxRate();
			}
		}
		
		return 0.0;
	}

}
