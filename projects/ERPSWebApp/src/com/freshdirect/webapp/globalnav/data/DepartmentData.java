package com.freshdirect.webapp.globalnav.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.freshdirect.webapp.ajax.browse.data.BasicData;
import com.freshdirect.webapp.ajax.browse.data.CategoryData;

public class DepartmentData extends BasicData {
	
	private static final long serialVersionUID = -3685296667484830177L;
	
	private List<List<Map<String, Object>>> dropDownCategories = new ArrayList<List<Map<String, Object>>>();
	private List<CategoryData> categories = new ArrayList<CategoryData>();
	private List<CategoryData> preferenceCategories = new ArrayList<CategoryData>();
	private List<CategoryData> popularCategories = new ArrayList<CategoryData>();
    private String heroImage;
    private String seasonalMedia;
    private String altText;
    private String deptPhoto;
	private List<List<CategoryData>> popularCategoryColumns = new ArrayList<List<CategoryData>>();
	private String preferenceCategoriesNavHeader;
	private boolean headerTextUsed;
	private boolean hideDropDown;
	
	public List<CategoryData> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryData> categories) {
		this.categories = categories;
	}

	public void addCategoryData(CategoryData categoryData) {
		this.categories.add(categoryData);
	}

	public List<CategoryData> getPopularCategories() {
		return popularCategories;
	}

	public void setPopularCategories(List<CategoryData> popularCategories) {
		this.popularCategories = popularCategories;
	}

	public void addPopularCategoryData(CategoryData categoryData) {
		this.popularCategories.add(categoryData);
	}

	public List<CategoryData> getPreferenceCategories() {
		return preferenceCategories;
	}

	public void setPreferenceCategories(List<CategoryData> preferenceCategories) {
		this.preferenceCategories = preferenceCategories;
	}

	public void addPreferenceCategoryData(CategoryData categoryData) {
		this.preferenceCategories.add(categoryData);
	}

	public void setDropDownCategories(ArrayList<List<Map<String, Object>>> dropDownCategories) {
		this.dropDownCategories = dropDownCategories;
	}

	public void addDropDownCategory(List<Map<String, Object>> dropDownCategories) {
		this.dropDownCategories.add(dropDownCategories);
	}

	public List<List<Map<String, Object>>> getDropDownCategories() {
		return dropDownCategories;
	}

	public void setDropDownCategories(List<List<Map<String, Object>>> dropDownCategories) {
		this.dropDownCategories = dropDownCategories;
	}

	public String getAltText() {
		return altText;
	}

	public void setAltText(String altText) {
		this.altText = altText;
	}

	public String getSeasonalMedia() {
		return seasonalMedia;
	}

	public void setSeasonalMedia(String seasonalMedia) {
		this.seasonalMedia = seasonalMedia;
	}

	public String getHeroImage() {
		return heroImage;
	}

	public void setHeroImage(String heroImage) {
		this.heroImage = heroImage;
	}
	public List<List<CategoryData>> getPopularCategoryColumns() {
		return popularCategoryColumns;
	}
	public void setPopularCategoryColumns(List<List<CategoryData>> popularCategoryColumns) {
		this.popularCategoryColumns = popularCategoryColumns;
	}
	public void addPopularCategoryColumn(List<CategoryData> popularCategoryColumn) {
		this.popularCategoryColumns.add(popularCategoryColumn);
	}

	public String getDeptPhoto() {
		return deptPhoto;
	}

	public void setDeptPhoto(String deptPhoto) {
		this.deptPhoto = deptPhoto;
	}

	public String getPreferenceCategoriesNavHeader() {
		return preferenceCategoriesNavHeader;
	}

	public void setPreferenceCategoriesNavHeader(String preferenceCategoriesNavHeader) {
		this.preferenceCategoriesNavHeader = preferenceCategoriesNavHeader;
	}

	public boolean isHeaderTextUsed() {
		return headerTextUsed;
	}

	public void setHeaderTextUsed(boolean headerTextUsed) {
		this.headerTextUsed = headerTextUsed;
	}

	public boolean isHideDropDown() {
		return hideDropDown;
	}

	public void setHideDropDown(boolean hideDropDown) {
		this.hideDropDown = hideDropDown;
	}
}
