package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.erp.ErpCOOLKey;
import com.freshdirect.fdstore.FDResourceException;

public interface CountryOfOriginServiceI {

	public Map<ErpCOOLKey, ErpCOOLInfo> getCountryOfOriginData(Date since) throws RemoteException;

	public void saveCountryOfOriginData(List<ErpCOOLInfo> cooList) throws FDResourceException, RemoteException;
}
