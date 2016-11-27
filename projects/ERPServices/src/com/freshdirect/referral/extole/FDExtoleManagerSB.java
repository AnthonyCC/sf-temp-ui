package com.freshdirect.referral.extole;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.referral.extole.model.ExtoleConversionRequest;
import com.freshdirect.referral.extole.model.ExtoleResponse;
import com.freshdirect.referral.extole.model.FDRafCreditModel;

public interface FDExtoleManagerSB extends EJBObject {

	public List<ExtoleConversionRequest> getExtoleCreateConversionRequest()
			throws FDResourceException, RemoteException;

	public List<ExtoleConversionRequest> getExtoleApproveConversionRequest()
			throws FDResourceException, RemoteException;

	public void updateConversionRequest(ExtoleResponse convResponse)
			throws FDResourceException, RemoteException;

	public void saveExtoleRewardsFile(List<FDRafCreditModel> rewards)
			throws FDResourceException, RemoteException;

	public void createConversion() throws ExtoleServiceException, IOException,
			FDResourceException, RemoteException;

	public void approveConversion() throws ExtoleServiceException, IOException,
			FDResourceException, RemoteException;

	public void downloadAndSaveRewards(String fileName) throws ExtoleServiceException,
			IOException, FDResourceException, RemoteException, ParseException;
}
