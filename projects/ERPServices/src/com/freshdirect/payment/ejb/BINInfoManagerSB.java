package com.freshdirect.payment.ejb;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.payment.BINInfo;

public interface BINInfoManagerSB extends EJBObject{
	
	public void saveBINInfo( List<List<BINInfo>> binInfos) throws FDResourceException, RemoteException;
	public NavigableMap<Long, BINInfo> getActiveBINs() throws FDResourceException, RemoteException;
	
}