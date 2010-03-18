package com.freshdirect.fdstore.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.smartstore.CmsRecommenderService;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;
import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.framework.util.BalkingExpiringReference;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CategoryModel extends ProductContainer {
	private static final long serialVersionUID = 5787890004203680537L;

	private static final Logger LOGGER = LoggerFactory.getInstance(CategoryModel.class);

	private static final Executor threadPool = new ThreadPoolExecutor(5, 5, 60,
			TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
			new ThreadPoolExecutor.DiscardPolicy());
	
	private static final long FIVE_MINUTES = 5l * 60l * 1000l;
	
	private final class RecommendedProductsRef extends BalkingExpiringReference {
		String zoneId;

		private RecommendedProductsRef(Executor executor, String zoneId) {
			super(FIVE_MINUTES, executor);
			this.zoneId = zoneId;
			// synchronous load
			try {
				set(load());
			} catch (RuntimeException e) {
				LOGGER.error("failed to initialize smart products for category " + CategoryModel.this.getContentName() + " and zone " + zoneId + " synchronously");
			}
		}

		@Override
		protected Object load() {
			CmsRecommenderService recommenderService = getRecommenderService();
			String categoryId = CategoryModel.this.getContentName();
			String recommenderId = CategoryModel.this.getRecommender() != null ?
					CategoryModel.this.getRecommender().getContentName()
					: null;
			LOGGER.debug("loader started for category " + categoryId + ", zone " + zoneId + ", recommender: " + recommenderId);
			if ( recommenderService != null && recommenderId != null ) {
				List<String> prodIds = recommenderService.recommendNodes(recommenderId, categoryId, zoneId);
				List<ProductModel> products = new ArrayList<ProductModel>( prodIds.size() );
				for ( String productId : prodIds ) {
					ContentNodeModel product = ContentFactory.getInstance().getContentNode( productId );
					if ( product instanceof ProductModel && !products.contains( product ) )
						products.add( (ProductModel)product );
				}
				LOGGER.debug( "found " + products.size() + " products for category " + categoryId + ", zone " + zoneId);
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
		
		public String getZoneId() {
			return zoneId;
		}
	}

	private CategoryAlias categoryAlias;

	/**
	 * map of zoneId --> recommended products reference
	 */
	private Map<String, RecommendedProductsRef> recommendedProductsRefMap = new HashMap<String, RecommendedProductsRef>();
	
	private Object recommendedProductsSync = new Object();

	private List subcategoriesModels = new ArrayList();

	private List productModels = new ArrayList();

	private List<ProductModel> featuredProductModels = new ArrayList<ProductModel>();
	
	private final List featuredBrandModels = new ArrayList();

	/**
	 * List of ProductModels and CategoryModels.
	 */
	private List candidateList = new ArrayList();
	
	// New Wine Store 

	private List wineSortCriteriaList = new ArrayList();
	
	private List wineFilterCriteriaList = new ArrayList();
	
	private List wineSideNavSectionsList = new ArrayList();
	
	private List wineSideNavFullsList = new ArrayList();
	
	private List<ProductModel> howToCookItProducts = new ArrayList<ProductModel> ();
	
        final private List<CategoryModel> virtualGroups = new ArrayList<CategoryModel>();
	
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
	
	public CategoryModel getTopCategory() {
	    ContentNodeModel parentNode2 = getParentNode();
	    if (parentNode2 instanceof CategoryModel) {
	        return ((CategoryModel)parentNode2).getTopCategory();
	    }
	    return this;
	}

	public boolean isUseAlternateImages() {
		return getAttribute( "USE_ALTERNATE_IMAGES", false );
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

	public int getColumnSpan(int defaultValue) {
		return getAttribute("COLUMN_SPAN", defaultValue);
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

	/**
	 * 
	 * @return CAT_LABEL
	 */
	public Image getCategoryLabel() {
	    return FDAttributeFactory.constructImage(this, "CAT_LABEL");
	}
	
        /**
         * Return the CAT_LABEL image, if the attribute is null, then it returns an
         * empty image object.
         * 
         * @return
         */
        public final Image getCategoryLabelNotNull() {
            Image img = getCategoryLabel();
            if (img != null) {
                return img;
            } else {
                return new Image();
            }
        }	
	
	
	
	public Image getCategoryNavBar() {
	    return FDAttributeFactory.constructImage(this, "CATEGORY_NAVBAR");
	}
	
	/**
	 * Use getTopMedia
	 * @return
	 */
	@Deprecated
	public List<Html> getCategoryTopMedia() {
		return getTopMedia();
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
	    return FDAttributeFactory.constructHtml(this, "SEPARATOR_MEDIA");
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
	
	public List<ProductModel> getHowToCookItProducts() {
	    ContentNodeModelUtil.refreshModels(this, "HOW_TO_COOK_IT_PRODUCTS", this.howToCookItProducts, false);
	    return new ArrayList<ProductModel> (howToCookItProducts);
	}
	
	/* [APPREQ-160] SmartStore, Category Level Aggregation */
	public boolean isDYFAggregated() {
		return getAttribute("SS_LEVEL_AGGREGATION", false);
	}

	public boolean isHavingBeer() {
		return getAttribute("CONTAINS_BEER", false);
	}

	/* [NEW WINE STORE CHANGES] */  
	
	
	
	public final Image getCategoryDetailImage() {
	    return FDAttributeFactory.constructImage(this, "CATEGORY_DETAIL_IMAGE");
	}
	 	
	public List getWineSortCriteria() {
		ContentNodeModelUtil.refreshModels(this, "WINE_SORTING", wineSortCriteriaList, false,true);
		return new ArrayList(wineSortCriteriaList);		
	}
	
	public List getWineFilterCriteria() {
		ContentNodeModelUtil.refreshModels(this, "WINE_FILTER", wineFilterCriteriaList, false,true);
		return new ArrayList(wineFilterCriteriaList);		
	}

	public List getWineSideNavSections() {
		ContentNodeModelUtil.refreshModels(this, "SIDE_NAV_SECTIONS", wineSideNavSectionsList, false);
		return new ArrayList(wineSideNavSectionsList);		
	}

	public List getWineSideNavFullList() {
		ContentNodeModelUtil.refreshModels(this, "SIDE_NAV_FULL_LIST", wineSideNavFullsList, false);
		return new ArrayList(wineSideNavFullsList);		
	}
	
	
	public String getContentTemplatePath(){		
		return this.getAttribute("TEMPLATE_PATH", null);		
	}
	
	public Recommender getRecommender() {
            return FDAttributeFactory.lookup(this, "recommender", (Recommender) null);
	}
	
	/** @return List of {@link CategoryModel} */
	public List<CategoryModel> getVirtualGroupRefs() {
            ContentNodeModelUtil.refreshModels(this, "VIRTUAL_GROUP", virtualGroups, false);
            return new ArrayList<CategoryModel>(virtualGroups);
	}

    public List getProducts() {
        List<ContentNodeModel> prodList = getPrivateProducts();

        if (categoryAlias == null) {
            List<CategoryModel> l = getVirtualGroupRefs();
            if (l != null) {
                this.categoryAlias = new CategoryAlias(l, getFilterList());
            }
        }

        if (categoryAlias != null) {
            try {
                Collection<ProductModel> aliasProds = this.categoryAlias.processCategoryAlias();
                if (aliasProds != null) { // if we had an error..then we get
                                          // null, cause empty list is valid
                    for (Iterator<ProductModel> pItr = aliasProds.iterator(); pItr.hasNext();) {
                        ContentNodeModelImpl newProd = (ContentNodeModelImpl) ((ContentNodeModelImpl) pItr.next()).clone();
                        newProd.setParentNode(this);
                        newProd.setPriority(prodList.size());
                        if (!prodList.contains(newProd)) { // don't put a
                                                           // duplicate product
                                                           // in there
                            prodList.add(newProd);
                            // LOGGER.debug(" ##### added aliased product: " +
                            // newProd.getContentName());
                        } else {
                            // LOGGER.debug(" #### "
                            // + newProd.getContentName()
                            // + " already in list, not adding product: "
                            // + newProd.getContentName());
                        }
                    }
                }
            } catch (Exception ex) {
                LOGGER.warn("exception during category aliasing", ex);
            }
        }

        Recommender recommender = getRecommender();
        if (recommender != null) {
            String zoneId = ContentFactory.getInstance().getCurrentPricingContext().getZoneId();
            LOGGER.info("Category[id=\"" + this.getContentKey().getId() + "\"].getSmartProducts(\"" + zoneId + "\")");
            synchronized (recommendedProductsSync) {
                if (recommendedProductsRefMap.get(zoneId) == null)
                    recommendedProductsRefMap.put(zoneId, new RecommendedProductsRef(threadPool, zoneId));
            }

            try {
                List recProds = (List) recommendedProductsRefMap.get(zoneId).get();
                if (recProds != null) {
                    for (Iterator pItr = recProds.iterator(); pItr.hasNext();) {
                        ContentNodeModelImpl newProd = (ContentNodeModelImpl) ((ContentNodeModelImpl) pItr.next()).clone();
                        newProd.setParentNode(this);
                        if (!prodList.contains(newProd)) {
                            newProd.setPriority(prodList.size());
                            prodList.add(newProd);
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.warn("exception during smart category recommendation", e);
            }
        }

        return prodList;
    }

    private List<String> getFilterList() {
        List<String> filterList;

        String filters = (String) getCmsAttributeValue("FILTER_LIST");
        if (filters != null) {
            filterList = new ArrayList<String>();
            StringTokenizer stFilterNames = new StringTokenizer(filters, ",");
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

	//** ContentFactory.getProductByName() is the main user..
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

	//** setNearestParentForProduct() is the main user..
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
	
	/**
	 * 
	 * @return CategoryModel, same as getAliasCategory
	 */
	public CategoryModel getAlias() {
	    return getAliasCategory();
	}
	
	@Override
	public List<ProductModel> getFeaturedProducts() {
		boolean bRefreshed = ContentNodeModelUtil.refreshModels(
			this,
			"FEATURED_PRODUCTS",
			featuredProductModels,
			false);

		if (bRefreshed) {
			ContentNodeModelUtil.setNearestParentForProducts(this, featuredProductModels);
		}
		return new ArrayList<ProductModel>(featuredProductModels);
	}

	/** Getter for property featured brands.
	 * @return List of BrandModels that are referenced by the  property brands.
	 */
	public List getFeaturedBrands() {
		ContentNodeModelUtil.refreshModels(this, "FEATURED_BRANDS", featuredBrandModels, false);

		return new ArrayList(featuredBrandModels);
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
            return (ContentKey) this.getCmsAttributeValue("ALIAS");
        }	

	/**
	 * Returns alias category if has
	 *
	 * @return category<CategoryModel>
	 */
        public CategoryModel getAliasCategory() {
            ContentKey key = getAliasAttributeValue();
            if (key != null) {
                return (CategoryModel) ContentFactory.getInstance().getContentNodeByKey(key);
            }
            return null;
        }
	
	public boolean getTreatAsProduct() {
	    return getAttribute("TREAT_AS_PRODUCT", false);
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
		private final List<CategoryModel> categoryRefs;
		private final List<String> productFilterNames;

		public CategoryAlias(List<CategoryModel> categoryRefs, List<String> prodFilterNames) {
			this.categoryRefs = categoryRefs;
			this.productFilterNames = prodFilterNames;
			// LOGGER.debug("Filter Name = " + productFilterNames);
		}

		public Collection<ProductModel> processCategoryAlias() throws FDResourceException {
			Collection<ProductModel> products = new ArrayList<ProductModel>();
			try {
				for (Iterator<CategoryModel> ci = categoryRefs.iterator(); ci.hasNext();) {
					CategoryModel cm = ci.next();
					products.addAll(cm.getPrivateProducts());
				}
				return filterProducts((List) products);
			} catch (FDResourceException fdre) {
				LOGGER.error("caught resource exception in CategoryAlias.processCategory()", fdre);
				throw fdre;
			}
		}

		private List<ProductModel> filterProducts(List<ProductModel> nodes) throws FDResourceException {
			List<ProductFilterI> productFilters = null;

			if (this.productFilterNames != null && !this.productFilterNames.isEmpty()) {
				productFilters = ProductFilterFactory.getInstance().getFilters(this.productFilterNames);
				// LOGGER.debug(" List of Filters = " + productFilters.size());
			} else
				productFilters = Collections.EMPTY_LIST;
			List<ProductModel> nodeList = nodes;
			try {
				for (Iterator<ProductFilterI> fi = productFilters.iterator(); fi.hasNext() && !nodeList.isEmpty();) {
					ProductFilterI prodFilter = fi.next();
					// LOGGER.debug(" about to apply filter:" + prodFilter.getClass().getName());
					nodeList = new ArrayList<ProductModel>(prodFilter.apply(nodeList));
				}
			} catch (FDResourceException fdre) {
				LOGGER.error("caught resource exception atttempting to get products from category", fdre);
				throw fdre;
			}
			return nodeList;
		}

	}

	/**
         * 
         * @return CategoryModel looked up by RATING_HOME
         */
        public CategoryModel getRatingHome() {
            ContentKey key = (ContentKey) getCmsAttributeValue("RATING_HOME");
            
            return key != null ? (CategoryModel) ContentFactory.getInstance().getContentNodeByKey(key) : null;
        }
        
        @SuppressWarnings("unchecked")
        public List<Html> getArticles() {
            return FDAttributeFactory.constructWrapperList(this, "ARTICLES");
        }
        
        @SuppressWarnings("unchecked")
        public List<Html> getTopMedia() {
            return FDAttributeFactory.constructWrapperList(this, "CATEGORY_TOP_MEDIA");
        }

        @SuppressWarnings("unchecked")
        public List<Html> getMiddleMedia() {
            return FDAttributeFactory.constructWrapperList(this, "CATEGORY_MIDDLE_MEDIA");
        }

        @SuppressWarnings("unchecked")
        public List<Html> getBottomMedia() {
            return FDAttributeFactory.constructWrapperList(this, "CATEGORY_BOTTOM_MEDIA");
        }
        
        public Html getPreviewMedia() {
            return FDAttributeFactory.constructHtml(this, "CATEGORY_PREVIEW_MEDIA");
        }
        
        public String getMaterialCharacteristic() {
            return (String) getCmsAttributeValue("MATERIAL_CHARACTERISTIC");
        }
        
        public Html getAlternateContent() {
            return FDAttributeFactory.constructHtml(this, "ALTERNATE_CONTENT");
        }
        
        public Html getCategoryStorageGuideMedia() {
            return FDAttributeFactory.constructHtml(this, "CAT_STORAGE_GUIDE_MEDIA");
        }
        
}
