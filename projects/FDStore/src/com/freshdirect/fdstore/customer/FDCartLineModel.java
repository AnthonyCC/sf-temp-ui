package com.freshdirect.fdstore.customer;

import com.freshdirect.customer.ErpInvoiceLineI;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpReturnLineModel;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.sap.PosexUtil;

/**
 *
 * @version    $Revision:16$
 * @author     $Author:Kashif Nadeem$
 * @stereotype fd-model
 */
public class FDCartLineModel extends AbstractCartLine {

	private static final long	serialVersionUID	= 6554964787371568944L;
	
	private EnumEventSource source;
	private String cartonNumber;
	
	public FDCartLineModel(ErpOrderLineModel orderLine) {
		this(orderLine, null, null, null);
	}

	public FDCartLineModel(
		ErpOrderLineModel orderLine,
		ErpInvoiceLineI firstInvoiceLine,
		ErpInvoiceLineI lastInvoiceLine,
		ErpReturnLineModel returnLine) {
		super(orderLine, firstInvoiceLine, lastInvoiceLine, returnLine);
	}

	public FDCartLineModel(FDSku sku, ProductModel productRef, FDConfigurableI configuration, String variantId, String pZoneId) {
		super(sku, productRef, configuration, variantId, pZoneId);
		this.orderLine.setCartlineId(ID_GENERATOR.getNextId());
	}
	
	public FDCartLineModel(FDSku sku, ProductModel productRef, FDConfigurableI configuration, String cartlineId, String recipeSourceId, boolean requestNotification, String variantId, String pZoneId) {
		super(sku, productRef, configuration, variantId, pZoneId);
		this.orderLine.setCartlineId(cartlineId);
		this.orderLine.setRecipeSourceId(recipeSourceId);
		this.orderLine.setRequestNotification(requestNotification);
		this.orderLine.setVariantId(variantId);
	}
	
	public FDCartLineModel( FDProductSelectionI ps ) {
		this( ps.getSku(), ps.getProductRef().lookupProductModel(), ps.getConfiguration(), null, ps.getPricingContext().getZoneId() );
	}

	public ErpOrderLineModel buildErpOrderLines(int baseLineNumber) throws FDResourceException, FDInvalidConfigurationException {
		this.refreshConfiguration();
		ErpOrderLineModel ol = (ErpOrderLineModel) this.orderLine.deepCopy();
      
		try {
			if(ol.getSku()!=null){

				FDProductInfo productInfo = FDCachedFactory.getProductInfo(ol.getSku().getSkuCode());
				EnumOrderLineRating rating=EnumOrderLineRating.getEnumByStatusCode(productInfo.getRating());
				ol.setProduceRating(rating);
				ol.setBasePrice(productInfo.getZonePriceInfo(getPricingContext().getZoneId()).getSellingPrice());
				ol.setBasePriceUnit(productInfo.getDefaultPriceUnit());	
				//ol.setUserZoneId(getPricingContext().getZoneId());				
				ol.setPricingZoneId(productInfo.getZonePriceInfo(getPricingContext().getZoneId()).getSapZoneId());
			}			
		} catch (FDResourceException e) {
			e.printStackTrace();
		} catch (FDSkuNotFoundException e) {
			e.printStackTrace();
		}
		
		ol.setOrderLineNumber(PosexUtil.getPosex(baseLineNumber));
      
		return ol;
	}

	public int getErpOrderLineSize() {
		return 1;
	}

	public FDCartLineI createCopy() {
		FDCartLineModel newLine = new FDCartLineModel(this.getSku(), this
				.getProductRef().lookupProductModel(), this.getConfiguration(), this.getVariantId(), this.getPricingContext().getZoneId());
		newLine.setRecipeSourceId(this.getRecipeSourceId());
		newLine.setRequestNotification(this.isRequestNotification());
		newLine.setSource(this.source);
		return newLine;
	}
	
	@Override
	public void setOrderLineId(String orderLineId){
		this.orderLine.setOrderLineId(orderLineId);
	}

	/**
	 *  Set the source of the event.
	 *  
	 *  @param source the part of the site this event was generated from.
	 */
	public void setSource(EnumEventSource source) {
		this.source = source;
	}
	
	/**
	 *  Get the source of the event.
	 *  
	 *  @return the part of the site this event was generated from.
	 */
	public EnumEventSource getSource() {
		return source;
	}

	public void setSavingsId(String savingsId){
		this.orderLine.setSavingsId(savingsId);
	}
	
	public String getSavingsId(){
		return this.orderLine.getSavingsId();
	}
	
	public void removeLineItemDiscount(){
		this.setDiscountAmount(0.0);
		this.setDiscount(null);
	}

	public boolean hasDiscount(String promoCode) {
		if(this.getDiscount() != null && this.getDiscount().getPromotionCode().equals(promoCode)) {
			return true;	
		}
		return false;
	}

	public String getCartonNumber() {
		// TODO Auto-generated method stub
		return cartonNumber;
	}

	public void setCartonNumber(String no) {
		// TODO Auto-generated method stub
		this.cartonNumber=no;
	}

 
	
}
