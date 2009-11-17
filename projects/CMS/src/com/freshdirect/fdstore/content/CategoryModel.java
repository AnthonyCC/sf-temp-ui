package com.freshdirect.fdstore.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Category;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.smartstore.CmsRecommenderService;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.framework.util.BalkingExpiringReference;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CategoryModel extends ContentNodeModelImpl {
	private static final long serialVersionUID = 5787890004203680537L;

	private static final Category LOGGER = LoggerFactory.getInstance(CategoryModel.class);

	private static final Executor threadPool = new ThreadPoolExecutor(5, 5, 60,
			TimeUnit.SECONDS, new LinkedBlockingQueue(),
			new ThreadPoolExecutor.DiscardPolicy());
	
	private static final long TWO_MINUTES = 2l * 60l * 1000l;
	
	private final class RecommendedProductsRef extends BalkingExpiringReference {
		private RecommendedProductsRef(long refreshPeriod, Executor executor) {
			super(refreshPeriod, executor);
		}

		@Override
		protected Object load() {
			CmsRecommenderService recommenderService = getRecommenderService();
			String categoryId = CategoryModel.this.getContentName();
			String recommenderId = CategoryModel.this.getRecommender() != null ?
					CategoryModel.this.getRecommender().getContentName()
					: null;
			LOGGER.debug("loader started for category " + categoryId + ", recommender: " + recommenderId);
			if (recommenderService != null && recommenderId != null) {
				List prodIds = recommenderService.recommendNodes(recommenderId, categoryId);
				List products = new ArrayList(prodIds.size());
				for (Iterator it = prodIds.iterator(); it.hasNext();) {
					String productId = (String) it.next();
					ContentNodeModel product = ContentFactory.getInstance().getContentNode(productId);
					if (product instanceof ProductModel &&
							!products.contains(product))
						products.add(product);
				}
				LOGGER.debug("found " + products.size() + " products for category " + categoryId);
				return products;
			} else {
				LOGGER.warn("recommender service ("
						+ recommenderId
						+ ") not found, returning previous recommendations for category "
						+ categoryId);
				// fallback
				throw new RuntimeException();
			}
		}
		
		private CmsRecommenderService getRecommenderService() {
			try {
				return (CmsRecommenderService) FDRegistry.getInstance()
						.getService("com.freshdirect.fdstore.CmsRecommenderService", CmsRecommenderService.class);
			} catch (RuntimeException e) {
				LOGGER.error("failed to initialize CMS recommender service", e);
			}
			return null;
		}
	}

	private CategoryAlias categoryAlias;
	
	private BalkingExpiringReference recommendedProductsRef;

	private List subcategoriesModels = new ArrayList();

	private List productModels = new ArrayList();

	private List featuredProductModels = new ArrayList();
	
	/**
	 * List of ProductModels and CategoryModels.
	 */
	private List candidateList = new ArrayList();
	
	// New Wine Store 

	private List wineSortCriteriaList = new ArrayList();
	
	private List wineFilterCriteriaList = new ArrayList();
	
	private List wineSideNavSectionsList = new ArrayList();
	
	private List wineSideNavFullsList = new ArrayList();
	
	
	public CategoryModel(com.freshdirect.cms.ContentKey cKey) {
		super(cKey);
		categoryAlias = null;
	}

	public DepartmentModel getDepartment() {
		ContentNodeModel start = this;

		while (!(start instanceof DepartmentModel) && (start != null)) {
			start = start.getParentNode();
		}
		return (DepartmentModel) start;
	}

	public boolean getSideNavBold() {
		return getAttribute("SIDENAV_BOLD", false);
	}

	public boolean getSideNavLink() {
		return getAttribute("SIDENAV_LINK", false);
	}

	public int getSideNavPriority() {
		return getAttribute("SIDENAV_PRIORITY", 1);
	}

	public int getColumnSpan() {
		return getAttribute("COLUMN_SPAN", 2);
	}

	public boolean getFakeAllFolders() {
		return getAttribute("FAKE_ALL_FOLDER", false);
	}
	
	public EnumShowChildrenType getSideNavShowChildren() {
		return EnumShowChildrenType.getShowChildrenType(getAttribute("SIDENAV_SHOWCHILDREN", EnumShowChildrenType.ALWAYS_FOLDERS
			.getId()));
	}

	public boolean getSideNavShowSelf() {
		return getAttribute("SIDENAV_SHOWSELF", false);
	}

	public Image getCategoryLabel() {
		return (Image)getAttribute("CAT_LABEL", (Object)null);
	}
	
	public Image getCategoryPhoto() {
		return (Image)getAttribute("CAT_PHOTO", (Object)null);
	}
	
	public List<Object> getCategoryTopMedia() {
		return (List<Object>)getAttribute( "CATEGORY_TOP_MEDIA", (Object)null );		
	}

	
	public EnumShowChildrenType getShowChildren() {
		return EnumShowChildrenType.getShowChildrenType(getAttribute("SHOWCHILDREN", EnumShowChildrenType.ALWAYS_FOLDERS.getId()));
	}

	public boolean getShowSelf() {
		return getAttribute("SHOWSELF", true);
	}

	public boolean isSecondaryCategory() {
		return getAttribute("SECONDARY_CATEGORY", false);
	}

	public boolean isFeatured() {
		return getAttribute("FEATURED", false);
	}
	
	
	public Html getSeparatorMedia() {
		return (Html)getAttribute("SEPARATOR_MEDIA",(Html)null);
	}

	public List getSubcategories() {
		ContentNodeModelUtil.refreshModels(this, "subcategories", subcategoriesModels, true);

		return new ArrayList(subcategoriesModels);
	}
	
        public List getCandidateList() {
            ContentNodeModelUtil.refreshModels(this, "CANDIDATE_LIST", candidateList, false);
            return new ArrayList(candidateList);
        }
        
        public int getManualSelectionSlots() {
            return getAttribute("MANUAL_SELECTION_SLOTS", 0);
        }

	/* This does not traverse the alias list */
	public List getPrivateProducts() {
		ContentNodeModelUtil.refreshModels(this, "products", productModels, true);

		return new ArrayList(productModels);
	}
	
	/* [APPREQ-77] */
	public Html getMediaContent() {
		return (Html)getAttribute("MEDIA_CONTENT",(Html)null);
	}

	/* [APPREQ-160] SmartStore, Category Level Aggregation */
	public boolean isDYFAggregated() {
		return getAttribute("SS_LEVEL_AGGREGATION", false);
	}

	public boolean isHavingBeer() {
		return getAttribute("CONTAINS_BEER", false);
	}

	/* [NEW WINE STORE CHANGES] */  
	
	
	
	public Image getCategoryDetailImage() {
		return (Image)getAttribute("CATEGORY_DETAIL_IMAGE", (Object)null);
	}
	 	
	public List getWineSortCriteria() {
		// TODO Auto-generated method stub		
		ContentNodeModelUtil.refreshModels(this, "WINE_SORTING", wineSortCriteriaList, false,true);
		return new ArrayList(wineSortCriteriaList);		
	}
	
	public List getWineFilterCriteria() {
		// TODO Auto-generated method stub		
		ContentNodeModelUtil.refreshModels(this, "WINE_FILTER", wineFilterCriteriaList, false,true);
		return new ArrayList(wineFilterCriteriaList);		
	}

	public List getWineSideNavSections() {
		// TODO Auto-generated method stub		
		ContentNodeModelUtil.refreshModels(this, "SIDE_NAV_SECTIONS", wineSideNavSectionsList, false);
		return new ArrayList(wineSideNavSectionsList);		
	}

	public List getWineSideNavFullList() {
		// TODO Auto-generated method stub		
		ContentNodeModelUtil.refreshModels(this, "SIDE_NAV_FULL_LIST", wineSideNavFullsList, false);
		return new ArrayList(wineSideNavFullsList);		
	}
	
	
	public String getContentTemplatePath(){		
		return this.getAttribute("TEMPLATE_PATH", null);		
	}
	
	public Recommender getRecommender() {
		return (Recommender) getAttribute("recommender", (Recommender) null);
	}
	
	/** @return List of {@link CategoryRef} */
	public List getVirtualGroupRefs() {
		com.freshdirect.fdstore.attributes.Attribute vGroup = this.getAttribute("VIRTUAL_GROUP");
		return vGroup == null ? null : (List) vGroup.getValue();
	}

	public List getProducts() {
		List prodList = getPrivateProducts();

		if (categoryAlias == null) {
			List l = getVirtualGroupRefs();
			if (l != null) {
				this.categoryAlias = new CategoryAlias(l, getFilterList());
			}
		}

		if (categoryAlias != null) {
			try {
				Collection aliasProds = this.categoryAlias.processCategoryAlias();
				if (aliasProds != null) { // if we had an error..then we get null, cause empty list is valid
					for (Iterator pItr = aliasProds.iterator(); pItr.hasNext();) {
						CmsContentNodeAdapter newProd = (CmsContentNodeAdapter) ((CmsContentNodeAdapter) pItr.next()).clone();
						newProd.setParentNode(this);
						newProd.setPriority(prodList.size());
						if (!prodList.contains(newProd)) { // don't put a duplicate product in there
							prodList.add(newProd);
//							LOGGER.debug(" ##### added aliased product: " + newProd.getContentName());
						} else {
//							LOGGER.debug(" #### "
//								+ newProd.getContentName()
//								+ " already in list, not adding product: "
//								+ newProd.getContentName());
						}
					}
				}
			} catch (Exception ex) {
				LOGGER.warn("exception during category aliasing", ex);
			}
		}

		Recommender recommender = getRecommender();
		if (recommender != null) {
			if (recommendedProductsRef == null)
				recommendedProductsRef = new RecommendedProductsRef(TWO_MINUTES, threadPool);

			try {
				List recProds = (List) recommendedProductsRef.get();
				if (recProds != null) {
					for (Iterator pItr = recProds.iterator(); pItr.hasNext();) {
						CmsContentNodeAdapter newProd = (CmsContentNodeAdapter) ((CmsContentNodeAdapter) pItr.next()).clone();
						newProd.setParentNode(this);
						newProd.setPriority(prodList.size());
						if (!prodList.contains(newProd)) {
							prodList.add(newProd);
						}
					}
				}
			} catch (Exception e) {
				LOGGER.warn("exception during smart category recommendation", e);
			}
		}
		
		return new ArrayList(prodList);
		
	}

    private List getFilterList() {
        List filterList;

        AttributeI filters = getCmsAttribute("FILTER_LIST");
        if ((filters != null) && (filters.getValue() != null)) {
            filterList = new ArrayList();
            StringTokenizer stFilterNames = new StringTokenizer((String) filters.getValue(), ",");
            for (; stFilterNames.hasMoreTokens();) {
                String tok = stFilterNames.nextToken();
                if (!filterList.contains(tok)) {
                    filterList.add(tok);
                }
            }
        } else {
            filterList = Collections.EMPTY_LIST;
        }
        return filterList;
    }

	//** ProductRef.lookupProduct() is the main user..
	public ProductModel getProductByName(String productId) {
		Collection prods = getProducts();
		for (Iterator pItr = prods.iterator(); pItr.hasNext();) {
			ProductModel pm = (ProductModel) pItr.next();
			if (pm.getContentName().equals(productId)) {
				return pm;
			}
		}
		return null;
	}

	//** ProductRef.lookupProduct() is the main user..
	public ProductModel getPrivateProductByName(String productId) {
		Collection prods = getPrivateProducts();
		for (Iterator pItr = prods.iterator(); pItr.hasNext();) {
			ProductModel pm = (ProductModel) pItr.next();
			if (pm.getContentName().equals(productId)) {
				return pm;
			}
		}
		return null;
	}
	
	public List getFeaturedProducts() {
		boolean bRefreshed = ContentNodeModelUtil.refreshModels(
			this,
			"FEATURED_PRODUCTS",
			featuredProductModels,
			false);

		if (bRefreshed) {
			ContentNodeModelUtil.setNearestParentForProducts(this, featuredProductModels);
		}
		return new ArrayList(featuredProductModels);
	}

	/**
	 * @return all the brands of available products within the category, recursively
	 */
	public Set getAllBrands() throws FDResourceException {
		Set brands = new TreeSet(FULL_NAME_COMPARATOR);
		this.getAllBrands(brands, this);
		return brands;
	}
	
	/**
	 * @return content key of the category/department this alias category
	 * points to. If it this category is not a alias category , return null.
	 */

	public ContentKey getAliasAttributeValue() {
		ContentRef attrValue = (ContentRef) this.getAttribute("ALIAS", (Object)null);
		if(attrValue != null){
			ContentKey refKey = null;
		    if(attrValue instanceof CategoryRef) {
		    	refKey = ((CategoryRef)attrValue).getCategory().getContentKey();
		    } else if(attrValue instanceof DepartmentRef){
		    	refKey = ((DepartmentRef)attrValue).getDepartment().getContentKey();
		    }
		    return refKey;
		}
		return null;
	}
	

	/**
	 * Returns alias category if has
	 *
	 * @return category<CategoryModel>
	 */
	public CategoryModel getAliasCategory() {
		ContentRef attrValue = (ContentRef) this.getAttribute("ALIAS", (Object)null);
		if (attrValue instanceof CategoryRef) {
	    	return ((CategoryRef)attrValue).getCategory();
		}

		return null;
	}
	
	
	/**
	 * Recursive method to get all brands.
	 */
	private void getAllBrands(Set brands, CategoryModel category) throws FDResourceException {
		if (category == null) {
			return;
		}

		List subFolders = category.getSubcategories();
		if (subFolders != null && subFolders.size() != 0) {
			for (Iterator sf = subFolders.iterator(); sf.hasNext();) {
				getAllBrands(brands, (CategoryModel) sf.next());
			}
		}

		Collection products = category.getProducts();
		for (Iterator p = products.iterator(); p.hasNext();) {
			ProductModel product = (ProductModel) p.next();
			if (product.isDiscontinued()) {
				continue;
			}
			brands.addAll(product.getBrands());
		}
	}

	private boolean hasCategoryAlias() {
		return categoryAlias != null;
	}

	private class CategoryAlias implements Serializable {
		private final List categoryRefs;
		private final List productFilterNames;

		public CategoryAlias(List categoryRefs, List prodFilterNames) {
			this.categoryRefs = categoryRefs;
			this.productFilterNames = prodFilterNames;
			LOGGER.debug("Filter Name = " + productFilterNames);
		}

		public Collection processCategoryAlias() throws FDResourceException {
			Collection products = new ArrayList();
			try {
				for (Iterator ci = categoryRefs.iterator(); ci.hasNext();) {
					CategoryModel cm = ((CategoryRef) ci.next()).getCategory();
					products.addAll(cm.getProducts());
				}
				return filterProducts((List) products);
			} catch (FDResourceException fdre) {
				LOGGER.error("caught resource exception in CategoryAlias.processCategory()", fdre);
				throw fdre;
			}
		}

		private List filterProducts(List nodes) throws FDResourceException {
			List productFilters = null;

			if (this.productFilterNames != null && !this.productFilterNames.isEmpty()) {
				productFilters = ProductFilterFactory.getInstance().getFilters(this.productFilterNames);
				LOGGER.debug(" List of Filters = " + productFilters.size());
			} else
				productFilters = Collections.EMPTY_LIST;
			List nodeList = nodes;
			try {
				for (Iterator fi = productFilters.iterator(); fi.hasNext() && !nodeList.isEmpty();) {
					ProductFilterI prodFilter = (ProductFilterI) fi.next();
					LOGGER.debug(" about to apply filter:" + prodFilter.getClass().getName());
					nodeList = new ArrayList(prodFilter.apply(nodeList));
				}
			} catch (FDResourceException fdre) {
				LOGGER.error("caught resource exception atttempting to get products from category", fdre);
				throw fdre;
			}
			return nodeList;
		}

	}

}
