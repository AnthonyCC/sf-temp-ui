package com.freshdirect.transadmin.model;

import java.lang.reflect.Field;
import java.util.Date;

public class PunchInfo implements java.io.Serializable, TrnBaseEntityI, PunchInfoI {

	private Date _date=null;
	private Date startTime=null;
	private Date endTime=null;
	private Date inPunchDTM=null;
	private Date outPunchDTM=null;
	private boolean isPunchedIn;
	private boolean isPunchedOut;
	private boolean isLate;
	private String employeeId;
	
	public PunchInfo()
	{
		
	}
	public PunchInfo(Date eventDate,String employeeId, Date startTime, Date endTime, Date inPunchDTM, Date outPunchDTM) {
		_date=eventDate;
		this.employeeId=employeeId;
		this.startTime=startTime;
		this.endTime=endTime;
		this.inPunchDTM=inPunchDTM;
		this.outPunchDTM=outPunchDTM;
		/*this.isPunchedIn=isPunchedIn;
		this.isPunchedOut=isPunchedOut;
		this.isLate=isLate;*/
	}
	
	

	public boolean isObsoleteEntity() {
		return false;
	}

	public Date getDate() {
		return _date;
	}

	public Date getEndTime() {
		return endTime;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public boolean isLate() {
		return isLate;
	}

	public void setLate(boolean late) {
		this.isLate=late;
	}
	
	public boolean isPunchedIn() 
	{
		if(outPunchDTM==null&&inPunchDTM!=null) return true;		
		return false;
	}

	public boolean isPunchedOut() {
		if(outPunchDTM!=null&&inPunchDTM!=null) return true;		
		return false;
	}

	public String toString() {
		StringBuffer result = new StringBuffer();
	    String newLine = System.getProperty("line.separator");

	    result.append( this.getClass().getName() );
	    result.append( " instance {" );
	    result.append(newLine);

	    Field[] fields = this.getClass().getDeclaredFields();
	    for ( int i=0;i<fields.length;i++  ) {
	    	Field field=fields[i];
	      result.append("  ");
	      try {
	        result.append( field.getName() );
	        result.append(": ");
	        result.append( field.get(this) );
	      }
	      catch ( IllegalAccessException ex ) {
	      }
	      result.append(newLine);
	    }
	    result.append("}");
	    return result.toString();
	}



	public Date getInPunchDTM() {
		return inPunchDTM;
	}



	public Date getOutPunchDTM() {
		return outPunchDTM;
	}


}
