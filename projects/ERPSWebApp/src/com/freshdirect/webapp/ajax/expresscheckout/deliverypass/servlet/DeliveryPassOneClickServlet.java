package com.freshdirect.webapp.ajax.expresscheckout.deliverypass.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.fdstore.FDStoreProperties;


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
		if(FDStoreProperties.isDlvPassFreeTrialOptinFeatureEnabled()){
			if ( null !=user && null != user.getIdentity()){
				FDIdentity identity = user.getIdentity();
				if(null == user.getDlvPassInfo() || EnumDlvPassStatus.NONE.equals(user.getDlvPassInfo().getStatus())) {
					if (!user.getDpFreeTrialOptin()){ //Eligible
						try {
							FDActionInfo info=AccountActivityUtil.getActionInfo(request.getSession(), "DeliveryPass Free-Trial Opt-in");
							FDCustomerManager.updateDpFreeTrialOptin(true, identity.getFDCustomerPK(), info);
							user.updateDpFreeTrialOptin(true); // Update the cached value
							responseData.put("STATUS", "SUCCESS");
						} catch (FDResourceException e) {
							LOG.warn("Expection while opting-in for free trial DP.",e);
							returnHttpError(500,"Failed to save Opt-in for free trial DP");
						}
					}else { //Already Opted-in for Free-Trial DP
						responseData.put("STATUS", "ERROR");
						responseData.put("MESSAGE", "You have already signed up for the free DeliveryPass trial. Your next order placed will have free delivery.");
						responseData.put("ERRORTYPE", "signedup");
					}
				} else { //Not eligible for Free-Trial DP
					responseData.put("STATUS", "ERROR");
					responseData.put("MESSAGE", "You have already had a DeliveryPass and are not eligible for the free trial.");
					responseData.put("ERRORTYPE", "ineligible");
				}
				
			}
		}else {
			LOG.warn("DP Free-trial Opt-in feature is not enabled");
			returnHttpError(500,"Failed to save Opt-in for free trial DP");
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
