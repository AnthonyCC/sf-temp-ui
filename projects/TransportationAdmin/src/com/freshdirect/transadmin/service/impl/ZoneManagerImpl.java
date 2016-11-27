package com.freshdirect.transadmin.service.impl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.dao.ZoneManagerDaoI;
import com.freshdirect.transadmin.model.SectorZipcode;
import com.freshdirect.transadmin.model.ZipCodeModel;
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.web.model.TimeRange;

public class ZoneManagerImpl extends BaseManagerImpl implements ZoneManagerI {
	
	private ZoneManagerDaoI zoneManagerDAO = null;
	
	protected BaseManagerDaoI getBaseManageDao() {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection getActiveZoneCodes() {
		// TODO Auto-generated method stub
		return getZoneManagerDAO().getActiveZoneCodes();
	}

	public ZoneManagerDaoI getZoneManagerDAO() {
		return zoneManagerDAO;
	}

	public void setZoneManagerDAO(ZoneManagerDaoI zoneManagerDAO) {
		this.zoneManagerDAO = zoneManagerDAO;
	}

	public Collection getActiveZoneCodes(String date) {
		return getZoneManagerDAO().getActiveZoneCodes(date);
	}
	
	public Map<String, List<TimeRange>> getWindowSteeringDiscounts(Date deliveryDate) {
		return getZoneManagerDAO().getWindowSteeringDiscounts(deliveryDate);
	}
	
	public Collection getDefaultZoneSupervisor(String zoneCode, String dayPart,String date){
		return getZoneManagerDAO().getDefaultZoneSupervisor(zoneCode, dayPart,date);
	}
	
	public Set getDeliverableZipCodes() throws DataAccessException {
		return getZoneManagerDAO().getDeliverableZipCodes();
	}
	
	public boolean checkNavTechZipCodeInfo(final String zipCode) throws DataAccessException {
		return getZoneManagerDAO().checkNavTechZipCodeInfo(zipCode);
	}
	public boolean checkZipCodeInfo(final String zipCode) throws DataAccessException{
		return getZoneManagerDAO().checkZipCodeInfo(zipCode);
	}
	public boolean checkWorkTabZipCodeInfo(final String zipCode) throws DataAccessException {
		return getZoneManagerDAO().checkWorkTabZipCodeInfo(zipCode);
	}
	public void addNewDeliveryZipCode(ZipCodeModel model) throws DataAccessException, SQLException {
		getZoneManagerDAO().addNewDeliveryZipCode(model);
	}
	public void addNewDeliveryZipCodeCoverage(ZipCodeModel model) throws DataAccessException, SQLException {
		getZoneManagerDAO().addNewDeliveryZipCodeCoverage(model);
	}
	public void updateDeliveryZipCodeCoverage(ZipCodeModel model) throws DataAccessException, SQLException {
		getZoneManagerDAO().updateDeliveryZipCodeCoverage(model);
	}
	public 	Set<ZipCodeModel> getZipCodeInfo(final String zipCode){
		return getZoneManagerDAO().getZipCodeInfo(zipCode);
	}
	public Map<String, SectorZipcode> getSectorZipCodeInfo(String SectorName) {
		return getZoneManagerDAO().getSectorZipCodeInfo(SectorName);
	}
}
