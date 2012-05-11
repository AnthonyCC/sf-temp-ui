package com.freshdirect.fdstore.content;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.freshdirect.cms.fdstore.FDContentTypes;

public class FilteringSortingMenuBuilder extends GenericFilteringMenuBuilder<FilteringSortingItem> {

	public FilteringSortingMenuBuilder(Map<EnumFilteringValue, List<Object>> filterValues, Set<EnumFilteringValue> filters) {
		super(filterValues, filters);
	}

	@Override
	public void buildMenu(List<FilteringSortingItem> items) { 
		for (EnumFilteringValue value : filters) {

			Map<String, FilteringMenuItem> domain = new HashMap<String, FilteringMenuItem>();
			for (FilteringSortingItem item : items) {

				Set<FilteringMenuItem> menuItems = item.getMenuValue(value);
				checkSelected(menuItems, filterValues.get(value));

				if (menuItems != null) {
					for (FilteringMenuItem menuItem : menuItems) {

						String menuName = menuItem.getFilteringUrlValue();
						FilteringMenuItem mI = domain.get(menuName);

						if (mI == null) {
							menuItem.setCounter(1);
							domain.put(menuName, menuItem);
						} else {
							mI.setCounter(mI.getCounter() + 1);
							domain.put(menuName, mI);
						}
					}
				}

			}

			domains.put(value, domain);
		}

		narrowTree(filterValues);

	}

	/**
	 * @param filterValues
	 * 
	 *            if subcategory or category selected first then domains above
	 *            them (department, category) needs to be narrowed this method
	 *            only needed when multiselection is not supported!
	 */
	private void narrowTree(Map<EnumFilteringValue, List<Object>> filterValues) {

		String dept = filterValues.get(EnumFilteringValue.DEPT) != null ? (String) filterValues.get(EnumFilteringValue.DEPT).get(0) : null;
		String cat = filterValues.get(EnumFilteringValue.CAT) != null ? (String) filterValues.get(EnumFilteringValue.CAT).get(0) : null;
		String subCat = filterValues.get(EnumFilteringValue.SUBCAT) != null ? (String) filterValues.get(EnumFilteringValue.SUBCAT).get(0) : null;
		String recipe = filterValues.get(EnumFilteringValue.RECIPE_CLASSIFICATION) != null ? (String) filterValues.get(EnumFilteringValue.RECIPE_CLASSIFICATION).get(0) : null;
		String brand = filterValues.get(EnumFilteringValue.BRAND) != null ? (String) filterValues.get(EnumFilteringValue.BRAND).get(0) : null;

		if (subCat != null && cat == null) {
			ContentNodeModel subCatModel = ContentFactory.getInstance().getContentNode(FDContentTypes.CATEGORY, subCat);
			cat = subCatModel.getParentNode().getContentKey().getId();
		}

		if (cat != null && dept == null) {
			ContentNodeModel catModel = ContentFactory.getInstance().getContentNode(FDContentTypes.CATEGORY, cat);
			dept = catModel.getParentNode().getContentKey().getId();
		}

		if (subCat != null) {
			narrowDomain(EnumFilteringValue.SUBCAT, subCat, false, null);
		}
		if (cat != null) {
			narrowDomain(EnumFilteringValue.CAT, cat, false, null);
			narrowDomain(EnumFilteringValue.SUBCAT, subCat, true, cat);				
		}
		if (dept != null) {
			narrowDomain(EnumFilteringValue.DEPT, dept, false, null);
			narrowDomain(EnumFilteringValue.CAT, cat, true, dept);
			Map<String, FilteringMenuItem> subCats = domains.get(EnumFilteringValue.SUBCAT);
			// narrow subcat's of department's cats
			if (subCats != null && !subCats.isEmpty() && cat == null) {
				DepartmentModel deptNode = (DepartmentModel) ContentFactory.getInstance().getContentNode(FDContentTypes.DEPARTMENT, dept);
				if (deptNode != null) {
					List<CategoryModel> categories = deptNode.getCategories();
					Set<String> parentCatIds = new HashSet<String>(categories.size());
					for (CategoryModel catNode : categories)
						parentCatIds.add(catNode.getContentKey().getId());
					
					Iterator<Entry<String, FilteringMenuItem>> it = subCats.entrySet().iterator();
					while (it.hasNext()) {
						Entry<String, FilteringMenuItem> item = it.next();
						ContentNodeModel itemNode = ContentFactory.getInstance().getContentNode(FDContentTypes.CATEGORY, item.getKey());
						if (itemNode == null || itemNode.getParentNode() == null
								|| !parentCatIds.contains(itemNode.getParentNode().getContentKey().getId()))
							it.remove();
					}
				}
			}
		}
		if(recipe != null ){
			narrowDomain(EnumFilteringValue.RECIPE_CLASSIFICATION, recipe, false, null);
		}
		if(brand != null){
			narrowDomain(EnumFilteringValue.BRAND,brand,false,null);
		}
	}

	private void narrowDomain(EnumFilteringValue domainId, String selected, boolean multiValue, String parent) {

		Map<String, FilteringMenuItem> domain = domains.get(domainId);
		Map<String, FilteringMenuItem> narrowedDomain = new HashMap<String, FilteringMenuItem>();
		
		if (!multiValue) {
			for (String menuId : domain.keySet()) {
				if (menuId.equals(selected)) {
					narrowedDomain.put(menuId, domain.get(menuId));
					break;
				}
			}
		} else {		
			for (String menuId : domain.keySet()) {
				ContentNodeModel subCatModel = ContentFactory.getInstance().getContentNode(FDContentTypes.CATEGORY, menuId);
				if(subCatModel != null && parent.equals(subCatModel.getParentNode().getContentKey().getId())){
					narrowedDomain.put(menuId, domain.get(menuId));
				}
			}
		}

		domains.put(domainId, narrowedDomain);
	}

	private void checkSelected(Set<FilteringMenuItem> menuItems, List<Object> itemFilteringValues) {
		if (itemFilteringValues == null) {
			return;
		}
		for (FilteringMenuItem menuItem : menuItems) {
			if (itemFilteringValues.contains(menuItem.getFilteringUrlValue())) {
				menuItem.setSelected(true);
			}
		}
	}

}
