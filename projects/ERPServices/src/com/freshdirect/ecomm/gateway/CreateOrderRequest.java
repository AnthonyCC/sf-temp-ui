/**
 * 
 */
package com.freshdirect.ecomm.gateway;

import java.util.Set;

import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.ErpCreateOrderModel;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.customer.FDActionInfo;

/**
 * @author tbalumuri
 *
 */
public class CreateOrderRequest {

	public CreateOrderRequest(FDActionInfo info, ErpCreateOrderModel createOrder, Set<String> appliedPromos, String id,
			boolean sendEmail, CustomerRatingI cra, CrmAgentRole crmAgentRole, EnumDlvPassStatus status,
			boolean isBulkOrder) {
		super();
		this.info = info;
		this.createOrder = createOrder;
		this.appliedPromos = appliedPromos;
		this.id = id;
		this.sendEmail = sendEmail;
		this.cra = cra;
		this.crmAgentRole = crmAgentRole;
		this.status = status;
		this.isBulkOrder = isBulkOrder;
	}
	public CreateOrderRequest() {
		super();
	}
	private FDActionInfo info;
	private ErpCreateOrderModel createOrder;
	private Set<String> appliedPromos;
	private String id;
	private boolean sendEmail;
	private CustomerRatingI cra;
	private CrmAgentRole crmAgentRole;
	private EnumDlvPassStatus status;
	private boolean isBulkOrder;
	public FDActionInfo getInfo() {
		return info;
	}
	public void setInfo(FDActionInfo info) {
		this.info = info;
	}
	public ErpCreateOrderModel getCreateOrder() {
		return createOrder;
	}
	public void setCreateOrder(ErpCreateOrderModel createOrder) {
		this.createOrder = createOrder;
	}
	public Set<String> getAppliedPromos() {
		return appliedPromos;
	}
	public void setAppliedPromos(Set<String> appliedPromos) {
		this.appliedPromos = appliedPromos;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isSendEmail() {
		return sendEmail;
	}
	public void setSendEmail(boolean sendEmail) {
		this.sendEmail = sendEmail;
	}
	public CustomerRatingI getCra() {
		return cra;
	}
	public void setCra(CustomerRatingI cra) {
		this.cra = cra;
	}
	public CrmAgentRole getCrmAgentRole() {
		return crmAgentRole;
	}
	public void setCrmAgentRole(CrmAgentRole crmAgentRole) {
		this.crmAgentRole = crmAgentRole;
	}
	public EnumDlvPassStatus getStatus() {
		return status;
	}
	public void setStatus(EnumDlvPassStatus status) {
		this.status = status;
	}
	public boolean isBulkOrder() {
		return isBulkOrder;
	}
	public void setBulkOrder(boolean isBulkOrder) {
		this.isBulkOrder = isBulkOrder;
	}
	
}
