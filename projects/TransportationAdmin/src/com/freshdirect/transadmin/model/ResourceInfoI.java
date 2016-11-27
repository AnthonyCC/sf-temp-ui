package com.freshdirect.transadmin.model;

import java.util.Date;
import java.util.List;

import com.freshdirect.transadmin.web.model.AssetScanInfo;

public interface ResourceInfoI {

	public String getName();
	
	/**
	 * @return the firstName
	 */
	public String getFirstName();


	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName);


	/**
	 * @return the lastName
	 */
	public String getLastName();


	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName);


	/**
	 * @return the employeeId
	 */
	public String getEmployeeId() ;


	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(String employeeId);
	

	public String getNextelNo() ;

	public void setNextelNo(String nextelNo) ;
	
	public PunchInfoI getPunchInfo();
	
	public void setPunchInfo(PunchInfoI punchInfo);
	
	public Date getAdjustmentTime();
	public void setAdjustmentTime(Date adjustmentTime);
	public List<AssetScanInfo> getScannedAssets();
	public void setScannedAssets(List<AssetScanInfo> scannedAssets);
}
