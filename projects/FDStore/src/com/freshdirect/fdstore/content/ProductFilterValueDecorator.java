package com.freshdirect.fdstore.content;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.content.nutrition.EnumClaimValue;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.customerrating.CustomerRatingsContext;
import com.freshdirect.fdstore.content.customerrating.CustomerRatingsDTO;
import com.freshdirect.fdstore.pricing.ProductModelPricingAdapter;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ProductFilterValueDecorator extends GenericFilterDecorator<FilteringSortingItem<ProductModel>> {
	
	private static final Logger LOG = LoggerFactory.getInstance( ProductFilterValueDecorator.class );

	public ProductFilterValueDecorator(Set<EnumFilteringValue> filters) {
		super(filters);
	}

	public void decorateItem(FilteringSortingItem<ProductModel> item) {

		ProductModelPricingAdapter node = (ProductModelPricingAdapter) item.getNode();

		List<ProductModel> parents = collectParents(node);
		
		boolean available = node.isFullyAvailable();
		try {
			for (EnumFilteringValue filter : filters) {
				switch (filter) {
					case DEPT: {
						Set<String> parentIds = new HashSet<String>();
						for (ProductModel parent : parents) {
							parentIds.add(parent.getDepartment().getContentKey().getId());
						}
						item.putFilteringValue(EnumFilteringValue.DEPT, parentIds);
						break;
					}
					case CAT: {
						Set<String> parentIds = new HashSet<String>();
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
							if (found != null)
								parentIds.add(found.getContentKey().getId());
						}
	
						item.putFilteringValue(EnumFilteringValue.CAT, parentIds);
						break;
					}
					case SUBCAT: {
						Set<String> parentIds = new HashSet<String>();
						for (ProductModel parent : parents) {
							ContentNodeModel parentModel = parent.getParentNode();
							ContentNodeModel found = null;
							while (parentModel != null && !FDContentTypes.STORE.equals(parentModel.getContentKey().getType())) {
								if (parentModel.getParentNode() != null &&
										FDContentTypes.CATEGORY.equals(parentModel.getParentNode().getContentKey().getType())) {
									if (parentModel.getParentNode().getParentNode() != null &&
											FDContentTypes.DEPARTMENT.equals(parentModel.getParentNode().getParentNode().getContentKey().getType())) {
										found = parentModel;
										break;
									}
								}
								parentModel = parentModel.getParentNode();
							}
							if (found != null)
								parentIds.add(found.getContentKey().getId());
						}
						item.putFilteringValue(EnumFilteringValue.SUBCAT, parentIds);
						break;
					}
					case BRAND: {
						Set<String> bk = new HashSet<String>();
						for (BrandModel brand : node.getBrands()) {
							bk.add(brand.getContentKey().getId());
						}
						item.putFilteringValue(EnumFilteringValue.BRAND, bk);
						break;
					}
					case EXPERT_RATING: {
						if (available) {
							item.putFilteringValue(EnumFilteringValue.EXPERT_RATING,(node.getProductRatingEnum().getValue() + 1) / 2 + "");
						}
						break;
					}
					case CUSTOMER_RATING: {
						if (available) {
							CustomerRatingsDTO customerRatingsDTO = CustomerRatingsContext.getInstance().getCustomerRatingByProductId(node.getContentKey().getId());
							if (customerRatingsDTO != null && (node.getProductRating() == null || node.getProductRatingEnum().getValue() == 0)) {
								
								BigDecimal averageRating = customerRatingsDTO.getAverageOverallRating();
								int starValue = (int) Math.ceil(averageRating.doubleValue());
								
								item.putFilteringValue(EnumFilteringValue.CUSTOMER_RATING,starValue + "");
							}
						}
						break;
					}
					case ON_SALE: {
						if (available) {
							PriceCalculator pricing = node.getPriceCalculator();
							if (pricing.getDealPercentage() > 0 || pricing.getTieredDealPercentage() > 0 || pricing.getGroupPrice() != 0.0) {
								item.putFilteringValue(EnumFilteringValue.ON_SALE, "1");
							} else {
								item.putFilteringValue(EnumFilteringValue.ON_SALE, "0");
							}
						}
						break;
					}
					case NEW_OR_BACK: {
						if (available) {
							if (node.isBackInStock()) {
								item.putFilteringValue(EnumFilteringValue.NEW_OR_BACK, "2");
							} else if (node.isNew()) {
								item.putFilteringValue(EnumFilteringValue.NEW_OR_BACK, "1");
							} else {
								item.putFilteringValue(EnumFilteringValue.NEW_OR_BACK, "0");
							}
						}
						break;
					}
					case KOSHER: {
						if (available) {
							if (node.getPriceCalculator().getKosherPriority() != 999 && node.getPriceCalculator().getKosherPriority() != 0) {
								item.putFilteringValue(EnumFilteringValue.KOSHER, "1");
							}
						}
						break;
					}
					case GLUTEN_FREE: {
						if (available) {
							item.putFilteringValue(EnumFilteringValue.GLUTEN_FREE, "0");
							if (node.getPriceCalculator().getProduct().getClaims() != null) {
								for (EnumClaimValue claim : node.getPriceCalculator().getProduct().getClaims()) {
									if ("FR_GLUT".equals(claim.getCode())) {
										item.putFilteringValue(EnumFilteringValue.GLUTEN_FREE, "1");
									}
								}
							}
						}
					}
				}
			}
		} catch (FDResourceException e) {
			LOG.error( "Error while decorating product", e );
		} catch (FDSkuNotFoundException e) {
			LOG.error( "Error while decorating product", e );
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
