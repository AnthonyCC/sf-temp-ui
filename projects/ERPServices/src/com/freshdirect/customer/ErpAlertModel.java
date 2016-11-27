/*
 * Created on Jun 30, 2005
 */
package com.freshdirect.customer;

import java.util.Date;
import com.freshdirect.framework.core.ModelSupport;

/**
 * @author jng
 */
public class ErpAlertModel extends ModelSupport {

	private String alertType;
	private Date createDate;
	private String createUserId;
	private String note;
	
	public String getAlertType() { return this.alertType; }
	public void setAlertType(String alertType) { this.alertType=alertType; } 
	public String getNote() { return this.note; }
	public void setNote(String note) { this.note=note; }
	public Date getCreateDate() { return this.createDate; }
	public void setCreateDate(Date createDate) { this.createDate=createDate; } 
	public String getCreateUserId() { return this.createUserId; }
	public void setCreateUserId(String createUserId) { this.createUserId=createUserId; }
	
}
