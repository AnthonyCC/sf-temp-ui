package com.freshdirect.webapp.util;


import java.util.Iterator;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.util.DealsHelper;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;

/**
 * A wrapper for the product.
 * 
 * Subclasses can provide additional information.
 * 
 * @author istvan
 *
 */
public class ProductImpression {
	
	protected final ProductModel productModel;
	protected PriceCalculator calculator;
	
	/**
	 * Constructor.
	 * @param productModel product
	 */
	public ProductImpression(ProductModel productModel) {
		this.productModel = productModel;
		this.calculator = productModel.getPriceCalculator();
	}
	
	protected ProductImpression(ProductModel productModel, PriceCalculator calculator) {
            this.productModel = productModel;
            this.calculator = calculator;
        }
	
	protected void setCalculator(PriceCalculator calculator) {
            this.calculator = calculator;
        }
	
	public PriceCalculator getCalculator() {
            return calculator;
        }
	
    
	
	/**
	 * Get product model.
	 * @return the product set in the constructor
	 */
	public ProductModel getProductModel() {
		return productModel;
	}
	
	/**
	 * Whether this product impression is transactional.
	 * @return false
	 */
	public boolean isTransactional() {
		return false;
	}
	
	/**
	 * Get the FD product corresponding to this impression.
	 * 
	 * This is a "convenience" method.
	 * 
	 * @return the {@link FDProduct} corresponding to the default sku
	 */
        public FDProduct getFDProduct() {
            try {
                return this.calculator.getProduct();
            } catch (FDResourceException e) {
                throw new FDRuntimeException(e);
            } catch (FDSkuNotFoundException e) {
                throw new FDRuntimeException(e);
            }
        }
	
	/**
	 * Get the default sku.
	 * 
	 * @return the product model's default sku
	 * @see ProductModel#getDefaultSku()
	 */
	public SkuModel getSku() {
	    return calculator.getSkuModel();
	}
	
	
	/**
	 * Returns configuration description
	 * @return
	 */
        public String getConfigurationDescription() {
            try {
                return calculator.getSizeDescription();
            } catch (FDResourceException e) {
                throw new FDRuntimeException(e);
            }
        }	

	/**
	 * Returns scaled price labels
	 *
	 * @return
	 */
	public String[] getScaledPrices() {
            FDProduct fdProduct = getFDProduct();
            return fdProduct != null ? fdProduct.getPricing().getZonePrice(getPricingZoneId()).getScaleDisplay() : new String[0];
	}


	/**
	 * Returns scaled percentages
	 *
	 * @return
	 */
        public int[] getScaledPercentages(double basePrice) {
            FDProduct fdProduct = getFDProduct();
            if (fdProduct != null) {
                int[] scalePercentage = fdProduct.getPricing().getZonePrice(getPricingZoneId()).getScalePercentage(basePrice);
                for (int i = 0; i < scalePercentage.length; i++)
                    if (DealsHelper.isDealOutOfBounds(scalePercentage[i]))
                        scalePercentage[i] = 0;
                return scalePercentage;
            } else {
                return new int[0];
            }
        }	

	/**
	 * Returns productInfo instance
	 * @return
	 */
	public FDProductInfo getProductInfo() {
            try {
                return this.calculator.getProductInfo();
            } catch (FDSkuNotFoundException ex) {
                // sku not found ...
            } catch (FDResourceException e) {
                // resource not found ...
            }
            return null;
	}



	/**
	 * Perform a full check on impression against ability to display
	 *
	 * @return
	 */
	@Deprecated
	public boolean validate() {
		boolean isValid = true;
		
		try {
			SkuModel sku = getSku();
			FDProduct p = getFDProduct();

			isValid = !(
				sku == null ||
				sku.getSkuCode() == null ||
				productModel.getFullName() == null ||
				productModel.getPrimaryBrandName() == null ||
				productModel.getSizeDescription() == null ||
				productModel.getDefaultPrice() == null ||
				p == null ||
				p.getPricing() == null
			);

		} catch (Exception exc) {
			// invalidate product
			isValid = false;
		}
		
		
		return isValid;
	}
	
	public String getPricingZoneId(){
		PricingContext pCtx = this.calculator.getPricingContext();
		return pCtx.getZoneId();
	}
	
	public boolean isGroupExists(String skuCode) {
		boolean groupExists = false;
		Iterator<SkuModel> it = this.getProductModel().getSkus().iterator();
		while(it.hasNext()){
			SkuModel sku = it.next();
			try{
				if(sku != null && !sku.isUnavailable() && sku.getProductInfo().isGroupExists() && sku.getSkuCode().equals(skuCode)) {
					//if atleast one sku participates in a group.
					groupExists = true;
					break;
				}
					
			}catch(FDSkuNotFoundException se){
				//ignore
			}catch(FDResourceException se){
				//ignore
			}
		}
		return groupExists;
	}
	/**
	 * Returns the FDGroup that is associated to any sku in that Product.
	 * @return
	 */
	public FDGroup getFDGroup() {
		FDGroup group = null;
		Iterator<SkuModel> it = this.getProductModel().getSkus().iterator();
		while(it.hasNext()){
			SkuModel sku = it.next();
			try{
				if(sku != null && !sku.isUnavailable()) {
					//if atleast one sku participates in a group.
					group = sku.getProductInfo().getGroup() ;
					if(group != null)
						break;
				}
					
			}catch(FDSkuNotFoundException se){
				//ignore
			}catch(FDResourceException se){
				//ignore
			}
		}
		return group;
	}
}
