package com.freshdirect.delivery.map.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

public interface DlvMapperSB extends EJBObject{
	
	public List getMapLayersForRegion() throws RemoteException;

}
