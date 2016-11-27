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

public interface AirclicManagerDaoI {
	
	Collection getEmployees(Date date, String zone);
	
	Set getSignature(final Date date, final String zone, final String empId);
		

}
