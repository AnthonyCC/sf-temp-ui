package com.freshdirect.transadmin.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpRouteMasterInfo;
import com.freshdirect.customer.ErpTruckMasterInfo;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sap.SapProperties;
import com.freshdirect.sap.command.SapRouteMasterInfo;
import com.freshdirect.sap.command.SapTruckMasterInfo;
import com.freshdirect.sap.ejb.SapException;
import com.freshdirect.transadmin.exception.TransAdminCacheException;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.service.EmployeeManagerI;

public class TransAdminCacheManager {
	
	private static TransAdminCacheManager instance = null;

	private EmployeeManagerI manager = null;

	private static Category LOGGER = LoggerFactory.getInstance(TransAdminCacheManager.class);
	
	private TransAdminCacheManager() {
	}

	public static synchronized TransAdminCacheManager getInstance() {
		if (instance == null) {
			instance = new TransAdminCacheManager();
		}
		return instance;
	}

	public void refreshCacheData(EnumCachedDataType dataType) {

		if (EnumCachedDataType.TRUCK_DATA.equals(dataType)) {
			if (!(truckDataHolder.getLastRefresh() < TransportationAdminProperties.getTruckCacheExpiryTime()))
				truckDataHolder.forceRefresh();
		}
		if (EnumCachedDataType.ROUTE_DATA.equals(dataType)) {
			if (!(routeDataHolder.getLastRefresh() < TransportationAdminProperties.getRouteCacheExpiryTime()))
				routeDataHolder.forceRefresh();
		}
		if (EnumCachedDataType.EMPLOYEE_DATA.equals(dataType)) {
			if (!(employeeDataHolder.getLastRefresh() < TransportationAdminProperties.getEmployeeCacheMinExpiryTime()))
				employeeDataHolder.forceRefresh();
			if (!(activeInactivedEmployeeDataHolder.getLastRefresh() < TransportationAdminProperties.getEmployeeCacheMinExpiryTime()))
				activeInactivedEmployeeDataHolder.forceRefresh();
			if (!(terminatedEmployeeDataHolder.getLastRefresh() < TransportationAdminProperties.getEmployeeCacheMinExpiryTime()))
				terminatedEmployeeDataHolder.forceRefresh();
		}
	}
	
	/**
	 * Cache to SAP Master truck information from SAP
	 */
	@SuppressWarnings("rawtypes")
	private TrnAdmExpiringReference truckDataHolder = new TrnAdmExpiringReference(
			TransportationAdminProperties.getTruckCacheExpiryTime() * 60 * 1000, TrnAdmExpiringReference.STORE_TRUCKINFODATA) {
		protected Object load(Object requestParam) {
			try {
				if (SapProperties.isBlackhole()) {
					throw new TransAdminCacheException("1004", null);
				} else {
					return loadAllTruckData();
				}
			} catch (SapException e) {
				LOGGER.error("Could not load load Referral program due to: ", e);
				throw new TransAdminCacheException("1003", e);
			}
		}
	};
		
	/**
	 * Cache to SAP Route information from SAP
	 */
	private TrnAdmExpiringReference routeDataHolder = new TrnAdmExpiringReference(
			TransportationAdminProperties.getRouteCacheExpiryTime() * 60 * 1000, TrnAdmExpiringReference.STORE_ROUTEINFODATA) {

		protected Object load(Object requestParam) {
			try {
				if (SapProperties.isBlackhole()) {
					throw new TransAdminCacheException("1004", null);
				} else {
					return loadAllRouteData(requestParam);		
				}
			} catch (SapException e) {
				LOGGER.error("Could not load route data from SAP: ", e);
				throw new TransAdminCacheException("1003", e);
			}
		}
	};
	
	/**
	 * Cache to store employee punch info from KRONOS
	 */
	private TrnAdmExpiringReference punchInfoDataHolder = new TrnAdmExpiringReference(
			TransportationAdminProperties.getPunchInfoCacheExpiryTime() * 60 * 1000, TrnAdmExpiringReference.STORE_EMPLOYEEPUNCHINFODATA) {

		protected Object load(Object requestParam) throws TransAdminCacheException {
			try {
				if(TransportationAdminProperties.isKronosBlackhole()) {
					throw new TransAdminCacheException("1004", null);
				} else {
					return loadPunchInfoData(requestParam);
				}
			} catch (Exception e) {				
				LOGGER.debug("Loading PunchInfo from store due to connectivity issue with Kronos database: ", e);
				throw new TransAdminCacheException("1003", e);
			}
		}
	};
	
	/**
	 * Cache to store all employee data from KRONOS database
	 */
	private TrnAdmExpiringReference employeeDataHolder = new TrnAdmExpiringReference
													(TransportationAdminProperties.getEmployeeCacheExpiryTime() * 60 * 1000
															, TrnAdmExpiringReference.STORE_EMPLOYEEDATA) {

		protected Object load(Object requestParam) {
			try {
				if(TransportationAdminProperties.isKronosBlackhole()) {
					throw new TransAdminCacheException("1004", null);
				} else {
					return loadAllEmployeeData();
				}
			} catch (Exception e) {
				LOGGER.error("Could not load load Referral program due to: ", e);
				throw new TransAdminCacheException("1003", e);
			}
		}
	};
	
	/**
	 * Cache to store all terminated employee data from KRONOS database
	 */
	private TrnAdmExpiringReference terminatedEmployeeDataHolder = new TrnAdmExpiringReference
												(TransportationAdminProperties.getEmployeeCacheExpiryTime() * 60 * 1000
														, TrnAdmExpiringReference.STORE_TERMINATEDEMPLOYEEDATA) {

		protected Object load(Object requestParam) {
			try {
				if(TransportationAdminProperties.isKronosBlackhole()) {
					throw new TransAdminCacheException("1004", null);
				} else {
					return loadAllTerminatedEmployeeData();					
				}				
			} catch (Exception e) {
				LOGGER.error("Could not load load Referral program due to: ", e);
				throw new TransAdminCacheException("1003", e);
			}
		}
	};

	@SuppressWarnings("unchecked")
	class EmployeeActiveInactiveReference extends TrnAdmExpiringReference {
				
		public EmployeeActiveInactiveReference(long refreshPeriod) {
			super(refreshPeriod, STORE_ACTINACTEMPLOYEEDATA);
		}
		
		protected Object load(Object requestParam) {
			try {
				if(TransportationAdminProperties.isKronosBlackhole()) {
					throw new TransAdminCacheException("1004", null);
				} else {
					List <EmployeeInfo> _listInfo = loadActiveInactiveEmployeeData();					
					Map<String, EmployeeInfo> employeeMapping = new HashMap<String, EmployeeInfo>();
					if(_listInfo != null) {
						for(EmployeeInfo _info : _listInfo){
							employeeMapping.put(_info.getEmployeeId(), _info);
						}
					}
					return employeeMapping != null && employeeMapping.size() > 0 ? employeeMapping : null;					
				}				
			} catch (Exception e) {
				LOGGER.error("Could not load load Referral program due to: ", e);
				throw new TransAdminCacheException("1003", e);
			}
		}

		protected Map<String, EmployeeInfo> getEmployeeMapping() {
			return (Map<String, EmployeeInfo>)this.get(null);
		}
	}
	
	/**
	 * Cache to store all active/Inactive employee data from KRONOS database
	 */
	private EmployeeActiveInactiveReference activeInactivedEmployeeDataHolder = new EmployeeActiveInactiveReference(TransportationAdminProperties.getEmployeeCacheExpiryTime() * 60 * 1000);

	public List loadAllTerminatedEmployeeData() throws SapException {
		return (List) manager.getKronosTerminatedEmployees();
	}

	public List loadAllEmployeeData() throws SapException {
		return (List) manager.getKronosEmployees();
	}

	public List loadActiveInactiveEmployeeData() throws SapException {
		return (List) manager.getKronosActiveInactiveEmployees();
	}

	public Map<String, ErpTruckMasterInfo> loadAllTruckData() throws SapException{
		SapTruckMasterInfo truckInfos = new SapTruckMasterInfo();
		truckInfos.execute();
		return truckInfos.getTruckMasterInfos();
	}

	public List loadAllRouteData(Object requestParam) throws SapException{
		SapRouteMasterInfo routeInfos = new SapRouteMasterInfo((String)requestParam);
		routeInfos.execute();
		return routeInfos.getRouteMasterInfos();
	}

	public Map getAllTruckMasterInfo() {
		return (Map) this.truckDataHolder.get(null);
	}

	public ErpTruckMasterInfo getTruckMasterInfo(String truckNumber) {
		Map trkList = (Map) this.truckDataHolder.get(null);
		if (trkList != null && trkList.containsKey(truckNumber))
			return (ErpTruckMasterInfo) trkList.get(truckNumber);
		return null;
	}

	public Collection getAllRouteMasterInfo(String requestedDate) {
		return (List) this.routeDataHolder.get(requestedDate);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection getAllEmployeeInfo(EmployeeManagerI mgr) {
		this.manager = mgr;
		if (null != (this.employeeDataHolder.get(null))) {
			return new ArrayList((List) this.employeeDataHolder.get(null));
		} else {
			return new ArrayList();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection getActiveInactiveEmployeeInfo(EmployeeManagerI mgr) {
		this.manager = mgr;
		if (null != (this.activeInactivedEmployeeDataHolder.get(null))) {
			return new ArrayList((((Map) this.activeInactivedEmployeeDataHolder.get(null)).values()));
		} else {
			return new ArrayList();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection getAllTerminatedEmployeeInfo(EmployeeManagerI mgr) {
		this.manager = mgr;
		if (null != (this.terminatedEmployeeDataHolder.get(null))) {
			return new ArrayList((List) this.terminatedEmployeeDataHolder.get(null));
		} else {
			return new ArrayList();
		}
	}

	public EmployeeInfo getEmployeeInfo(String empId, EmployeeManagerI mgr) {
		this.manager = mgr;
		List empList = (List) this.employeeDataHolder.get(null);
		if (empList == null)
			return null;
		for (int i = 0; i < empList.size(); i++) {
			EmployeeInfo info = (EmployeeInfo) empList.get(i);
			if (info != null && info.getEmployeeId().equalsIgnoreCase(empId))
				return info;
		}
		return null;
	}

	public EmployeeInfo getActiveInactiveEmployeeInfo(String empId,	EmployeeManagerI mgr) {
		this.manager = mgr;
		return this.activeInactivedEmployeeDataHolder.getEmployeeMapping().get(empId);
	}

	public ErpRouteMasterInfo getRouteMasterInfo(String routeNumber, Date requestedDate) {
		if (requestedDate == null) {
			requestedDate = new Date();
		}
		try {
			List trkList = (List) this.routeDataHolder.get(TransStringUtil.getServerDate(requestedDate));
			if (trkList == null)
				return null;
			for (int i = 0; i < trkList.size(); i++) {
				ErpRouteMasterInfo info = (ErpRouteMasterInfo) trkList.get(i);
				if (info.getRouteNumber().equalsIgnoreCase(routeNumber))
					return info;
			}
		} catch (Exception ex) {
			throw new RuntimeException(
					"Exception Occurred while getting Route details for route number "
							+ routeNumber);
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public Map<String, ErpRouteMasterInfo> getERPRouteInfo(Date requestedDate) {
		if (requestedDate == null) {
			requestedDate = new Date();
		}
		Map<String, ErpRouteMasterInfo> routeMapping = new HashMap<String, ErpRouteMasterInfo>();
		try {
			List trkList = (List) this.routeDataHolder.get(TransStringUtil.getServerDate(requestedDate));
			if (trkList == null)
				return null;
			for (int i = 0; i < trkList.size(); i++) {
				ErpRouteMasterInfo routeInfo = (ErpRouteMasterInfo) trkList.get(i);				
				if (!routeMapping.containsKey(routeInfo.getRouteNumber()))
					routeMapping.put(routeInfo.getRouteNumber(), routeInfo);					
			}
		} catch (Exception ex) {
			throw new RuntimeException("Exception Occurred while getting Route details from SAP");
		}
		return routeMapping;
	}
	
	public Map getActiveInactiveEmployees(EmployeeManagerI mgr) {
		this.manager = mgr;
		return  ((Map)this.activeInactivedEmployeeDataHolder.get(null));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection loadPunchInfoData(Object  requestedDate) throws Exception {		
		return this.manager.getPunchInfo((String)requestedDate);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection getPunchInfo(String requestedDate, EmployeeManagerI mgr) {		
		this.manager = mgr;
		return (Collection) this.punchInfoDataHolder.get(requestedDate);
	}
	
}
