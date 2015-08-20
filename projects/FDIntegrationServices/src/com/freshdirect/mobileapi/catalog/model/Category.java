package com.freshdirect.mobileapi.catalog.model;

import java.util.ArrayList;
import java.util.List;


public class Category {
	
	private String name;

    private String id;
    
    public Category (String id, String name) {
    	this.id=id;
    	this.name=name;
    }
    
    private List<String> categories = new ArrayList<String>();
    
    private String  departmentId;
    
    private List<String> products= new ArrayList<String>();
    
    private Category parent;

	public String getName() {
		return name;
	}

	

	public String getId() {
		return id;
	}

	

	public List<String> getCategories() {
		return categories;
	}

	public void addCategory(String categoryId) {
		
		this.categories.add(categoryId);
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public List<String> getProducts() {
		return products;
	}

	public void setProducts(List<String> products) {
		this.products = products;
	}

    public void addProduct(String productId) {
		
		this.products.add(productId);
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}
    
    

}
