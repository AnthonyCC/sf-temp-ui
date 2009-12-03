package com.freshdirect.mobileapi.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.CategoryNodeTree;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.FilteredSearchResults;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ContentNodeTree.TreeElement;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.model.Brand;
import com.freshdirect.mobileapi.model.Category;
import com.freshdirect.mobileapi.model.Department;
import com.freshdirect.mobileapi.model.Product;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.filter.AvailableFilter;
import com.freshdirect.mobileapi.model.filter.IphoneFilter;
import com.freshdirect.mobileapi.model.tagwrapper.SmartSearchTagWrapper;
import com.freshdirect.mobileapi.util.GeneralCacheAdministratorFactory;
import com.freshdirect.mobileapi.util.ProductModelSortUtil;
import com.freshdirect.mobileapi.util.ProductModelSortUtil.SortType;
import com.freshdirect.webapp.util.AutoCompleteFacade;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

public class ProductServiceImpl implements ProductService {

    private static final org.apache.log4j.Category LOG = LoggerFactory.getInstance(ProductServiceImpl.class);

    private static GeneralCacheAdministrator cacheAdmin = GeneralCacheAdministratorFactory.getCacheAdminInstance();

    private int REFRESH_PERIOD = 120;

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
        String cacheKey = ProductServiceImpl.class.toString() + productId + categoryId;

        Product result = null;
        try {
            result = (Product) cacheAdmin.getFromCache(cacheKey, REFRESH_PERIOD);
        } catch (NeedsRefreshException nre) {
            try {
                LOG.info("Refreshing/Getting product from CMS " + productId + " with key" + cacheKey);
                ProductModel product = ContentFactory.getInstance().getProductByName(categoryId, productId);
                result = Product.wrap(product);
                cacheAdmin.putInCache(cacheKey, result);
            } catch (Throwable ex) {
                LOG.error("Throwable caught at cache update", ex);
                result = (Product) nre.getCacheContent();
                LOG.debug("Cancelling cache update. Exception encountered.");
                cacheAdmin.cancelUpdate(cacheKey);
                if (null == result) {
                    throw new ServiceException(ex.getMessage(), ex);
                }
            }
        }

        return result;
    }

    @Override
    public List<Product> search(String searchTerm, Integer page, Integer max, SessionUser user) throws ServiceException {
        return search(searchTerm, page, max, null, null, null, user);
    }

    private static final AvailableFilter availableFilter = new AvailableFilter();

    private static final IphoneFilter iphoneFilter = new IphoneFilter();

    public List<Product> search(String searchTerm, Integer page, Integer max, ProductModelSortUtil.SortType sortType, String brandId,
            String categoryId, SessionUser user) throws ServiceException {
        String cacheKey = ProductServiceImpl.class.toString() + searchTerm;
        FilteredSearchResults fres = null;
        List<Product> result = new ArrayList<Product>();
        List<ProductModel> productModels = null;

        SmartSearchTagWrapper wrapper = new SmartSearchTagWrapper(user);
        ResultBundle resultBundle;
        try {
            //Rsung - Default sort type if missing
            if (sortType == null) {
                sortType = SortType.RELEVANCY;
            }
            resultBundle = wrapper.getSearchResult(searchTerm, null, categoryId, brandId, 0, max, "", sortType.getSortValue());
            fres = (FilteredSearchResults) resultBundle.getExtraData(SmartSearchTagWrapper.SEARCH_RESULTS);

            //Set two more mobile specific filters
            LOG.debug("Total Products before iphone filter: " + fres.getProductsSize());
            fres.setFilteredProducts(availableFilter.apply(fres.getFilteredProducts()));
            fres.setFilteredProducts(iphoneFilter.apply(fres.getFilteredProducts()));
            LOG.debug("Total Products after iphone filter: " + fres.getProductsSize());

            int start = (page - 1) * max;

            fres.setStart(start);
            fres.setPageSize(max);

            productModels = fres.getFilteredProductsListPage();
            recentSearchTotalCount = fres.getProductsSize();
            if (fres.isSuggestionMoreRelevant()) {
                spellingSuggestion = fres.getSpellingSuggestion();
                LOG.debug("SPELLING SUGGESTION - Did you mean? " + spellingSuggestion);
            } else {
                spellingSuggestion = fres.getSpellingSuggestion();
                LOG.debug("SPELLING SUGGESTION - Did you mean? " + spellingSuggestion);
            }
        } catch (FDException e) {
            throw new ServiceException(e);
        }

        LOG.debug("Total Products retrieved: " + recentSearchTotalCount);
        LOG.debug("Total Products filtered retrieved: " + fres.getFilteredProducts().size());

        CategoryNodeTree tree = CategoryNodeTree.createTree(fres.getFilteredProducts(), false);
        departments = new HashSet<Department>();
        categories = new HashSet<Category>();

        Iterator categoryIterator = tree.getRoots().iterator();
        while (categoryIterator.hasNext()) {
            TreeElement treeElement = (TreeElement) categoryIterator.next();
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
        for (ProductModel pm : productModels) {
            Product product;
            try {
                product = Product.wrap(pm, user.getFDSessionUser().getUser());
                result.add(product);
                brands.addAll(product.getBrands());
            } catch (ModelException e) {
                LOG.warn("ModelException encountered while preparing search result.", e);
            }
        }

        return result;
    }

    @Override
    public List<Product> search(String searchTerm) throws ServiceException {
        return search(searchTerm, 1, 25, null);
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
    public List<String> getAutoSuggestions(String searchTerm) {
        AutoCompleteFacade facade = new AutoCompleteFacade();
        return facade.getTerms(searchTerm);
    }

}
