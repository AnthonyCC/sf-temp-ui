package com.freshdirect.mobileapi.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.mobileapi.model.Product;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.util.SortType;

public interface ProductService {

    public List<Product> search(String searchTerm, HttpServletRequest request) throws ServiceException;

    public List<Product> search(String searchTerm, Integer start, Integer max, SessionUser user, HttpServletRequest request) throws ServiceException;

    public Product getProduct(String categoryId, String productId) throws ServiceException;

    public List<Product> search(String searchTerm, String upc, Integer page, Integer max, SortType sortType, String brandId,
            String categoryId, String departmentId, SessionUser user, HttpServletRequest request) throws ServiceException;

    public List<String> getAutoSuggestions(String searchTerm, HttpServletRequest request);
}
