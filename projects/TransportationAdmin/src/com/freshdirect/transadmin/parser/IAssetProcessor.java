package com.freshdirect.transadmin.parser;

import java.io.IOException;
import java.sql.SQLException;

import com.freshdirect.transadmin.service.AssetManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;

public interface IAssetProcessor {
	
	boolean processAssetRecord(Object object) throws SQLException;
	
	void initialize(AssetManagerI assetManagerService, DomainManagerI domainManagerService);
	
	void flushResults(String destination) throws IOException;
}
