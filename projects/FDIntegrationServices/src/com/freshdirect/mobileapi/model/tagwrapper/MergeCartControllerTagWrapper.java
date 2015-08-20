package com.freshdirect.mobileapi.model.tagwrapper;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.MergeCartControllerTag;

public class MergeCartControllerTagWrapper extends NonStandardControllerTagWrapper implements RequestParamName, SessionParamName {

	public MergeCartControllerTagWrapper(SessionUser user) {
		super(new MergeCartControllerTag(), user);
	}

	@Override
	protected void setResult() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Object getResult() throws FDException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static final String FAKE_SUCCESS_PAGE = "FAKE_SUCCESS_PAGE";
	
	public ActionResult mergeCart(FDCartModel currentCart) throws  FDException{
		//AddExprectedSession
		//CurrentCart, User
		addExpectedSessionValues( new String[] { SESSION_PARAM_USER, CURRENT_CART  },  new String[] { SESSION_PARAM_USER, CURRENT_CART });
		
		//AddExpectedRequest
		addExpectedRequestValues(new String[] {}, new String[] {});
		
		addSessionValue(CURRENT_CART, currentCart);
		
		//Set SuccessPage some NonNull Values (Fake)
		((MergeCartControllerTag)wrapTarget).setSuccessPage(FAKE_SUCCESS_PAGE);
		addRequestValue("chosen_cart", "merge");
		setMethodMode(true);
		ActionResult actionResult = executeTagLogic();
		return actionResult;
		
		
	}

}
