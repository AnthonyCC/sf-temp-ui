package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.content.nutrition.EnumClaimValue;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.pricing.ProductModelPricingAdapter;

public class ProductFilterMenuDecorator extends GenericFilterDecorator<FilteringSortingItem<ProductModel>> {

	public ProductFilterMenuDecorator(Set<EnumFilteringValue> filters) {
		super(filters);
	}

	public void decorateItem(FilteringSortingItem<ProductModel> item) {

		ProductModelPricingAdapter node = (ProductModelPricingAdapter) item.getNode();

		List<ProductModel> parents = collectParents(node);

		boolean available = node.isFullyAvailable();

		try {

			for (EnumFilteringValue filter : filters) {

				FilteringMenuItem menu = new FilteringMenuItem();
				Set<FilteringMenuItem> menus = new HashSet<FilteringMenuItem>();

				switch (filter) {
				case DEPT: {

					for (ProductModel parent : parents) {
						menu.setName(parent.getDepartment().getFullName());
						menu.setFilteringUrlValue(parent.getDepartment().getContentKey().getId());
						menu.setFilter(filter);
						menus.add(menu);
						menu = new FilteringMenuItem();
					}

					item.putMenuValue(EnumFilteringValue.DEPT, menus);
					break;
				}
				case CAT: {

					for (ProductModel parent : parents) {
						ContentNodeModel parentModel = parent.getParentNode();
						ContentNodeModel found = null;
						while (parentModel != null && !FDContentTypes.STORE.equals(parentModel.getContentKey().getType())) {
							if (parentModel.getParentNode() != null &&
									FDContentTypes.DEPARTMENT.equals(parentModel.getParentNode().getContentKey().getType())) {
								found = parentModel;
								break;
							}
							parentModel = parentModel.getParentNode();
						}
						if (found != null) {
							menu.setName(found.getFullName());
							menu.setFilteringUrlValue(found.getContentKey().getId());
							menu.setFilter(filter);
							menus.add(menu);
							menu = new FilteringMenuItem();
						}
					}

					item.putMenuValue(EnumFilteringValue.CAT, menus);
					break;
				}
				case SUBCAT: {

					for (ProductModel parent : parents) {
						ContentNodeModel parentModel = parent.getParentNode();
						while (FDContentTypes.CATEGORY.equals(parentModel.getParentNode().getContentKey().getType())) {
							menu.setName(parentModel.getFullName());
							menu.setFilteringUrlValue(parentModel.getContentKey().getId());
							menu.setFilter(filter);
							menus.add(menu);
							menu = new FilteringMenuItem();
							parentModel = parentModel.getParentNode();
						}
					}

					item.putMenuValue(EnumFilteringValue.SUBCAT, menus);
					break;
				}
				case BRAND: {
					for (BrandModel brand : node.getBrands()) {
						menu.setName(brand.getFullName());
						menu.setFilteringUrlValue(brand.getContentKey().getId());
						menu.setFilter(filter);
						menus.add(menu);
						menu = new FilteringMenuItem();
					}
					item.putMenuValue(EnumFilteringValue.BRAND, menus);
					break;
				}
				case EXPERT_RATING: {
					if (available && node.getProductRatingEnum().getValue() != 0) {
						if (node.getProductRating() != null) {
							menu.setName((node.getProductRatingEnum().getValue() + 1) / 2 + "");
							menu.setFilteringUrlValue((node.getProductRatingEnum().getValue() + 1) / 2 + "");
							menu.setFilter(filter);
							menus.add(menu);
							item.putMenuValue(EnumFilteringValue.EXPERT_RATING, menus);
						}
					}
					break;
				}
				case ON_SALE: {
					if (available) {
						PriceCalculator pricing = node.getPriceCalculator();
						if (pricing.getDealPercentage() > 0 || pricing.getTieredDealPercentage() > 0 || pricing.getGroupPrice() != 0.0) {
							menu.setName("On Sale");
							menu.setFilteringUrlValue("1");
							menu.setFilter(filter);
							menus.add(menu);
							item.putMenuValue(EnumFilteringValue.ON_SALE, menus);
						}
					}
					break;
				}
				case NEW_OR_BACK: {
					if (available) {
						if (node.isBackInStock()) {
							menu.setName("Back in stock");
							menu.setFilteringUrlValue("2");
							menu.setFilter(filter);
							menus.add(menu);
							item.putMenuValue(EnumFilteringValue.NEW_OR_BACK, menus);
						} else if (node.isNew()) {
							menu.setName("New");
							menu.setFilteringUrlValue("1");
							menu.setFilter(filter);
							menus.add(menu);
							item.putMenuValue(EnumFilteringValue.NEW_OR_BACK, menus);
						}
					}
					break;
				}
				case KOSHER: {
					if (node.getPriceCalculator().getKosherPriority() != 999 && node.getPriceCalculator().getKosherPriority() != 0) {
						menu.setName("Kosher");
						menu.setFilteringUrlValue("1");
						menu.setFilter(filter);
						menus.add(menu);
						item.putMenuValue(EnumFilteringValue.KOSHER, menus);
					}
					break;
				}
				case GLUTEN_FREE: {
					if (available) {
						if (node.getPriceCalculator().getProduct().getClaims() != null) {
							for (EnumClaimValue claim : node.getPriceCalculator().getProduct().getClaims()) {
								if ("FR_GLUT".equals(claim.getCode())) {
									menu.setName("Gluten free");
									menu.setFilteringUrlValue("1");
									menu.setFilter(filter);
									menus.add(menu);
									item.putMenuValue(EnumFilteringValue.GLUTEN_FREE, menus);
								}
							}
						}
					}
				}
				}
			}

		} catch (FDResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FDSkuNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private List<ProductModel> collectParents(ProductModelPricingAdapter node) {

		List<ProductModel> parentNodes = new ArrayList<ProductModel>();

		Collection<ContentKey> parents = node.getParentKeys();
		if (parents != null) {
			for (ContentKey parentKey : parents) {
				ProductModel nodeByKey = ContentFactory.getInstance().getProductByName(
						parentKey.getId(),
						node.getContentKey().getId());
				if (nodeByKey.isDisplayableBasedOnCms() && nodeByKey.isSearchable()) {
					parentNodes.add(nodeByKey);
				}
			}
		}

		return parentNodes;
	}

}
