package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.customer.ErpGrpPriceModel;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDGroupNotFoundException;
import com.freshdirect.fdstore.GroupScalePricing;

public interface ErpGrpInfoSB extends EJBObject{

    
	public Collection<GroupScalePricing> findGrpInfoMaster(FDGroup grpIds[]) throws RemoteException;
   
    public Collection<FDGroup> loadAllGrpInfoMaster() throws RemoteException;  
    
	public GroupScalePricing findGrpInfoMaster(FDGroup group) throws FDGroupNotFoundException, RemoteException;
	
	public FDGroup getGroupIdentityForMaterial(String matId) throws RemoteException;
	
	
	public Collection getFilteredSkus(List skuList) throws RemoteException;
	
    public int getLatestVersionNumber(String grpId) throws RemoteException;	
       
}
