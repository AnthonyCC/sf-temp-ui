package com.freshdirect.mobileapi.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpHeaders;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.CategoryNodeTree;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentNodeTree.TreeElement;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.model.Brand;
import com.freshdirect.mobileapi.model.Category;
import com.freshdirect.mobileapi.model.Department;
import com.freshdirect.mobileapi.model.Product;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.tagwrapper.HttpContextWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.HttpRequestWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.SmartSearchTagWrapper;
import com.freshdirect.mobileapi.util.SortType;
import com.freshdirect.webapp.search.unbxd.UnbxdServiceUnavailableException;
import com.freshdirect.webapp.taglib.fdstore.SmartSearchTag;
import com.freshdirect.webapp.util.AutoCompleteFacade;
import com.freshdirect.webapp.util.RequestUtil;

public class ProductServiceImpl implements ProductService {

    private static final org.apache.log4j.Category LOG = LoggerFactory.getInstance(ProductServiceImpl.class);

    private Set<Brand> brands;

    private Set<Department> departments;

    private Set<Category> categories;

    private int recentSearchTotalCount;

    private String spellingSuggestion;

    /*
     * (non-Javadoc)
     * @see com.freshdirect.mobileapi.service.ProductService#getProduct(java.lang.String, java.lang.String)
     */
    @Override
    @Deprecated
    public Product getProduct(String categoryId, String productId) throws ServiceException {

        ProductModel product = ContentFactory.getInstance().getProductByName(categoryId, productId);
        Product result;
        try {
            result = Product.wrap(product);
        } catch (ModelException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return result;
    }

    @Override
    public List<Product> search(String searchTerm, Integer page, Integer max, SessionUser user, HttpServletRequest request, HttpContextWrapper wrapper) throws ServiceException {
        return search(searchTerm, null, page, max, null, null, null, null, user, request, wrapper);
    }

    @Override
    public List<Product> search(String searchTerm, String upc, Integer page, Integer max, SortType sortType, String brandId,
            String categoryId, String deparmentId, SessionUser user, HttpServletRequest request, HttpContextWrapper basewrapper) throws ServiceException {
        List<Product> result = new ArrayList<Product>();

        List<ProductModel> productModels = null;
        SmartSearchTag search = null;

        ResultBundle resultBundle;
        try {
            //Rsung - Default sort type if missing
            if (sortType == null) {
                sortType = SortType.RELEVANCY;
            }
            SmartSearchTagWrapper wrapper = (SmartSearchTagWrapper) basewrapper;
            resultBundle = wrapper.getSearchResult(searchTerm, upc, deparmentId, categoryId, brandId, (page - 1), max, "", sortType.getSortValue());
            search = (SmartSearchTag) resultBundle.getExtraData(SmartSearchTagWrapper.ID);

            //Set two more mobile specific filters
            LOG.debug("Total Products before iphone filter: " + search.getNoOfProductsBeforeProductFilters());
            LOG.debug("Total Products after iphone filter: " + search.getNoOfProducts());

            productModels = search.getProducts(); // changed from search.getPageProducts(); due to APPDEV-2797
            recentSearchTotalCount = search.getNoOfBrandFilteredProducts();
            Collection<String>spellingSuggestions = search.getSpellingSuggestions();
            if (!spellingSuggestions.isEmpty())
            	spellingSuggestion = spellingSuggestions.iterator().next();
            LOG.debug("SPELLING SUGGESTION - Did you mean? " + spellingSuggestion);
        } catch (FDException e) {
            throw new ServiceException(e);
        }

        LOG.debug("Total Products retrieved: " + search.getNoOfProductsBeforeProductFilters());
        LOG.debug("Total Products filtered retrieved: " + search.getNoOfProducts());

        CategoryNodeTree tree = search.getFilteredCategoryTree();
        departments = new HashSet<Department>();
        categories = new HashSet<Category>();

        Iterator<TreeElement> categoryIterator = tree.getRoots().iterator();
        while (categoryIterator.hasNext()) {
            TreeElement treeElement = categoryIterator.next();
            ContentNodeModel model = treeElement.getModel();

            if (model instanceof DepartmentModel) {
                Department department = Department.wrap((DepartmentModel) model);
                departments.add(department);
            }
            Collection<TreeElement> childElement = treeElement.getChildren();
            Iterator<TreeElement> childIterator = childElement.iterator();
            while (childIterator.hasNext()) {
                TreeElement grandSonElement = childIterator.next();

                if (grandSonElement.getModel() instanceof CategoryModel) {
                    Category category = Category.wrap((CategoryModel) grandSonElement.getModel());
                    categories.add(category);
                }
            }
        }

        brands = new HashSet<Brand>();
        brands = new HashSet<Brand>();
        for (BrandModel brand : search.getBrands()) {
        	brands.add(Brand.wrap(brand));
		}
        
        for (ProductModel product : productModels)
            try {
                result.add(Product.wrap(product, user.getFDSessionUser().getUser(), null, EnumCouponContext.PRODUCT));
            } catch (ModelException e) {
                LOG.warn("ModelException encountered while preparing search result.", e);
            }

        return result;
    }
    
    public List<String> searchProductIds(String searchTerm, String upc, Integer page, Integer max, SortType sortType, String brandId,
            String categoryId, String deparmentId, SessionUser user, HttpServletRequest request, HttpContextWrapper basewrapper) throws ServiceException{
    	
    	List<String> result = new ArrayList<String>();

        List<ProductModel> productModels = null;
        SmartSearchTag search = null;

        ResultBundle resultBundle;
        try {
            //Rsung - Default sort type if missing
            if (sortType == null) {
                sortType = SortType.RELEVANCY;
            }
            SmartSearchTagWrapper wrapper = (SmartSearchTagWrapper) basewrapper;
            resultBundle = wrapper.getSearchResult(searchTerm, upc, deparmentId, categoryId, brandId, (page - 1), max, "", sortType.getSortValue());
            search = (SmartSearchTag) resultBundle.getExtraData(SmartSearchTagWrapper.ID);

            //Set two more mobile specific filters
            LOG.debug("Total Products before iphone filter: " + search.getNoOfProductsBeforeProductFilters());
            LOG.debug("Total Products after iphone filter: " + search.getNoOfProducts());

            productModels = search.getProducts(); // changed from search.getPageProducts(); due to APPDEV-2797
            recentSearchTotalCount = search.getNoOfBrandFilteredProducts();
            Collection<String>spellingSuggestions = search.getSpellingSuggestions();
            if (!spellingSuggestions.isEmpty())
            	spellingSuggestion = spellingSuggestions.iterator().next();
            LOG.debug("SPELLING SUGGESTION - Did you mean? " + spellingSuggestion);
        } catch (FDException e) {
            throw new ServiceException(e);
        }

        LOG.debug("Total Products retrieved: " + search.getNoOfProductsBeforeProductFilters());
        LOG.debug("Total Products filtered retrieved: " + search.getNoOfProducts());

        CategoryNodeTree tree = search.getFilteredCategoryTree();
        departments = new HashSet<Department>();
        categories = new HashSet<Category>();

        Iterator<TreeElement> categoryIterator = tree.getRoots().iterator();
        while (categoryIterator.hasNext()) {
            TreeElement treeElement = categoryIterator.next();
            ContentNodeModel model = treeElement.getModel();

            if (model instanceof DepartmentModel) {
                Department department = Department.wrap((DepartmentModel) model);
                departments.add(department);
            }
            Collection<TreeElement> childElement = treeElement.getChildren();
            Iterator<TreeElement> childIterator = childElement.iterator();
            while (childIterator.hasNext()) {
                TreeElement grandSonElement = childIterator.next();

                if (grandSonElement.getModel() instanceof CategoryModel) {
                    Category category = Category.wrap((CategoryModel) grandSonElement.getModel());
                    categories.add(category);
                }
            }
        }

        brands = new HashSet<Brand>();
        brands = new HashSet<Brand>();
        for (BrandModel brand : search.getBrands()) {
        	brands.add(Brand.wrap(brand));
		}
        
        for (ProductModel product : productModels)
            	result.add(product.getContentName());

        return result;
    }

    @Override
    public List<Product> search(String searchTerm, HttpServletRequest request, HttpContextWrapper wrapper) throws ServiceException {
        return search(searchTerm, 1, 25, null, request, wrapper);
    }

    public Set<Brand> getBrands() {
        return brands;
    }

    public void setBrands(Set<Brand> brands) {
        this.brands = brands;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public int getRecentSearchTotalCount() {
        return recentSearchTotalCount;
    }

    public String getSpellingSuggestion() {
        return spellingSuggestion;
    }

    public void setSpellingSuggestion(String spellingSuggestion) {
        this.spellingSuggestion = spellingSuggestion;
    }

	@Override
    public List<String> getAutoSuggestions(String searchTerm, HttpServletRequest request) {
        AutoCompleteFacade facade = new AutoCompleteFacade();
        List<String> autosuggestions = Collections.emptyList();
        try {
            autosuggestions = facade.getTerms(searchTerm, request);
        } catch(UnbxdServiceUnavailableException exception){
            LOG.error(exception);
        }
        return autosuggestions;
    }

}