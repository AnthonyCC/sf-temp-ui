package com.freshdirect.mobileapi.service;

import java.util.List;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.mobileapi.model.Product;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.util.Filter;
import com.freshdirect.mobileapi.util.ProductModelSortUtil;

public interface ProductService {

    public List<Product> search(String searchTerm) throws ServiceException;

    public List<Product> search(String searchTerm, Integer start, Integer max, SessionUser user) throws ServiceException;

    public Product getProduct(String categoryId, String productId) throws ServiceException;

    public List<Product> search(String searchTerm, Integer page, Integer max, ProductModelSortUtil.SortType sortType, String brandId,
            String categoryId, SessionUser user) throws ServiceException;

    public List<String> getAutoSuggestions(String searchTerm);
}
