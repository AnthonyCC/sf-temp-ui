package com.freshdirect.webapp.ajax.signup;

import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.ActionResult;

public interface SignUpService {

    ActionResult validate(SignUpRequest request);

    FDCustomerModel createFdCustomer(FDUserI user, SignUpRequest request);

    ErpCustomerModel createErpCustomer(FDUserI user, SignUpRequest request);

}
