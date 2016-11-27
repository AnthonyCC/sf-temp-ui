package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.fdstore.content.ProductModel;


public class PairItProductModel{
	
	private List<String> pairItProductIds = new ArrayList<String>();
	private String heading = null;
	private String text = null;

	public void setCompleteMeal(ProductModel productModel) {
		if (productModel != null) {
			for (ProductModel prod : productModel.getCompleteTheMeal()) {
				if (prod.getContentKey() != null)
					this.pairItProductIds.add(prod.getContentKey().getId());
			}
			this.heading = productModel.getPairItHeading();
			this.text = productModel.getPairItText();
		}
	}

	public List<String> getPairItProductIds() {
		return pairItProductIds;
	}

	public void setPairItProductIds(List<String> pairItProductIds) {
		this.pairItProductIds = pairItProductIds;
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	
	
	
}
