package com.freshdirect.storeapi.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.attributes.FDAttributeFactory;

public class GlobalMenuSectionModel extends ContentNodeModelImpl {

	private List<CategoryModel> subCategoryItems = new ArrayList<CategoryModel>();

    public GlobalMenuSectionModel(ContentKey key) {
        super(key);
    }

	public ProductContainer getLinkedProductContainer() {
		Object value = getCmsAttributeValue("linkedProductContainer");
		return value instanceof ContentKey ? (ProductContainer) ContentFactory.getInstance().getContentNodeByKey((ContentKey) value) : null;
	}


	public List<CategoryModel> getSubCategoryItems() {
		ContentNodeModelUtil.refreshModels(this, "subCategoryItems", subCategoryItems, false, true);
		return new ArrayList<CategoryModel>(subCategoryItems);
	}

	public List<CategoryModel> getAllSubCategoryItems() {

		if (getShowAllSubCategories()){
			return getLinkedProductContainer().getSubcategories();

		} else {

			return getSubCategoryItems();
		}
	}


	@Override
    public Html getEditorial() {
		return FDAttributeFactory.constructHtml(this, "editorial");
	}

	public String getTitleLable() {
		ProductContainer productContainer = getLinkedProductContainer();
		if (productContainer != null)
			return productContainer.getGlobalMenuTitleLabel();
		else
			return "Unknown title label";
	}

	public String getLinkLable() {
		ProductContainer productContainer = getLinkedProductContainer();
		if (productContainer != null)
			return productContainer.getGlobalMenuLinkLabel();
		else
			return "Unknown link label";
	}

	public boolean getShowAllSubCategories() {
		return getAttribute("SHOW_ALL_SUB_CATEGORIES", false);
	}
}
