package com.freshdirect.webapp.ajax.reorder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.content.nutrition.EnumClaimValue;
import com.freshdirect.content.nutrition.EnumOrganicValue;
import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.GenericFilterDecorator;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.BrandModel;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.storeapi.content.EnumQuickShopFilteringValue;
import com.freshdirect.storeapi.content.EnumSearchFilteringValue;
import com.freshdirect.storeapi.content.FilteringMenuItem;
import com.freshdirect.storeapi.content.FilteringSortingItem;
import com.freshdirect.storeapi.content.FilteringValue;
import com.freshdirect.storeapi.content.PriceCalculator;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;

public class QuickShopFilterValueDecorator extends GenericFilterDecorator<FilteringSortingItem<QuickShopLineItemWrapper>> {

	private static final Logger LOG = LoggerFactory.getInstance(QuickShopFilterValueDecorator.class);

	public QuickShopFilterValueDecorator(Set<FilteringValue> filters) {
		super(filters);
	}

	@Override
	public void decorateItem(FilteringSortingItem<QuickShopLineItemWrapper> item) {

		QuickShopLineItemWrapper node = item.getNode();
        ProductModel product = node.getProduct();

		List<ProductModel> parents = collectParents(product);

		for (FilteringValue filterSource : filters) {

			if (!(filterSource instanceof EnumQuickShopFilteringValue)) {
				throw new IllegalArgumentException("Only EnumQuickShopFilteringValue allowed here.");
			}

			EnumQuickShopFilteringValue filter = (EnumQuickShopFilteringValue) filterSource;

			FilteringMenuItem menu = new FilteringMenuItem();
			Set<FilteringMenuItem> menus = new HashSet<FilteringMenuItem>();

			try {
				switcher: switch (filter) {

				case TIME_FRAME_ALL: {

					menu.setName(filter.getDisplayName());
					menu.setFilteringUrlValue(filter.getName());
					menu.setFilter(filter);
					menus.add(menu);

                        item.putFilteringValue(filter, filter.getName());
                        item.putMenuValue(filter, menus);
					break;
				}

				case TIME_FRAME_LAST: {

					if (node.isInLastOrder()) {

						menu.setName(filter.getDisplayName());
						menu.setFilteringUrlValue(filter.getName());
						menu.setFilter(filter);
						menus.add(menu);

                            item.putFilteringValue(filter, filter.getName());
                            item.putMenuValue(filter, menus);

					}

					break;
				}

				case TIME_FRAME_30: {

					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DAY_OF_MONTH, -30);

					if (cal.getTime().before(node.getDeliveryDate())) {

						menu.setName(filter.getDisplayName());
						menu.setFilteringUrlValue(filter.getName());
						menu.setFilter(filter);
						menus.add(menu);

                            item.putFilteringValue(filter, filter.getName());
                            item.putMenuValue(filter, menus);

					}

					break;
				}

				case TIME_FRAME_60: {

					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DAY_OF_MONTH, -60);

					if (cal.getTime().before(node.getDeliveryDate())) {

						menu.setName(filter.getDisplayName());
						menu.setFilteringUrlValue(filter.getName());
						menu.setFilter(filter);
						menus.add(menu);

                            item.putFilteringValue(filter, filter.getName());
                            item.putMenuValue(filter, menus);

					}

					break;
				}

				case TIME_FRAME_90: {

					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DAY_OF_MONTH, -90);

					if (cal.getTime().before(node.getDeliveryDate())) {

						menu.setName(filter.getDisplayName());
						menu.setFilteringUrlValue(filter.getName());
						menu.setFilter(filter);
						menus.add(menu);

                            item.putFilteringValue(filter, filter.getName());
                            item.putMenuValue(filter, menus);

					}

					break;
				}

				case TIME_FRAME_180: {

					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DAY_OF_MONTH, -180);

					if (cal.getTime().before(node.getDeliveryDate())) {

						menu.setName(filter.getDisplayName());
						menu.setFilteringUrlValue(filter.getName());
						menu.setFilter(filter);
						menus.add(menu);

                            item.putFilteringValue(filter, filter.getName());
                            item.putMenuValue(filter, menus);

					}

					break;
				}

				case ORDERS_BY_DATE: {
					Date deliveryDate = node.getDeliveryDate();
					menu.setName(DateUtil.removeDotAfterMay(DateUtil.formatDateWithMonDYear(deliveryDate)));
					menu.setYear(DateUtil.formatDateWithYear(deliveryDate));
					menu.setDeliveryDate(deliveryDate);
					menu.setFilteringUrlValue(node.getOrderId());
					menu.setFilter(filter);
					menu.setInfo(node.getOrderStatus());
					menus.add(menu);

                        item.putFilteringValue(filter, node.getOrderId());
                        item.putMenuValue(filter, menus);

					break;
				}

				case DEPT: {

				    final CategoryModel primaryHome = product.getPrimaryHome();
				    if (primaryHome == null)
				        break;

				    final DepartmentModel department = primaryHome.getDepartment();
				    if (department == null)
				        break;

				    // prepare the menus
					menu.setName(department.getFullName());

					menu.setFilteringUrlValue(department.getContentKey().getId());
					menu.setFilter(filter);
					menus.add(menu);

                        item.putFilteringValue(filter, department.getContentKey().getId());
                        item.putMenuValue(filter, menus);

					break;
				}

				case GLUTEN_FREE: {

					item.putFilteringValue(EnumSearchFilteringValue.GLUTEN_FREE, "0");
					if (product.getPriceCalculator().getProduct() != null && product.getPriceCalculator().getProduct().getClaims() != null) {
						for (EnumClaimValue claim : product.getPriceCalculator().getProduct().getClaims()) {
							if ("FR_GLUT".equals(claim.getCode())) {
                                    item.putFilteringValue(filter, filter.getName());

								menu.setName("Gluten free");
                                    menu.setFilteringUrlValue(filter.getName());
								menu.setFilter(filter);
								menus.add(menu);
                                    item.putMenuValue(filter, menus);
							}
						}
					}

					break;
				}

				case KOSHER: {

					if (product.getPriceCalculator().getKosherPriority() != 999 && product.getPriceCalculator().getKosherPriority() != 0) {
                            item.putFilteringValue(filter, filter.getName());

						menu.setName("Kosher");
                            menu.setFilteringUrlValue(filter.getName());
						menu.setFilter(filter);
						menus.add(menu);
                            item.putMenuValue(filter, menus);
					}

					break;
				}

				case ON_SALE: {

					PriceCalculator pricing = product.getPriceCalculator();
					if (pricing.getDealPercentage() > 0 || pricing.getTieredDealPercentage() > 0 || pricing.getGroupPrice() != 0.0) {
                            item.putFilteringValue(filter, filter.getName());

						// prepare the menus
						menu.setName("On Sale");
                            menu.setFilteringUrlValue(filter.getName());
						menu.setFilter(filter);
						menus.add(menu);
                            item.putMenuValue(filter, menus);
					}

					break;
				}

				case LOCAL: {

					for (ProductModel parent : parents) {
					    final DepartmentModel department = parent.getDepartment();
					    if (department == null)
					        continue;

						if ("local".equalsIgnoreCase(department.getContentKey().getId())) {

                                menu.setName(filter.getDisplayName());
                                menu.setFilteringUrlValue(filter.getName());
							menu.setFilter(filter);
							menus.add(menu);

                                item.putMenuValue(filter, menus);
                                item.putFilteringValue(filter, filter.getName());
						}
					}

					break;
				}

				case ORGANIC: {

					for (ProductModel parent : parents) {
						final DepartmentModel department = parent.getDepartment();
						if (department == null)
						    continue;

						if ("orgnat".equalsIgnoreCase(department.getContentKey().getId())) {

                                menu.setName(filter.getDisplayName());
                                menu.setFilteringUrlValue(filter.getName());
							menu.setFilter(filter);
							menus.add(menu);

                                item.putMenuValue(filter, menus);
                                item.putFilteringValue(filter, filter.getName());

							break switcher;
						}
					}

					// check erps props
					Set<EnumOrganicValue> commonOrgs = product.getCommonNutritionInfo(ErpNutritionInfoType.ORGANIC);
					if (!commonOrgs.isEmpty()) {
						for (Iterator<EnumOrganicValue> ic = commonOrgs.iterator(); ic.hasNext();) {
							EnumOrganicValue claim = ic.next();
							if (!EnumOrganicValue.getValueForCode("NONE").equals(claim)) {

                                    menu.setName(filter.getDisplayName());
                                    menu.setFilteringUrlValue(filter.getName());
								menu.setFilter(filter);
								menus.add(menu);

                                    item.putMenuValue(filter, menus);
                                    item.putFilteringValue(filter, filter.getName());

								break switcher;
							}
						}
					}

					break;
				}

				case STARTER_LISTS: {

					if (item.getNode().getStarterList() != null) {

						menu.setName(item.getNode().getStarterList().getFullName());
						menu.setFilteringUrlValue(item.getNode().getStarterList().getContentKey().getId());
						menu.setFilter(filter);
						menus.add(menu);

                            item.putMenuValue(filter, menus);
                            item.putFilteringValue(filter, item.getNode().getStarterList().getContentKey().getId());
					}

					break;
				}

				case YOUR_LISTS: {

					if (item.getNode().getCclId() != null) {

						menu.setName(item.getNode().getListName());
						menu.setFilteringUrlValue(item.getNode().getCclId());
						menu.setFilter(filter);
						if (node.getRecipeId() != null) {
							menu.setInfo("RECIPE");
						}
						menus.add(menu);

                            item.putMenuValue(filter, menus);
                            item.putFilteringValue(filter, item.getNode().getCclId());
					}

					break;
				}

                    case BRAND: {
                        for (BrandModel brand : product.getBrands()) {
                            menu.setName(brand.getFullName());
                            menu.setFilteringUrlValue(brand.getContentKey().getId());
                            menu.setFilter(filter);
                            menus.add(menu);
                        }
                        item.putMenuValue(filter, menus);
                        item.putFilteringValue(filter, filter.getName());
                        break;
                    }
				}
			} catch (FDResourceException e) {
				LOG.error("Error while decorating product", e);
			} catch (FDSkuNotFoundException e) {
				LOG.error("Error while decorating product", e);
			}
		}

	}

    private List<ProductModel> collectParents(ProductModel node) {
		List<ProductModel> parentNodes = new ArrayList<ProductModel>();

		Collection<ContentKey> parents = ContentFactory.getInstance().getParentKeys(node.getContentKey());
		if (parents != null) {
			for (ContentKey parentKey : parents) {
				ProductModel nodeByKey = ContentFactory.getInstance().getProductByName(parentKey.getId(), node.getContentKey().getId());
				if (nodeByKey != null && nodeByKey.isDisplayableBasedOnCms() && nodeByKey.isSearchable()) {
					parentNodes.add(nodeByKey);
				}
			}
		}

		return parentNodes;
	}
}
