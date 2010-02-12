package com.freshdirect.fdstore.oas.ejb;

import java.io.Serializable;

/**@author ekracoff on Jul 22, 2004*/
public class AdServerRow implements Serializable{
	private final String productId;
	private final boolean available;
	private final String price;
	private final String zoneId;
	private final String zoneType;

	public AdServerRow(String productId, boolean isAvailable, String price) {
		this.productId = productId;
		this.available = isAvailable;
		this.price = price;
		this.zoneId= null;
		this.zoneType = null;
	}

	public AdServerRow(String productId, boolean available, String price,
			String zoneId, String zoneType) {
		super();
		this.productId = productId;
		this.available = available;
		this.price = price;
		this.zoneId = zoneId;
		this.zoneType = zoneType;
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

	/**
	 * @return the zoneId
	 */
	public String getZoneId() {
		return zoneId;
	}

	/**
	 * @return the zoneType
	 */
	public String getZoneType() {
		return zoneType;
	}
	
}