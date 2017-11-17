package com.freshdirect.storeapi.content;

import com.freshdirect.fdstore.FDResourceException;

/**
 * Department filter
 *
 * @author zsombor
 *
 */
public class DepartmentFilter extends AbstractProductFilter {

    String departmentId;

    public DepartmentFilter(String departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public boolean applyTest(ProductModel prod) throws FDResourceException {
        if (prod.getDepartment().getContentKey().id.equals(departmentId)) {
            return true;
        }
        return false;
    }

}
