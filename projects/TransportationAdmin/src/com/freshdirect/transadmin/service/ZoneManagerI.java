package com.freshdirect.transadmin.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.transadmin.web.model.TimeRange;

public interface ZoneManagerI extends BaseManagerI {
	
	Collection getActiveZoneCodes();
	Collection getActiveZoneCodes(String date);
	Map<String, List<TimeRange>> getWindowSteeringDiscounts(Date deliveryDate);
}
