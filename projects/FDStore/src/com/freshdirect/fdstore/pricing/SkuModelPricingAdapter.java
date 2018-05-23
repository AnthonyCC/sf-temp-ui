package com.freshdirect.fdstore.pricing;

import java.util.Date;
import java.util.List;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.storeapi.content.BrandModel;
import com.freshdirect.storeapi.content.DomainValue;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.SkuModel;

public class SkuModelPricingAdapter extends SkuModel {

    private static final long serialVersionUID = 5880715133248410377L;

    private PricingContext pricingCtx;
	private SkuModel innerSku;

	public SkuModelPricingAdapter(SkuModel sku, PricingContext pCtx) {
        super(sku.getContentKey(), sku.getParentKey());
		this.innerSku = sku;
		this.pricingCtx = pCtx;
	}

	@Override
    public Object clone() {
		return innerSku.clone();
	}

	@Override
    public String getSkuCode(){
	    return innerSku.getSkuCode();
	}

	@Override
    public List<DomainValue> getVariationMatrix() {
		return innerSku.getVariationMatrix();
	}

	@Override
    public List<DomainValue> getVariationOptions() {
		return innerSku.getVariationOptions();
	}

	@Override
    public boolean isDiscontinued() {
		return innerSku.isDiscontinued();
	}

	@Override
    public boolean isTempUnavailable() {
		return innerSku.isTempUnavailable();
	}

	@Override
    public boolean isOutOfSeason() {
		return innerSku.isOutOfSeason();
	}

	@Override
    public boolean isUnavailable() {
		return innerSku.isUnavailable();
	}


	@Override
    public boolean isAvailableWithin(int days) {
		return innerSku.isAvailableWithin(days);
	}

	@Override
    public Date getEarliestAvailability() {
		return innerSku.getEarliestAvailability();
	}

	/** @return null if sku is available */
	@Override
    public String getEarliestAvailabilityMessage() {
		return innerSku.getEarliestAvailabilityMessage();
	}


	@Override
    public FDProductInfo getProductInfo() throws FDResourceException, FDSkuNotFoundException {
		return innerSku.getProductInfo();
	}

    @Override
    public FDProduct getProduct() throws FDResourceException, FDSkuNotFoundException {
    	return innerSku.getProduct();
	}

    @Override
    public ProductModel getProductModel() {
        return ProductPricingFactory.getInstance().getPricingAdapter(innerSku.getProductModel());
    }

	@Override
    public List<BrandModel> getBrands() {
		return innerSku.getBrands();
	}

	@Override
    public PricingContext getPricingContext() {
		return this.pricingCtx;
	}

	public SkuModel getRealSku() {
		return innerSku;
	}

}
