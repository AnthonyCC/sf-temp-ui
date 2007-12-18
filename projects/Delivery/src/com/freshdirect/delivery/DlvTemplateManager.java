package com.freshdirect.delivery;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import com.freshdirect.delivery.admin.DlvHistoricTimeslotData;
import com.freshdirect.delivery.ejb.DlvTemplateManagerHome;
import com.freshdirect.delivery.ejb.DlvTemplateManagerSB;
import com.freshdirect.delivery.model.DlvRegionModel;

public class DlvTemplateManager {
	
	private final static DlvTemplateManager instance = new DlvTemplateManager();
	private DlvTemplateManagerHome managerHome = null;
	
	/** Creates new DlvTemplateManager */
    private DlvTemplateManager() {
		//private constructor so that we have a single instance of this class in jvm
	}
	
	public static DlvTemplateManager getInstance() {
		return instance;
	}
	
	public synchronized DlvRegionModel getRegion(String name) throws DlvResourceException {
		DlvRegionModel region = null;
		
		if (this.managerHome == null) {
			lookupTemplateManagerHome();
		}
		
		try {
			DlvTemplateManagerSB sb = managerHome.create();
			region = sb.getRegion(name);
			
		} catch (CreateException ce) {
			this.managerHome=null;
			throw new DlvResourceException(ce);
		} catch (RemoteException re) {
			this.managerHome=null;
			throw new DlvResourceException(re);
		}
		
		
		return region;
	}
	
	public synchronized DlvRegionModel getRegion(String name, Date startDate) throws DlvResourceException{
		DlvRegionModel region = null;
		if(this.managerHome == null){
			lookupTemplateManagerHome();
		}
		
		try{
			DlvTemplateManagerSB sb = managerHome.create();
			region = sb.getRegion(name, startDate);
		}catch(CreateException ce) {
			this.managerHome = null;
			throw new DlvResourceException(ce);
		}catch(RemoteException re){
			this.managerHome = null;
			throw new DlvResourceException(re);
		}
		
		return region;
	}
	
	public synchronized Collection getRegions() throws DlvResourceException {
		if(this.managerHome == null){
			lookupTemplateManagerHome();
		}
		try{
			DlvTemplateManagerSB sb = managerHome.create();
			return sb.getRegions();
		}catch(CreateException ce){
			this.managerHome = null;
			throw new DlvResourceException(ce);
		}catch(RemoteException re){
			this.managerHome = null;
			throw new DlvResourceException(re);
		}
	}
	
	public ArrayList getTimeslotForDateRangeAndZone(Date begDate, Date endDate, Date curTime, String zoneCode) throws DlvResourceException{
		if(this.managerHome == null){
			lookupTemplateManagerHome();
		}
		try {
			DlvTemplateManagerSB sb = managerHome.create();
			return sb.getTimeslotForDateRangeAndZone(begDate, endDate, curTime, zoneCode);
		} catch (CreateException ce) {
			this.managerHome=null;
			throw new DlvResourceException(ce);
		} catch (RemoteException re) {
			this.managerHome=null;
			throw new DlvResourceException(re);
		}
		
	}
	
	public DlvHistoricTimeslotData getTimeslotForDateRangeAndRegion(Date startDate, Date endDate, String regionId) throws DlvResourceException {
		if(this.managerHome == null){
			lookupTemplateManagerHome(); 
		}
		try{
			DlvTemplateManagerSB sb = managerHome.create();
			return sb.getTimeslotForDateRangeAndRegion(startDate, endDate, regionId);
		}catch(CreateException e){
			this.managerHome = null;
			throw new DlvResourceException(e);
		}catch(RemoteException e){
			this.managerHome = null;
			throw new DlvResourceException(e);
		}
	}
			
	
	protected void lookupTemplateManagerHome() throws DlvResourceException {
		Context ctx = null;
		try {
			ctx = DlvProperties.getInitialContext();
			this.managerHome = (DlvTemplateManagerHome) ctx.lookup( DlvProperties.getDlvTemplateManagerHome() );
		} catch (NamingException ne) {
			throw new DlvResourceException(ne);
		} finally {
			try {
                if(ctx != null){
                    ctx.close();
                    ctx = null;
                }
			} catch (NamingException e) {}
		}
	}

}
