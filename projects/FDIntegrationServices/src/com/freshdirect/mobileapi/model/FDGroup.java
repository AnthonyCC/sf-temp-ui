package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.mobileapi.model.tagwrapper.GetGSProductsTagWrapper;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;

public class FDGroup {
	private static final Category LOG = Logger.getLogger(FDGroup.class);

    public static List<Product> getGroupScaleProducts(String grpId, String version, SessionUser user, boolean isWebRequest) throws FDException {
        List<Product> products = new ArrayList<Product>();

        GetGSProductsTagWrapper tagWrapper = new GetGSProductsTagWrapper(user);

        ResultBundle resultBundle = tagWrapper.getGSSKUList(grpId, version);
        
        List<SkuModel> skus = (List<SkuModel>)resultBundle.getExtraData(GetGSProductsTagWrapper.SKU_LIST_ID);
        if(skus != null) {
	        for (SkuModel sku : skus) {
	            ProductModel productModel = sku.getProductModel();
	        	 try {
		            Product product = Product.wrap(productModel, user.getFDSessionUser().getUser(), null, EnumCouponContext.PRODUCT);
		            if (isWebRequest){
		                product.setProductData(ProductDetailPopulator.createProductData(user.getFDSessionUser(), productModel));
                    }
		            products.add(product);
	        	 }catch (Exception e) {
	                 //Don't let one rotten egg ruin it for the bunch
	                 LOG.error("ModelException encountered. Product ID=" + productModel.getFullName(), e);
	             }
	        }
        }
        return products;
    }
}
