package com.freshdirect.fdstore.content.browse.sorter;

import java.util.Comparator;

import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.fdstore.content.ProductModel;


public class AvailabilityComparator implements Comparator<FilteringProductItem> {
	
	@Override
	public int compare(FilteringProductItem item1, FilteringProductItem item2) {
        ProductModel productModel1 = item1.getProductModel();
        ProductModel productModel2 = item2.getProductModel();

        boolean availabilty1 = productModel1==null ? false : !productModel1.isUnavailable();
        boolean availabilty2 = productModel2==null ? false : !productModel2.isUnavailable();
	    
		if (availabilty1 && !availabilty2){
			return -1;
		} else if (!availabilty1 && availabilty2){
			return 1;
		} else {
			return 0;
		}
	}
}
