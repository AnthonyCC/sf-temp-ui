package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.fdstore.content.ProductModel;


public class PairItProductModel{
	
	private List<String> pairItProductIds = new ArrayList<String>();

	public void setCompleteMeal(ProductModel productModel) {
		if (productModel != null) {
			for (ProductModel prod : productModel.getCompleteTheMeal()) {
				if (prod.getContentKey() != null)
					this.pairItProductIds.add(prod.getContentKey().getId());
			}
		}
	}

	public List<String> getPairItProductIds() {
		return pairItProductIds;
	}

	public void setPairItProductIds(List<String> pairItProductIds) {
		this.pairItProductIds = pairItProductIds;
	}
	
}
