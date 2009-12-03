package com.freshdirect.mobileapi.model.tagwrapper;

import java.util.List;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.GetPeakProduceTag;

public class GetPeakProduceTagWrapper extends GetterTagWrapper {
    String departmentId;

    public GetPeakProduceTagWrapper(String departmentId,SessionUser user) {
        super(new GetPeakProduceTag(),user);
        this.departmentId = departmentId;
    }

    public List<ProductModel> getPeakProduct() throws FDException {
        ((GetPeakProduceTag) wrapTarget).setDeptId(departmentId);
        ((GetPeakProduceTag) wrapTarget).setGlobalPeakProduceSku("true");
        return (List<ProductModel>) getResult();
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

}