package com.freshdirect.transadmin.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpRouteMasterInfo;
import com.freshdirect.customer.ErpTruckMasterInfo;
import com.freshdirect.framework.util.ExpiringReference;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sap.SapProperties;
import com.freshdirect.sap.command.SapRouteMasterInfo;
import com.freshdirect.sap.command.SapTruckMasterInfo;
import com.freshdirect.sap.ejb.SapException;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.service.EmployeeManagerI;

public class TransAdminCacheManager {

	private List internalProgIdList = new ArrayList();
	private static TransAdminCacheManager instance = null;

	private EmployeeManagerI manager = null;

	private static Category LOGGER = LoggerFactory.getInstance(TransAdminCacheManager.class);

	@SuppressWarnings("rawtypes")
	private ExpiringReference truckDataHolder = new ExpiringReference(
			TransportationAdminProperties.getTruckCacheExpiryTime() * 60 * 1000) {
		protected Object load() {
			try {
				if (!SapProperties.isBlackhole()) {
					return loadAllTruckData();
				}
			} catch (SapException e) {
				LOGGER.error("Could not load load Referral program due to: ", e);
			}
			return Collections.EMPTY_MAP;
		}
	};

	private TrnAdmExpiringReference routeDataHolder = new TrnAdmExpiringReference(
			TransportationAdminProperties.getRouteCacheExpiryTime() * 60 * 1000) {

		protected Object load(Object requestParam) {
			try {
				if (!SapProperties.isBlackhole()) {
					return loadAllRouteData(requestParam);
				}
			} catch (SapException e) {
				LOGGER.error("Could not load load Referral program due to: ", e);
			}
			return Collections.EMPTY_LIST;
		}

	};
	
	private TrnAdmExpiringReference punchInfoDataHolder = new TrnAdmExpiringReference(
			TransportationAdminProperties.getPunchInfoCacheExpiryTime() * 60 * 1000) {

		protected Object load(Object requestParam) {
			try {
				if (!TransportationAdminProperties.isKronosBlackhole()) {
					return loadPunchInfoData(requestParam);
				}
			} catch (Exception e) {
				LOGGER.error("Could not load punch info due to: ", e);
			}
			return Collections.EMPTY_LIST;
		}

	};

	private CustomExpiringReference employeeDataHolder = new CustomExpiringReference
													(TransportationAdminProperties.getEmployeeCacheExpiryTime() * 60 * 1000
															, CustomExpiringReference.STORE_EMPLOYEEDATA) {

		protected Object load() {
			try {
				if(TransportationAdminProperties.isKronosBlackhole()) {
					return this.getEx();
				} else {
					List data = loadAllEmployeeData();
					/*if(data != null && data.size() > 0)
						this.writeToStore(data);*/
					return data != null && data.size() > 0 ? data : null;
				}
			} catch (SapException e) {
				LOGGER.error("Could not load load Referral program due to: ", e);
			}
			return null;
		}
	};

	private CustomExpiringReference terminatedEmployeeDataHolder = new CustomExpiringReference
												(TransportationAdminProperties.getEmployeeCacheExpiryTime() * 60 * 1000
														, CustomExpiringReference.STORE_TERMINATEDEMPLOYEEDATA) {

		protected Object load() {
			try {
				if(TransportationAdminProperties.isKronosBlackhole()) {
					return this.getEx();
				} else {
					List data = loadAllTerminatedEmployeeData();
					/*if(data != null && data.size() > 0)
						this.writeToStore(data);*/
					return data != null && data.size() > 0 ? data : null;					
				}				
			} catch (SapException e) {
				LOGGER.error("Could not load load Referral program due to: ", e);
			}
			return null;
		}
	};


	
	class EmployeeActiveInactiveReference extends CustomExpiringReference {
				
		public EmployeeActiveInactiveReference(long refreshPeriod) {
			super(refreshPeriod, STORE_ACTINACTEMPLOYEEDATA);
		}
				
		protected Object load() {
			try {
				if(TransportationAdminProperties.isKronosBlackhole()) {
					return this.getEx();
				} else {
					List <EmployeeInfo> _listInfo = loadActiveInactiveEmployeeData();
					if(_listInfo == null || _listInfo.size() == 0) {
						_listInfo = (List <EmployeeInfo>)this.getEx();
					}
					Map<String, EmployeeInfo> employeeMapping = new HashMap<String, EmployeeInfo>();
					if(_listInfo != null) {
						for(EmployeeInfo _info : _listInfo){
							employeeMapping.put(_info.getEmployeeId(), _info);
						}
					}
					return employeeMapping != null && employeeMapping.size() > 0 ? employeeMapping : null;					
				}				
			} catch (SapException e) {
				LOGGER.error("Could not load load Referral program due to: ", e);
			}
			return Collections.EMPTY_LIST;
		}

		protected Map<String, EmployeeInfo> getEmployeeMapping() {
			return (Map<String, EmployeeInfo>)this.get();
		}
	}
	
	private EmployeeActiveInactiveReference activeInactivedEmployeeDataHolder = new EmployeeActiveInactiveReference
																					(TransportationAdminProperties.getEmployeeCacheExpiryTime() * 60 * 1000);	

	private TransAdminCacheManager(){
	}


	public static synchronized TransAdminCacheManager getInstance(){
		if(instance==null){
			instance=new TransAdminCacheManager();
		}
		return instance;
	}

	public void refreshCacheData(EnumCachedDataType dataType) {

		if (EnumCachedDataType.TRUCK_DATA.equals(dataType)) {
			if (!(truckDataHolder.getLastRefresh() < TransportationAdminProperties
					.getEmployeeCacheMinExpiryTime()))
				truckDataHolder.forceRefresh();
		}
		if (EnumCachedDataType.EMPLOYEE_DATA.equals(dataType)) {
			if (!(employeeDataHolder.getLastRefresh() < TransportationAdminProperties
					.getEmployeeCacheMinExpiryTime()))
				employeeDataHolder.forceRefresh();
			if (!(activeInactivedEmployeeDataHolder.getLastRefresh() < TransportationAdminProperties
					.getEmployeeCacheMinExpiryTime()))
				activeInactivedEmployeeDataHolder.forceRefresh();
			if (!(terminatedEmployeeDataHolder.getLastRefresh() < TransportationAdminProperties
					.getEmployeeCacheMinExpiryTime()))
				terminatedEmployeeDataHolder.forceRefresh();
		}
	}

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
		return (Map) this.truckDataHolder.get();
	}

	public ErpTruckMasterInfo getTruckMasterInfo(String truckNumber) {
		Map trkList = (Map) this.truckDataHolder.get();
		if (trkList != null && trkList.containsKey(truckNumber))
			return (ErpTruckMasterInfo) trkList.get(truckNumber);
		return null;
	}

	public Collection getAllRouteMasterInfo(String requestedDate) {
		return (List) this.routeDataHolder.get(requestedDate);
	}

	public Collection getAllEmployeeInfo(EmployeeManagerI mgr) {
		this.manager = mgr;
		if (null != (this.employeeDataHolder.get())) {
			return new ArrayList((List) this.employeeDataHolder.get());
		} else {
			return new ArrayList();
		}
	}

	public Collection getActiveInactiveEmployeeInfo(EmployeeManagerI mgr) {
		this.manager = mgr;
		if (null != (this.activeInactivedEmployeeDataHolder.get())) {
			return new ArrayList((((Map) this.activeInactivedEmployeeDataHolder.get()).values()));
		} else {
			return new ArrayList();
		}
	}

	public Collection getAllTerminatedEmployeeInfo(EmployeeManagerI mgr) {
		this.manager = mgr;
		if (null != (this.terminatedEmployeeDataHolder.get())) {
			return new ArrayList((List) this.terminatedEmployeeDataHolder.get());
		} else {
			return new ArrayList();
		}
	}

	public EmployeeInfo getEmployeeInfo(String empId, EmployeeManagerI mgr) {
		this.manager = mgr;
		List empList = (List) this.employeeDataHolder.get();
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
		// added new code
		/*
		 * List empList = (List) this.activeInactivedEmployeeDataHolder.get();
		 * if(empList!=null) { for(int i=0;i<empList.size();i++){ EmployeeInfo
		 * info=(EmployeeInfo)empList.get(i); if(info != null &&
		 * info.getEmployeeId().equalsIgnoreCase(empId)) return info; }
		 * 
		 * } return null;
		 */

		return this.activeInactivedEmployeeDataHolder.getEmployeeMapping().get(empId);
	}

	public ErpRouteMasterInfo getRouteMasterInfo(String routeNumber, Date requestedDate) {
		if (requestedDate == null) {
			requestedDate = new Date();
		}
		try {
			List trkList = (List) this.routeDataHolder.get(TransStringUtil
					.getServerDate(requestedDate));
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
	
	public Map getActiveInactiveEmployees(EmployeeManagerI mgr) {
		this.manager=mgr;
		return  ((Map)this.activeInactivedEmployeeDataHolder.get());
	}
	
	public Collection loadPunchInfoData(Object  requestedDate) throws SapException {		
		return this.manager.getPunchInfo((String)requestedDate);
	}
	
	public Collection getPunchInfo(String requestedDate, EmployeeManagerI mgr) {		
		this.manager = mgr;
		return (Collection) this.punchInfoDataHolder.get(requestedDate);
	}
	
}
