package com.freshdirect.storeapi.content;

import java.util.Collection;
import java.util.List;

import com.freshdirect.fdstore.FDResourceException;

public interface ProductItemFilterI {
	
	List<FilteringProductItem> apply(Collection<FilteringProductItem> nodes) throws FDResourceException;
	
	boolean apply(FilteringProductItem prod) throws FDResourceException;
	
	String getId();
	
	String getParentId();
	
	String getName();
	
	boolean isInvert();
	
	FilterCacheStrategy getCacheStrategy();
	
}
