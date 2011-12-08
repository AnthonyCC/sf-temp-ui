package com.freshdirect.transadmin.dao.hibernate;

import java.util.Collection;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.dao.LocationManagerDaoI;
import com.freshdirect.transadmin.model.DeliveryGroup;
import com.freshdirect.transadmin.model.DlvBuilding;
import com.freshdirect.transadmin.model.DlvBuildingDetail;
import com.freshdirect.transadmin.model.DlvLocation;
import com.freshdirect.transadmin.model.DlvScenarioDay;
import com.freshdirect.transadmin.model.DlvServiceTimeScenario;
import com.freshdirect.transadmin.model.DlvServiceTimeType;
import com.freshdirect.transadmin.model.TrnFacility;
import com.freshdirect.transadmin.model.TrnFacilityType;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.util.TransStringUtil;


public class LocationManagerDaoHibernateImpl extends BaseManagerDaoHibernateImpl  implements LocationManagerDaoI  {

	public Collection getServiceTimeTypes() throws DataAccessException {

		return getDataList("DlvServiceTimeType");
	}

	public DlvServiceTimeType getServiceTimeType(String id) throws DataAccessException {

		return (DlvServiceTimeType)getEntityById("DlvServiceTimeType","id",id);
	}

	public DlvServiceTimeScenario getServiceTimeScenario(String code) {
		return (DlvServiceTimeScenario)getEntityById("DlvServiceTimeScenario","code",code);
	}

	public DlvServiceTimeScenario getDefaultServiceTimeScenario() {
		return (DlvServiceTimeScenario)getEntityById("DlvServiceTimeScenario","isDefault","X");
	}

	public Collection getServiceTimes() throws DataAccessException {

		return getDataList("DlvServiceTime");
	}
	
	public Collection getZonesForServiceTimeTypes(List serviceTypeLst) throws DataAccessException {
		DetachedCriteria crit = DetachedCriteria.forClass(Zone.class);
		crit.add(Expression.in("defaultServiceTimeType.code", serviceTypeLst));
		return this.getHibernateTemplate().findByCriteria(crit);
	}

	public Collection getServiceTimeScenarios() throws DataAccessException {
		
		return getDataList("DlvServiceTimeScenario Order By code");
	}
	
	public void deleteServiceTimeScenario(DlvServiceTimeScenario scenario) throws DataAccessException {
		removeEntityEx(scenario);
	}
	
	public Collection getDlvServiceTimeScenarioDays() throws DataAccessException {		
		return getDataList("DlvScenarioDay");
	}
	
	public DlvScenarioDay getServiceTimeScenarioDay(String code) throws DataAccessException {
		
		return (DlvScenarioDay)getEntityById("DlvScenarioDay","scenariodayId",code);
	}
	
	public Collection getScenariosWithNoDay() throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from DlvServiceTimeScenario s");
		strBuf.append(" where 0=(select count(*) from DlvScenarioDay sd where sd.scenario.code=s.code)");
		return  getHibernateTemplate().find(strBuf.toString());
	}
	
	
	public Collection getDefaultServiceTimeScenarioDay() throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from DlvScenarioDay sd where sd.dayOfWeek is null and sd.normalDate is null)");
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
	
	public Collection getServiceTimeScenarios(String date) throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from DlvServiceTimeScenario s, DlvScenarioDay sd");
		strBuf.append(" where s.code=sd.scenario.code");
		strBuf.append(" and sd.normalDate=TO_DATE('").append(date).append("','mm/dd/yyyy')");
		strBuf.append(" Order By s.code");
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
	
	public Collection getServiceTimeScenariosForDayofWeek(int dayOfWeek) throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from DlvServiceTimeScenario s, DlvScenarioDay sd");
		strBuf.append(" where s.code=sd.scenario.code");
		strBuf.append(" and sd.dayOfWeek=").append(dayOfWeek);
		strBuf.append(" Order By s.code");
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}

	public Collection getDeliveryLocations(String srubbedAddress, String apt, String zipCode
												, String confidence, String quality) throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from DlvLocation dl");
		boolean hasCondition = false;

		hasCondition = appendLocationQuery(strBuf, "building.srubbedStreet", srubbedAddress, hasCondition);

		hasCondition = appendLocationQuery(strBuf, "apartment", apt, hasCondition);

		hasCondition = appendLocationQuery(strBuf, "building.zip", zipCode, hasCondition);
		hasCondition = appendLocationQuery(strBuf, "building.geocodeConfidence", confidence, hasCondition);
		hasCondition = appendLocationQuery(strBuf, "building.geocodeQuality", quality, hasCondition);

		strBuf.append(" order by dl.building.srubbedStreet, dl.apartment desc");

		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}

	public Collection getDeliveryLocations(String buildingId) throws DataAccessException {
		return (Collection) getHibernateTemplate().find("from DlvLocation dl where dl.building.buildingId ='"+buildingId+"'");
	}

	public Collection getDeliveryBuildings(String srubbedAddress, String zipCode, String confidence, String quality, String group) throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from DlvBuilding dl");
		boolean hasCondition = false;
				
		hasCondition = appendLocationQuery(strBuf, "srubbedStreet", srubbedAddress, hasCondition);

		hasCondition = appendLocationQuery(strBuf, "zip", zipCode, hasCondition);
		hasCondition = appendLocationQuery(strBuf, "geocodeConfidence", confidence, hasCondition);
		hasCondition = appendLocationQuery(strBuf, "geocodeQuality", quality, hasCondition);
		hasCondition = appendLocationQuery(strBuf, "buildingGroups.groupId", group, hasCondition);

		strBuf.append(" order by dl.srubbedStreet desc");

		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}

	public DlvBuilding getDlvBuilding(String id) throws DataAccessException {
		return (DlvBuilding)getEntityById("DlvBuilding","id",id);
	}

	private boolean appendLocationQuery(StringBuffer strBuf, String field, String value, boolean currentState) {

		if(!TransStringUtil.isEmpty(value) && !value.equals("null")) {
			if(currentState) {
				strBuf.append(" and dl.").append(field).append("='").append(value).append("'");
			} else {
				strBuf.append(" where dl.").append(field).append("='").append(value).append("'");
			}
			currentState = true;
		}
		return currentState;
	}
	public DlvLocation getDlvLocation(String id) throws DataAccessException {
		return (DlvLocation)getEntityById("DlvLocation","id",id);
	}

	public String[] getServiceTypes() throws DataAccessException {
		return new String[]{"HOME","CORPORATE","DEPOT","PICKUP"};
	}

    public DlvBuildingDetail getDlvBuildingDtl(String id) throws DataAccessException {
		//return (DlvBuildingDtl)getEntityById("DlvBuildingDtl","dlvBuildingDtlId",id);
		return (DlvBuildingDetail)getEntityById("DlvBuildingDetail","building.buildingId",id);
	}
	public Collection getDeliveryBuildingDetails(String srubbedAddress, String zipCode) throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from DlvBuildingDetail dl");
		boolean hasCondition = false;

		hasCondition = appendLocationQuery(strBuf, "building.srubbedStreet", srubbedAddress, hasCondition);


		hasCondition = appendLocationQuery(strBuf, "building.zip", zipCode, hasCondition);


		//strBuf.append(" order by dl.building.srubbedStreet desc");
//		System.out.println("getHibernateTemplate().find(strBuf.toString()) >"+getHibernateTemplate().find(strBuf.toString()));
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
	
	public Collection getDlvScenarioZones(String scenarioId)  throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from DlvScenarioZones sz");			
		strBuf.append(" where sz.scenarioZonesId.scenarioId='").append(scenarioId).append("'");		
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
	
	public Collection getDefaultZoneSupervisors(String zoneCode)  throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from ZoneSupervisor s");			
		strBuf.append(" where s.zone.zoneCode='").append(zoneCode).append("'");		
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
	
	public Collection getDeliveryGroups() throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from DeliveryGroup e ");
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
	
	public DeliveryGroup getDeliveryGroupById(String id) throws DataAccessException {

		return (DeliveryGroup)getEntityById("DeliveryGroup","id",id);
	}

	public Collection getTrnFacilitys() throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from TrnFacility ");
		return (Collection) getHibernateTemplate().find(strBuf.toString());
}
	
	public Collection getTrnFacilityTypes() throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from TrnFacilityType ");
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
	
	public TrnFacility getTrnFacility(String id) throws DataAccessException {
		return (TrnFacility)getEntityById("TrnFacility","id",id);
	}
	
	public TrnFacilityType getTrnFacilityType(String id) throws DataAccessException {
		return (TrnFacilityType)getEntityById("TrnFacilityType","id",id);
	}
}
