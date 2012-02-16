package com.freshdirect.transadmin.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.model.SectorZipcode;
import com.freshdirect.transadmin.model.ZipCodeModel;
import com.freshdirect.transadmin.web.model.TimeRange;

public interface ZoneManagerDaoI {

	Collection getActiveZoneCodes() throws DataAccessException;

	Collection getActiveZoneCodes(String date) throws DataAccessException;

	Map<String, List<TimeRange>> getWindowSteeringDiscounts(Date deliveryDate) throws DataAccessException;

	Collection getDefaultZoneSupervisor(String zoneCode, String dayPart, String date) throws DataAccessException;

	Set getDeliverableZipCodes() throws DataAccessException;

	boolean checkNavTechZipCodeInfo(final String zipCode) throws DataAccessException;

	boolean checkZipCodeInfo(final String zipCode) throws DataAccessException;

	boolean checkWorkTabZipCodeInfo(final String zipCode) throws DataAccessException;

	void addNewDeliveryZipCode(ZipCodeModel model) throws SQLException;

	void addNewDeliveryZipCodeCoverage(ZipCodeModel model) throws SQLException;

	void updateDeliveryZipCodeCoverage(ZipCodeModel model) throws SQLException;

	Set<ZipCodeModel> getZipCodeInfo(String zipCode) throws DataAccessException;

	Map<String, SectorZipcode> getSectorZipCodeInfo(String neighbourhoodName) throws DataAccessException;

}
