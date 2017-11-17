package com.freshdirect.webapp.taglib.fdstore;

import com.freshdirect.fdstore.FDNotFoundException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.PopulatorUtil;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class ProductGroupTag extends AbstractGetterTag<ProductModel> {
	private static final long serialVersionUID = 1L;


	private String categoryId;
	private String productId;
	private String skuCode;
	
	public void setCategoryId(String catId) {
		this.categoryId = catId;	
	}
	
	public void setProductId(String pid) {
		this.productId = pid;
	}
	
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	@Override
    protected ProductModel getResult() throws FDSkuNotFoundException, FDNotFoundException {
        ProductModel product = ContentFactory.getInstance().getProductByName(categoryId, productId);

        if (product == null && skuCode != null && !"".equalsIgnoreCase(skuCode)) {
            product = PopulatorUtil.getProductByName(skuCode);
        } else {
            PopulatorUtil.isNodeNotFound(product, categoryId, productId);
			}

        return ProductPricingFactory.getInstance().getPricingAdapter(product, null);
		}

	public static class TagEI extends AbstractGetterTag.TagEI {
		@Override
		protected String getResultType() {
			return "com.freshdirect.storeapi.content.ProductModel";
		}
	}
}
