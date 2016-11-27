package com.freshdirect.transadmin.service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.transadmin.model.SectorZipcode;
import com.freshdirect.transadmin.model.ZipCodeModel;
import com.freshdirect.transadmin.web.model.TimeRange;

public interface ZoneManagerI extends BaseManagerI {
	
	Collection getActiveZoneCodes();

	Collection getActiveZoneCodes(String date);

	Map<String, List<TimeRange>> getWindowSteeringDiscounts(Date deliveryDate);

	Collection getDefaultZoneSupervisor(String zoneCode, String dayPart,
			String date);

	Set getDeliverableZipCodes();

	boolean checkNavTechZipCodeInfo(String zipCode);

	boolean checkZipCodeInfo(String zipCode);

	boolean checkWorkTabZipCodeInfo(String zipCode);

	void addNewDeliveryZipCode(ZipCodeModel model) throws SQLException;

	void addNewDeliveryZipCodeCoverage(ZipCodeModel model) throws SQLException;

	void updateDeliveryZipCodeCoverage(ZipCodeModel model) throws SQLException;

	Set<ZipCodeModel> getZipCodeInfo(final String zipCode);

	Map<String, SectorZipcode> getSectorZipCodeInfo(String neighbourhoodName);
}
