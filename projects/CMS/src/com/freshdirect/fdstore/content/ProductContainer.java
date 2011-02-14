package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

/**
 * Encapsulates common interface for DepartmentModel and CategoryModel
 * 
 * @author zsombor
 * 
 */
public abstract class ProductContainer extends ContentNodeModelImpl implements HasRedirectUrl, HasTemplateType, YmalSetSource {

    private List<Domain> rating = new ArrayList<Domain>();

    private Map<String,List<Domain>> ratingDomains = new HashMap<String,List<Domain>>();

    
    public abstract List<ProductModel> getFeaturedProducts();

    public abstract List<CategoryModel> getSubcategories();

    public ProductContainer(ContentKey key) {
        super(key);
    }

    public List<String> getFeaturedProductIds() {
        List<ProductModel> featuredProducts = getFeaturedProducts();
        List<String> featuredProdIds = new ArrayList<String>();
        if (featuredProducts != null) {

            for (ProductModel model : featuredProducts) {
                String id = model.getContentKey().getId();
                if (!featuredProdIds.contains(id)) {
                    featuredProdIds.add(id);
                }
            }
        }
        return featuredProdIds;
    }
    
    public ProductModel getFirstAvailableFeaturedProduct() {
        List<ProductModel> featuredProducts = getFeaturedProducts();
        
        for ( ProductModel prod : featuredProducts ) {
        	if ( !prod.isUnavailable() ) {
        		return prod;        		
        	}
        }
		return null;    	
    }

    public List<Domain> getRating() {
        ContentNodeModelUtil.refreshModels(this, "RATING", rating, false, true);

        return new ArrayList<Domain>(rating);
    }
    
    public String getHideUrl() {
        return (String) getCmsAttributeValue("HIDE_URL");
    }
    
    public EnumLayoutType getLayout(EnumLayoutType defValue) {
        return getLayout(defValue != null ? defValue.getId() : 0);
    }

    public EnumLayoutType getLayout(int defaultValue) {
        return EnumLayoutType.getLayoutType(getAttribute("LAYOUT", defaultValue));
    }
    
    public EnumLayoutType getLayout() {
        return getLayout(EnumLayoutType.GENERIC);
    }

    public int getLayoutType(EnumLayoutType defValue) {
        return getLayoutType(defValue != null ? defValue.getId() : 0);
    }

    public int getLayoutType(int defaultValue) {
        return getAttribute("LAYOUT", defaultValue);
    }
    
    public boolean isShowSideNav() {
        return getAttribute("SHOW_SIDE_NAV", true);
    }
    
    public boolean isFavoriteShowPrice() {
        return getAttribute("FAVORITE_ALL_SHOW_PRICE", false);
    }

    public Image getMgrPhoto() {
        return FDAttributeFactory.constructImage(this, "DEPT_MGR");
    }
    
    public Html getDepartmentManagerBio() {
        return FDAttributeFactory.constructHtml(this, "DEPT_MGR_BIO");
    }

    public String getRedirectUrl() {
        return (String) getCmsAttributeValue("REDIRECT_URL");
    }
    
    public Integer getTemplateType() {
        return (Integer) getCmsAttributeValue("TEMPLATE_TYPE");
    }
    
    public boolean isRatingBreakOnSubfolders() {
        return getAttribute("RATING_BREAK_ON_SUBFOLDERS", false);
    }
    
    public boolean isShowRatingRelatedImage() {
        return getAttribute("SHOW_RATING_RELATED_IMAGE", false);
    }
    
    public int getTemplateType(int defaultValue) {
        Integer i = getTemplateType();
        return i != null ? i.intValue() : defaultValue;
    }

    /**
     * 
     * @return CAT_PHOTO image
     */
    public final Image getCategoryPhoto() {
        return FDAttributeFactory.constructImage(this, "CAT_PHOTO");
    }
    
    public final Image getCategoryTitle() {
        return FDAttributeFactory.constructImage(this, "CAT_TITLE");
    }
    
    /**
     * Return the CAT_PHOTO image, if the attribute is null, then it returns an empty image object. 
     * @return
     */
    public final Image getCategoryPhotoNotNull() {
        Image img = getCategoryPhoto();
        if (img != null) {
            return img;
        }
		return new Image();
    }

    /**
     * 
     * @return RATING_GROUP_NAMES
     */
    public String getRatingGroupNames() {
        Object value = getCmsAttributeValue("RATING_GROUP_NAMES");
        return value instanceof String ? (String) value : null;
    }

    public String getRatingGroupLabel(String name) {
        Object value = getCmsAttributeValue(name + "_LABEL");
        return value instanceof String ? (String) value : null;
    }

    /**
     * Return a list of domains named by type.
     * 
     * @param type
     * @return
     */
    public List<Domain> getRatingDomain(String type) {
        List<Domain> list = this.ratingDomains.get(type);
        if (list == null) {
            list = new ArrayList<Domain>();
            this.ratingDomains.put(type, list);
        }
        ContentNodeModelUtil.refreshModels(this, type, list, false);
        
        return list;
    }
    
    public Image getProductDetailImage() {
        return FDAttributeFactory.constructImage(this, "PROD_IMAGE_DETAIL");
    }
    

    public String getSeasonText() {
        return (String) getCmsAttributeValue("SEASON_TEXT");
    }
    
    public boolean isNutritionSort() {
        return getAttribute("NUTRITION_SORT", true);
    }
    
    public final Html getMediaContent() {
        return FDAttributeFactory.constructHtml(this, "MEDIA_CONTENT");
    }
    
    /**
     * Inheritable attribute, defined in Category
     * @return
     */
    public final boolean isHideInactiveSideNav() {
        return getAttribute("HIDE_INACTIVE_SIDE_NAV", false);
    }
    
    
    /**
     * Inheritable attribute, defined in Category
     * @return
     */
    public final boolean getSideNavShowSelf() {
        return getAttribute("SIDENAV_SHOWSELF", false);
    }

    /**
     * Inheritable attribute, defined in Category
     * @return
     */
    public EnumShowChildrenType getSideNavShowChildren() {
        return EnumShowChildrenType.getShowChildrenType(getAttribute("SIDENAV_SHOWCHILDREN", EnumShowChildrenType.ALWAYS_FOLDERS
                .getId()));
    }
    

    public int getColumnNum() {
        return getAttribute("COLUMN_NUM", 1);
    }
    
    public int getColumnSpan() {
        return getAttribute("COLUMN_SPAN", 1);
    }

    /**
     * 
     * @return LIST_AS attributum, 'full' if not specified.
     */
    public final String getListAs() {
        return getAttribute("LIST_AS", "full");
    }
    
    public final String getListAs(String defaultValue) {
        return getAttribute("LIST_AS", defaultValue);
    }
    
    
    // =================================
    // YmalSetSource interface methods :
    // =================================
    
	/**
	 *  The list of YmalSet objects related to this node.
	 */
	private final List<YmalSet> ymalSets = new ArrayList<YmalSet>();

	@Override
	public List<YmalSet> getYmalSets() {
		return YmalSetSourceUtil.getYmalSets( this, ymalSets );
	}
	
	@Override
	public boolean hasActiveYmalSets() {
		return YmalSetSourceUtil.hasActiveYmalSets( this, ymalSets );
	}

	@Override
	public YmalSetSource getParentYmalSetSource() {
		return YmalSetSourceUtil.getParentYmalSetSource( this );
	}
	
        public boolean isHideIphone() {
            return getAttribute("HIDE_IPHONE", false);
        }
	

}
