package com.freshdirect.fdstore.content;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

						String menuName = menuItem.getName();
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

	}
	
	private void checkSelected(Set<FilteringMenuItem> menuItems, List<Object> itemFilteringValues){
		if(itemFilteringValues==null){
			return;
		}
		for(FilteringMenuItem menuItem: menuItems){
			if(itemFilteringValues.contains(menuItem.getFilteringUrlValue())){
				menuItem.setSelected(true);
			}
		}
	}

}
