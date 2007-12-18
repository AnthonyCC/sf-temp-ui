package com.freshdirect.delivery;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import com.freshdirect.delivery.ejb.DlvRestrictionManagerHome;
import com.freshdirect.delivery.ejb.DlvRestrictionManagerSB;
import com.freshdirect.delivery.model.RestrictedAddressModel;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;

public class DlvRestrictionManager {
	
	private static DlvRestrictionManagerHome dlvRestrictionHome = null;
	
	
	protected static void lookupManagerHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			dlvRestrictionHome = (DlvRestrictionManagerHome) ctx.lookup(FDStoreProperties.getDlvRestrictionManagerHome());
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				ctx.close();
			} catch (NamingException e) {
			}
		}
	}
	
	public static RestrictionI getDlvRestriction(String restrictionId) throws FDResourceException
	{
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
			return sb.getDlvRestriction(restrictionId);
	
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static RestrictedAddressModel getAddressRestriction(String address1,String apartment,String zipCode) throws FDResourceException
	{
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
			return sb.getAddressRestriction(address1,apartment,zipCode);
	
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	
	public static List getDlvRestrictions(String dlvReason,String dlvType,String dlvCriterion) throws FDResourceException
	{
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
			return sb.getDlvRestrictions(dlvReason,dlvType,dlvCriterion);
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
		
	}
	
//	public static RestrictedAddressModel getAddressRestriction(String address1,String apartment,String zipCode) throws FDResourceException
//	{
//		if (dlvRestrictionHome == null) {
//			lookupManagerHome();
//		}
//		try {
//			DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
//			return sb.getAddressRestriction(address1,apartment,zipCode);
//		} catch (CreateException ce) {
//			dlvRestrictionHome = null;
//			throw new FDResourceException(ce, "Error creating session bean");
//		} catch (RemoteException re) {
//			dlvRestrictionHome = null;
//			throw new FDResourceException(re, "Error talking to session bean");
//		}
//		
//	}

	
	public static void addDlvRestriction(RestrictionI restriction) throws FDResourceException
	{
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
			sb.addDlvRestriction(restriction);
	
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	public static void addAddressRestriction(RestrictedAddressModel restriction) throws FDResourceException
	{
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
			sb.addAddressRestriction(restriction);
	
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
			
	
	public static void storeDlvRestriction(RestrictionI restriction) throws FDResourceException
	{
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
			sb.storeDlvRestriction(restriction);
	
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	
	public static void storeAddressRestriction(RestrictedAddressModel restriction,String address1, String apartment, String zipCode) throws FDResourceException
	{
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
			sb.storeAddressRestriction(restriction,address1,apartment,zipCode);
	
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	
	
	public static void deleteDlvRestriction(String restrictionId) throws FDResourceException
	{
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
			sb.deleteDlvRestriction(restrictionId);
	
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	
	public static void deleteAddressRestriction(String address1,String apartment,String zipCode) throws FDResourceException
	{
		if (dlvRestrictionHome == null) {
			lookupManagerHome();
		}
		try {
			DlvRestrictionManagerSB sb = dlvRestrictionHome.create();
			sb.deleteAddressRestriction(address1,apartment,zipCode);
	
		} catch (CreateException ce) {
			dlvRestrictionHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			dlvRestrictionHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

}
