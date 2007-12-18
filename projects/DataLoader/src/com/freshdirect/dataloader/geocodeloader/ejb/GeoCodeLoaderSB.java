package com.freshdirect.dataloader.geocodeloader.ejb;

import javax.ejb.*;
import java.rmi.RemoteException;
import java.util.List;

public interface GeoCodeLoaderSB extends EJBObject{
	
	public void geoCodeDeliveryInfo(int startIndex, int maxRecords)  throws RemoteException;
	
	public void updateDeliveryInfoModel(List addressModel) throws RemoteException;


}
