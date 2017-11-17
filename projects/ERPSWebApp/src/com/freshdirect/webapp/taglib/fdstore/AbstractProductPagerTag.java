package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.SearchSortType;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.fdstore.util.ProductPagerNavigator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupportEx;
import com.freshdirect.storeapi.content.BrandModel;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.CategoryNodeTree;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.FilteringSortingItem;
import com.freshdirect.storeapi.content.ProductContainer;
import com.freshdirect.storeapi.content.ProductFilterI;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.SearchResults;
import com.freshdirect.storeapi.content.ContentNodeTree.TreeElement;

public abstract class AbstractProductPagerTag extends BodyTagSupportEx implements CategoryTreeContainer {
	private static final long serialVersionUID = 5631516839214295727L;

	private static Category LOGGER = LoggerFactory.getInstance(SmartSearchTag.class);

	private boolean mocked;
	private String mockCustomerId;
	protected ProductPagerNavigator nav;
	private Collection<ProductFilterI> productFilters;
	private SearchResults results;
	private int noOfProductsBeforeProductFilters;
	private int noOfProducts;
	private int noOfFilteredProducts;
	private int noOfBrandFilteredProducts;
	private int pageSize = 0;
	private int pageOffset = 0;
	private int pageNo = 1;
	private int pageCount = 1;
	private List<ProductModel> productsUnwrap = null;
	private List<FilteringSortingItem<ProductModel>> pageProducts = Collections.emptyList();
	private List<ProductModel> pageProductsUnwrap = null;
	private SortedSet<CategoryModel> pageCategories;
	private SortedSet<BrandModel> brands;
	private CategoryNodeTree categoryTree;
	private CategoryNodeTree filteredCategoryTree;
	private ProductContainer parentContainer;

	private FDUserI user;

	public AbstractProductPagerTag() {
		super();
	}
	
	/**
	 * This is the mock constructor
	 * 
	 * @param nav
	 * @param customerId
	 */
	protected AbstractProductPagerTag(ProductPagerNavigator nav, String customerId) {
		this.mocked = true;
		this.nav = nav;
		this.mockCustomerId = customerId;
	}
	
	@Override
	final public int doStartTag() throws JspException {
		reset();
		if (!isMocked())
			pageContext.setAttribute(this.id, this);

		// Sanity check the search criteria before performing search
		if (nav.isEmptySearch()) {
			LOGGER.debug("search criteria was null or empty");
			return EVAL_BODY_INCLUDE;
		}

		// get the initial results
		results = getResults();

		noOfProductsBeforeProductFilters = results.getProducts().size();

		// this workaround is for iPhone
		if (productFilters != null && !productFilters.isEmpty() && !results.getProducts().isEmpty()) {
			Iterator<FilteringSortingItem<ProductModel>> it = results.getProducts().iterator();
			List<ProductModel> miniList = new ArrayList<ProductModel>();
			while (it.hasNext()) {
				FilteringSortingItem<ProductModel> item = it.next();
				for (ProductFilterI filter : productFilters) {
					try {
						// HACK sorry for this hack but the original ProductFilterI is queer
						miniList.clear();
						miniList.add(item.getModel());
						List<ProductModel> filtered = filter.apply(miniList);
						if (filtered.isEmpty()) {
							it.remove();
							break;
						}
					} catch (FDResourceException e) {
					}
				}
			}
		}

		noOfProducts = results.getProducts().size();

		// build up tree
		categoryTree = buildProductCategoryTree();

		
		// filter products by category and department
		parentContainer = filterProductsByCategory(nav, categoryTree, results);

		noOfFilteredProducts = results.getProducts().size();

		// collect brands
		collectBrands();

		// filter products by brands
		filterProductsByBrands();
		noOfBrandFilteredProducts = results.getProducts().size();

		// create filtered tree
		filteredCategoryTree = buildProductCategoryTree();

		// sort products
		Comparator<FilteringSortingItem<ProductModel>> comparator = getProductSorter(results.getProducts(), nav.getSortBy(), nav.isSortOrderingAscending());
		if (comparator != null)
			Collections.sort(results.getProducts(), comparator);

		// create product page window
		createProductPageWindow();
		
		postProcess(results);

		return EVAL_BODY_INCLUDE;
	}

	protected abstract SearchResults getResults();

	protected abstract Comparator<FilteringSortingItem<ProductModel>> getProductSorter(List<FilteringSortingItem<ProductModel>> products,
			SearchSortType sortBy, boolean ascending);
	
	protected abstract void postProcess(SearchResults results);
	
	private void reset() {
		results = null;
		noOfProductsBeforeProductFilters = 0;
		noOfProducts = 0;
		noOfFilteredProducts = 0;
		noOfBrandFilteredProducts = 0;
		pageSize = 0;
		pageOffset = 0;
		pageNo = 1;
		pageCount = 1;
		productsUnwrap = null;
		pageProducts = Collections.emptyList();
		pageProductsUnwrap = null;
		pageCategories = null;
		brands = null;
		categoryTree = null;
		filteredCategoryTree = null;
		parentContainer = null;
		user = null;
	}

	private CategoryNodeTree buildProductCategoryTree() {
		return CategoryNodeTree.createTree(results, true);
	}

	protected void createProductPageWindow() {
		pageSize = nav.getPageSize();
		pageOffset = nav.getPageOffset();
		pageNo = nav.getPageNumber();
		pageProducts = new ArrayList<FilteringSortingItem<ProductModel>>(pageSize <= 0 ? results.getProducts().size() : pageSize);
		int noOfPagedProducts = results.getProducts().size();
		pageCount = pageSize == 0 ? 1 : noOfPagedProducts / pageSize;
		if (pageSize != 0 && noOfPagedProducts % pageSize > 0)
			pageCount++;
		int max = pageSize == 0 ? pageOffset + noOfPagedProducts : pageOffset + pageSize;
		for (int i = pageOffset; i < max; i++) {
			// APPDEV-4236 - Call to getRelatedProducts failing for alcohol
			if (i >= results.getProducts().size() || results.getProducts().size() == 0)
				break;
			pageProducts.add(results.getProducts().get(i));
		}
		pageCategories = new TreeSet<CategoryModel>(ContentNodeModel.FULL_NAME_COMPARATOR);
		for (FilteringSortingItem<ProductModel> product : pageProducts)
			pageCategories.add(product.getModel().getPrimaryHome());
	}

	/**
	 * Ugly hack to create a hooked override. Sorry but the two codes (new products & search) do not do the same so it needs same
	 * hacking like this. Sorry again.
	 * 
	 * @param nav
	 * @param categoryTree
	 * @param results
	 * @return
	 */
	protected ProductContainer filterProductsByCategory(final ProductPagerNavigator nav, CategoryNodeTree categoryTree, SearchResults results) {
		String parentId = nav.getDepartment();
		ProductContainer parentContainer = null;
		TreeElement parentElement = null;
		if (parentId != null)
			parentElement = categoryTree.getTreeElement(parentId);
		parentId = nav.getCategory();
		if (parentId != null) {
			TreeElement elem = categoryTree.getTreeElement(parentId);
			if (elem != null)
				parentElement = elem;
		}
		if (parentElement != null)
			parentContainer = (ProductContainer) parentElement.getModel();

		if (results.getProducts().isEmpty())
			return parentContainer; // nothing to do
	
		if (nav.isRecipesFiltered())
			results.emptyProducts();
		
		if (parentElement != null) {
			Set<ContentKey> filtered = parentElement.getAllChildren();
			Iterator<FilteringSortingItem<ProductModel>> it = results.getProducts().iterator();
			while (it.hasNext()) {
				FilteringSortingItem<ProductModel> item = it.next();
				if (!filtered.contains(item.getModel().getContentKey()))
					it.remove();
			}
		}
		
		return parentContainer;
	}

	protected void filterProductsByBrands() {
		if (results.getProducts().isEmpty())
			return; // nothing to do
		String brandId = nav.getBrand();
		if (brandId == null)
			return;
	
		Iterator<FilteringSortingItem<ProductModel>> it = results.getProducts().iterator();
		OUTER: while (it.hasNext()) {
			FilteringSortingItem<ProductModel> item = it.next();
			for (BrandModel brand : item.getModel().getBrands())
				if (brandId.equals(brand.getContentKey().getId()))
					continue OUTER;
			it.remove();
		}
	}

	protected void collectBrands() {
		brands = new TreeSet<BrandModel>(ContentNodeModel.FULL_NAME_COMPARATOR);
		for (FilteringSortingItem<ProductModel> item : results.getProducts())
			brands.addAll(item.getModel().getBrands());
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setNav(ProductPagerNavigator nav) {
		this.nav = nav;
	}

	public ProductPagerNavigator getNav() {
		return nav;
	}

	public void setProductFilters(Collection<ProductFilterI> productFilters) {
		this.productFilters = productFilters;
	}

	public Collection<ProductFilterI> getProductFilters() {
		return productFilters;
	}

	public List<ProductModel> getProducts() {
		if (productsUnwrap == null)
			productsUnwrap = ProductPricingFactory.getInstance().getPricingAdapter(
					FilteringSortingItem.unwrap(results.getProducts()), getPricingContext());
		return productsUnwrap;
	}

	public int getNoOfProductsBeforeProductFilters() {
		return noOfProductsBeforeProductFilters;
	}

	public int getNoOfProducts() {
		return noOfProducts;
	}

	public int getNoOfFilteredProducts() {
		return noOfFilteredProducts;
	}

	public int getNoOfBrandFilteredProducts() {
		return noOfBrandFilteredProducts;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getPageOffset() {
		return pageOffset;
	}

	public int getPageCount() {
		return pageCount;
	}

	public int getPageNo() {
		return pageNo;
	}

	public List<ProductModel> getPageProducts() {
		if (pageProductsUnwrap == null)
			pageProductsUnwrap = ProductPricingFactory.getInstance().getPricingAdapter(
					FilteringSortingItem.unwrap(pageProducts), getPricingContext());
		return pageProductsUnwrap;
	}
	
	protected List<FilteringSortingItem<ProductModel>> getPageProductsInternal() {
		pageProductsUnwrap = null; // invalidate
		return pageProducts;
	}

	public boolean hasProducts() {
		return !pageProducts.isEmpty();
	}

	public SortedSet<CategoryModel> getPageCategories() {
		return pageCategories;
	}

	public SortedSet<BrandModel> getBrands() {
		return brands;
	}

	public CategoryNodeTree getCategoryTree() {
		return categoryTree;
	}

	public CategoryNodeTree getFilteredCategoryTree() {
		return filteredCategoryTree;
	}

	public ProductContainer getParentContainer() {
		return parentContainer;
	}

	public FDUserI getFDUser() {
		if (user == null) {
			if (mocked) {
				if (mockCustomerId != null)
					try {
						user = FDCustomerManager.recognize(new FDIdentity(mockCustomerId));
					} catch (FDAuthenticationException e) {
						LOGGER.warn("authentication failed for customer: " + mockCustomerId, e);
					} catch (FDResourceException e) {
						LOGGER.warn("failed to recognize customer: " + mockCustomerId, e);
					}
			} else
				user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
		}
		return user;
	}

	public String getUserId() {
		FDUserI user = getFDUser();
		if (user != null) {
			FDIdentity identity = user.getIdentity();
			if (identity != null)
				return identity.getErpCustomerPK();
		}
		return null;
	}

	public PricingContext getPricingContext() {
		FDUserI user = getFDUser();
		if (user != null)
			return user.getPricingContext();
		return PricingContext.DEFAULT;
	}

	public SearchResults getProcessedResults() {
		return results;
	}
	
	public boolean isMocked() {
		return mocked;
	}
}