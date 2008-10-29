package com.freshdirect.transadmin.dao.hibernate;

import java.util.Collection;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.dao.DomainManagerDaoI;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.model.TrnEmployee;
import com.freshdirect.transadmin.model.TrnRoute;
import com.freshdirect.transadmin.model.TrnTruck;
import com.freshdirect.transadmin.model.TrnZone;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.util.TransStringUtil;

public class DomainManagerDaoHibernateImpl
		extends BaseManagerDaoHibernateImpl  implements DomainManagerDaoI {

	public Collection getEmployees() throws DataAccessException {

		return getDataList("TrnEmployee where OBSOLETE IS NULL Order By  LAST_NAME");
	}

	public Collection getZones() throws DataAccessException {

		return getDataList("TrnZone where OBSOLETE IS NULL Order By ZONE_ID");
	}
	
	public Collection getAreas() throws DataAccessException {

		return getDataList("TrnArea Order By CODE");
	}

	public Collection getRoutes() throws DataAccessException {

		return getDataList("TrnRoute where OBSOLETE IS NULL Order By ROUTE_NUMBER");
	}
	
	public Collection getRouteForZone(String zoneId) throws DataAccessException {
		return getDataList("TrnRoute tr WHERE tr.obsolete IS NULL AND ZONE_ID='"+zoneId+"'  Order By ROUTE_NUMBER");
	}

	public Collection getTrucks() throws DataAccessException {

		return getDataList("TrnTruck where OBSOLETE IS NULL Order By TRUCK_NUMBER");
	}
	
	public Collection getZoneTypes() throws DataAccessException {

		return getDataList("TrnZoneType Order By ID");
	}
	
	public Collection getMarkedAreas() throws DataAccessException {

		return getDataList("TrnArea where ACTIVE = 'X' Order By CODE");
	}

	public Collection getEmployeeJobType() throws DataAccessException {

		return getDataList("TrnEmployeeJobType");
	}

	public Collection getTimeSlots() throws DataAccessException {
		return getDataList("TrnTimeslot  Order By SLOT_ID");
	}

	public String[] getDays() throws DataAccessException {
		return TransStringUtil.getDays();
	}

	public String[] getTimings() throws DataAccessException {
		return new String[]{"AM","PM"};
	}

	public String[] getTruckTypes() throws DataAccessException {
		return new String[]{"None","Pickup","Regular"};
	}

	public Collection getSupervisors() throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from TrnEmployee te where te.obsolete IS NULL AND te.trnEmployeeJobType.jobTypeId = '9'  Order By  LAST_NAME");

		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}


	public TrnEmployee getEmployee(String id) throws DataAccessException  {
		return (TrnEmployee)getEntityById("TrnEmployee","employeeId",id);
	}

	public TrnZone getZone(String id) throws DataAccessException  {
		return (TrnZone)getEntityById("TrnZone","zoneId",id);
	}
	
	
	public TrnRoute getRoute(String id) throws DataAccessException  {
		return (TrnRoute)getEntityById("TrnRoute","routeId",id);
	}

	public TrnTruck getTruck(String id) throws DataAccessException  {
		return (TrnTruck)getEntityById("TrnTruck","truckId",id);
	}
	
	public TrnZoneType getZoneType(String id) throws DataAccessException  {
		return (TrnZoneType)getEntityById("TrnZoneType","zoneTypeId",id);
	}
	
	public TrnArea getArea(String id) throws DataAccessException  {
		return (TrnArea)getEntityById("TrnArea","code",id);
	}
	
	public TrnCutOff getCutOff(String id) throws DataAccessException {		
		return (TrnCutOff)getEntityById("TrnCutOff","cutOffId",id);
	}
	
	public Collection getCutOffs() throws DataAccessException {
		return getDataList("TrnCutOff Order By  sequenceNo");
	}
	
	public Collection getRouteNumberGroup(String date, String cutOff, String area) throws DataAccessException {
		
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from TrnRouteNumber tr");
		strBuf.append(" where tr.id.routeDate='").append(date).append("'");
		
		if(cutOff != null) {
			strBuf.append(" and tr.id.cutOffId='").append(cutOff).append("'");
		}
		
		if(area != null) {
			strBuf.append(" and tr.id.areaCode='").append(area).append("'");
		}

		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
	
	public Collection getDeliveryModels() throws DataAccessException {
		return getDataList("TrnDeliveryModel");
	}

}
