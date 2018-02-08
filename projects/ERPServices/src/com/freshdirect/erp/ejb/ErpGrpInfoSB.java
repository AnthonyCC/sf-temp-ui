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
/**
 *@deprecated Please use the ERPGroupScaleController and ErpGrpInfoServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
@SuppressWarnings("javadoc")
public interface ErpGrpInfoSB extends EJBObject
{
	@Deprecated
	public Collection<GroupScalePricing> findGrpInfoMaster(FDGroup grpIds[]) throws RemoteException;
	@Deprecated
	public Collection<FDGroup> loadAllGrpInfoMaster() throws RemoteException;
	@Deprecated
	public GroupScalePricing findGrpInfoMaster(FDGroup group) throws FDGroupNotFoundException, RemoteException;
	@Deprecated
	public  Map<String,FDGroup> getGroupIdentityForMaterial(String matId) throws RemoteException;
	@Deprecated
	public Map<SalesAreaInfo, FDGroup> getGroupIdentitiesForMaterial(String matId) throws RemoteException;
	@Deprecated
	public Collection getFilteredSkus(List skuList) throws RemoteException;
	@Deprecated
	public int getLatestVersionNumber(String grpId) throws RemoteException;
	@Deprecated
	public Collection<FDGroup> findGrpsForMaterial(String matId) throws RemoteException;
	@Deprecated
	public FDGroup getLatestActiveGroup(String groupID) throws FDGroupNotFoundException, RemoteException;
	@Deprecated
	public Map<String,List<String>> getModifiedOnlyGroups(Date lastModified) throws RemoteException;

}
