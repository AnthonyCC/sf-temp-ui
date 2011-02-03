/*
 * 
 * DlvDepotManagerSessionManager.java
 * Date: Jul 29, 2002 Time: 7:47:02 PM
 */
package com.freshdirect.delivery.depot.ejb;

/**
 * 
 * @author knadeem
 */
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.delivery.DlvProperties;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.depot.DlvDepotModel;
import com.freshdirect.delivery.depot.DlvLocationModel;
import com.freshdirect.delivery.ejb.DlvManagerHome;
import com.freshdirect.delivery.ejb.DlvManagerSB;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class DlvDepotManagerSessionBean extends SessionBeanSupport {

	private static final Category LOGGER = LoggerFactory.getInstance(DlvDepotManagerSessionBean.class);
	private DlvDepotHome depotHome = null;
	private DlvManagerHome managerHome = null;

	/** 
	 * Creates new DlvDepotManagerSessionManager 
	 */
	public DlvDepotManagerSessionBean() {
		super();
	}

	/**
	 * Template method that returns the cache key to use for caching resources.
	 *
	 * @return the bean's home interface name
	 */
	protected String getResourceCacheKey() {
		return "com.freshdirect.delivery.depot.ejb.DlvDepotManagerHome";
	}

	/**
	 * method to get all the depots setup in the system
	 * 
	 * @return collection of DlvDepotModels
	 * @throws DlvResourceException if there are problems accessing remote objects
	 */
	public Collection getDepots() throws DlvResourceException {
		Collection depotModels = new ArrayList();
		if (this.depotHome == null) {
			this.lookupDlvDepotHome();
		}
		try {
			Collection depots = this.depotHome.findAll();

			for (Iterator i = depots.iterator(); i.hasNext();) {
				DlvDepotEB eb = (DlvDepotEB) i.next();
				depotModels.add(eb.getModel());
			}

		} catch (RemoteException re) {
			this.depotHome = null;
			LOGGER.warn("RemoteException in getDepots", re);
			throw new DlvResourceException("Cannot talk to DlvDepotEntityBean " + re.getMessage());
		} catch (FinderException fe) {
			//it is not thrown ever just for ejbc
		}
		return depotModels;
	}

	/**
	 * method to get depot for given depotId
	 * 
	 * @param String depotId to lookup
	 * @return DlvDepotModel, null in case cannot fine depot for given id
	 * @throws DlvResourceException if there are any problems accessing remote objects
	 */
	public DlvDepotModel getDepot(String depotCode) throws DlvResourceException {

		DlvDepotModel depot = null;
		if (this.depotHome == null) {
			this.lookupDlvDepotHome();
		}

		try {
			DlvDepotEB eb = this.depotHome.findByDepotCode(depotCode);
			depot = (DlvDepotModel) eb.getModel();
		} catch (FinderException fe) {
			LOGGER.info("Could not find a Depot for depot code: " + depotCode);
		} catch (RemoteException re) {
			this.depotHome = null;
			LOGGER.warn("RemoteException in getDepot", re);
			throw new DlvResourceException("Cannot talk to DlvDepotEntityBean " + re.getMessage());
		}
		//if cannot find object returning a null model :) intensional
		return depot;
	}

	/**
	 * method to verify access code for given depot
	 * 
	 * @param String depotId to check access code for
	 * @param String accessCode to check
	 * @return boolean true if access code matches false otherwise
	 * @throws DlvResourceException if there are any problems accessing remote objects
	 */
	public boolean checkAccessCode(String depotCode, String accessCode) throws DlvResourceException {
		boolean valid = false;
		/*
		if(this.depotHome == null){
			this.lookupDlvDepotHome();
		}
		
		try{
			DlvDepotEB eb = this.depotHome.findByDepotCode(depotCode);
			DlvDepotModel depot = (DlvDepotModel)eb.getModel();
			
			valid = depot.getRegistrationCode().equals(accessCode);
		}catch(FinderException fe){
			LOGGER.info("Could not find a Depot for depot Codt: "+depotCode);
		}catch(RemoteException re){
			this.depotHome = null;
			LOGGER.warn("RemoteException in checkAccessCode", re);
			throw new DlvResourceException("Cannot talk to DlvDepotEntityBean "+re.getMessage());
		}
		*/
		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement ps =
				conn.prepareStatement("select count(*) from dlv.depot where depot_code=? and UPPER(registration_code)=UPPER(?)");
			LOGGER.info("deptCode = " + depotCode + ",  accessCode = " + accessCode);
			ps.setString(1, depotCode);
			ps.setString(2, accessCode);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int c = rs.getInt(1);
				LOGGER.info("c = " + c);
				valid = (c == 1) ? true : false;
			}
			rs.close();
			ps.close();
		} catch (SQLException sqle) {
			throw new DlvResourceException(sqle);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqle2) {
					LOGGER.warn("Difficulty closing connection", sqle2);
				}
			}
		}

		return valid;
	}

	/**
	 * method to get particular location for a given depotCode matching the location id
	 * 
	 * @param String depotCode for which to get the location
	 * @param String locationId of the location to return
	 * @return DlvLocationModel
	 * @throws DlvResourceException if there are any problems talking to remote resources
	 */
	public DlvLocationModel getDepotLocation(String depotCode, String locationId) throws DlvResourceException {

		DlvLocationModel location = null;
		if (this.depotHome == null) {
			this.lookupDlvDepotHome();
		}

		try {
			DlvDepotEB eb = this.depotHome.findByDepotCode(depotCode);
			DlvDepotModel depot = (DlvDepotModel) eb.getModel();
			location = depot.getLocation(locationId);
		} catch (FinderException fe) {
			LOGGER.info("Could not find a Depot for depot code: " + depotCode);
		} catch (RemoteException re) {
			this.depotHome = null;
			LOGGER.warn("RemoteException in getDepot", re);
			throw new DlvResourceException("Cannot talk to DlvDepotEntityBean " + re.getMessage());
		}
		//if cannot find object returning a null model :) intensional
		return location;
	}

	/**
	 * method to get particular location for a given depotId matching the location id
	 * 
	 * @param String depot id for which to get the location
	 * @param String locationId of the location to return
	 * @return DlvLocationModel
	 * @throws DlvResourceException if there are any problems talking to remote resources
	 */
	public DlvLocationModel getLocationForDepotId(String depotId, String locationId) throws DlvResourceException {

		DlvLocationModel location = null;
		if (this.depotHome == null) {
			this.lookupDlvDepotHome();
		}

		try {
			DlvDepotEB eb = this.depotHome.findByPrimaryKey(new PrimaryKey(depotId));
			DlvDepotModel depot = (DlvDepotModel) eb.getModel();
			location = depot.getLocation(locationId);
		} catch (FinderException fe) {
			LOGGER.info("Could not find a Depot for depot code: " + depotId);
		} catch (RemoteException re) {
			this.depotHome = null;
			LOGGER.warn("RemoteException in getDepot", re);
			throw new DlvResourceException("Cannot talk to DlvDepotEntityBean " + re.getMessage());
		}
		//if cannot find object returning a null model :) intensional
		return location;
	}

	/**
	 * method to add a new location to a depot
	 * 
	 * @param String depot id
	 * @param DlvLocationModel location to be added
	 * @throws DlvResourceException if there are any problems talking to remote reaources
	 */
	public void addDepotLocation(String depotId, DlvLocationModel location) throws DlvResourceException {
		if (this.depotHome == null) {
			this.lookupDlvDepotHome();
		}

		try {
			DlvDepotEB eb = this.depotHome.findByPrimaryKey(new PrimaryKey(depotId));
			eb.addLocation(location);

		} catch (FinderException fe) {
			LOGGER.info("Could not find a Depot for depot code: " + depotId);
		} catch (RemoteException re) {
			this.depotHome = null;
			LOGGER.warn("RemoteException in getDepot", re);
			throw new DlvResourceException("Cannot talk to DlvDepotEntityBean " + re.getMessage());
		}

	}

	/**
	 * method to update location for a depot
	 * 
	 * @param String depot id
	 * @param DlvLocationModel location to be updated
	 * @throws DlvResourceException if there are any problems talking to remote reaources
	 */
	public void updateDepotLocation(String depotId, DlvLocationModel location) throws DlvResourceException {
		if (this.depotHome == null) {
			this.lookupDlvDepotHome();
		}

		try {
			DlvDepotEB eb = this.depotHome.findByPrimaryKey(new PrimaryKey(depotId));
			eb.updateLocation(location);

		} catch (FinderException fe) {
			LOGGER.info("Could not find a Depot for depot code: " + depotId);
		} catch (RemoteException re) {
			this.depotHome = null;
			LOGGER.warn("RemoteException in getDepot", re);
			throw new DlvResourceException("Cannot talk to DlvDepotEntityBean " + re.getMessage());
		}

	}

	/**
	 * method to delete location for a depot
	 * 
	 * @param String depot id
	 * @param String locationId to be deleted
	 * @throws DlvResourceException if there are any problems talking to remote reaources
	 */
	public void deleteDepotLocation(String depotId, String locationId) throws DlvResourceException {
		if (this.depotHome == null) {
			this.lookupDlvDepotHome();
		}

		try {
			DlvDepotEB eb = this.depotHome.findByPrimaryKey(new PrimaryKey(depotId));
			eb.deleteLocation(locationId);

		} catch (FinderException fe) {
			LOGGER.info("Could not find a Depot for depot code: " + depotId);
		} catch (RemoteException re) {
			this.depotHome = null;
			LOGGER.warn("RemoteException in getDepot", re);
			throw new DlvResourceException("Cannot talk to DlvDepotEntityBean " + re.getMessage());
		}

	}

	/**
	 * method to create a new depot
	 * 
	 * @param DlvDepotModel model of new depot
	 * @throws DlvResourceException if there are any problems talking to remote reaources
	 */
	public void addNewDepot(DlvDepotModel model) throws DlvResourceException {
		if (this.depotHome == null) {
			this.lookupDlvDepotHome();
		}

		try {
			this.depotHome.create(model);

		} catch (CreateException ce) {
			LOGGER.info("Could not create new Depot");
		} catch (RemoteException re) {
			this.depotHome = null;
			LOGGER.warn("RemoteException in getDepot", re);
			throw new DlvResourceException("Cannot talk to DlvDepotEntityBean " + re.getMessage());
		}

	}

	/**
	 * method to update a depot
	 * 
	 * @param String depotId id of the depot to be updated
	 * @param DlvDepotModel updated model
	 * @throws DlvResourceException if there are any problems talking to remote reaources
	 */
	public void updateDepot(String depotId, DlvDepotModel depotModel) throws DlvResourceException {
		if (this.depotHome == null) {
			this.lookupDlvDepotHome();
		}

		try {
			DlvDepotEB eb = this.depotHome.findByPrimaryKey(new PrimaryKey(depotId));
			eb.updateFromModel(depotModel);

		} catch (FinderException fe) {
			LOGGER.info("Could not find Depot: " + depotId);
		} catch (RemoteException re) {
			this.depotHome = null;
			LOGGER.warn("RemoteException in getDepot", re);
			throw new DlvResourceException("Cannot talk to DlvDepotEntityBean " + re.getMessage());
		}

	}

	/**
	 * method to delete a depot
	 * 
	 * @param String depotId of the depot to be deleted
	 * @throws DlvResourceException if there are any problems talking to remote reaources
	 */
	public void deleteDepot(String depotId) throws DlvResourceException {
		if (this.depotHome == null) {
			this.lookupDlvDepotHome();
		}

		try {
			DlvDepotEB eb = this.depotHome.findByPrimaryKey(new PrimaryKey(depotId));
			eb.remove();

		} catch (FinderException fe) {
			LOGGER.info("Could not find Depot: " + depotId);
		} catch (RemoveException re) {
			LOGGER.info("Could not remove Depot: " + depotId);
		} catch (RemoteException re) {
			this.depotHome = null;
			LOGGER.warn("RemoteException in getDepot", re);
			throw new DlvResourceException("Cannot talk to DlvDepotEntityBean " + re.getMessage());
		}

	}

	public Collection getZonesForRegionId(String regionId) throws DlvResourceException {
		if (this.managerHome == null) {
			this.lookupDlvManagerHome();
		}
		try {
			DlvManagerSB sb = this.managerHome.create();
			return sb.getZonesForRegionId(regionId);
		} catch (CreateException ce) {
			throw new DlvResourceException(ce);
		} catch (RemoteException re) {
			throw new DlvResourceException(re);
		}
	}

	public Collection getAllRegions() throws DlvResourceException {
		if (this.managerHome == null) {
			this.lookupDlvManagerHome();
		}
		try {
			DlvManagerSB sb = this.managerHome.create();
			return sb.getAllRegions();
		} catch (CreateException ce) {
			throw new DlvResourceException(ce);
		} catch (RemoteException re) {
			throw new DlvResourceException(re);
		}
	}

	/**
	 * utility method to lookup DlvDepotEntityBeans home interface
	 * 
	 * @throws DlvResourceException if there is a NamingException while getting the ctx or home interface
	 */

	protected void lookupDlvDepotHome() throws DlvResourceException {
		Context ctx = null;
		try {
			ctx = DlvProperties.getInitialContext();
			this.depotHome = (DlvDepotHome) ctx.lookup(DlvProperties.getDlvDepotHome());
		} catch (NamingException ne) {
			throw new DlvResourceException(ne);
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (NamingException e) {
			}
		}
	}

	/**
	 * utility method to lookup DlvManagerSessionBean home interface
	 * 
	 * @throws DlvResourceException if there is a NamingException while getting the ctx or home interface
	 */

	protected void lookupDlvManagerHome() throws DlvResourceException {
		Context ctx = null;
		try {
			ctx = DlvProperties.getInitialContext();
			this.managerHome = (DlvManagerHome) ctx.lookup(DlvProperties.getDlvManagerHome());
		} catch (NamingException ne) {
			throw new DlvResourceException(ne);
		} finally {
			try {
				ctx.close();
			} catch (NamingException e) {
			}
		}
	}

}
