package com.freshdirect.webapp.ajax.browse.data;

import java.io.Serializable;
import java.util.List;

import com.freshdirect.webapp.ajax.product.data.ProductData;

public class SectionData implements Serializable, DescriptiveDataI {

	private static final long serialVersionUID = -7084570028082072513L;
	private String catId;
	private String headerText;
	private String headerImage;
	private String media;
	private List<ProductData> products;
	private List<CategoryData> categories;
	private List<SectionData> sections;

	public String getHeaderText() {
		return headerText;
	}
	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}
	public String getHeaderImage() {
		return headerImage;
	}
	public void setHeaderImage(String headerImage) {
		this.headerImage = headerImage;
	}
	public String getMedia() {
		return media;
	}
	public void setMedia(String media) {
		this.media = media;
	}
	public List<ProductData> getProducts() {
		return products;
	}
	public void setProducts(List<ProductData> products) {
		this.products = products;
	}
	public List<CategoryData> getCategories() {
		return categories;
	}
	public void setCategories(List<CategoryData> categories) {
		this.categories = categories;
	}
	public List<SectionData> getSections() {
		return sections;
	}
	public void setSections(List<SectionData> sections) {
		this.sections = sections;
	}
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	
}
