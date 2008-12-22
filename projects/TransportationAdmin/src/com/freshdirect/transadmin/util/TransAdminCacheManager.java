package com.freshdirect.transadmin.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpRouteMasterInfo;
import com.freshdirect.customer.ErpTruckMasterInfo;
import com.freshdirect.framework.util.ExpiringReference;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sap.command.SapRouteMasterInfo;
import com.freshdirect.sap.command.SapTruckMasterInfo;
import com.freshdirect.sap.ejb.SapException;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.service.EmployeeManagerI;

public class TransAdminCacheManager {		 	
		 			
	private List internalProgIdList=new ArrayList();
	private static TransAdminCacheManager instance = null;
	
	private EmployeeManagerI manager=null; 
	
	private static Category LOGGER = LoggerFactory.getInstance(TransAdminCacheManager.class);
	// make the time constant in property
	private ExpiringReference truckDataHolder = new ExpiringReference(60 * 60 * 1000) {
		protected Object load() {
			try {
				return loadAllTruckData();
			} catch (SapException e) {
				LOGGER.error("Could not load load Referral program due to: ", e);				
			}
			return Collections.EMPTY_LIST;
		}
	};
	
	
	// make the time constant in property
	private TrnAdmExpiringReference routeDataHolder = new TrnAdmExpiringReference(60 * 60 * 1000) {
		
		
		
		protected Object load(Object requestParam) {
			try {
				
				return loadAllRouteData(requestParam);
			} catch (SapException e) {
				LOGGER.error("Could not load load Referral program due to: ", e);				
			}
			return Collections.EMPTY_LIST;
		}
							
	};
	
	
//	 make the time constant in property
	private ExpiringReference employeeDataHolder = new ExpiringReference(60 * 60 * 1000) {
						
		protected Object load() {
			try {
				return loadAllEmployeeData();
			} catch (SapException e) {
				LOGGER.error("Could not load load Referral program due to: ", e);				
			}
			return Collections.EMPTY_LIST;
		}
	};
	
	
//	 make the time constant in property
	private ExpiringReference terminatedEmployeeDataHolder = new ExpiringReference(60 * 60 * 1000) {
						
		protected Object load() {
			try {
				return loadAllTerminatedEmployeeData();
			} catch (SapException e) {
				LOGGER.error("Could not load load Referral program due to: ", e);				
			}
			return Collections.EMPTY_LIST;
		}
	};

		
	private TransAdminCacheManager(){		
	}
		
	
	public static synchronized TransAdminCacheManager getInstance(){
		if(instance==null){
			instance=new TransAdminCacheManager();
		}
		return instance;		
	}
	
	
	public void refreshCacheData(EnumCachedDataType dataType){
		
		System.out.println("refreshing crap truck data");
		
		if(EnumCachedDataType.TRUCK_DATA.equals(dataType)){			
			truckDataHolder.forceRefresh();
		}
		
	}
	
	public List loadAllTerminatedEmployeeData() throws SapException{
		
		return (List)manager.getKronosTerminatedEmployees();
	}		
	
	public List loadAllEmployeeData() throws SapException{
		
		return (List)manager.getKronosEmployees();	
	}		
	
	
	public List loadAllTruckData() throws SapException{		
		SapTruckMasterInfo truckInfos = new SapTruckMasterInfo();

			truckInfos.execute();

		return truckInfos.getTruckMasterInfos();	
	}		
	
	public List loadAllRouteData(Object requestParam) throws SapException{
		
		System.out.println(" Cache : loadAllRouteData"+requestParam);
		SapRouteMasterInfo routeInfos = new SapRouteMasterInfo((String)requestParam);

		routeInfos.execute();

		System.out.println(" Cache : loadAllRouteData values:"+routeInfos.getRouteMasterInfos());
		
		return routeInfos.getRouteMasterInfos();	
		
	}		
	
	
	public List getAllTruckMasterInfo()
	{	
		return (List) this.truckDataHolder.get();
	}
	
			
	public ErpTruckMasterInfo getTruckMasterInfo(String truckNumber)
	{	
		List trkList = (List) this.truckDataHolder.get();
		if(trkList==null) return null;
		for(int i=0;i<trkList.size();i++){
			ErpTruckMasterInfo info=(ErpTruckMasterInfo)trkList.get(i);
			if(info.getTruckNumber().equalsIgnoreCase(truckNumber))
				return info;
		}
		return null;
	}


	public Collection getAllRouteMasterInfo(String requestedDate) {
		// TODO Auto-generated method stub		
		return (List) this.routeDataHolder.get(requestedDate);
	}
	
	
	public Collection getAllEmployeeInfo(EmployeeManagerI mgr) {
		// TODO Auto-generated method stub	
		this.manager=mgr;
		return  (List)this.employeeDataHolder.get();
	}
	
	
	public Collection getAllTerminatedEmployeeInfo(EmployeeManagerI mgr) {
		// TODO Auto-generated method stub	
		this.manager=mgr;
		return  (List)this.terminatedEmployeeDataHolder.get();
	}
	
	
	public EmployeeInfo getEmployeeInfo(String empId,EmployeeManagerI mgr)
	{	
		this.manager=mgr;
		List empList = (List) this.employeeDataHolder.get();
		if(empList==null) return null;
		for(int i=0;i<empList.size();i++){
			EmployeeInfo info=(EmployeeInfo)empList.get(i);
			if(info.getEmployeeId().equalsIgnoreCase(empId))
				return info;
		}
		return null;
	}
	
	public ErpRouteMasterInfo getRouteMasterInfo(String routeNumber,Date requestedDate)
	{	
		if(requestedDate==null)
		{
			requestedDate=new Date();
		}
		try {
			List trkList = (List) this.routeDataHolder.get(TransStringUtil.getServerDate(requestedDate));
			if(trkList==null) return null;
			for(int i=0;i<trkList.size();i++){
				ErpRouteMasterInfo info=(ErpRouteMasterInfo)trkList.get(i);
				if(info.getRouteNumber().equalsIgnoreCase(routeNumber))
					return info;
			}
		}catch(Exception ex){
			throw new RuntimeException("Exception Occurred while getting Route details for route number "+routeNumber);
		}
		return null;
	}	
}
