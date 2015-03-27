package com.freshdirect.webapp.ajax.quickshop;


import static com.freshdirect.fdstore.content.EnumQuickShopFilteringValue.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.content.nutrition.EnumClaimValue;
import com.freshdirect.content.nutrition.EnumOrganicValue;
import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.EnumQuickShopFilteringValue;
import com.freshdirect.fdstore.content.EnumSearchFilteringValue;
import com.freshdirect.fdstore.content.FilteringMenuItem;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.FilteringValue;
import com.freshdirect.fdstore.content.GenericFilterDecorator;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.pricing.ProductModelPricingAdapter;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;

/**
 * Deprecated with Quickshop 2.2 version. Replaced with {@link com.freshdirect.webapp.ajax.reorder.QuickShopFilterValueDecorator}
 */
@Deprecated
public class QuickShopFilterValueDecorator extends GenericFilterDecorator<FilteringSortingItem<QuickShopLineItemWrapper>> {

	private static final Logger LOG = LoggerFactory.getInstance(QuickShopFilterValueDecorator.class);

	public QuickShopFilterValueDecorator(Set<FilteringValue> filters) {
		super(filters);
	}

	@Override
	public void decorateItem(FilteringSortingItem<QuickShopLineItemWrapper> item) {

		QuickShopLineItemWrapper node = item.getNode();
		ProductModelPricingAdapter product = node.getProduct();
		
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

					item.putFilteringValue(TIME_FRAME_ALL, filter.getName());
					item.putMenuValue(TIME_FRAME_ALL, menus);
					break;
				}

				case TIME_FRAME_LAST: {

					if (node.isInLastOrder()) {

						menu.setName(filter.getDisplayName());
						menu.setFilteringUrlValue(filter.getName());
						menu.setFilter(filter);
						menus.add(menu);

						item.putFilteringValue(TIME_FRAME_LAST, filter.getName());
						item.putMenuValue(TIME_FRAME_LAST, menus);

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

						item.putFilteringValue(TIME_FRAME_30, filter.getName());
						item.putMenuValue(TIME_FRAME_30, menus);

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

						item.putFilteringValue(TIME_FRAME_60, filter.getName());
						item.putMenuValue(TIME_FRAME_60, menus);

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

						item.putFilteringValue(TIME_FRAME_90, filter.getName());
						item.putMenuValue(TIME_FRAME_90, menus);

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

						item.putFilteringValue(TIME_FRAME_180, filter.getName());
						item.putMenuValue(TIME_FRAME_180, menus);

					}

					break;
				}

				case ORDERS_BY_DATE: {

					menu.setName(DateUtil.formatDateWithDayOfWeek(node.getDeliveryDate()));
					menu.setFilteringUrlValue(node.getOrderId());
					menu.setFilter(filter);
					menu.setInfo(node.getOrderStatus());
					menus.add(menu);

					item.putFilteringValue(ORDERS_BY_DATE, node.getOrderId());
					item.putMenuValue(ORDERS_BY_DATE, menus);

					break;
				}

				case DEPT: {

					// prepare the menus
					menu.setName(product.getPrimaryHome().getDepartment().getFullName());	
					

					menu.setFilteringUrlValue(product.getPrimaryHome().getDepartment().getContentKey().getId());
					menu.setFilter(filter);
					menus.add(menu);

					item.putFilteringValue(DEPT, product.getPrimaryHome().getDepartment().getContentKey().getId());
					item.putMenuValue(DEPT, menus);
					
					break;
				}

				case GLUTEN_FREE: {

					item.putFilteringValue(EnumSearchFilteringValue.GLUTEN_FREE, "0");
					if (product.getPriceCalculator().getProduct()!=null && product.getPriceCalculator().getProduct().getClaims() != null) {
						for (EnumClaimValue claim : product.getPriceCalculator().getProduct().getClaims()) {
							if ("FR_GLUT".equals(claim.getCode())) {
								item.putFilteringValue(GLUTEN_FREE, GLUTEN_FREE.getName());

								menu.setName("Gluten free");
								menu.setFilteringUrlValue(GLUTEN_FREE.getName());
								menu.setFilter(filter);
								menus.add(menu);
								item.putMenuValue(GLUTEN_FREE, menus);
							}
						}
					}
					
					break;
				}
				
				case KOSHER: {
					
					if (product.getPriceCalculator().getKosherPriority() != 999 && product.getPriceCalculator().getKosherPriority() != 0) {
						item.putFilteringValue(KOSHER, KOSHER.getName());	
						

						
						menu.setName("Kosher");
						menu.setFilteringUrlValue(KOSHER.getName());
						menu.setFilter(filter);
						menus.add(menu);
						item.putMenuValue(KOSHER, menus);
					}
					
					break;
				}
				
				case ON_SALE: {
					
					PriceCalculator pricing = product.getPriceCalculator();
					if (pricing.getDealPercentage() > 0 || pricing.getTieredDealPercentage() > 0 || pricing.getGroupPrice() != 0.0) {
						item.putFilteringValue(ON_SALE, ON_SALE.getName());
						
						//prepare the menus
						menu.setName("On Sale");
						menu.setFilteringUrlValue(ON_SALE.getName());
						menu.setFilter(filter);
						menus.add(menu);
						item.putMenuValue(ON_SALE, menus);
					}
					
					break;
				}
				
				case LOCAL: {
					
					for(ProductModel parent: parents){
						if("local".equalsIgnoreCase(parent.getDepartment().getContentKey().getId())){
							
							menu.setName(LOCAL.getDisplayName());
							menu.setFilteringUrlValue(LOCAL.getName());
							menu.setFilter(filter);
							menus.add(menu);
							
							item.putMenuValue(LOCAL, menus);
							item.putFilteringValue(LOCAL, LOCAL.getName());
						}
					}
					
					break;
				}
				
				case ORGANIC: {
					
					for(ProductModel parent: parents){
						if("orgnat".equalsIgnoreCase(parent.getDepartment().getContentKey().getId())){
							
							menu.setName(ORGANIC.getDisplayName());
							menu.setFilteringUrlValue(ORGANIC.getName());
							menu.setFilter(filter);
							menus.add(menu);
							
							item.putMenuValue(ORGANIC, menus);
							item.putFilteringValue(ORGANIC, ORGANIC.getName());
							
							break switcher;
						}
					}
					
					//check erps props
					Set<EnumOrganicValue> commonOrgs = product.getCommonNutritionInfo(ErpNutritionInfoType.ORGANIC);
					if (!commonOrgs.isEmpty()) {
						for (Iterator<EnumOrganicValue> ic = commonOrgs.iterator(); ic.hasNext();) {
							EnumOrganicValue claim = ic.next();
							if(!EnumOrganicValue.getValueForCode("NONE").equals(claim)){
								
								menu.setName(ORGANIC.getDisplayName());
								menu.setFilteringUrlValue(ORGANIC.getName());
								menu.setFilter(filter);
								menus.add(menu);
								
								item.putMenuValue(ORGANIC, menus);
								item.putFilteringValue(ORGANIC, ORGANIC.getName());
								
								break switcher;
							}
						}
					}
					
					break;
				}

				case STARTER_LISTS: {
					
					if(item.getNode().getStarterList()!=null){
						
						menu.setName(item.getNode().getStarterList().getFullName());
						menu.setFilteringUrlValue(item.getNode().getStarterList().getContentKey().getId());
						menu.setFilter(filter);
						menus.add(menu);
						
						item.putMenuValue(STARTER_LISTS, menus);
						item.putFilteringValue(STARTER_LISTS, item.getNode().getStarterList().getContentKey().getId());
					}
				}
				
				case YOUR_LISTS: {
					
					if(item.getNode().getCclId()!=null){
						
						menu.setName(item.getNode().getListName());
						menu.setFilteringUrlValue(item.getNode().getCclId());
						menu.setFilter(filter);
						if(node.getRecipeId()!=null){
							menu.setInfo("RECIPE");				
						}
						menus.add(menu);
						
						item.putMenuValue(YOUR_LISTS, menus);
						item.putFilteringValue(YOUR_LISTS, item.getNode().getCclId());
					}
				}
				}
			} catch (FDResourceException e) {
				LOG.error("Error while decorating product", e);
			} catch (FDSkuNotFoundException e) {
				LOG.error("Error while decorating product", e);
			}
		}

	}
	
	private static List<ProductModel> collectParents(ProductModelPricingAdapter node) {

		List<ProductModel> parentNodes = new ArrayList<ProductModel>();

		Collection<ContentKey> parents = node.getParentKeys();
		if (parents != null) {
			for (ContentKey parentKey : parents) {
				ProductModel nodeByKey = ContentFactory.getInstance().getProductByName(
						parentKey.getId(),
						node.getContentKey().getId());
				if (nodeByKey != null && nodeByKey.isDisplayableBasedOnCms() && nodeByKey.isSearchable()) {
					parentNodes.add(nodeByKey);
				}
			}
		}

		return parentNodes;
	}

}
