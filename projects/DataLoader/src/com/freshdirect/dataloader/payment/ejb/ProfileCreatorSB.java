package com.freshdirect.dataloader.payment.ejb;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.freshdirect.dataloader.payment.ProfileCreatorResult;
import com.freshdirect.fdstore.FDResourceException;

public interface ProfileCreatorSB extends EJBObject {
	
	public void createProfiles(String batchId) throws SQLException, FDResourceException,RemoteException;

}
