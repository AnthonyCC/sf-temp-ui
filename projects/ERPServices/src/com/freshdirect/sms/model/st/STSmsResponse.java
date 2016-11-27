package com.freshdirect.sms.model.st;

import java.io.Serializable;
import java.util.Date;

/**
 * This bean holds the information from the response.
 * @author kkanuganti
 *
 */
public class STSmsResponse implements Serializable {

	private static final long serialVersionUID = 1260257840691077207L;
	/**
	 * captures the SMS priority 
	 */
	private byte sms_priority;
	/**
	 * captures the system Id 
	 */
	private int system_id;
	/**
	 * captures the status:SUCCESS/FAIL regarding the delivery if the SMS
	 *  
	 */
	private String status;
	/**
	 * captures the error code in case of failure 
	 */
	private int error;
	/**
	 * captures the detailed error description 
	 */
	private String error_description;
	/**
	 * captures the SMS category 
	 */
	private String sms_category;
	/**
	 * captures the mobile number the SMS was sent to 
	 */
	private long sms_to;
	/**
	 * captures the SMS message sent to the given mobile number 
	 */
	private String sms_msg;
	/**
	 * captures the SMS ID 
	 */
	private int sms_id;
	/**
	 * captures the SMS External ID type (FreshDirect) 
	 */
	private String sms_ext_id_type;
	
	private Date date;
	
	private String orderId;
	
	private String customerId;
	
	
	
	
	
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public byte getSms_priority() {
		return sms_priority;
	}
	public void setSms_priority(byte sms_priority) {
		this.sms_priority = sms_priority;
	}
	public int getSystem_id() {
		return system_id;
	}
	public void setSystem_id(int system_id) {
		this.system_id = system_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public String getError_description() {
		return error_description;
	}
	public void setError_description(String error_description) {
		this.error_description = error_description;
	}
	public String getSms_category() {
		return sms_category;
	}
	public void setSms_category(String sms_category) {
		this.sms_category = sms_category;
	}
	public long getSms_to() {
		return sms_to%10000000000L;
	}
	public void setSms_to(long sms_to) {
		this.sms_to = sms_to;
	}
	public String getSms_msg() {
		return sms_msg;
	}
	public void setSms_msg(String sms_msg) {
		this.sms_msg = sms_msg;
	}
	public int getSms_id() {
		return sms_id;
	}
	public void setSms_id(int sms_id) {
		this.sms_id = sms_id;
	}
	public String getSms_ext_id_type() {
		return sms_ext_id_type;
	}
	public void setSms_ext_id_type(String sms_ext_id_type) {
		this.sms_ext_id_type = sms_ext_id_type;
	}
}
