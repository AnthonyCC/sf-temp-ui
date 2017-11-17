package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.Domain;
import com.freshdirect.mobileapi.controller.data.Image;
import com.freshdirect.mobileapi.controller.data.Image.ImageSizeType;
import com.freshdirect.mobileapi.controller.data.ImageBanner;
import com.freshdirect.mobileapi.controller.data.response.FilterOption;
import com.freshdirect.mobileapi.model.comparator.FilterOptionLabelComparator;

public class Category extends ProductContainer {

    private String name;

    private String id;

    private String topText;
    
    private String bottomText;
    
    private String sectionHeader;
    
    private List<Image> images = new ArrayList<Image>();
    
    private boolean healthWarning = false;
    
    private boolean isBottomLevel = false;
    
    private Map<String, Set<FilterOption>> filterOptions = new LinkedHashMap<String, Set<FilterOption>>();
    
    private List<Category> categories = new ArrayList<Category>();
    
    private List<ImageBanner> heroCarousel = new ArrayList<ImageBanner>();

    private String pageTitle;

    private String seoMetaDescription;

    public static Category wrap(CategoryModel model) {
        return wrapCategory(model, 0);
    }
    
    public static Category wrap(CategoryModel model, long noOfProducts) {
        return wrapCategory(model, noOfProducts);
    }
    
    private static Category wrapCategory(CategoryModel model, long noOfProducts) {
        Category result = new Category();
        result.name = model.getFullName();
        result.id = model.getContentKey().getId();
        result.topText = model.getTopText();
        result.bottomText = model.getBottomText();
        
        List<Image> images = new ArrayList<Image>();
        result.setImages(images);
        
        com.freshdirect.fdstore.content.Image smallImage = model.getCategoryPhoto();
        if(smallImage != null) {
        	Image detailImage = new Image();
        	detailImage.setHeight(smallImage.getHeight());
            detailImage.setWidth(smallImage.getWidth());
            detailImage.setSource(smallImage.getPath());
            detailImage.setType(ImageSizeType.THUMB);
            images.add(detailImage);
        }
        result.setNoOfProducts(noOfProducts);

        List<Domain> domains = model.getWineSideNavFullList();
        TreeSet<FilterOption> wineFilters = new TreeSet<FilterOption>(new FilterOptionLabelComparator());
        for (Domain domain : domains) {
			FilterOption filterOption = new FilterOption();
			filterOption.setId(domain.getContentName());
			filterOption.setLabel(domain.getName());
			wineFilters.add(filterOption);
		}
        result.filterOptions.put("wine", wineFilters);
        
        // health warning
        final boolean hasAlcohol = model.isHavingBeer();
        result.setHealthWarning(hasAlcohol);
        
        List<com.freshdirect.fdstore.content.ImageBanner> heroCarouselList = model.getHeroCarousel();
        List<ImageBanner> heroCarouselImgBannerList = new ArrayList<ImageBanner>();
       
        for(com.freshdirect.fdstore.content.ImageBanner heroCarousel: heroCarouselList){
     	   
 		if (heroCarousel != null) {
 			
 			ImageBanner heroCarouselImgBanner = new ImageBanner();
 			
 			heroCarouselImgBanner.setFlagColor(heroCarousel.getFlagColor());
 			heroCarouselImgBanner.setFlagText(heroCarousel.getFlagText());
 			if (heroCarousel.getImageBannerImage() != null) {
 				heroCarouselImgBanner.setImage(new Image(heroCarousel
 						.getImageBannerImage()).getSource());
 			}
 			heroCarouselImgBanner.setPrice(heroCarousel.getPrice());
 			if (heroCarousel.getTarget() != null
 					&& heroCarousel.getTarget().getContentKey() != null) {
 				heroCarouselImgBanner.setTargetTypeId(heroCarousel
 						.getTarget().getContentKey().getEncoded());
 			}
 			heroCarouselImgBanner.setText(heroCarousel.getDescription());

 			if (heroCarouselImgBanner != null) {
 				heroCarouselImgBannerList.add(heroCarouselImgBanner);
 			}

 		}
 		
 		if(heroCarouselImgBannerList!=null && heroCarouselImgBannerList.size()!= 0) {
 		result.setHeroCarousel(heroCarouselImgBannerList);
 		}
 		
        }

        if (EnumEStoreId.FDX == CmsManager.getInstance().getEStoreEnum()) {
            result.setPageTitle(model.getFdxPageTitle());
            result.setSeoMetaDescription(model.getFdxSEOMetaDescription());
        } else {
            result.setPageTitle(model.getPageTitle());
            result.setSeoMetaDescription(model.getSEOMetaDescription());
        }

        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getTopText() {
        return topText;
    }

    public void setTopText(String topText) {
        this.topText = topText;
    }
    
    public String getBottomText() {
        return bottomText;
    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public Map<String, Set<FilterOption>> getFilterOptions() {
		return filterOptions;
	}

	public void setFilterOptions(Map<String, Set<FilterOption>> tags) {
		this.filterOptions = tags;
	}

	public String getSectionHeader() {
		return sectionHeader;
	}

	public void setSectionHeader(String sectionHeader) {
		this.sectionHeader = sectionHeader;
	}

    public boolean isHealthWarning() {
        return healthWarning;
    }

    public void setHealthWarning(boolean healthWarning) {
        this.healthWarning = healthWarning;
    }
    
    public boolean isBottomLevel() {
    	return isBottomLevel;
    }
    
    public void setBottomLevel(boolean bottomLevel) {
        this.isBottomLevel = bottomLevel;
    }

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	
	public void addCategories(Category cat){
		this.categories.add(cat);
	}

	public List<ImageBanner> getHeroCarousel() {
		return heroCarousel;
	}

	public void setHeroCarousel(List<ImageBanner> heroCarousel) {
		this.heroCarousel = heroCarousel;
	}

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getSeoMetaDescription() {
        return seoMetaDescription;
    }

    public void setSeoMetaDescription(String seoMetaDescription) {
        this.seoMetaDescription = seoMetaDescription;
    }

}
