package com.freshdirect.referral.extole;

import java.io.IOException;

import com.freshdirect.referral.extole.model.ExtoleConversionRequest;
import com.freshdirect.referral.extole.model.ExtoleResponse;

/*
 * This interface defines 
 * all the Extole API methods
 * */

public interface ExtoleService {

	/* This method is for Create a registration or conversion */
	public ExtoleResponse createConversion(ExtoleConversionRequest conversion)
			throws ExtoleServiceException, IOException;

	/* This method is for Approve or Deny a Conversion */
	public ExtoleResponse approveConversion(ExtoleConversionRequest approveConversion)
			throws ExtoleServiceException, IOException;

}
