package com.freshdirect.fdstore.content;

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

    public boolean applyTest(ProductModel prod) throws FDResourceException {
        if (prod.getDepartment().getContentKey().getId().equals(departmentId)) {
            return true;
        }
        return false;
    }

}
