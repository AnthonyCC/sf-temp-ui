/**
 * 
 * ErpComplaintInfo.java
 * Created Dec 23, 2002
 */
package com.freshdirect.customer;

/**
 *
 *  @author knadeem
 */
import com.freshdirect.framework.core.ModelSupport;

public class ErpComplaintInfoModel extends ModelSupport {
	
	private String customerId;
	private ErpComplaintModel complaint;
	
	public ErpComplaintInfoModel(String customerId, ErpComplaintModel complaint){
		this.customerId = customerId;
		this.complaint = complaint;
	}
	
	public String getCustomerId(){
		return this.customerId;
	}
	
	public ErpComplaintModel getComplaint(){
		return this.complaint;
	} 

}
