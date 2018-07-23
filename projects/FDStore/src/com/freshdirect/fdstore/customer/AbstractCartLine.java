package com.freshdirect.fdstore.customer;

import java.util.Random;
import java.util.Set;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.content.attributes.EnumAttributeName;
import com.freshdirect.customer.ErpCouponDiscountLineModel;
import com.freshdirect.customer.ErpInvoiceLineI;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpReturnLineI;
import com.freshdirect.customer.ErpReturnLineModel;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.ecoupon.EnumCouponStatus;
import com.freshdirect.storeapi.content.AvailabilityFactory;
import com.freshdirect.storeapi.content.ProductModel;

public abstract class AbstractCartLine extends FDProductSelection implements FDCartLineI {
	
	private static final long	serialVersionUID	= -2991659761444741368L;

	protected final static IDGenerator ID_GENERATOR = new HiLoGenerator("CUST", "CARTLINE_SEQ");
	
	private final static Random RND = new Random();
	/** Random ID, not persisted */
	private final int randomId = RND.nextInt();

	private final FDInvoiceLineModel firstInvoiceLine;
	private final FDInvoiceLineModel lastInvoiceLine;
	private final ErpReturnLineModel returnLine;

	private final String variantId;
	private EnumCouponStatus couponStatus;
	@Deprecated private boolean couponApplied;

	protected AbstractCartLine(FDSku sku, ProductModel productRef, FDConfigurableI configuration, String variantId, UserContext userCtx) {
		super(sku, productRef, configuration, variantId, userCtx);

		this.firstInvoiceLine = null;
		this.lastInvoiceLine = null;
		this.returnLine = null;
		
		this.variantId = variantId;
	}
	
	//Introduced for Storefront 2.0 implementation only.
	protected AbstractCartLine(FDSku sku, ProductModel productRef, FDConfigurableI configuration, String variantId, UserContext userCtx, String plantId) {
		super(sku, productRef, configuration, variantId, userCtx,plantId);
		
		this.firstInvoiceLine = null;
		this.lastInvoiceLine = null;
		this.returnLine = null;
		
		this.variantId = variantId;
	}

	public AbstractCartLine(
			ErpOrderLineModel orderLine,
			ErpInvoiceLineI firstInvoiceLine,
			ErpInvoiceLineI lastInvoiceLine,
			ErpReturnLineModel returnLine) {
		this(orderLine, firstInvoiceLine, lastInvoiceLine, returnLine, false);
	}

	public AbstractCartLine(
	        ErpOrderLineModel orderLine,
	        ErpInvoiceLineI firstInvoiceLine,
	        ErpInvoiceLineI lastInvoiceLine,
	        ErpReturnLineModel returnLine,
	        boolean lazy) {
	        super(orderLine, lazy);

	        if (firstInvoiceLine==null) {
	    		this.firstInvoiceLine = null;
			} else {
				this.firstInvoiceLine = new FDInvoiceLineModel(firstInvoiceLine,getUserContext());
			}
	        if (lastInvoiceLine==null){
	        	this.lastInvoiceLine = null;
	        } else {
	        this.lastInvoiceLine = new FDInvoiceLineModel(lastInvoiceLine,getUserContext());
	        }
	        this.returnLine = returnLine;

	        this.variantId = orderLine.getVariantId();
	    }

	@Override
    public int getRandomId() {
		return this.randomId;
	}
	
	@Override
    public String getCartlineId(){
		return this.orderLine.getCartlineId();
	}

	//
	// PRICING
	//

	@Override
    public Discount getDiscount() {
		return this.orderLine.getDiscount();
	}

	@Override
    public void setDiscount(Discount discount) {
		this.orderLine.setDiscount(discount);
		this.fireConfigurationChange();
	}

	
	@Override
    public ErpCouponDiscountLineModel getCouponDiscount() {
		return this.orderLine.getCouponDiscount();
	}

	@Override
    public void setCouponDiscount(ErpCouponDiscountLineModel discount) {
		this.orderLine.setCouponDiscount(discount);
		this.fireConfigurationChange();
	}
	
	@Override
    public void clearCouponDiscount(){
		this.setCouponDiscount(null);
		this.setCouponStatus(null);
		this.setCouponApplied(false);
	}
	//
	// INVOICE, RETURN
	//

	@Override
    public boolean hasInvoiceLine() {
		return this.getInvoiceLine() != null;
	}

	@Override
    public FDInvoiceLineModel getInvoiceLine() {
		return this.lastInvoiceLine;
	}

	public FDInvoiceLineModel getFirstInvoiceLine() {
		return this.firstInvoiceLine;
	}

	public FDInvoiceLineModel getLastInvoiceLine() {
		return this.lastInvoiceLine;
	}

	@Override
    public ErpReturnLineI getReturnLine() {
		return this.returnLine;
	}

	@Override
    public boolean hasReturnLine() {
		return this.getReturnLine() != null;
	}

	@Override
    public boolean hasRestockingFee() {
		return this.returnLine == null ? false : this.returnLine.isRestockingOnly();
	}

	//
	// CONVENIENCE
	//

	@Override
    public ErpAffiliate getAffiliate() {
		return this.orderLine.getAffiliate();
	}

	@Override
    public boolean isSample() {
		return this.getDiscount() != null && EnumDiscountType.SAMPLE.equals(this.getDiscount().getDiscountType());
	}

	@Override
    public boolean hasTax() {
		double value = this.hasInvoiceLine() ? this.getInvoiceLine().getTaxValue() : this.getTaxValue();
		return value > 0;
	}

	@Override
    public boolean hasDepositValue() {
		double value = this.hasInvoiceLine() ? this.getInvoiceLine().getDepositValue() : this.getDepositValue();
		return value > 0;
	}

	@Override
    public String getMaterialNumber() {
		return this.orderLine.getMaterialNumber();
	}
	
	@Override
    public String getMaterialGroup(){
		return this.orderLine.getMaterialGroup();
	}

	@Override
    public Set<EnumDlvRestrictionReason> getApplicableRestrictions() {
		FDProduct fdp = this.lookupFDProduct();
		FDProductInfo fdpi = this.lookupFDProductInfo();
		return AvailabilityFactory.getApplicableRestrictions(fdp,fdpi);
	}

	@Override
    public String getOrderLineId() {
		if(this.orderLine.getPK() == null)
			return this.orderLine.getOrderLineId() == null? "": this.orderLine.getOrderLineId();
		else
			return this.orderLine.getPK().getId();
	}

	@Override
    public String getOrderLineNumber() {
		return this.orderLine.getOrderLineNumber();
	}
		
	//
	// FORMATTING, DISPLAY
	// 

	@Override
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

	@Override
    public String getSubstitutedQuantity() {
		FDInvoiceLineI invLine = this.getInvoiceLine();
		if (null!=invLine && null!= invLine.getSubstitutedSkuCode() && !"".equalsIgnoreCase(invLine.getSubstitutedSkuCode().trim())) {
			return QUANTITY_FORMATTER.format(this.getFirstInvoiceLine().getQuantity());
		} else {
			return "";
		}
	}
	
	public String getSubstituteProductName() {
		if (!this.hasInvoiceLine()) {
			return "";
		}
		return this.getFirstInvoiceLine().getSubstituteProductName(); 
	
	}
	
	public String getSubstituteProductDefaultPrice() {
		if (!this.hasInvoiceLine()) {
			return "";
		}
		return this.getFirstInvoiceLine().getSubstituteProductDefaultPrice();
	}
	
	public String getSubSkuStatus(){
		if (!this.hasInvoiceLine()) {
			return "";
		}
		return this.getFirstInvoiceLine().getSubSkuStatus();
	}
	
	
	@Override
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

	@Override
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

	@Override
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

	@Override
    public boolean hasAdvanceOrderFlag() {
		FDProduct fdp = this.lookupFDProduct();
		return fdp.getAttributeBoolean(EnumAttributeName.ADVANCE_ORDER_FLAG.getName(),false);
	}
	

	@Override
    public String getVariantId() {
		return this.variantId;
	}

	@Override
    public void setOrderLineId(String orderLineId){
		this.orderLine.setOrderLineId(orderLineId);
	}
	
	@Override
    public boolean isDiscountApplied() {
		return ((this.getDiscount() != null && (EnumDiscountType.DOLLAR_OFF.equals(this.getDiscount().getDiscountType()) 
				|| EnumDiscountType.PERCENT_OFF.equals(this.getDiscount().getDiscountType())))|| 
				(this.getCouponDiscount()!=null && EnumDiscountType.DOLLAR_OFF.equals(this.getCouponDiscount().getDiscountType())));
	}
	
	public String getDiscountedUnitPrice(){
		String discountedUnitPrice="";
		double discountAmt =0.0;
		if(!isDiscountApplied()) {
			return "";
		}
		if(null !=getDiscount()){
			if(EnumDiscountType.DOLLAR_OFF.equals(this.getDiscount().getDiscountType())) {
				discountAmt=this.getDiscount().getAmount();
			}else if(EnumDiscountType.PERCENT_OFF.equals(this.getDiscount().getDiscountType())){
				discountAmt = this.price.getBasePrice() * this.getDiscount().getAmount();
			}else {
				throw new IllegalArgumentException("Invalid Discount Type");			
			}
		}
		if(null!=getCouponDiscount()){
			discountAmt = discountAmt+getCouponDiscount().getDiscountAmt();
		}
		discountedUnitPrice = CURRENCY_FORMATTER.format(this.price.getBasePrice() - discountAmt)  + "/" + this.price.getBasePriceUnit().toLowerCase();
		return discountedUnitPrice;
	}
	public String getLineItemDiscount() {
		return CURRENCY_FORMATTER.format(this.price.getPromotionValue());
	}

	public String getLineItemCouponDiscount() {
		return CURRENCY_FORMATTER.format(this.price.getCouponDiscountValue());
	}
	
	/**
	 * @return the couponStatus
	 */
	@Override
    public EnumCouponStatus getCouponStatus() {
		return couponStatus;
	}

	/**
	 * @param couponStatus the couponStatus to set
	 */
	@Override
    public void setCouponStatus(EnumCouponStatus couponStatus) {
		this.couponStatus = couponStatus;
	}
	
	@Override @Deprecated
	public boolean hasCouponApplied() {
		return couponApplied;
	}

	@Override @Deprecated
	public void setCouponApplied(boolean applied) {
		this.couponApplied =applied;
		
	}

	@Override
	public void setEStoreId(EnumEStoreId eStore) {
		this.orderLine.setEStoreId(eStore);
		
	}

	@Override
	public EnumEStoreId getEStoreId() {
		return this.orderLine.getEStoreId();
	}

	@Override
	public void setPlantId(String plantId) {
		
		 this.orderLine.setPlantID(plantId);
	}

	@Override
	public String getPlantId() {
		
		return this.orderLine.getPlantID();
	}
}
