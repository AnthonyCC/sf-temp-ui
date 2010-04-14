package com.freshdirect.transadmin.web.model;

import java.util.Date;

import com.freshdirect.transadmin.model.PunchInfoI;
import com.freshdirect.transadmin.util.EnumStatus;
import com.freshdirect.transadmin.web.model.DispatchCommand;

public class FulltimePunchInfo implements PunchInfoI 
{

	private DispatchCommand command ;
	
	public FulltimePunchInfo(DispatchCommand command )
	{
		this.command=command;
	}
	public Date getDate() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getEmployeeId() {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getEndTime() {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getInPunchDTM() {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getOutPunchDTM() {
		if(EnumStatus.OffPremises.equals(command.getDispatchStatus())) return new Date();
		return null;
	}

	public Date getStartTime() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isLate() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isPunchedIn() {
		if(EnumStatus.OffPremises.equals(command.getDispatchStatus()))	return false;
		return true;
	}

	public boolean isPunchedOut() 
	{
		if(EnumStatus.CheckedIn.equals(command.getDispatchStatus()))	return true;
		return false;
		//return true;
	}
	


}
