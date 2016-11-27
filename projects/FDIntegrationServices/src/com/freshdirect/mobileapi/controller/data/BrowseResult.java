package com.freshdirect.mobileapi.controller.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import com.freshdirect.mobileapi.controller.data.response.Idea;
import com.freshdirect.mobileapi.model.Category;
import com.freshdirect.mobileapi.model.Department;

public class BrowseResult extends Message {
    
    private Integer totalResultCount = 0;
    
    private Integer resultCount = 0;
    
    private List<ProductSearchResult> products = new ArrayList<ProductSearchResult>();
    
    private List<Department> departments = new ArrayList<Department>();

    private List<Category> categories = new ArrayList<Category>();
    
    private Map<String, SortedSet<String>> filters = new LinkedHashMap<String, SortedSet<String>>();

    private boolean isBottomLevel = false;
    
    //DOOR3 FD-iPad FDIP-1046
    private Idea featuredCard = null;

    public List<ProductSearchResult> getProducts() {
        return products;
    }

    public void setProducts(List<ProductSearchResult> products) {
        this.products = products;
    }

    public void setProductsFromModel(List<com.freshdirect.mobileapi.model.Product> products) {
        this.products.clear();
        for (com.freshdirect.mobileapi.model.Product product : products) {
            this.products.add(ProductSearchResult.wrap(product));
        }
    }
   
    public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public Integer getTotalResultCount() {
        return totalResultCount;
    }

    public void setTotalResultCount(Integer totalResultCount) {
        this.totalResultCount = totalResultCount;
    }

	public Integer getResultCount() {
		return resultCount;
	}

	public void setResultCount(Integer resultCount) {
		this.resultCount = resultCount;
	}

	public Map<String, SortedSet<String>> getFilters() {
		return filters;
	}

	public void setFilters(Map<String, SortedSet<String>> filters) {
		this.filters = filters;
	}

	public boolean isBottomLevel() {
		return isBottomLevel;
	}

	public void setBottomLevel(boolean isBottomLevel) {
		this.isBottomLevel = isBottomLevel;
	}

	//DOOR3 FD-iPad FDIP-1046
	public Idea getFeaturedCard() {
		return featuredCard;
	}

	//DOOR3 FD-iPad FDIP-1046
	public void setFeaturedCard(Idea featuredCard) {
		this.featuredCard = featuredCard;
	}
}
