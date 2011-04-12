package com.freshdirect.transadmin.util;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.transadmin.constants.EnumIssueStatus;
import com.freshdirect.transadmin.constants.EnumServiceStatus;
import com.freshdirect.transadmin.datamanager.model.ITruckScheduleInfo;
import com.freshdirect.transadmin.model.MaintenanceIssue;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.VIRRecord;
import com.freshdirect.transadmin.model.VerificationLog;
import com.freshdirect.transadmin.service.DomainManagerI;

public class MaintenanceLogUtil {
	
	public static List processVIRRecord(List errorList, VIRRecord command,
			DomainManagerI domainManagerService) {
	
		//Verify if any 'OPEN' Maintenance issue exists with the same truckNumber,IssueType and IssueSubType
		Collection maintenanceIssues = null;
		if(command.getIssueSubType() != null && command.getIssueSubType()!= null 
				&& "No Issue".equalsIgnoreCase(command.getIssueType().getIssueTypeName())
				&& "No Issue".equalsIgnoreCase(command.getIssueSubType().getIssueSubTypeName())){
			maintenanceIssues = domainManagerService.getMaintenanceIssue(command.getIssueType(), command.getIssueSubType());
		}else{
			maintenanceIssues = domainManagerService
					.getMaintenanceIssue(command.getTruckNumber(), command
							.getIssueType(), command.getIssueSubType());
		}
		
		if(maintenanceIssues != null && maintenanceIssues.size() > 0){
			for (Iterator<MaintenanceIssue> it = maintenanceIssues.iterator(); it.hasNext();) {
				MaintenanceIssue _mIssue =  it.next();
				command.setMaintenanceIssue(_mIssue);
			}			
			domainManagerService.saveEntityEx(command);
		}else{
			MaintenanceIssue mIssue = new MaintenanceIssue();
			
			mIssue.setTruckNumber(command.getTruckNumber());
			mIssue.setIssueType(command.getIssueType());
			mIssue.setIssueSubType(command.getIssueSubType());
			mIssue.setCreateDate(new Timestamp(System.currentTimeMillis()));
			mIssue.setCreatedBy(command.getCreatedBy());
			mIssue.setComments(command.getComments());
			mIssue.setDamageLocation(command.getDamageLocation());
			mIssue.setIssueSide(command.getIssueSide());
			mIssue.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			mIssue.setVendor(command.getVendor());
			mIssue.setServiceStatus(EnumServiceStatus.INSERVICE.getDescription());
			mIssue.setIssueStatus(EnumIssueStatus.OPEN.getName());
			
			try{				
				domainManagerService.saveEntityEx(mIssue);
				command.setMaintenanceIssue(mIssue);
				domainManagerService.saveEntityEx(command);
			}catch(Exception e){
				errorList.add("Adding VIR Record failed.");
			}
		}	
		
		return errorList;
	}
	
	
}
