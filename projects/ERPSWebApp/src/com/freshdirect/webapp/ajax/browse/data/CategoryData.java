package com.freshdirect.webapp.ajax.browse.data;


public class CategoryData extends BasicData {

	private static final long serialVersionUID = 6420471260944576323L;
	private String image;
	private boolean available = true;
	
	public CategoryData(String image, String id, String name) {
		super(id, name);
		this.image = image;
	}
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
}
