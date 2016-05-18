package com.freshdirect.mobileapi.controller.data;

import java.util.ArrayList;
import java.util.List;

public class ProductList extends Message{
int numofproducts;
private List<Product> productlist = new ArrayList<Product>();

    public List<Product> getProducts() {
        return productlist;
    }

    public void setProducts(List<Product> products) {
        this.productlist = products;
    }
    
    public int getNoOfProducts() {
        return numofproducts;
    }

    public void setNoOfProducts(int numofproducts) {
        this.numofproducts = numofproducts;
    }

}
