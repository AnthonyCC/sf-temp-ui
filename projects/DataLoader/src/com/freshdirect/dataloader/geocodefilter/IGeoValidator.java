package com.freshdirect.dataloader.geocodefilter;

import java.io.IOException;
import java.sql.Connection;

import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdstore.FDResourceException;

public interface IGeoValidator {
	
	boolean validateAddress(Connection conn, Object object, boolean filterRestricted)  throws FDResourceException, FDInvalidAddressException;
	
	void initialize();
	
	void flushResults(String destination) throws IOException;
}
