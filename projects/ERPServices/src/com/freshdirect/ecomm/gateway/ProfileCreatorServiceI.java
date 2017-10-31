package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.sql.SQLException;

import com.freshdirect.fdstore.FDResourceException;

public interface ProfileCreatorServiceI {
	
	public void createProfiles(String batchId) throws SQLException, FDResourceException,RemoteException;;

}
