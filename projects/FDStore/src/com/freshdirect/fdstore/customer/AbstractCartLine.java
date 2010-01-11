package com.freshdirect.fdstore.customer;

import java.util.Random;
import java.util.Set;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.content.attributes.EnumAttributeName;
import com.freshdirect.customer.ErpInvoiceLineI;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpReturnLineI;
import com.freshdirect.customer.ErpReturnLineModel;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.content.AvailabilityFactory;
import com.freshdirect.fdstore.content.ProductModel;

public abstract class AbstractCartLine extends FDProductSelection implements FDCartLineI {
	
	protected final static IDGenerator ID_GENERATOR = new HiLoGenerator("CUST", "CARTLINE_SEQ");
	
	private final static Random RND = new Random();
	/** Random ID, not persisted */
	private final int randomId = RND.nextInt();

	private final ErpInvoiceLineI firstInvoiceLine;
	private final ErpInvoiceLineI lastInvoiceLine;
	private final ErpReturnLineModel returnLine;

	private final String variantId;

	protected AbstractCartLine(FDSku sku, ProductModel productRef, FDConfigurableI configuration, String variantId) {
		super(sku, productRef, configuration, variantId);

		this.firstInvoiceLine = null;
		this.lastInvoiceLine = null;
		this.returnLine = null;
		
		this.variantId = variantId;
		//dummy();
	}

	public AbstractCartLine(
		ErpOrderLineModel orderLine,
		ErpInvoiceLineI firstInvoiceLine,
		ErpInvoiceLineI lastInvoiceLine,
		ErpReturnLineModel returnLine) {
		super(orderLine);

		this.firstInvoiceLine = firstInvoiceLine;
		this.lastInvoiceLine = lastInvoiceLine;
		this.returnLine = returnLine;

		this.variantId = orderLine.getVariantId();
	}

	public int getRandomId() {
		return this.randomId;
	}
	
	public String getCartlineId(){
		return this.orderLine.getCartlineId();
	}

	//
	// PRICING
	//

	public Discount getDiscount() {
		return this.orderLine.getDiscount();
	}

	public void setDiscount(Discount discount) {
		this.orderLine.setDiscount(discount);
		this.fireConfigurationChange();
	}

	//
	// INVOICE, RETURN
	//

	public boolean hasInvoiceLine() {
		return this.getInvoiceLine() != null;
	}

	public ErpInvoiceLineI getInvoiceLine() {
		return this.lastInvoiceLine;
	}

	public ErpInvoiceLineI getFirstInvoiceLine() {
		return this.firstInvoiceLine;
	}

	public ErpInvoiceLineI getLastInvoiceLine() {
		return this.lastInvoiceLine;
	}

	public ErpReturnLineI getReturnLine() {
		return this.returnLine;
	}

	public boolean hasReturnLine() {
		return this.getReturnLine() != null;
	}

	public boolean hasRestockingFee() {
		return this.returnLine == null ? false : this.returnLine.isRestockingOnly();
	}

	//
	// CONVENIENCE
	//

	public ErpAffiliate getAffiliate() {
		return this.orderLine.getAffiliate();
	}

	public boolean isSample() {
		return this.getDiscount() != null && EnumDiscountType.SAMPLE.equals(this.getDiscount().getDiscountType());
	}

	public boolean hasTax() {
		double value = this.hasInvoiceLine() ? this.getInvoiceLine().getTaxValue() : this.getTaxValue();
		return value > 0;
	}

	public boolean hasDepositValue() {
		double value = this.hasInvoiceLine() ? this.getInvoiceLine().getDepositValue() : this.getDepositValue();
		return value > 0;
	}

	public String getMaterialNumber() {
		return this.orderLine.getMaterialNumber();
	}

	public Set getApplicableRestrictions() {
		FDProduct fdp = this.lookupFDProduct();
		return AvailabilityFactory.getApplicableRestrictions(fdp);
	}

	public String getOrderLineId() {
		if(this.orderLine.getPK() == null)
			return this.orderLine.getOrderLineId() == null? "": this.orderLine.getOrderLineId();
		else
			return this.orderLine.getPK().getId();
	}

	public String getOrderLineNumber() {
		return this.orderLine.getOrderLineNumber();
	}
		
	//
	// FORMATTING, DISPLAY
	// 

	public String getDeliveredQuantity() {
		if (!this.hasInvoiceLine()) {
			return "";
		}
		if (this.isSoldBySalesUnits() && this.isPricedByLb()) {
			return QUANTITY_FORMATTER.format(this.getFirstInvoiceLine().getWeight());
		} else {
			return QUANTITY_FORMATTER.format(this.getFirstInvoiceLine().getQuantity());
		}
	}

	public String getReturnedQuantity() {
		if (!this.hasReturnLine()) {
			return "";
		}
		if (this.isSoldBySalesUnits() && this.isPricedByLb()) {
			return QUANTITY_FORMATTER.format(this.getFirstInvoiceLine().getWeight());
		} else {
			return QUANTITY_FORMATTER.format(this.getReturnLine().getQuantity());
		}
	}

	public String getDisplayQuantity() {
		StringBuffer qty = new StringBuffer();

		if (this.isSoldBySalesUnits()) {

			FDSalesUnit unit = this.lookupFDSalesUnit();

			qty.append(unit.getDescriptionQuantity());

			if (this.isPricedByLb()) {

				if (this.hasInvoiceLine()) {
					qty.append("/").append(QUANTITY_FORMATTER.format(this.getFirstInvoiceLine().getWeight()));
				}

				if (this.hasReturnLine()) {
					qty.append("/").append(QUANTITY_FORMATTER.format(this.getFirstInvoiceLine().getWeight()));
				}

			}
			qty.append(" ");
			qty.append(unit.getDescriptionUnit());

		} else {
			qty.append(QUANTITY_FORMATTER.format(this.getQuantity()));

			if (this.hasInvoiceLine()) {
				qty.append("/").append(QUANTITY_FORMATTER.format(this.getFirstInvoiceLine().getQuantity()));
			}
			if (this.hasReturnLine()) {
				qty.append("/").append(QUANTITY_FORMATTER.format(this.getReturnLine().getQuantity()));
			}

		}
		return qty.toString();
	}

	public String getReturnDisplayQuantity() {

		StringBuffer qty = new StringBuffer();

		qty.append(QUANTITY_FORMATTER.format(this.getQuantity()));

		if (this.isSoldBySalesUnits()) {
			FDSalesUnit unit = this.lookupFDSalesUnit();

			qty.append('(').append(unit.getDescriptionQuantity()).append(')');

			if (this.isPricedByLb()) {
				if (this.hasInvoiceLine()) {
					qty.append('/').append(QUANTITY_FORMATTER.format(this.getInvoiceLine().getQuantity()));
					qty.append('(').append(QUANTITY_FORMATTER.format(this.getInvoiceLine().getWeight())).append(')');
				}
				if (this.hasReturnLine()) {
					qty.append('/').append(QUANTITY_FORMATTER.format(this.getInvoiceLine().getQuantity()));
					qty.append('(').append(QUANTITY_FORMATTER.format(this.getReturnLine().getQuantity())).append(')');
				}
			}
			qty.append(' ').append(unit.getDescriptionUnit());

		} else {
			if (this.hasInvoiceLine()) {
				qty.append('/').append(QUANTITY_FORMATTER.format(this.getInvoiceLine().getQuantity()));
			}
			if (this.hasReturnLine()) {
				qty.append('/').append(QUANTITY_FORMATTER.format(this.getReturnLine().getQuantity()));
			}
		}
		return qty.toString();
	}

	public boolean hasAdvanceOrderFlag() {
		FDProduct fdp = this.lookupFDProduct();
		return fdp.getAttributeBoolean(EnumAttributeName.ADVANCE_ORDER_FLAG.getName(),false);
	}
	

	public String getVariantId() {
		return this.variantId;
	}

	public void setOrderLineId(String orderLineId){
		this.orderLine.setOrderLineId(orderLineId);
	}
	
	public boolean isDiscountApplied() {
		return this.getDiscount() != null && (EnumDiscountType.DOLLAR_OFF.equals(this.getDiscount().getDiscountType()) 
				|| EnumDiscountType.PERCENT_OFF.equals(this.getDiscount().getDiscountType()));
	}
	
	public String getDiscountedUnitPrice(){
		if(!isDiscountApplied()) {
			return "";
		}
		if(EnumDiscountType.DOLLAR_OFF.equals(this.getDiscount().getDiscountType())) 
			return CURRENCY_FORMATTER.format(this.price.getBasePrice() - this.getDiscount().getAmount())  + "/" + this.price.getBasePriceUnit().toLowerCase();
		else if(EnumDiscountType.PERCENT_OFF.equals(this.getDiscount().getDiscountType())){
			double discountAmt = this.price.getBasePrice() * this.getDiscount().getAmount();
			return CURRENCY_FORMATTER.format(this.price.getBasePrice() - discountAmt)  + "/" + this.price.getBasePriceUnit().toLowerCase();
		}else {
			throw new IllegalArgumentException("Invalid Discount Type");
			
		}
	}
	public String getLineItemDiscount() {
		return CURRENCY_FORMATTER.format(this.price.getPromotionValue());
	}
}
