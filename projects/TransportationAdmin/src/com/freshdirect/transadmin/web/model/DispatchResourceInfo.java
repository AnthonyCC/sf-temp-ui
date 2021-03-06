package com.freshdirect.transadmin.web.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.PunchInfoI;
import com.freshdirect.transadmin.model.ResourceInfoI;

/**
 * @author kkanuganti
 *
 */
public class DispatchResourceInfo extends BaseCommand implements ResourceInfoI { 
	private String nextelNo;
	private static final String SEPERATOR = " ";
	private String lastName;
	private String firstName;
	private String employeeId;
	private PunchInfoI punchInfo;
	private List<AssetScanInfo> scannedAssets;


	public String getName() {
		
		if((lastName==null ||"".equals(lastName))&&(firstName==null || "".equals(firstName)))
			return "";
		if(lastName==null || "".equals(lastName))
			return firstName;
		if(firstName==null || "".equals(firstName))
			return lastName;
		return lastName+SEPERATOR+firstName;
	}

	public String getNameWithFirstInitial() {
		if((lastName==null ||"".equals(lastName))&&(firstName==null || "".equals(firstName)))
			return "";
		if(lastName==null || "".equals(lastName))
			return firstName;
		if(firstName==null || "".equals(firstName))
			return lastName;
		return lastName+", "+firstName.substring(0,1);
	}
	
	public String toString() {
		
		return employeeId;
	}
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}


	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}


	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	/**
	 * @return the employeeId
	 */
	public String getEmployeeId() {
		return employeeId;
	}


	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	

	public String getNextelNo() {
		return this.nextelNo != null ? this.nextelNo : "";
	}

	public void setNextelNo(String nextelNo) {
		this.nextelNo = nextelNo;
	}

	public PunchInfoI getPunchInfo() {
		return punchInfo;
	}

	public void setPunchInfo(PunchInfoI punchInfo) {
		this.punchInfo=punchInfo;
		
	}

	public Date getAdjustmentTime() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setAdjustmentTime(Date adjustmentTime) {
		// TODO Auto-generated method stub
		
	}

	public List<AssetScanInfo> getScannedAssets() {
		return scannedAssets;
	}

	public void setScannedAssets(List<AssetScanInfo> scannedAssets) {
		this.scannedAssets = scannedAssets;
	}
	
}
