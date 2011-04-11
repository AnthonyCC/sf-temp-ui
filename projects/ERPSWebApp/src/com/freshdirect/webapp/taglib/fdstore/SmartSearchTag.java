package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.content.AbstractProductFilter;
import com.freshdirect.fdstore.content.BrandFilter;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.CategoryNodeTree;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentNodeTree;
import com.freshdirect.fdstore.content.ContentNodeTree.TreeElement;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.fdstore.content.FilteredSearchResults;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.content.SearchSortType;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.SearchNavigator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;

/**
 * @author zsombor
 * 
 */
public class SmartSearchTag extends BodyTagSupport {

	private static final long	serialVersionUID	= 3093054384959548572L;

	public static class FilterChain extends AbstractProductFilter {

        List<AbstractProductFilter> filters;

        public FilterChain() {
            filters = new ArrayList<AbstractProductFilter>();
        }

        public FilterChain(List<AbstractProductFilter> filters) {
            this.filters = filters;
        }

        /**
         * Convenience method
         */
        public FilterChain(AbstractProductFilter f1, AbstractProductFilter f2) {
            this.filters = new ArrayList<AbstractProductFilter>();
            if (f1 != null) {
                this.filters.add(f1);
            }
            if (f2 != null) {
                this.filters.add(f2);
            }
        }

        public void add(AbstractProductFilter a) {
            this.filters.add(a);
        }

        public boolean applyTest(ProductModel prod) throws FDResourceException {
            for (Iterator<AbstractProductFilter> it = filters.iterator(); it.hasNext();) {
                AbstractProductFilter filter = it.next();
                if (!filter.applyTest(prod)) {
                    return false;
                }
            }

            return true;
        }

        public boolean isEmpty() {
            return filters.isEmpty();
        }

    }
        

    private static Category  LOGGER = LoggerFactory.getInstance(SmartSearchTag.class);

    private String           searchResults;                                           // search
    // results
    // key
    private String           productList;
    private String           categorySetName;
    private String           brandSetName;

    private String           categoryTreeName;

    private String           selectedCategoriesName;

    private String filteredCategoryTreeName;

    public void setSearchResults(String s) {
        this.searchResults = s;
    }

    public void setProductList(String s) {
        this.productList = s;
    }

    public void setCategorySet(String categorySet) {
        this.categorySetName = categorySet;
    }

    public void setBrandSet(String brandSetName) {
        this.brandSetName = brandSetName;
    }

    public void setCategoryTree(String categoryTreeName) {
        this.categoryTreeName = categoryTreeName;
    }

    public void setFilteredCategoryTreeName(String filteredCategoryTreeName) {
        this.filteredCategoryTreeName = filteredCategoryTreeName;
    }

    public void setSelectedCategories(String selectedCategories) {
        this.selectedCategoriesName = selectedCategories;
    }
    
    public int doStartTag() throws JspException {

        ServletRequest request = pageContext.getRequest();
        String searchTerm = request.getParameter("searchParams");
        if (searchTerm != null) {
        	searchTerm = searchTerm.trim();
        	if (searchTerm.length() == 0)
        		searchTerm = null;
        }
        String upc = request.getParameter("upc");
        if (upc != null) {
        	upc = upc.trim();
        	if (upc.length() == 0)
        		upc = null;
        }

        //
        // Sanity check the search criteria before performing search
        //
        if (searchTerm == null && upc == null) {
            LOGGER.debug("search criteria was null or empty");
            return EVAL_BODY_BUFFERED;
        }
        
        String userId = getUserId(); // ErpCustomerPK
        
        SearchResults res;
        if (upc != null) {
            res = ContentSearch.getInstance().searchUpc(userId, upc);        	
        } else {
            res = ContentSearch.getInstance().search(searchTerm);        	
        }

        
        String departmentId = request.getParameter("deptId");
        if (departmentId != null && departmentId.length() == 0) {
            departmentId = null;
        }


        boolean reverseOrder = "desc".equalsIgnoreCase(request.getParameter("order"));
        FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
        FilteredSearchResults fres = new FilteredSearchResults(res.getSearchTerm(), res, userId, upc != null ? upc : searchTerm.toLowerCase(),user != null ? user.getPricingContext() : PricingContext.DEFAULT);
        CategoryNodeTree contentTree = putTree(categoryTreeName, fres.getProducts(), true);
        fres.setNodeTree(contentTree);
        fres.setScoreOracle(new FilteredSearchResults.HierarchicalScoreOracle(contentTree));

        fres.sortProductsBy(SearchSortType.findByLabel(request.getParameter("sort")), reverseOrder);

        fres.setStart(Math.max(getIntParameter("start", 0), 0));
        {
            // calculate page size.
            String view = pageContext.getRequest().getParameter("view");
            if (view == null) {
                view = SearchNavigator.getDefaultViewName();  // "list"; // default view
            }
            int defaultPageSize = 0;
            SearchNavigator.SearchDefaults defs = SearchNavigator.DEFAULTS.get(view);
            if (defs != null) {
            	defaultPageSize = defs.normalPageSize;
            }
            
            fres.setPageSize(Math.max(Math.min(getIntParameter("pageSize", defaultPageSize), 100), 0));
        }

        String categoryId = pageContext.getRequest().getParameter("catId");

        List<ProductModel> filtered;
        List<ProductModel> convFiltered;
        {
            
            if (categoryId != null) {
                filtered = contentTree.collectChildNodes(categoryId, new ContentNodeTree.UniqueProductFilter());
                Collections.sort(filtered, fres.getCurrentComparator());
            } else if (departmentId != null) {
                filtered = contentTree.collectChildNodes(departmentId, new ContentNodeTree.UniqueProductFilter());
                Collections.sort(filtered, fres.getCurrentComparator());
            } else {
                filtered = fres.getProducts();
            }
            convFiltered = new ArrayList<ProductModel>(filtered.size());
            // get brand names - all or just for the selected category
            if (brandSetName != null) {
                TreeSet<BrandModel> brandSet = new TreeSet<BrandModel>(ContentNodeModel.FULL_NAME_COMPARATOR);
                for (int i = 0; i < filtered.size(); i++) {
                    ProductModel prod = filtered.get(i);
                    //Convert them to ProductModelPricingAdapter for zone pricing.
                    convFiltered.add(prod);
                	brandSet.addAll(prod.getBrands());
                }
                pageContext.setAttribute(brandSetName, brandSet);

            }

            // STEP 2 - Then (re)filter to brands too - if it was given
            String brand = request.getParameter("brandValue");

            if (brand != null && brand.length() > 0) {
                BrandFilter bf = new BrandFilter(brand);
                try {
                	convFiltered = bf.apply(convFiltered);
                } catch (FDResourceException e) {
                    e.printStackTrace();
                }
            }
            fres.setFilteredProducts(convFiltered);
        }

        
        // recipe filter
        // set recipe filter and sort ordering when full recipes are displayed
        if ("rec".equalsIgnoreCase(departmentId)) {
            fres.setRecipeFilter(request.getParameter("classification"), reverseOrder);
        }
        
        
        

        // set search results in PageContext
        pageContext.setAttribute(searchResults, fres);

        if (productList != null) {
            pageContext.setAttribute(productList, fres.getFilteredProductsListPage());
        }

        if (categorySetName != null) {
            List<ProductModel> products = fres.getProducts();
            TreeSet<CategoryModel> categorySet = new TreeSet<CategoryModel>(ContentNodeModel.FULL_NAME_COMPARATOR);
            for (int i = 0; i < products.size(); i++) {
                ProductModel prod = products.get(i);
                categorySet.add(prod.getPrimaryHome());
            }
            pageContext.setAttribute(categorySetName, categorySet);
        }

        if (filteredCategoryTreeName != null) {
            putTree(filteredCategoryTreeName, convFiltered, false);
        }
        
        if (selectedCategoriesName != null) {
            // calculate the set of the selected categories, to the root
            Set<String> selectedCategories = new HashSet<String>();
            if (categoryId != null) {
                TreeElement treeElement = contentTree.getTreeElement(categoryId);
                while (treeElement != null) {
                    selectedCategories.add(categoryId);
                    ContentNodeModel contentNodeModel = contentTree.getParent(treeElement.getModel());
                    if (contentNodeModel != null) {
                        categoryId = contentNodeModel.getContentKey().getId();
                        treeElement = contentTree.getTreeElement(categoryId);
                    } else {
                        treeElement = null;
                    }
                }
            }
            pageContext.setAttribute(selectedCategoriesName, selectedCategories);
        }

        return EVAL_BODY_BUFFERED;
    }


    /**
     * Generates a content tree for the given products and assigns to attribute
     * 
     * @param attrName
     *            (String) Attribute name
     * @param products
     *            (List<ProductModel>) List of products
     * @param multipleHome
     *            (boolean) add products to their multiple home or not.
     */
    protected CategoryNodeTree putTree(String attrName, List<ProductModel> products, boolean multipleHome) {
        CategoryNodeTree tree = CategoryNodeTree.createTree(products, multipleHome);
        putTree(attrName, tree);
        return tree;
    }

    protected void putTree(String attrName, CategoryNodeTree tree) {
        if (attrName != null) {
            pageContext.setAttribute(attrName, tree);
        }
    }


    int getIntParameter(String param, int def) {
        try {
            String value = pageContext.getRequest().getParameter(param);
            if (value != null) {
                return Integer.parseInt(value.trim());
            }
            return def;
        } catch (Exception e) {
            return def;
        }
    }

    protected String getUserId() {
        FDUserI user = FDSessionUser.getFDSessionUser(pageContext.getSession());
        if (user == null) {
            return null;
        }
        FDIdentity identity = user.getIdentity();
        if (identity == null) {
            return null;
        }
        return identity.getErpCustomerPK();
    }
    
    @SuppressWarnings( "unused" )
	private PricingContext getPricingContext() {
        FDUserI user = FDSessionUser.getFDSessionUser(pageContext.getSession());
        if (user == null) {
            throw new FDRuntimeException("User object is Null");
        }
        return user.getPricingContext();
    }
}
