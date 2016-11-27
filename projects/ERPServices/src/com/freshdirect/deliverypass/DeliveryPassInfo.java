package com.freshdirect.deliverypass;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.freshdirect.framework.util.DateUtil;

public class DeliveryPassInfo implements Serializable {
	private DeliveryPassModel model;
	private DlvPassUsageInfo usageInfo;
	
	public DeliveryPassInfo(DeliveryPassModel model, DlvPassUsageInfo usageInfo) {
		super();
		this.model = model;
		this.usageInfo = usageInfo;
	}

	public boolean isUnlimited() {
		return model.getType().isUnlimited();
	}
	public String getName(){
		return model.getType().getName();
	}
	
	public int getRemainingDlvs(){
		return model.getRemainingDlvs();
	}
	
	public String getStatusName(){
		if(isUnlimited()) {
			//Make sure the pass is not Expired. If expired return the status as expired.
			Date today = new Date();
			
			if(EnumDlvPassStatus.ACTIVE.equals(model.getStatus())&& today.after(getExpirationDate())){
				return EnumDlvPassStatus.EXPIRED.getDisplayName();
			}
		}
		return model.getStatus().getDisplayName();
	}
	
	public int getUsageCount(){
		return model.getUsageCount();
	}
	
	public int getCreditCount(){
		//BSGS Pass.
		return this.model.getNoOfCredits();	
	}
	
	public int getTotalDlvs(){
		return model.getTotalNoOfDlvs();
	}
	
	public Date getPurchaseDate(){
		return model.getPurchaseDate();
	}
	
	public String getPurchaseOrderId(){
		return model.getPurchaseOrderId();
	}
	
	public List getUsageLines(){
		if(usageInfo != null){
			return usageInfo.getUsageLines();
		} else {
			return null;
		}
			
	}
	
	public Date getExpirationDate() {
		return model.getExpirationDate();
	}

	public Date getOrgExpirationDate() {
		return model.getOrgExpirationDate();
	}
	
	
	public int getExtendedWeeks(){
		//Unlimited Pass.
		if((model.getExpirationDate()==null) ||(model.getOrgExpirationDate()==null))
			return 0;
		int noOfDays = DateUtil.getDiffInDays(model.getExpirationDate(), model.getOrgExpirationDate());
		int extendedWeeks = noOfDays / 7;
		return extendedWeeks;
	}
	
	public DlvPassUsageLine getLastUsedOrder(){
		DlvPassUsageLine order = null;
		List usageLines = getUsageLines();
		if(usageLines != null && usageLines.size() > 0){
			//Get the top item from the list since it is ordered by delivery date.
			order = (DlvPassUsageLine)usageLines.get(0);
		}
		return order;
	}

	public String getDlvPassId() {
		return model.getPK().getId();
	}
	
	public String getCustomerId() {
		return model.getCustomerId();
	}
	
	public double getAmount() {
		return model.getAmount();
	}
	
	public DeliveryPassModel getModel(){
		return model;
	}
}
