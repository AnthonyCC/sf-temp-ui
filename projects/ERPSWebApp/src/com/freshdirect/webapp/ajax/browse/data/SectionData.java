package com.freshdirect.webapp.ajax.browse.data;

import java.io.Serializable;
import java.util.List;

import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.ajax.product.data.RecipeData;
import com.freshdirect.webapp.globalnav.data.DepartmentData;

public class SectionData implements Serializable, DescriptiveDataI {

	private static final long serialVersionUID = -7084570028082072513L;
	private String catId;
	private String headerText;
	private String headerImage;
	private String media;
	private String mediaLocation;
	private String middleMedia;
	private String superDepartmentName;
	private List<ProductData> products;
	private List<CategoryData> categories;
	private List<DepartmentData> departments;
	private List<SectionData> sections;
	private List<RecipeData> recipes;

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
	public List<DepartmentData> getDepartments() {
		return departments;
	}
	public void setDepartments(List<DepartmentData> departments) {
		this.departments = departments;
	}
	public String getSuperDepartmentName() {
		return superDepartmentName;
	}
	public void setSuperDepartmentName(String superDepartmentName) {
		this.superDepartmentName = superDepartmentName;
	}
	@Override
	public String getMediaLocation() {
		return mediaLocation;
	}
	@Override
	public void setMediaLocation(String mediaLocation) {
		this.mediaLocation=mediaLocation;
	}
	@Override
	public String getMiddleMedia() {
		return middleMedia;
	}
	@Override
	public void setMiddleMedia(String middleMedia) {
		this.middleMedia=middleMedia;
	}
	public List<RecipeData> getRecipes() {
		return recipes;
	}
	public void setRecipes(List<RecipeData> recipes) {
		this.recipes = recipes;
	}
	
}
