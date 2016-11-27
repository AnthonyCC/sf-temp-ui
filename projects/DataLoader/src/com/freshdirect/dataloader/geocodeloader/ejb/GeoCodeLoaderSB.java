package com.freshdirect.dataloader.geocodeloader.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

public interface GeoCodeLoaderSB extends EJBObject{
	
	public void geoCodeDeliveryInfo(int startIndex, int maxRecords)  throws RemoteException;
	
	public void updateDeliveryInfoModel(List addressModel) throws RemoteException;


}
