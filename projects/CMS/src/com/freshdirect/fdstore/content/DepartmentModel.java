package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

public class DepartmentModel extends ProductContainer {

	private static final int MAX_ITEMS_PER_COLUMN_DEFAULT = 15;

	private final List<ProductModel> featuredProductModels = new ArrayList<ProductModel>();

	private final List<CategoryModel> categoryModels = new ArrayList<CategoryModel>();
	
	private final List<CategoryModel> deptNav = new ArrayList<CategoryModel>();
	
	private final List<CategoryModel> featuredCategories = new ArrayList<CategoryModel> ();

    private final List<TileList> tileList = new ArrayList<TileList> ();
    
    private final List<ProductModel> merchantRecommenderProducts = new ArrayList<ProductModel>();
    
    private final List<CategorySectionModel> categorySections = new ArrayList<CategorySectionModel>();
	
    private final List<BannerModel> tabletNoPurchaseSuggestions = new ArrayList<BannerModel>();    
    private final List<ImageBanner> heroCarousel = new ArrayList<ImageBanner>();
	
	public DepartmentModel(ContentKey cKey) {
		super(cKey);
	}


	public Image getTitleImage() {
        return FDAttributeFactory.constructImage(this, "DEPT_TITLE");
	}

	public Image getPhoto() {
        return FDAttributeFactory.constructImage(this, "DEPT_PHOTO");
	}

	public Image getPhotoSmall() {
        return FDAttributeFactory.constructImage(this, "DEPT_PHOTO_SMALL");
	}

	public Image getMgrPhotoNoName() {
        return FDAttributeFactory.constructImage(this, "DEPT_MGR_NONAME");
	}

	public Image getGlobalNavBar() {
        return FDAttributeFactory.constructImage(this, "DEPT_NAVBAR");
	}

	public Image getGlobalNavBarRollover() {
        return FDAttributeFactory.constructImage(this, "DEPT_NAVBAR_ROLLOVER");
	}
	
	public boolean isUseAlternateImages() {
		return getAttribute( "USE_ALTERNATE_IMAGES", false );
	}
	
	public boolean isHidddenInQuickshop() {
	    return getAttribute("HIDE_IN_QUICKSHOP", false);
	}
	
   /**
     * this is a Department level attribute, ASSOC_EDITORIAL
     */
    @SuppressWarnings("unchecked")
    public List<Html> getAssocEditorial() {
        return (List<Html>) FDAttributeFactory.constructWrapperList(this, "ASSOC_EDITORIAL");
    }

	/**
	 * @return List of Html
	 */
    @SuppressWarnings("unchecked")
	public List<Html> getDepartmentBottom() {
        return FDAttributeFactory.constructWrapperList(this, "DEPARTMENT_BOTTOM");
	}
	
	/**
	 * @return List of Html
	 */
    @SuppressWarnings("unchecked")
	public List<Html> getDepartmentMiddleMedia() {
        return FDAttributeFactory.constructWrapperList(this, "DEPARTMENT_MIDDLE_MEDIA");
	}
	

	public int getMaxRowCount() {
		return getAttribute("MAX_ROWCOUNT", 1);
	}

	public List<CategoryModel> getCategories() {
		ContentNodeModelUtil.refreshModels(this, "categories", categoryModels, true);
		return new ArrayList<CategoryModel>(categoryModels);
	}

    public List<CategoryModel> getFeaturedCategories() {
        ContentNodeModelUtil.refreshModels(this, "FEATURED_CATEGORIES", featuredCategories, false);
        return new ArrayList<CategoryModel>(featuredCategories);
    }


    public List<CategoryModel> getDeptNav() {
        ContentNodeModelUtil.refreshModels(this, "DEPT_NAV", deptNav, false);
        return new ArrayList<CategoryModel>(deptNav);
    }	

	@Override
	public List<CategoryModel> getSubcategories() {
	    return getCategories();
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
	
	public Html getDeptStorageGuideMedia() {
	    return FDAttributeFactory.constructHtml(this, "DEPT_STORAGE_GUIDE_MEDIA");
	}


	public static Comparator<DepartmentModel> DepartmentNameComparator = new Comparator<DepartmentModel>() {
		public int compare( DepartmentModel dept1, DepartmentModel dept2 ) {
			String name1 = dept1.getFullName().toLowerCase();
			String name2 = dept2.getFullName().toLowerCase();
			return (name1.compareTo(name2));
		}
	};

    public Set<ContentKey> getAllChildProductKeys() {
    	Set<ContentKey> keys = new HashSet<ContentKey>();
    	for (CategoryModel c : getCategories())
    		keys.addAll(c.getAllChildProductKeys());
    	return keys;
    }

	public String getTemplatePath() {
		return getAttribute("DEPARTMENT_TEMPLATE_PATH", "");
	}
	
	public String getAltTemplatePath() {
		return getAttribute("DEPARTMENT_ALT_TEMPLATE_PATH", "");
	}

	public List<TileList> getTileList() {
		ContentNodeModelUtil.refreshModels(this, "tile_list", tileList, false);
		return Collections.unmodifiableList(tileList);
	}

	public Image getTitleBar() {
        return FDAttributeFactory.constructImage(this, "titleBar");
	}

	public Html getDepartmentBanner() {
		return FDAttributeFactory.constructHtml(this, "departmentBanner");
	}


	public String getFeaturedRecommenderTitle() {
		return getAttribute("featuredRecommenderTitle", "");
	}
	
	public boolean isFeaturedRecommenderRandomizeProducts() {
	    return getAttribute("featuredRecommenderRandomizeProducts", false);
	}

	public String getFeaturedRecommenderSiteFeature() {
		return getAttribute("featuredRecommenderSiteFeature", "");
	}

	public CategoryModel getFeaturedRecommenderSourceCategory() {
		return getSingleRelationshipNode("featuredRecommenderSourceCategory");
	}
	
	public String getMerchantRecommenderTitle() {
		return getAttribute("merchantRecommenderTitle", "");
	}

	public List<ProductModel> getMerchantRecommenderProducts() {
		ContentNodeModelUtil.refreshModels(this, "merchantRecommenderProducts", merchantRecommenderProducts, false);
		return new ArrayList<ProductModel>(merchantRecommenderProducts);
	}

	public boolean isMerchantRecommenderRandomizeProducts() {
	    return getAttribute("merchantRecommenderRandomizeProducts", false);
	}

	@Override
	public List<ProductModel> getStaticProducts() {
		return Collections.emptyList();
	}

	public Image getHeroImage() {
        return FDAttributeFactory.constructImage(this, "heroImage");
	}

	public Html getSeasonalMedia() {
        return FDAttributeFactory.constructHtml(this, "seasonalMedia");
	}

	public boolean isTopLevelCategory(){
		return false;
	}

    public List<CategorySectionModel> getCategorySections() {
        ContentNodeModelUtil.refreshModels(this, "categorySections", categorySections, false);
        return new ArrayList<CategorySectionModel>(categorySections);
    }
    
    public EnumBrowseEditorialLocation getBannerLocation(String defaultValue) {
		return EnumBrowseEditorialLocation.valueOf(getAttribute("bannerLocation", defaultValue));
	}
    
    public EnumBrowseEditorialLocation getCarouselPosition(String defaultValue) {
		return EnumBrowseEditorialLocation.valueOf(getAttribute("carouselPosition", defaultValue));
	}
    
    public EnumBrowseCarouselRatio getCarouselRatio(String defaultValue) {
		return EnumBrowseCarouselRatio.valueOf(getAttribute("carouselRatio", defaultValue));
	}

	public String getRegularCategoriesNavHeader() {
		return getAttribute("regularCategoriesNavHeader", "");
	}

	public String getPreferenceCategoriesNavHeader() {
		return getAttribute("preferenceCategoriesNavHeader", "");
	}

	public String getRegularCategoriesLeftNavBoxHeader() {
		return getAttribute("regularCategoriesLeftNavBoxHeader", "");
	}

	public String getPreferenceCategoriesLeftNavBoxHeader() {
		return getAttribute("preferenceCategoriesLeftNavBoxHeader", "");
	}
	
	public int getMaxItemsPerColumn() {
		return getAttribute("maxItemsPerColumn", MAX_ITEMS_PER_COLUMN_DEFAULT);
	}

	/**
	 * Show Category Section Headers
	 * 
	 * APPDEV-3773
	 */
	public boolean isShowCatSectionHeaders() {
		return getAttribute( "showCatSectionHeaders", false );
	}
	
	public boolean isHideGlobalNavDropDown() {
		return getAttribute("hideGlobalNavDropDown", false);
	}
	
    public List<BannerModel> getTabletNoPurchaseSuggestions() {
        ContentNodeModelUtil.refreshModels(this, "tabletNoPurchaseSuggestions", tabletNoPurchaseSuggestions, false);
        return new ArrayList<BannerModel>(tabletNoPurchaseSuggestions);
    }

	public Image getTabletIcon() {
        return FDAttributeFactory.constructImage(this, "tabletIcon");
	}
	
	public BannerModel getTabletHeaderBanner() {
		return FDAttributeFactory.lookup(this, "tabletHeaderBanner", null);
	}



	public List<ImageBanner> getHeroCarousel() {
		ContentNodeModelUtil.refreshModels(this, "heroCarousel", heroCarousel, false);
		return new ArrayList<ImageBanner>(heroCarousel);
	}



	
	
}
