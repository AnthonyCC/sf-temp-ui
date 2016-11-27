package com.freshdirect.mobileapi.model.tagwrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.freshdirect.cms.ContentType;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.DepartmentModel;
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

    @SuppressWarnings("unchecked")
	public Collection<Object> getPeakProduct(int maxPeakProduceCount) throws FDException {
    	DepartmentModel department = (DepartmentModel) ContentFactory.getInstance().getContentNode( ContentType.get( "Department" ), departmentId );
		if ( department != null ) {
		
			((GetPeakProduceTag) wrapTarget).setDeptId(departmentId);
			((GetPeakProduceTag) wrapTarget).setUseMinCount(false);
			
			 return (Collection<Object>) ((GetPeakProduceTag) wrapTarget).getPeakProduce(department, maxPeakProduceCount,getUser().getUserContext());
        
		}
        return new ArrayList<Object>();
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

}