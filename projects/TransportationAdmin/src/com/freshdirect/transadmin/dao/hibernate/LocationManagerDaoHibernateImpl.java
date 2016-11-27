package com.freshdirect.transadmin.dao.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.freshdirect.transadmin.dao.LocationManagerDaoI;
import com.freshdirect.transadmin.model.CustomerInfo;
import com.freshdirect.transadmin.model.DeliveryGroup;
import com.freshdirect.transadmin.model.DlvBuilding;
import com.freshdirect.transadmin.model.DlvBuildingDetail;
import com.freshdirect.transadmin.model.DlvLocation;
import com.freshdirect.transadmin.model.DlvScenarioDay;
import com.freshdirect.transadmin.model.DlvServiceTimeScenario;
import com.freshdirect.transadmin.model.DlvServiceTimeType;
import com.freshdirect.transadmin.model.TrnFacility;
import com.freshdirect.transadmin.model.TrnFacilityLocation;
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
	
	public Collection getTrnFacilityByType(String facilityType) throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append(" from TrnFacility f ");
		strBuf.append(" where f.trnFacilityType.name='").append(facilityType).append("'");
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
	
	@SuppressWarnings("rawtypes")
	public Collection getTrnFacilityLocations() throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from TrnFacilityLocation ");
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
	
	public TrnFacilityLocation getTrnFacilityLocation(String id) throws DataAccessException {
		return (TrnFacilityLocation)getEntityById("TrnFacilityLocation","id",id);
	}

	@Override
	public Collection getCustomerInfo(final String context, final String id) throws DataAccessException {
		
		List ResultList = (List)getHibernateTemplate().execute(new HibernateCallback() {
		public Object doInHibernate(Session session) throws HibernateException, SQLException {
			Query query = null;
			
			if("location".equals(context)){
				query = session.createSQLQuery("SELECT A.CUSTOMER_ID, A.FIRST_NAME, A.LAST_NAME FROM CUST.ADDRESS A, DLV.DELIVERY_BUILDING B, " +
						"DLV.DELIVERY_LOCATION L  WHERE A.SCRUBBED_ADDRESS = B.SCRUBBED_STREET AND ((L.APARTMENT IS NOT NULL AND UPPER(L.APARTMENT) = UPPER(A.APARTMENT)) " +
						"OR (L.APARTMENT IS NULL AND A.APARTMENT IS NULL)) AND L.BUILDINGID = B.ID " +
						"and L.ID = ?")
					.addScalar("CUSTOMER_ID", Hibernate.STRING)
					.addScalar("FIRST_NAME", Hibernate.STRING)
					.addScalar("LAST_NAME", Hibernate.STRING);
			}else{
				query = session.createSQLQuery("SELECT A.CUSTOMER_ID, A.FIRST_NAME, A.LAST_NAME FROM CUST.ADDRESS A, DLV.DELIVERY_BUILDING B WHERE A.SCRUBBED_ADDRESS = B.SCRUBBED_STREET " +
					" AND B.ID = ?")
					.addScalar("CUSTOMER_ID", Hibernate.STRING)
					.addScalar("FIRST_NAME", Hibernate.STRING)
					.addScalar("LAST_NAME", Hibernate.STRING);
			}
			query.setParameter(0, id);
			List list = query.list();
			List<CustomerInfo> customerList = new ArrayList<CustomerInfo>();
			Iterator it = list.iterator();
			while(it.hasNext()){
				Object[] row = (Object[])it.next();
				CustomerInfo customerInfo = new CustomerInfo();
				customerInfo.setCustomerId((String)row[0]);
				customerInfo.setFirstName((String)row[1]);
				customerInfo.setLastName((String)row[2]);
				customerList.add(customerInfo);
			}
			return (Collection)customerList;
			}
		});
		return ResultList;
		
		}
}
