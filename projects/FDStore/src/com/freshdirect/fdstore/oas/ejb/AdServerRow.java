package com.freshdirect.fdstore.oas.ejb;

import java.io.Serializable;

/**@author ekracoff on Jul 22, 2004*/
public class AdServerRow implements Serializable{
	private final String productId;
	private final boolean available;
	private final String price;

	public AdServerRow(String productId, boolean isAvailable, String price) {
		this.productId = productId;
		this.available = isAvailable;
		this.price = price;
	}

	public boolean isAvailable() {
		return available;
	}

	public String getPrice() {
		return price;
	}

	public String getProductId() {
		return productId;
	}

	public boolean equals(Object o) {
		if (!(o instanceof AdServerRow))
			return false;
		if (this.productId.equals(((AdServerRow) o).getProductId()))
			return true;

		return false;

	}
	
}