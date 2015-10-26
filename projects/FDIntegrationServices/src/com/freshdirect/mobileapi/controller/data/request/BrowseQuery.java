package com.freshdirect.mobileapi.controller.data.request;

import com.freshdirect.mobileapi.catalog.model.CatalogKey;
import com.freshdirect.mobileapi.controller.data.Message;

public class BrowseQuery extends Message {
	public static final Integer DEFAULT_PAGE = 1;
	public static final Integer DEFAULT_MAX = 25;

	private String id;
	
	private String nutritionName;

	private Integer max = DEFAULT_MAX;

	private Integer page = DEFAULT_PAGE;

	private String sortBy;

	private String category;

	private String department;
	
	private String groupId;
	
	private String groupVersion;
	
	private String zipCode;
	
	private String serviceType;
	
	private String address1;
	
	private String apartment;
	
	private String city;
	
	private String state;
	
	private String productCount;
	
	private String key;
	
	private Integer loadCategoriesCarousel;
	
	private Integer carouselProductCount;
	
	private String filterById;

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getApartment() {
		return apartment;
	}

	public void setApartment(String apartment) {
		this.apartment = apartment;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getNutritionName() {
		return nutritionName;
	}

	public void setNutritionName(String nutritionName) {
		this.nutritionName = nutritionName;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	
	public String getFilterById(){
		return filterById;
	}
	
	public void setFilterById(String filterById) {
		this.filterById = filterById;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupVersion() {
		return groupVersion;
	}

	public void setGroupVersion(String groupVersion) {
		this.groupVersion = groupVersion;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getProductCount() {
		return productCount;
	}

	public void setProductCount(String productCount) {
		this.productCount = productCount;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	private CatalogKey _catalogKey;
	
	public CatalogKey getCatalogKey()
	{
		if(key == null || key.isEmpty())
			return null;
		
		if(_catalogKey == null)
			_catalogKey = CatalogKey.parse(key);
		return _catalogKey;
	}
	
		public Integer getLoadCategoriesCarousel() {
		return loadCategoriesCarousel;
	}

	public void setLoadCategoriesCarousel(Integer loadCategoriesCarousel) {
		this.loadCategoriesCarousel = loadCategoriesCarousel;
	}

	public Integer getCarouselProductCount() {
		return carouselProductCount;
	}

	public void setCarouselProductCount(Integer carouselProductCount) {
		this.carouselProductCount = carouselProductCount;
	}


}
