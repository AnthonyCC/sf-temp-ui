package com.freshdirect.storeapi.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.core.domain.ContentKey;


public class ProductFilterGroupModel extends ContentNodeModelImpl {

    private static final long serialVersionUID = 6029343728537326271L;

    private List<ProductFilterModel> productFilters = new ArrayList<ProductFilterModel>();

    public ProductFilterGroupModel(ContentKey key) {
        super(key);
    }

	public String getName() {
		return (String) getCmsAttributeValue("name");
	}

	public List<ProductFilterModel> getProductFilterModels() {
		ContentNodeModelUtil.refreshModels(this, "productFilters", productFilters, true);
		return new ArrayList<ProductFilterModel>(productFilters);
	}

	public String getType() {
		return (String) getCmsAttributeValue("type");
	}

	public String getAllSelectedLabel() {
		return (String) getCmsAttributeValue("allSelectedLabel");
	}

    public boolean isDisplayOnCategoryListingPage() {
        return getAttribute("displayOnCategoryListingPage", false);
    }

    public boolean isExcludeResidentalSearch() {
        return getAttribute("excludeResidentalSearch", false);
    }

    public boolean isExcludeCorporateSearch() {
        return getAttribute("excludeCorporateSearch", false);
    }

    public boolean isExcludeFoodKickSearch() {
        return getAttribute("excludeFoodKickSearch", false);
    }
}