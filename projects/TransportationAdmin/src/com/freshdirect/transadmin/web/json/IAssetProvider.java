package com.freshdirect.transadmin.web.json;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.web.model.ScheduleCheckResult;

public interface IAssetProvider {
	
	String saveAsset(String assetId, String assetType
			, String assetNo, String description
			, String status
			, String[][] assetAttributes);
}
