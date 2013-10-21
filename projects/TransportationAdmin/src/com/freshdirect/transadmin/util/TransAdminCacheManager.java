package com.freshdirect.transadmin.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpRouteMasterInfo;
import com.freshdirect.customer.ErpTruckMasterInfo;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.model.EquipmentType;
import com.freshdirect.sap.SapProperties;
import com.freshdirect.sap.command.SapRouteMasterInfo;
import com.freshdirect.sap.command.SapTruckMasterInfo;
import com.freshdirect.sap.ejb.SapException;
import com.freshdirect.transadmin.cache.AsyncCacheException;
import com.freshdirect.transadmin.cache.AsyncCacheExceptionType;
import com.freshdirect.transadmin.cache.BaseAsyncCache;
import com.freshdirect.transadmin.model.EmployeeInfo;

import com.freshdirect.transadmin.security.AuthUser;
import com.freshdirect.transadmin.service.AssetManagerI;

import com.freshdirect.transadmin.model.EventLogRouteModel;
import com.freshdirect.transadmin.service.DomainManagerI;

import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.service.IEventLogManagerService;
import com.freshdirect.transadmin.service.exception.TransAdminServiceException;

public class TransAdminCacheManager {
	
	private static TransAdminCacheManager instance = null;

	private EmployeeManagerI manager = null;

	private AssetManagerI assetMgr = null;
	private IEventLogManagerService eventLogMngService = null;
	
	private DomainManagerI domainMngService = null;



	private static Category LOGGER = LoggerFactory.getInstance(TransAdminCacheManager.class);
	
	protected static final String STORE_EMPLOYEEPUNCHINFODATA = "TRANSAPP_CACHE_EMPLOYEE_PUNCHINFODATA.ser";
	
	protected static final String STORE_TRUCKINFODATA = "TRANSAPP_CACHE_TRUCKINFODATA.ser";
	
	protected static final String STORE_ROUTEINFODATA = "TRANSAPP_CACHE_ROUTEINFODATA.ser";
	
	protected static final String STORE_EMPLOYEEDATA = "TRANSAPP_CACHE_EMPLOYEEDATA.ser";
	
	protected static final String STORE_TERMINATEDEMPLOYEEDATA = "TRANSAPP_CACHE_TERMINATED_EMPLOYEEDATA.ser";
	
	protected static final String STORE_ACTINACTEMPLOYEEDATA = "TRANSAPP_CACHE_ACTINACT_EMPLOYEEDATA.ser";
	
	protected static final String STORE_EQUIPMENTDATA = "TRANSAPP_CACHE_EQUIPMENTTYPES.ser";
	

	/* Event log data holder */
	protected static final String STORE_HANDOFF_ROUTEINFODATA = "TRANSAPP_CACHE_HANDOFFROUTEINFODATA.ser";
	
	/* AdHoc Route data holder */
	protected static final String STORE_ADHOCROUTEINFODATA = "TRANSAPP_CACHE_ADHOCROUTEINFODATA.ser";
	
	/* EventLog AuthUser holder */
	protected static final String STORE_AUTHUSERDATA = "TRANSAPP_CACHE_AUTHUSERINFODATA.ser";

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
			if (!(truckDataHolder.getLastRefresh() < TransportationAdminProperties.getTruckCacheMinExpiryTime())) {
				truckDataHolder.forceRefresh();
			}
		}
		if (EnumCachedDataType.ROUTE_DATA.equals(dataType)) {
			if (!(routeDataHolder.getLastRefresh() < TransportationAdminProperties.getRouteCacheMinExpiryTime())) {
				routeDataHolder.forceRefresh();
			}
		}
		if (EnumCachedDataType.EMPLOYEE_DATA.equals(dataType)) {
			if (!(employeeDataHolder.getLastRefresh() < TransportationAdminProperties.getEmployeeCacheMinExpiryTime())) {
				employeeDataHolder.forceRefresh();
			}
			if (!(activeInactivedEmployeeDataHolder.getLastRefresh() < TransportationAdminProperties.getEmployeeCacheMinExpiryTime())) {
				activeInactivedEmployeeDataHolder.forceRefresh();
			}
			if (!(terminatedEmployeeDataHolder.getLastRefresh() < TransportationAdminProperties.getEmployeeCacheMinExpiryTime())) {
				terminatedEmployeeDataHolder.forceRefresh();
			}
		}
	}
	
	/**
	 * Cache to SAP Master truck information from SAP
	 */
	@SuppressWarnings("rawtypes")
	private BaseAsyncCache<String, Map<String, ErpTruckMasterInfo>> truckDataHolder = new BaseAsyncCache<String, Map<String, ErpTruckMasterInfo>>(
			TransportationAdminProperties.getTruckCacheExpiryTime() * 60 * 1000, STORE_TRUCKINFODATA) {
		protected Map<String, ErpTruckMasterInfo> loadData(String requestParam) throws AsyncCacheException {
			try {
				if (SapProperties.isBlackhole()) {
					throw new AsyncCacheException(AsyncCacheExceptionType.LOAD_BLOCKED);
				} else {
					return loadAllTruckData();
				}
			} catch (Exception e) {
				LOGGER.error("Could not load truck data due to: ", e);
				throw new AsyncCacheException(e, AsyncCacheExceptionType.LOAD_FAILED);
			}
		}
		
		protected Map<String, ErpTruckMasterInfo> loadDefault(String requestParam) {
			return new HashMap<String, ErpTruckMasterInfo>();
		}
	};
		
	/**
	 * Cache to SAP Route information from SAP
	 */
	private BaseAsyncCache<String, List> routeDataHolder = new BaseAsyncCache<String, List>(
			TransportationAdminProperties.getRouteCacheExpiryTime() * 60 * 1000, STORE_ROUTEINFODATA) {

		protected List loadData(String requestParam)  throws AsyncCacheException {
			try {
				if (SapProperties.isBlackhole()) {
					throw new AsyncCacheException(AsyncCacheExceptionType.LOAD_BLOCKED);
				} else {
					return loadAllRouteData(requestParam);		
				}
			} catch (Exception e) {
				LOGGER.error("Could not load route data from SAP: ", e);
				throw new AsyncCacheException(e, AsyncCacheExceptionType.LOAD_FAILED);
			}
		}
		
		protected List loadDefault(String requestParam) {
			return new ArrayList();
		}
	};
	
	/**
	 * Cache to store employee punch info from KRONOS
	 */
	private BaseAsyncCache<String, Collection> punchInfoDataHolder = new BaseAsyncCache <String, Collection>(
			TransportationAdminProperties.getPunchInfoCacheExpiryTime() * 60 * 1000, STORE_EMPLOYEEPUNCHINFODATA) {

		protected Collection loadData(String requestParam)  throws AsyncCacheException {
			try {
				if(TransportationAdminProperties.isKronosBlackhole()) {
					throw new AsyncCacheException(AsyncCacheExceptionType.LOAD_BLOCKED);
				} else {
					return loadPunchInfoData(requestParam);
				}
			} catch (Exception e) {				
				LOGGER.debug("Loading PunchInfo from store due to connectivity issue with Kronos database: ", e);
				throw new AsyncCacheException(e, AsyncCacheExceptionType.LOAD_FAILED);
			}
		}
		
		protected Collection loadDefault(String requestParam) {
			return new ArrayList();
		}
	};
	
	/**
	 * Cache to store all employee data from KRONOS database
	 */
	private BaseAsyncCache<String, List> employeeDataHolder = new BaseAsyncCache<String, List>
													(TransportationAdminProperties.getEmployeeCacheExpiryTime() * 60 * 1000
															, STORE_EMPLOYEEDATA) {

		protected List loadData(String requestParam) throws AsyncCacheException {
			try {
				if(TransportationAdminProperties.isKronosBlackhole()) {
					throw new AsyncCacheException(AsyncCacheExceptionType.LOAD_BLOCKED);
				} else {
					return loadAllEmployeeData();
				}
			} catch (Exception e) {
				LOGGER.error("Could not load Employee Data due to: ", e);
				throw new AsyncCacheException(e, AsyncCacheExceptionType.LOAD_FAILED);
			}
		}
		
		protected List loadDefault(String requestParam) {
			return new ArrayList();
		}
	};
	
	/**
	 * Cache to store all terminated employee data from KRONOS database
	 */
	private BaseAsyncCache<String, Map<String, EmployeeInfo>> terminatedEmployeeDataHolder = new BaseAsyncCache<String, Map<String, EmployeeInfo>>
												(TransportationAdminProperties.getEmployeeCacheExpiryTime() * 60 * 1000
														, STORE_TERMINATEDEMPLOYEEDATA) {

		protected Map<String, EmployeeInfo> loadData(String requestParam) throws AsyncCacheException  {
			try {
				if(TransportationAdminProperties.isKronosBlackhole()) {
					throw new AsyncCacheException(AsyncCacheExceptionType.LOAD_BLOCKED);
				} else {
					return loadAllTerminatedEmployeeData();					
				}				
			} catch (Exception e) {
				LOGGER.error("Could not load terminated employee data due to: ", e);
				throw new AsyncCacheException(e, AsyncCacheExceptionType.LOAD_FAILED);
			}
		}
		
		protected Map<String, EmployeeInfo> loadDefault(String requestParam) {
			return new HashMap<String, EmployeeInfo>();
		}
	};
	
	/**
	 * Cache to store equipment types
	 */
	private BaseAsyncCache<String, Map<String, List<EquipmentType>>> equipmentTypeDataHolder = new BaseAsyncCache<String, Map<String, List<EquipmentType>>>
												(TransportationAdminProperties.getEquipmentTypeCacheExpiryTime() * 60 * 1000
														, STORE_EQUIPMENTDATA) {

		protected Map<String, List<EquipmentType>> loadData(String requestParam) throws AsyncCacheException  {
			try {
					return loadEquipmentTypes();
			} catch (Exception e) {
				LOGGER.error("Could not load equipment type data due to: ", e);
				throw new AsyncCacheException(e, AsyncCacheExceptionType.LOAD_FAILED);
			}
		}
		
		protected Map<String, List<EquipmentType>> loadDefault(String requestParam) {
			return new HashMap<String, List<EquipmentType>>();
		}
	};
	
	
	/**
	 * Cache to store all terminated employee data from KRONOS database
	 */
	private BaseAsyncCache<String, Map<String, EmployeeInfo>> activeInactivedEmployeeDataHolder = new BaseAsyncCache<String, Map<String, EmployeeInfo>>
												(TransportationAdminProperties.getEmployeeCacheExpiryTime() * 60 * 1000
														, STORE_ACTINACTEMPLOYEEDATA) {
		protected Map<String, EmployeeInfo> loadData(String requestParam) throws AsyncCacheException  {
			try {
				if(TransportationAdminProperties.isKronosBlackhole()) {
					throw new AsyncCacheException(AsyncCacheExceptionType.LOAD_BLOCKED);
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
				LOGGER.error("Could not load active inactive employee due to: ", e);
				throw new AsyncCacheException(e, AsyncCacheExceptionType.LOAD_FAILED);
			}
		}
		
		protected Map<String, EmployeeInfo> loadDefault(String requestParam) {
			return new HashMap<String, EmployeeInfo>();
		}
	};

	public Map<String, EmployeeInfo> loadAllTerminatedEmployeeData() throws SapException {
		return  manager.getKronosTerminatedEmployees();
	}

	public Map<String, List<EquipmentType>> loadEquipmentTypes(){
		return  assetMgr.loadEquipmentTypes();
	}

	public List<EquipmentType> getEquipmentTypes(AssetManagerI mgr, String region) {
		this.assetMgr = mgr;
		if (null != (this.equipmentTypeDataHolder.get(""))) {
			return  this.equipmentTypeDataHolder.get("").get(region);
		} else {
			return new ArrayList<EquipmentType>();
		}
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

	public Map<String, ErpTruckMasterInfo> getAllTruckMasterInfo() {
		return this.truckDataHolder.get("");
	}

	public ErpTruckMasterInfo getTruckMasterInfo(String truckNumber) {
		Map trkList = this.truckDataHolder.get("");
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
		if (null != (this.employeeDataHolder.get(""))) {
			return new ArrayList((List) this.employeeDataHolder.get(""));
		} else {
			return new ArrayList();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getActiveInactiveEmployeeInfo(EmployeeManagerI mgr) {
		this.manager = mgr;
		if (null != (this.activeInactivedEmployeeDataHolder.get(""))) {
			return (Map) this.activeInactivedEmployeeDataHolder.get("");
		} else {
			return new HashMap();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, EmployeeInfo> getAllTerminatedEmployeeInfo(EmployeeManagerI mgr) {
		this.manager = mgr;
		if (null != (this.terminatedEmployeeDataHolder.get(""))) {
			return  this.terminatedEmployeeDataHolder.get("");
		} else {
			return new HashMap();
		}
	}

	public EmployeeInfo getEmployeeInfo(String empId, EmployeeManagerI mgr) {
		this.manager = mgr;
		List empList = (List) this.employeeDataHolder.get("");
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
		return this.activeInactivedEmployeeDataHolder.get("").get(empId);
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
		return  ((Map)this.activeInactivedEmployeeDataHolder.get(""));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection loadPunchInfoData(String  requestedDate) throws Exception {		
		return this.manager.getPunchInfo(requestedDate);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection getPunchInfo(String requestedDate, EmployeeManagerI mgr) {		
		this.manager = mgr;
		return (Collection) this.punchInfoDataHolder.get(requestedDate);
	}
	
	
	/**
	 * Cache to Route information from database (Handoff tables) 
	 */
	private BaseAsyncCache<String, Map<String, EventLogRouteModel>> eventLogRouteDataHolder = new BaseAsyncCache<String, Map<String, EventLogRouteModel>>(
			TransportationAdminProperties.getRouteCacheExpiryTime() * 60 * 1000, STORE_HANDOFF_ROUTEINFODATA) {

		protected Map<String, EventLogRouteModel> loadData(String requestParam)  throws AsyncCacheException {
			try {
				return lookUpRouteInfo(requestParam);
			} catch (Exception e) {
				LOGGER.error("Could not load Route data from database: ", e);
				throw new AsyncCacheException(e, AsyncCacheExceptionType.LOAD_FAILED);
			}
		}
		
		protected Map<String, EventLogRouteModel> loadDefault(String requestParam) {
			return new HashMap<String, EventLogRouteModel>();
		}
	};
	
	public Map<String, EventLogRouteModel> lookUpRouteInfo(Object requestParam) {
		Map<String, EventLogRouteModel> routeMapping = null;
		try {
			routeMapping = eventLogMngService.lookUpRoutes(TransStringUtil.getDate((String) requestParam));					
		} catch (TransAdminServiceException e) {
			LOGGER.error("lookUpRouteInfo# Delivery date: "+ (String) requestParam + " failed to populate route windows ", e);
		} catch (ParseException pe) {			
			LOGGER.error("lookUpRouteInfo# Unable to parse the date", pe);
		}
		return routeMapping; 
	}
	
	public Map<String, EventLogRouteModel> lookUpDeliveryRoutes(String deliveryDate, IEventLogManagerService eventLogService) {
		
		this.eventLogMngService = eventLogService;		
		
		Map<String, EventLogRouteModel> routeMapping = eventLogRouteDataHolder.get(deliveryDate);
		
		return routeMapping;
	}
	
	public Set<String> lookUpRoutes(String deliveryDate, IEventLogManagerService eventLogService) {
		
		this.eventLogMngService = eventLogService;		
		Set<String> routes = new TreeSet<String>();
		Map<String, EventLogRouteModel> routeMapping = eventLogRouteDataHolder.get(deliveryDate);
		routes.addAll(routeMapping.keySet());
		
		return routes;
	}
	
	public Set<String> lookUpRouteWindows(String deliveryDate, String routeNo, IEventLogManagerService eventLogService) { 
		this.eventLogMngService = eventLogService;		
		Set<String> routeWindows = new TreeSet<String>();
		Map<String, EventLogRouteModel> routeMapping = eventLogRouteDataHolder.get(deliveryDate);		
		if(routeMapping.get(routeNo) != null) {
			routeWindows.addAll(routeMapping.get(routeNo).getWindows());	
		}
		return routeWindows;
	}
	
	public Set<String> lookUpRouteStops(String deliveryDate, String routeNo, IEventLogManagerService eventLogService) {
		
		this.eventLogMngService = eventLogService;		
		Set<String> routeStops = new TreeSet<String>();
		Map<String, EventLogRouteModel> routeMapping = eventLogRouteDataHolder.get(deliveryDate);		
		if(routeMapping.get(routeNo) != null) {
			routeStops.addAll(routeMapping.get(routeNo).getStops());	
		}
		return routeStops;
	}
	

	/**
	 * Cache to addHocRoute information from database
	 */
	private BaseAsyncCache<String, List> addHocRouteDataHolder = new BaseAsyncCache<String, List>(
																TransportationAdminProperties.getEmployeeCacheExpiryTime() * 60 * 1000, STORE_ADHOCROUTEINFODATA) {

		protected List loadData(String requestParam) throws AsyncCacheException {
			try {
				return loadAddHocRouteInfo();
			} catch (Exception e) {
				LOGGER.error("Could not load Addhoc route data due to: ", e);
				throw new AsyncCacheException(e,
						AsyncCacheExceptionType.LOAD_FAILED);
			}
		}

		protected List loadDefault(String requestParam) {
			return new ArrayList();
		}
	};

	
	public List loadAddHocRouteInfo() {		
		return (List) domainMngService.getAdHocRoutes();	
	}
	
	public List lookUpAddHocRoute(DomainManagerI domainMngService) {
		
		this.domainMngService = domainMngService;		
		
		return addHocRouteDataHolder.get("");
		
	}
	
	/**
	 * Cache to AuthUser privilages information from database
	 */
	private BaseAsyncCache<String, Map<String, AuthUser>> authUserDataHolder = new BaseAsyncCache<String, Map<String, AuthUser>>(
			TransportationAdminProperties.getRouteCacheExpiryTime() * 60 * 1000, STORE_AUTHUSERDATA) {

		protected Map<String, AuthUser> loadData(String requestParam) throws AsyncCacheException {
			try {
				return loadAuthUserPrivileges();
			} catch (Exception e) {
				LOGGER.error("Could not load Auth User data due to: ", e);
				throw new AsyncCacheException(e,
						AsyncCacheExceptionType.LOAD_FAILED);
			}
		}

		protected Map<String, AuthUser> loadDefault(String requestParam) {
			return new HashMap<String, AuthUser>();
		}
	};
	
	public Map<String, AuthUser> loadAuthUserPrivileges() {		
		
		return  this.eventLogMngService.getAuthUserPrivileges();	
	}
	
	public AuthUser lookUpAuthUserPrivileges(String userName, IEventLogManagerService eventLogService) {
		this.eventLogMngService = eventLogService;
		Map<String, AuthUser> userMapping = authUserDataHolder.get("");
		for(Map.Entry<String, AuthUser> userEntry : userMapping.entrySet()){
			if(userEntry.getKey().equals(userName)){
				return userEntry.getValue();
			}
		}
		return new AuthUser();
	}
	
}
