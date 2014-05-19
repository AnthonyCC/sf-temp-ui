package com.freshdirect.webapp.ajax.product.data;


public class CartLineData {
	private Integer randomId;
	private ProductData product;
	private String quantity;
	private String price;
	private String description;
	private String configurationDescription;
	private String recipeName;
	
	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public ProductData getProduct() {
		return product;
	}

	public void setProduct(ProductData product) {
		this.product = product;
	}

	public int getRandomId() {
		return randomId;
	}

	public void setRandomId(Integer randomId) {
		this.randomId = randomId;
	}

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getConfigurationDescription() {
		return configurationDescription;
	}

	public void setConfigurationDescription(String configurationDescription) {
		this.configurationDescription = configurationDescription;
	}

}
