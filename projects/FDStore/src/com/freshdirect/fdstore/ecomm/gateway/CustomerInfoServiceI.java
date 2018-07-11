package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;

import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerCreditHistoryModel;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDCustomerRequest;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.PasswordNotExpiredException;
import com.freshdirect.fdstore.request.FDProductRequest;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.model.EnumReservationType;

public interface CustomerInfoServiceI {
	public PrimaryKey getCustomerId(String userId) throws FDResourceException, RemoteException;

	public FDCustomerInfo getCustomerInfo(FDIdentity identity) throws FDResourceException, RemoteException;

	public FDCustomerInfo getSOCustomerInfo(FDIdentity identity) throws FDResourceException, RemoteException;

	public void updateUserId(FDActionInfo info, String userId)
			throws FDResourceException, ErpDuplicateUserIdException, RemoteException;

	/**
	 * Store the user.
	 *
	 * @param user
	 *            the customer's user object
	 *
	 * @throws FDResourceException
	 *             if an error occurred using remote resources
	 */
	public void storeUser(FDUser user) throws FDResourceException, RemoteException;

	public void storeCohortName(String userId, String cohortName) throws FDResourceException, RemoteException;

	public boolean isPasswordRequestExpired(String emailAddress, String passReq)
			throws FDResourceException, RemoteException;

	public void changePassword(FDActionInfo info, String emailAddress, String password)
			throws FDResourceException, RemoteException;

	public void setProfileAttribute(FDIdentity identity, String key, String value, FDActionInfo info)
			throws RemoteException, FDResourceException;

	public void createCase(CrmSystemCaseInfo caseInfo) throws RemoteException, FDResourceException;

	public FDCustomerCreditHistoryModel getCreditHistory(FDIdentity identity)
			throws FDResourceException, RemoteException;

	public FDReservation makeReservation(FDUserI user, String timeslotId, EnumReservationType rsvType, String addressId,
			FDActionInfo aInfo, boolean chefsTable, TimeslotEvent event, boolean isForced)
			throws FDResourceException, ReservationException, RemoteException;

	public void cancelReservation(FDIdentity identity, FDReservation reservation,
			FDActionInfo actionInfo, TimeslotEvent event) throws FDResourceException, RemoteException;
	
	public void storeProductRequest(List<FDProductRequest> productRequest) throws RemoteException, FDResourceException;

}
