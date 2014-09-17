package com.freshdirect.fdstore.content.browse.sorter;

import java.util.Date;

import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.fdstore.content.ProductModel;

public class RecencyComparator extends OptionalObjectComparator<FilteringProductItem, Long> {
	
	@Override
	protected Long getValue(FilteringProductItem obj) {
		ProductModel prod = obj.getProductModel();
		Date addedDate = ContentFactory.getInstance().getNewProducts().get(prod);
		
		if (addedDate == null){
			addedDate = ContentFactory.getInstance().getBackInStockProducts().get(prod);
		}
		
		return addedDate == null ? null : -1 * addedDate.getTime(); //default is descending
	}

}
