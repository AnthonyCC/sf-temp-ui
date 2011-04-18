package com.freshdirect.transadmin.dao.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.freshdirect.transadmin.dao.DomainManagerDaoI;
import com.freshdirect.transadmin.model.Asset;
import com.freshdirect.transadmin.model.AssetAttribute;
import com.freshdirect.transadmin.model.DispositionType;
import com.freshdirect.transadmin.model.EmployeeRole;
import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.model.EmployeeSubRoleType;
import com.freshdirect.transadmin.model.IssueLog;
import com.freshdirect.transadmin.model.IssueSubType;
import com.freshdirect.transadmin.model.IssueType;
import com.freshdirect.transadmin.model.MaintenanceIssue;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.RouteMappingId;
import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.model.VIRRecord;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.model.ZonetypeResource;
import com.freshdirect.transadmin.util.TransStringUtil;

public class DomainManagerDaoHibernateImpl
		extends BaseManagerDaoHibernateImpl  implements DomainManagerDaoI {


	public Collection getZones() throws DataAccessException {

		return getDataList("Zone Order By ZONE_CODE");
	}

	public Collection getAreas() throws DataAccessException {

		return getDataList("TrnArea Order By CODE");
	}

	public Collection getAdHocRoutes() throws DataAccessException {

		return getDataList("TrnAdHocRoute where OBSOLETE IS NULL Order By ROUTE_ID");
	}

	public Collection getZoneTypes() throws DataAccessException {

		return getDataList("TrnZoneType Order By ID");
	}

	public Collection getMarkedAreas() throws DataAccessException {

		return getDataList("TrnArea where ACTIVE = 'X' Order By CODE");
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

	public Zone getZone(String code) throws DataAccessException  {
		return (Zone)getEntityById("Zone","zoneCode",code);
	}


	public TrnAdHocRoute getAdHocRoute(String id) throws DataAccessException  {
		return (TrnAdHocRoute)getEntityById("TrnAdHocRoute","routeId",id);
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

		
	public void saveRouteNumberGroup(final Map routeMapping) throws DataAccessException {
		
		List _routeMapping = new ArrayList();
		if(routeMapping != null) {
			Iterator _iterator = routeMapping.keySet().iterator();
			while(_iterator.hasNext()) {
				final RouteMappingId mappingId = (RouteMappingId)_iterator.next();
				_routeMapping.addAll((Collection)routeMapping.get(mappingId));
				this.getHibernateTemplate().execute(new HibernateCallback() {
					 public Object doInHibernate(Session session) throws HibernateException, SQLException {
					 
						 //Query query = session.createQuery("delete from RouteMapping where routeMappingId.routeDate = :rdate and routeMappingId.cutOffId = :coid and routeMappingId.groupCode = :gcode");
						 Query query = session.createQuery("delete from RouteMapping where ROUTE_DATE = :rdate and CUTOFF_ID = :coid and GROUP_CODE = :gcode");
						 query.setDate("rdate", mappingId.getRouteDate());
						 query.setString("coid", mappingId.getCutOffId());
						 query.setString("gcode", mappingId.getGroupCode());
						 
						 int rowCount = query.executeUpdate();
					     
						 return null;
					 }
				});
			}
			this.saveEntityList(_routeMapping);
		}
	}
	
	public Collection getRouteMapping(String routeDate, String routeId) throws DataAccessException {
		
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("RouteMapping tr WHERE tr.routeMappingId.routeDate='"+routeDate+"'");
		if(routeId != null) {
			strBuf.append(" and tr.routeMappingId.routeID='"+routeId+"'");
		}
		strBuf.append(" order by tr.routeMappingId.routeID");
		return (Collection) getDataList(strBuf.toString());		
	}
	
	public Collection getRouteMappingByCutOff(String routeDate, String cutOff) throws DataAccessException {
		
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("RouteMapping tr WHERE tr.routeMappingId.routeDate='"+routeDate+"'");
		if(cutOff != null) {
			strBuf.append(" and tr.routeMappingId.cutOffId='"+cutOff+"'");
		}
		
		return (Collection) getDataList(strBuf.toString());		
	}

	public Collection getDeliveryModels() throws DataAccessException {
		return getDataList("TrnDeliveryModel");
	}

	public Collection getEmployeeRoleTypes() throws DataAccessException {
		return getDataList("EmployeeRoleType Order By code");
	}

	public Collection getEmployeeSubRoleTypes() throws DataAccessException {
		return getDataList("EmployeeSubRoleType Order By code");
	}
	public Collection getZonetypeResources(String zoneTypeId) throws DataAccessException {
//		System.out.println("Inside get zone type resources $$$$$$$$$$$$$");
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from ZonetypeResource ztr");
		strBuf.append(" where ztr.id.zonetypeId='").append(zoneTypeId).append("'");
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}

	public Collection getRegions() throws DataAccessException {
		// TODO Auto-generated method stub
		return getDataList("Region where OBSOLETE IS NULL Order By CODE");
	}

	public Collection getDispositionTypes() throws DataAccessException {
		return getDataList("DispositionType Order By code");
	}

	public Region getRegion(String code) throws DataAccessException {
		// TODO Auto-generated method stub
		return (Region)getEntityById("Region","code",code);
	}

	// employee
	public Collection getEmployeeRole(String empId) throws DataAccessException {
		// TODO Auto-generated method stub
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from EmployeeRole te where  te.id.kronosId ='").append(empId).append("'");
		Collection c=(Collection) getHibernateTemplate().find(strBuf.toString());
		for(Iterator it=c.iterator();it.hasNext();)
		{
			EmployeeRole e=(EmployeeRole)it.next();
			e.migrate();
		}
		return c;
	}
	
	public Collection getTeamInfo()  throws DataAccessException {
		
		return getDataList("EmployeeTeam t ORDER BY t.leadKronosId");
	}
	
	public Collection getTeamMembersByEmployee(String empId)  throws DataAccessException {
		
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from EmployeeTeam te ");
		if(empId!=null)
			strBuf.append("where  te.leadKronosId =(select tx.leadKronosId " +
										"from EmployeeTeam tx where tx.kronosId ='").append(empId).append("')");
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
	
	public Collection getTeamByEmployee(String empId)  throws DataAccessException {
		
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from EmployeeTeam te ");
		if(empId!=null)
			strBuf.append("where  te.kronosId ='").append(empId).append("'");
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
	
	public Collection getTeamByLead(String leadId) throws DataAccessException {
		// TODO Auto-generated method stub
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from EmployeeTeam te where  te.leadKronosId ='").append(leadId).append("'");
		return (Collection) getHibernateTemplate().find(strBuf.toString());		
	}


	public Collection getEmployeeRoles() throws DataAccessException {
		// TODO Auto-generated method stub
		return getDataList("EmployeeRole ORDER BY ROLE");
	}


	public EmployeeRoleType getEmployeeRoleType(String roleTypeId) throws DataAccessException {
		// TODO Auto-generated method stub

		return (EmployeeRoleType) getEntityById("EmployeeRoleType","code",roleTypeId);
	}
	public EmployeeSubRoleType getEmployeeSubRoleType(String subRoleTypeId) throws DataAccessException {
		// TODO Auto-generated method stub

		return (EmployeeSubRoleType) getEntityById("EmployeeSubRoleType","code",subRoleTypeId);
	}

	public DispositionType getDispositionType(String dispCode) throws DataAccessException {
		// TODO Auto-generated method stub

		return (DispositionType) getEntityById("DispositionType","code",dispCode);
	}
	// not used
	public Collection getEmployees() throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection getEmployeesByRoleType(String roleTypeId) throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from EmployeeRole er");
		strBuf.append(" where er.id.role='").append(roleTypeId).append("'");
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}

	public void saveZoneType(TrnZoneType zoneType) throws DataAccessException {

		if(zoneType.getZoneTypeId()==null ||"".equals(zoneType.getZoneTypeId())) {
			Set resources=zoneType.getZonetypeResources();
			zoneType.setZonetypeResources(null);
			getHibernateTemplate().saveOrUpdate(zoneType);
			if(resources!=null && resources.size()>0) {
				Iterator it=resources.iterator();
				while(it.hasNext()) {
					ZonetypeResource ztr=(ZonetypeResource)it.next();
					ztr.getId().setZonetypeId(zoneType.getZoneTypeId());
				}
			}
			zoneType.setZonetypeResources(resources);
			saveEntityList(zoneType.getZonetypeResources());

		}
		else {
			saveEntity(zoneType);
		}


	}

	public Collection getActiveZones() throws DataAccessException {
		// TODO Auto-generated method stub
		return getDataList("Zone where OBSOLETE IS NULL Order By ZONE_CODE");
	}
	
	public Collection getScheduleEmployee(String employeeId, String weekOf) throws DataAccessException {
		// TODO Auto-generated method stub
		return getDataList("ScheduleEmployee where employeeId ='"+employeeId+"' and week_of='"+weekOf+"'");
	}
	
	public Collection getScheduleEmployees(String weekOf, String day) throws DataAccessException {
		// TODO Auto-generated method stub
		return getDataList("ScheduleEmployee where day='"+day+"' and week_of='"+weekOf+"' and region is not null" );
	}

	public Collection getScheduleEmployee(String employeeId, String weekOf, String day) throws DataAccessException {
		return getDataList("ScheduleEmployee where employeeId ='"+employeeId+"' and day='"+day+"' and week_of='"+weekOf+"'");
	}

	public Collection getUPSRouteInfo(String routeDate) {
		// TODO Auto-generated method stub
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("UPSRouteInfo u WHERE u.routeDate='"+routeDate+"'");		
		return (Collection) getDataList(strBuf.toString());		
	}

	public Collection getEmployeeStatus(String empId)
			throws DataAccessException {
		
		StringBuffer strBuf = new StringBuffer();
		
		strBuf.append("from EmployeeStatus e ");
		if (empId != null)
			strBuf.append("where  e.personnum ='").append(empId).append("'");
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
	
	public Collection getDeliveryGroups() throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from DeliveryGroup e ");
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}

	public Collection getIssueTypes() throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from IssueType Order By issueTypeName");
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
	
	public IssueType getIssueType(String issueTypeName) throws DataAccessException {

		return (IssueType)getEntityById("IssueType", "issueTypeName", issueTypeName);
	}
	
	public IssueType getIssueTypeById(String issueTypeId) throws DataAccessException {

		return (IssueType)getEntityById("IssueType", "issueTypeId", issueTypeId);
	}
	
	public IssueSubType getIssueSubType(String issueSubTypeName) throws DataAccessException {

		return (IssueSubType)getEntityById("IssueSubType", "issueSubTypeName", issueSubTypeName);
	}
	
	public Collection getIssueSubTypes() throws DataAccessException {

		return getDataList("IssueSubType Order By issueSubTypeName");
	}
	
	public Collection getVIRRecords() throws DataAccessException {

		return getDataList("VIRRecord Order By createDate desc");
	}
	
	public Collection getVIRRecords(Date createDate, String truckNumber) throws DataAccessException {
		
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from VIRRecord v where");
				
		if(createDate != null && !"".equals(createDate)  )
			strBuf.append(" trunc(v.createDate) = ? ");		
		if(createDate != null && !"".equals(createDate) && truckNumber != null && !"".equals(truckNumber))
			strBuf.append(" and");
		if (truckNumber != null && !truckNumber.equals(""))
			strBuf.append(" v.truckNumber ='").append(truckNumber).append("'");		
		
		return (Collection) getHibernateTemplate().find(strBuf.toString(),new Object[] { createDate });
}

	
	public VIRRecord getVIRRecord(String id) throws DataAccessException{
		return (VIRRecord)getEntityById("VIRRecord", "id", id);
	}
	
	public Collection getMaintenanceIssue(String truckNumber, String issueType, String issueSubType) throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from MaintenanceIssue m ");
		if(truckNumber != null && issueType !=null && issueSubType != null){
			strBuf.append("where m.truckNumber='").append(truckNumber).append("'");
			strBuf.append("and m.issueType='").append(issueType).append("'");
			strBuf.append("and m.issueSubType='").append(issueSubType).append("'");
			strBuf.append("and m.issueStatus='Open'");
		}
		
		return (Collection)getHibernateTemplate().find(strBuf.toString());
	}
	
	public Collection getMaintenanceIssue(IssueType issueType, IssueSubType issueSubType) throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from MaintenanceIssue m ");
		if(issueType !=null && issueSubType != null){			
			strBuf.append("where  m.issueType='").append(issueType).append("'");
			strBuf.append("and m.issueSubType='").append(issueSubType).append("'");
			strBuf.append("and m.issueStatus='Open'");
		}
		
		return (Collection)getHibernateTemplate().find(strBuf.toString());
	}
	public MaintenanceIssue getMaintenanceIssue(String id) throws DataAccessException{
		return (MaintenanceIssue)getEntityById("MaintenanceIssue", "id", id);
	}
	
	public Collection getMaintenanceIssues() throws DataAccessException {

		return getDataList("MaintenanceIssue Order By createDate desc");
	}
	
	public Collection getMaintenanceIssues(String issueStatus, String serviceStatus) throws DataAccessException {
		
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("MaintenanceIssue m where");
		if(issueStatus != null && !"".equals(issueStatus))
			strBuf.append(" m.issueStatus='"+issueStatus+"'");
		if( serviceStatus !=null && issueStatus !=null && !"".equals(issueStatus) && !"".equals(serviceStatus))
			strBuf.append(" and");
		if(serviceStatus != null && !"".equals(serviceStatus))
			strBuf.append(" m.serviceStatus='"+serviceStatus+"'");		
		
		return (Collection) getDataList(strBuf.toString());		
	}
	
	public void saveMaintenanceIssue(MaintenanceIssue command) throws DataAccessException {
		if (command.getId() == null || "".equals(command.getId())) {
			getHibernateTemplate().save(command);		
		}else{
			saveEntity(command);		
		}		
	}
	
	public String saveVIRRecord(VIRRecord virRecord){
				
		if (virRecord.getId() == null || "".equals(virRecord.getId())) {
			Set attributes = virRecord.getVirRecordIssues();
			virRecord.setVirRecordIssues(null);
			getHibernateTemplate().save(virRecord);
			if (attributes != null && attributes.size() > 0) {
				Iterator it = attributes.iterator();
				while (it.hasNext()) {
					IssueLog log = (IssueLog) it.next();
					log.setVirRecord(virRecord);
				}
			}
			virRecord.setVirRecordIssues(attributes);
			saveEntityList(virRecord.getVirRecordIssues());

		} 
		return virRecord.getId();
	}

}
