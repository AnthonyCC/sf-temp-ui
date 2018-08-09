package com.freshdirect.ecomm.model;

import java.util.Set;

import com.freshdirect.customer.ErpCreateOrderModel;
import com.freshdirect.ecommerce.data.customer.CrmAgentRoleData;
import com.freshdirect.ecommerce.data.customer.CustomerRatingAdapterData;
import com.freshdirect.ecommerce.data.list.FDActionInfoData;
import com.freshdirect.ecommerce.data.sap.ErpAbstractOrderModelData;

/**
 * @author tbalumuri
 *
 */
public class CreateOrderRequestData {

	public CreateOrderRequestData() {
		super();
	}
	public CreateOrderRequestData(FDActionInfoData info, ErpCreateOrderModel model, Set<String> appliedPromos,
			String id, boolean sendMail, CustomerRatingAdapterData cra, CrmAgentRoleData agentRole,
			String deliveryPassStatus, boolean bulkOrder) {
		super();
		this.info = info;
		this.model = model;
		this.appliedPromos = appliedPromos;
		this.id = id;
		this.sendMail = sendMail;
		this.cra = cra;
		this.agentRole = agentRole;
		this.deliveryPassStatus = deliveryPassStatus;
		this.bulkOrder = bulkOrder;
	}
	private FDActionInfoData info;
	private ErpCreateOrderModel model;
	Set<String> appliedPromos;
	private String id;
	private boolean sendMail;
	private CustomerRatingAdapterData cra;
	private CrmAgentRoleData agentRole;
	private String deliveryPassStatus;
	private boolean bulkOrder;
	private boolean isRealTimeAuthNeeded;
	private boolean isOptIn;
	private boolean isFriendReferred;
	private int fdcOrderCount; 
	public FDActionInfoData getInfo() {
		return info;
	}
	public ErpCreateOrderModel getModel() {
		return model;
	}
	public Set<String> getAppliedPromos() {
		return appliedPromos;
	}
	public String getId() {
		return id;
	}
	public boolean isSendMail() {
		return sendMail;
	}
	public CustomerRatingAdapterData getCra() {
		return cra;
	}
	public CrmAgentRoleData getAgentRole() {
		return agentRole;
	}
	public String getDeliveryPassStatus() {
		return deliveryPassStatus;
	}
	public boolean isBulkOrder() {
		return bulkOrder;
	}
	public void setInfo(FDActionInfoData info) {
		this.info = info;
	}
	public void setModel(ErpCreateOrderModel model) {
		this.model = model;
	}
	public void setAppliedPromos(Set<String> appliedPromos) {
		this.appliedPromos = appliedPromos;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setSendMail(boolean sendMail) {
		this.sendMail = sendMail;
	}
	public void setCra(CustomerRatingAdapterData cra) {
		this.cra = cra;
	}
	public void setAgentRole(CrmAgentRoleData agentRole) {
		this.agentRole = agentRole;
	}
	public void setDeliveryPassStatus(String deliveryPassStatus) {
		this.deliveryPassStatus = deliveryPassStatus;
	}
	public void setBulkOrder(boolean bulkOrder) {
		this.bulkOrder = bulkOrder;
	}
	public boolean isRealTimeAuthNeeded() {
		return isRealTimeAuthNeeded;
	}
	public void setRealTimeAuthNeeded(boolean isRealTimeAuthNeeded) {
		this.isRealTimeAuthNeeded = isRealTimeAuthNeeded;
	}
	public boolean isOptIn() {
		return isOptIn;
	}
	public void setOptIn(boolean isOptIn) {
		this.isOptIn = isOptIn;
	}
	public boolean isFriendReferred() {
		return isFriendReferred;
	}
	public void setFriendReferred(boolean isFriendReferred) {
		this.isFriendReferred = isFriendReferred;
	}
	public int getFdcOrderCount() {
		return fdcOrderCount;
	}
	public void setFdcOrderCount(int fdcOrderCount) {
		this.fdcOrderCount = fdcOrderCount;
	}
}
