package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDGroupNotFoundException;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.fdstore.SalesAreaInfo;

@SuppressWarnings("javadoc")
public interface ErpGrpInfoSB extends EJBObject
{

	public Collection<GroupScalePricing> findGrpInfoMaster(FDGroup grpIds[]) throws RemoteException;

	public Collection<FDGroup> loadAllGrpInfoMaster() throws RemoteException;

	public GroupScalePricing findGrpInfoMaster(FDGroup group) throws FDGroupNotFoundException, RemoteException;

	public  Map<String,FDGroup> getGroupIdentityForMaterial(String matId) throws RemoteException;
	
	public Map<SalesAreaInfo, FDGroup> getGroupIdentitiesForMaterial(String matId) throws RemoteException;

	public Collection getFilteredSkus(List skuList) throws RemoteException;

	public int getLatestVersionNumber(String grpId) throws RemoteException;

	public Collection<FDGroup> findGrpsForMaterial(String matId) throws RemoteException;

	public FDGroup getLatestActiveGroup(String groupID) throws FDGroupNotFoundException, RemoteException;
	
	public Map<String,List<String>> getModifiedOnlyGroups(Date lastModified) throws RemoteException;

}
