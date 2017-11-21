package com.freshdirect.storeapi.content.grabber;

import java.util.Collection;

import com.freshdirect.storeapi.content.ProductGrabberModel;
import com.freshdirect.storeapi.content.ProductModel;

public interface GrabberServiceI {
	Collection<ProductModel> getProducts(ProductGrabberModel model);
}
