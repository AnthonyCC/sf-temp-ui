package com.freshdirect.transadmin.model;

import java.util.Date;

import com.freshdirect.transadmin.util.TransStringUtil;

public class ActivityLog implements java.io.Serializable, TrnBaseEntityI
{
    private String logId;
	private String userId;
	private Date date;
	private int type;
	private String id;
	private String fieldName;
	private String oldValue;
	private String newValue;
	private String dateString;
	private String typeString;
	
	public String getTypeString()
	{
		switch(type)
		{
			case 1: return "PLAN";
			case 2: return "PLAN RESOURCE";
			case 3: return "DISPATCH";
			case 4: return "DISPATCH RESOURCE";
			default :return "";
		}
		
	}
	
	public boolean isObsoleteEntity() {
		// TODO Auto-generated method stub
		return false;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getDateString() 
	{
		String result=null;
		try {
			if(date!=null)
			result= TransStringUtil.getServerTime(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	
	

}
