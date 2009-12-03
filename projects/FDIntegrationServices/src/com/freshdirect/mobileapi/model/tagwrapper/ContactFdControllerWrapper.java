package com.freshdirect.mobileapi.model.tagwrapper;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.ContactFdControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.ContactFdControllerTag.Selection;

public class ContactFdControllerWrapper extends ControllerTagWrapper implements RequestParamName {

    public static final String CONTACT_US_SUBJECTS = "CONTACT_US_SUBJECTS";

    public ContactFdControllerWrapper(SessionUser user) {
        super(new ContactFdControllerTag(), user);
    }

    public Selection[] getSubjects() {
        return ContactFdControllerTag.selections;
    }

    public ResultBundle submitRequest(String subject, String orderId, String message) throws FDException {

        addRequestValue(REQ_PARAM_CONTACT_US_SUBJECT, subject);

        FDUserI user = (FDUserI) this.pageContext.getSession().getAttribute(SessionName.USER);
        addRequestValue(REQ_PARAM_CUSTOMER_PK, user.getIdentity().getErpCustomerPK());

        addRequestValue("salePK", orderId);
        addRequestValue("message", message);

        addExpectedRequestValues(new String[] { "email", "first_name", "last_name", "home_phone", "home_phone_ext", "work_phone",
                "work_phone_ext", "alt_phone", "alt_phone_ext" }, new String[] {});

        setMethodMode(true);
        ((ContactFdControllerTag)this.wrapTarget).setSuccessPage(null);
        ResultBundle result = new ResultBundle(executeTagLogic(), this);
        return result;
    }
}
