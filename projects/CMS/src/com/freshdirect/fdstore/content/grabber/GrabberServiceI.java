package com.freshdirect.fdstore.content.grabber;

import java.util.Collection;

import com.freshdirect.fdstore.content.ProductGrabberModel;
import com.freshdirect.storeapi.content.ProductModel;

public interface GrabberServiceI {
	Collection<ProductModel> getProducts(ProductGrabberModel model);
}
