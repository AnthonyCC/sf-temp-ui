package com.freshdirect.mobileapi.model.tagwrapper;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.mobileapi.controller.data.request.ContactUsData;
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
    
    public Selection[] getSubjectsFdx() {
        return ContactFdControllerTag.selectionsFdx;
    }
    
    public Selection[] getSubjectsFdxAnonymous() {
        return ContactFdControllerTag.selectionsFdxAnonymous;
    }

    public ResultBundle submitRequest(String subject, String orderId, String message, ContactUsData contactUsData) throws FDException {

        addRequestValue(REQ_PARAM_CONTACT_US_SUBJECT, subject);

        FDUserI user = (FDUserI) this.pageContext.getSession().getAttribute(SessionName.USER);
      /*  if(user != null && user.getIdentity()!=null) {*/
        if(user != null && user.getIdentity()!=null) {
        addRequestValue(REQ_PARAM_CUSTOMER_PK, user.getIdentity().getErpCustomerPK());
        }

        addRequestValue("salePK", orderId);
        addRequestValue("message", message);
        addRequestValue("sendMessage",10);
        addRequestValue("email",contactUsData.getEmail());
        addRequestValue("first_name",contactUsData.getFirstname());
        addRequestValue("last_name",contactUsData.getLastname());
        addRequestValue("home_phone",contactUsData.getHomePhone());
        

       
	
	addExpectedSessionValues(new String[] { SessionName.PENDING_HELP_EMAIL_EVENT },new String[] { SessionName.PENDING_HELP_EMAIL_EVENT }); //gets,sets
	

        addExpectedRequestValues(new String[] { "email", "first_name", "last_name", "home_phone", "home_phone_ext", "work_phone",
                "work_phone_ext", "alt_phone", "alt_phone_ext", "customerPK" }, new String[] {});

        setMethodMode(true);
        ((ContactFdControllerTag)this.wrapTarget).setSuccessPage(null);
        ResultBundle result = new ResultBundle(executeTagLogic(), this);
        return result;
     /*   } else {
        	ResultBundle result1 = new ResultBundle();
        	result1.getActionResult().addError(true, "ERROR", "Session Expired");
        	return result1;
        }*/
    }
}
