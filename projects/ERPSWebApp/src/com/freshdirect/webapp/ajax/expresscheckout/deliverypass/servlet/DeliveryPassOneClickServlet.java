package com.freshdirect.webapp.ajax.expresscheckout.deliverypass.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;


/**
 * @author Nikhil Subramanyam
 *
*/

public class DeliveryPassOneClickServlet extends BaseJsonServlet {

	private static final long serialVersionUID = 8455137897970811345L;
	private final static String CLASS_NAME = DeliveryPassOneClickServlet.class.getSimpleName();
	private static final Logger LOG = LoggerFactory.getInstance(DeliveryPassOneClickServlet.class);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {

		Map<String, Object> responseData= new HashMap<String, Object>();
		if ( null !=user && null != user.getIdentity() && !user.getDpFreeTrialOptin() ){
			FDIdentity identity = user.getIdentity();
			if(null == user.getDlvPassInfo() || EnumDlvPassStatus.NONE.equals(user.getDlvPassInfo().getStatus())) {
				try {
					FDCustomerManager.updateDpFreeTrialOptin(true, identity.getFDCustomerPK());
					user.updateDpFreeTrialOptin(true); // Update the cached value
					responseData.put("SUCCESS", true);
				} catch (FDResourceException e) {
					LOG.warn("Expection while opting-in for free trial DP.",e);
					responseData.put("ERROR", true);
					BaseJsonServlet.returnHttpError(500,"Failed to save Opt-in for free trial DP");
				}
			} else {
				responseData.put("ERROR", true);
				responseData.put("ERROR", "User is not eligible for free-trial DP");
			}
		}
		writeResponseData(response,responseData );
	}

	@Override
	protected void doGet( HttpServletRequest request, HttpServletResponse response, FDUserI user ) throws HttpErrorResponse {
		doPost(request, response,user);
	}

	@Override
    protected int getRequiredUserLevel() {
        return FDUserI.SIGNED_IN;
    }

    @Override
    protected boolean synchronizeOnUser() {
        return false;
    }
}
