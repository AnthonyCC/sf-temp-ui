package com.freshdirect.fdstore.content.browse.sorter;

import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.PopulatorUtil;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.SkuModel;

public class ExpertRatingComparator extends OptionalObjectComparator<FilteringProductItem, Integer> {
	private static int EXTREME_VALUE = 0;
	
	@Override
	protected Integer getValue(FilteringProductItem obj) {
		int value = EXTREME_VALUE;
		try {
			ProductModel prod = obj.getProductModel();
			SkuModel sku = PopulatorUtil.getDefSku(prod);
			EnumOrderLineRating rating = prod.getProductRatingEnum(sku==null ? null : sku.getSkuCode());
			if (rating != null) {
				value = rating.getValue();
			}
		} catch (FDResourceException e) {
		}
		return -1 * value; //default is descending
	}

}
