package com.freshdirect.storeapi.content;

import java.util.Collection;
import java.util.List;

import com.freshdirect.fdstore.FDResourceException;

public interface ProductFilterI {
	List<ProductModel> apply(Collection<ProductModel> nodes) throws FDResourceException;
	boolean apply(ProductModel prod) throws FDResourceException;
}
