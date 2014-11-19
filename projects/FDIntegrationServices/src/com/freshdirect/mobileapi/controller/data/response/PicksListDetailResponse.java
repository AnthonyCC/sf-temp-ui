package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;

import com.freshdirect.mobileapi.controller.data.Image;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.Product;

public class PicksListDetailResponse extends Message {
	String title;
	String description;
	Image image;
	List<Product> products;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}	
}