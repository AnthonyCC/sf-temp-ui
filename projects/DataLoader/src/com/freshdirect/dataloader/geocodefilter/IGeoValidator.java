package com.freshdirect.dataloader.geocodefilter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.freshdirect.delivery.InvalidAddressException;
import com.freshdirect.delivery.ejb.DlvManagerSessionBean;

public interface IGeoValidator {
	
	boolean validateAddress(Connection conn, Object object, boolean filterRestricted)  throws SQLException,InvalidAddressException;
	
	void initialize(DlvManagerSessionBean dlvManager);
	
	void flushResults(String destination) throws IOException;
}
