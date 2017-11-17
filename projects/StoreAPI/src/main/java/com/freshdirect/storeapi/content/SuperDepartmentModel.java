package com.freshdirect.storeapi.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.attributes.FDAttributeFactory;

public class SuperDepartmentModel extends ContentNodeModelImpl {

	private List<DepartmentModel> departments = new ArrayList<DepartmentModel>();
    private final List<ProductModel> sdMerchantRecommenderProducts = new ArrayList<ProductModel>();

    public SuperDepartmentModel(ContentKey cKey) {
        super(cKey);
    }

	public String getName() {
		return (String) getCmsAttributeValue("name");
	}

	@Override
    public String getFullName() {
		return (String) getCmsAttributeValue("name");
	}

	public String getBrowseName() {
		return getAttribute("browseName", "");
	}

	public String getSdFeaturedRecommenderTitle() {
		return getAttribute("sdFeaturedRecommenderTitle", "");
	}

	public boolean isSdFeaturedRecommenderRandomizeProducts() {
	    return getAttribute("sdFeaturedRecommenderRandomizeProducts", false);
	}

	public String getSdFeaturedRecommenderSiteFeature() {
		return getAttribute("sdFeaturedRecommenderSiteFeature", "");
	}

	public boolean isSdMerchantRecommenderRandomizeProducts() {
	    return getAttribute("sdMerchantRecommenderRandomizeProducts", false);
	}
	public String getSdMerchantRecommenderTitle() {
		return getAttribute("sdMerchantRecommenderTitle", "");
	}

	public List<DepartmentModel> getDepartments() {
		ContentNodeModelUtil.refreshModels(this, "departments", departments, false);
		return new ArrayList<DepartmentModel>(departments);
	}

	public Image getTitleBar() {
        return FDAttributeFactory.constructImage(this, "titleBar");
	}

	public Html getSuperDepartmentBanner() {
		return FDAttributeFactory.constructHtml(this, "superDepartmentBanner");
	}

	public CategoryModel getSdFeaturedRecommenderSourceCategory() {
		return getSingleRelationshipNode("sdFeaturedRecommenderSourceCategory");
	}

	public List<ProductModel> getSdMerchantRecommenderProducts() {
		ContentNodeModelUtil.refreshModels(this, "sdMerchantRecommenderProducts", sdMerchantRecommenderProducts, false);
		return new ArrayList<ProductModel>(sdMerchantRecommenderProducts);
	}

	public EnumBrowseEditorialLocation getBannerLocation(String defaultValue) {
		return EnumBrowseEditorialLocation.valueOf(getAttribute("bannerLocation", defaultValue));
	}

	public Html getBrowseMiddleMedia() {
		return FDAttributeFactory.constructHtml(this, "middleMedia");
	}

	public EnumBrowseEditorialLocation getCarouselPosition(String defaultValue) {
		return EnumBrowseEditorialLocation.valueOf(getAttribute("carouselPosition", defaultValue));
	}

    public EnumBrowseCarouselRatio getCarouselRatio(String defaultValue) {
		return EnumBrowseCarouselRatio.valueOf(getAttribute("carouselRatio", defaultValue));
	}

    public boolean isHideGlobalNavDropDown() {
		return getAttribute("hideGlobalNavDropDown", false);
	}

    public String getPageTitle() {
		return getAttribute("PAGE_TITLE", "");
	}

    public String getFdxPageTitle() {
        return getAttribute("PAGE_TITLE_FDX", "");
    }

    public String getSEOMetaDescription() {
		return getAttribute("SEO_META_DESC", "");
	}

    public String getFdxSEOMetaDescription() {
        return getAttribute("SEO_META_DESC_FDX", "");
    }
}
