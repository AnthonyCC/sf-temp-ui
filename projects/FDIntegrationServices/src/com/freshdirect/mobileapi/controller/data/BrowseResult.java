package com.freshdirect.mobileapi.controller.data;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.response.FilterOption;
import com.freshdirect.mobileapi.model.Category;
import com.freshdirect.mobileapi.model.Department;

public class BrowseResult extends Message {
    
    private Integer totalResultCount = 0;

    private List<ProductSearchResult> products = new ArrayList<ProductSearchResult>();
    
    private List<Department> departments = new ArrayList<Department>();

    private List<Category> categories = new ArrayList<Category>();

   
    public Integer getResultCount() {
        return products.size();
    }

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


}
