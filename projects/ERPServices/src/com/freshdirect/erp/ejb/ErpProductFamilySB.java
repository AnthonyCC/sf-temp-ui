package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.freshdirect.customer.ErpProductFamilyModel;



public interface ErpProductFamilySB extends EJBObject{
	
	
	public String getFamilyIdForMaterial(String matId) throws RemoteException;
	
	public ErpProductFamilyModel findFamilyInfo(String familyId) throws RemoteException;

	public ErpProductFamilyModel findSkyFamilyInfo(String materialId)throws RemoteException;

}
