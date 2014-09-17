package com.freshdirect.fdstore.content.browse.sorter;

import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.FilteringProductItem;

public class DepartmentComparator extends OptionalObjectComparator<FilteringProductItem, String> {

	@Override
	protected String getValue(FilteringProductItem obj) {
		DepartmentModel department = obj.getProductModel().getDepartment();
		return department == null ? null : department.getFullName();
	}

}
