
package com.freshdirect.fdstore.ecomm.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.ecommerce.data.customer.FDUserData;
import com.freshdirect.fdstore.customer.ExternalCampaign;
import com.freshdirect.fdstore.customer.FDRecipientList;
import com.freshdirect.fdstore.customer.SavedRecipientModel;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;
import com.freshdirect.fdstore.promotion.AssignedCustomerParam;

public class RecognizedUserData implements Serializable {

	private static final long serialVersionUID = 8232352098448470220L;
	
	private FDUserData fdUserData;
	
	private Map<String, AssignedCustomerParam> asssignedCustomerParam;
	
	private Set<ExternalCampaign> externalCampaign;
	
	private FDUserDlvPassInfo dlvPassInfo;
	
	private List<SavedRecipientModel> receipts;
	private FDRecipientList receipentList;
	private List<ErpOrderLineModel> orderLines;
	
	private AddressModel address;
	
	public RecognizedUserData() {
		
	}

	public FDUserData getFdUserData() {
		return fdUserData;
	}

	public void setFdUserData(FDUserData fdUserData) {
		this.fdUserData = fdUserData;
	}

	public Map<String, AssignedCustomerParam> getAsssignedCustomerParam() {
		return asssignedCustomerParam;
	}

	public void setAsssignedCustomerParam(Map<String, AssignedCustomerParam> asssignedCustomerParam) {
		this.asssignedCustomerParam = asssignedCustomerParam;
	}

	public Set<ExternalCampaign> getExternalCampaign() {
		return externalCampaign;
	}

	public void setExternalCampaign(Set<ExternalCampaign> externalCampaign) {
		this.externalCampaign = externalCampaign;
	}

	public FDUserDlvPassInfo getDlvPassInfo() {
		return dlvPassInfo;
	}

	public void setDlvPassInfo(FDUserDlvPassInfo dlvPassInfo) {
		this.dlvPassInfo = dlvPassInfo;
	}

	public List<SavedRecipientModel> getReceipts() {
		return receipts;
	}

	public void setReceipts(List<SavedRecipientModel> receipts) {
		this.receipts = receipts;
	}

	public List<ErpOrderLineModel> getOrderLines() {
		return orderLines;
	}

	public void setOrderLines(List<ErpOrderLineModel> orderLines) {
		this.orderLines = orderLines;
	}

	public AddressModel getAddress() {
		return address;
	}

	public void setAddress(AddressModel address) {
		this.address = address;
	}

	public FDRecipientList getReceipentList() {
		return receipentList;
	}

	public void setReceipentList(FDRecipientList receipentList) {
		this.receipentList = receipentList;
	}
	
}
