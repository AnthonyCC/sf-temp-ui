package com.freshdirect.fdstore.pricing;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.storeapi.content.ProductModel;

public class ProductPricingFactory {

    private static final ProductPricingFactory INSTANCE = new ProductPricingFactory();

    private ProductPricingFactory() {
	}
	
	public static ProductPricingFactory getInstance() {
		return INSTANCE;
	}

    public ProductModelPricingAdapter getPricingAdapter(ProductModel product) {
		if (product == null) {
			return null;
			
		} else if(product instanceof ProductModelPricingAdapter) {
			return (ProductModelPricingAdapter)product;
		} else {
			return new ProductModelPricingAdapter(product);
		}
	}

    public List<ProductModel> getPricingAdapter(List<ProductModel> products) {
		if (products == null) {
			return null;
		}
		List<ProductModel> pricingAdapters = new ArrayList<ProductModel>(products.size());
		for (ProductModel product : products) {
            pricingAdapters.add(getPricingAdapter(product));
		}
		return pricingAdapters;
	}


}
