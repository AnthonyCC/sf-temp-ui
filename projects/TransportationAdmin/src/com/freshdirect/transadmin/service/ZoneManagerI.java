package com.freshdirect.transadmin.service;

import java.util.Collection;

public interface ZoneManagerI extends BaseManagerI {
	
	Collection getActiveZoneCodes();
	Collection getActiveZoneCodes(String date);
}
