package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.fdstore.content.BannerModel;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.RecipeTagModel;
import com.freshdirect.mobileapi.controller.data.Image;
import com.freshdirect.mobileapi.model.Category;

public class Idea {
	public static enum ThumbnailType { TabletThumbnail, Banner }
	
	private Image featureImage;
	private String featureText;
	private String primaryText;
	private String secondaryText;
	private String destinationSection;
	private String destinationId;
	private String bannerType;
	private Map<String, Image> otherImages = new LinkedHashMap<String, Image>();
	private boolean prodcutLevel;
	private String redirectURL;

	public static Idea ideaFor(BannerModel model) {
		Idea idea = new Idea();
		idea.setBannerType(model.getContentType());
		if (model.getImage() != null)
			idea.setFeatureImage(new Image(model.getImage()));
		final ContentNodeModel target = model.getLink();
		if (target != null) {
			idea.setFeatureText(target.getFullName());
			idea.setDestinationId(target.getContentName());

			if (target.getContentType() == ContentNodeModel.TYPE_BRAND) {
				idea.setDestinationSection("producer");
			} else if (target.getContentType() == ContentNodeModel.TYPE_RECIPE) {
				idea.setDestinationSection("recipe");
			} else if (target.getContentType() == ContentNodeModel.TYPE_DEPARTMENT) {
				idea.setDestinationSection("department");
			} else {
				idea.setDestinationSection("category");
			}

		}
		return idea;
	}

	public static Idea ideaFor(CategoryModel categoryModel, ThumbnailType thumbnailType) {
		Category category = Category.wrap(categoryModel);
		Idea idea = new Idea();
		idea.setDestinationId(category.getId());
		idea.setDestinationSection("category");
		idea.setFeatureText(categoryModel.getFullName());
		idea.setPrimaryText(categoryModel.getPrimaryText());
		idea.setSecondaryText(categoryModel.getSecondaryText());
		idea.setredirectURL(categoryModel.getRedirectURL());
		
		//APPDEV- 4368:: Need Indicator for Empty Picks List Begin
		/**
		 * Based on categoryModel need to get the product list and return the boolean value
		 */
		if(hasProduct(categoryModel)){
			idea.setProductLevel(true);
		}
		else{
			idea.setProductLevel(false);
		}
		// APPDEV- 4368:: Need Indicator for Empty Picks List End

		Map<String, Image> otherImages = new LinkedHashMap<String, Image>();
		// Grab all related images
		// 1. Category methods
        otherImages.put("CategoryDetailImage", Image.wrap(categoryModel.getCategoryDetailImage()));
        otherImages.put("CategoryLabel", Image.wrap(categoryModel.getCategoryLabel()));
        otherImages.put("CategoryNavBar", Image.wrap(categoryModel.getCategoryNavBar()));
        otherImages.put("CategoryPhoto", Image.wrap(categoryModel.getCategoryPhoto()));
        otherImages.put("CategoryTitle", Image.wrap(categoryModel.getCategoryTitle()));
        otherImages.put("GlobalNavPostNameImage", Image.wrap(categoryModel.getGlobalNavPostNameImage()));
        otherImages.put("MgrPhoto", Image.wrap(categoryModel.getMgrPhoto()));
        otherImages.put("NameImage", Image.wrap(categoryModel.getNameImage()));
        otherImages.put("Photo", Image.wrap(categoryModel.getPhoto()));
        otherImages.put("ProductDetailImage", Image.wrap(categoryModel.getProductDetailImage()));
        otherImages.put("SideNavImage", Image.wrap(categoryModel.getSideNavImage()));
        otherImages.put("TabletThumbnailImage", Image.wrap(categoryModel.getTabletThumbnailImage()));
		
		// 2. banners
        Set<ContentKey> contentKeysByType = CmsManager.getInstance().getContentKeysByType(ContentType.get("Banner"));
        for (ContentKey key : contentKeysByType) {
            BannerModel banner = (BannerModel) ContentFactory.getInstance().getContentNodeByKey(key);
            if (banner.getLink() != null && StringUtils.equals(banner.getLink().getContentName(), categoryModel.getContentName())) {
            	final Image featureImage = Image.wrap(banner.getImage());
            	if (idea.getFeatureImage() != null) // use first banner as featured image
            		idea.setFeatureImage(featureImage);
				otherImages.put("Banner" + banner.getContentName(), featureImage);
            }
        }
        
        if (idea.getFeatureImage() == null || thumbnailType == ThumbnailType.TabletThumbnail) { // no banner found
        	idea.setFeatureImage(Image.wrap(categoryModel.getTabletThumbnailImage()));
        }

        idea.setOtherImages(otherImages);
        
		return idea;
	}
	//APPDEV- 4368:: Need Indicator for Empty Picks List Begin
	private static boolean hasProduct(CategoryModel categoryModel){
		boolean hasProduct = false;
		
		if(!categoryModel.getSubcategories().isEmpty())
		{
			List<CategoryModel> subCategories = categoryModel.getSubcategories();
			for (CategoryModel m1 : subCategories) {
				boolean result = hasProduct(m1);
				if(result){
					return result; 
				}
			}
		}
		if(categoryModel.getProducts().size()>0)
		{
			return isProductAvailable(categoryModel.getProducts());
		}else
			return false;
	}
	
	private static boolean isProductAvailable(List<ProductModel> prodList){
		boolean result = false;
		
		for(ProductModel model:prodList){
			if(!(model.isUnavailable() || model.isDiscontinued())){
				result = true;
				break;
			}
		}
		return result;
	}
	//APPDEV- 4368:: Need Indicator for Empty Picks List End
	public static Idea ideaFor(RecipeTagModel recipeTagModel) {
		Idea idea = new Idea();
		idea.setDestinationId(recipeTagModel.getContentName());
		idea.setDestinationSection("recipe");
		com.freshdirect.fdstore.content.Image tabletImage = recipeTagModel.getTabletImage();
		Image image = new Image(tabletImage.getPath(), tabletImage.getHeight(), tabletImage.getWidth());
		idea.setFeatureImage(image);
		return idea;
	}
	
	public static Idea ideaFor(RecipeTagModel recipeTagModel, boolean isRecipe) {
		Idea idea = new Idea();
		
		//check if this is an individual recipe and return tagID instead of tag name if it is
        if (isRecipe) {
            idea.setDestinationId(recipeTagModel.getTagId());
        } else {
            idea.setDestinationId(recipeTagModel.getContentName());
        }
		idea.setDestinationSection("recipe");
		idea.setFeatureText(recipeTagModel.getFullName());
		com.freshdirect.fdstore.content.Image tabletImage = recipeTagModel.getTabletImage();
		Image image = new Image(tabletImage.getPath(), tabletImage.getHeight(), tabletImage.getWidth());
		idea.setFeatureImage(image);
		return idea;
	}
	
	public static Idea ideaFor(Recipe recipe) {
		Idea idea = new Idea();
		idea.setDestinationId(recipe.getContentName());
		idea.setDestinationSection("recipe");
		com.freshdirect.fdstore.content.Image tabletImage = recipe.getTabletThumbnailImage();
		Image image = new Image(tabletImage.getPath(), tabletImage.getHeight(), tabletImage.getWidth());
		idea.setFeatureImage(image);
		return idea;
	}

	public Image getFeatureImage() {
		return featureImage;
	}

	public void setFeatureImage(Image featureImage) {
		this.featureImage = featureImage;
	}

	public String getFeatureText() {
		return featureText;
	}

	public void setFeatureText(String featureText) {
		this.featureText = featureText;
	}
	
	public String getPrimaryText() {
		return primaryText;
	}
	
	public void setPrimaryText(String primaryText) {
		this.primaryText = primaryText;
	}
	
	public String getRedirectURL() {
		return redirectURL;
	}
	
	public void setredirectURL(String redirectURL) {
		if(redirectURL != null && redirectURL.equals("nm")){
			this.redirectURL = null;
			return;
		}
		this.redirectURL = redirectURL;
	}
	
	public String getSecondaryText() {
		return secondaryText;
	}

	public void setSecondaryText(String secondaryText) {
		this.secondaryText = secondaryText;
	}

	public String getDestinationSection() {
		return destinationSection;
	}

	public void setDestinationSection(String destinationSection) {
		this.destinationSection = destinationSection;
	}

	public String getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(String destinationId) {
		this.destinationId = destinationId;
	}

	public String getBannerType() {
		return bannerType;
	}

	public void setBannerType(String bannerType) {
		this.bannerType = bannerType;
	}

	public Map<String, Image> getOtherImages() {
		return otherImages;
	}

	public void setOtherImages(Map<String, Image> otherImages) {
		this.otherImages = otherImages;
	}
	
	public void setProductLevel(boolean prodcutLevel) {
		this.prodcutLevel = prodcutLevel;
	}
	
	public boolean isProductLevel() {
    	return prodcutLevel;
    }
}
