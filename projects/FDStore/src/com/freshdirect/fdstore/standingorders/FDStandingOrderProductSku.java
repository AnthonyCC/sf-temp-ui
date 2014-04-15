package com.freshdirect.fdstore.standingorders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import com.freshdirect.framework.core.ModelSupport;

public class FDStandingOrderProductSku extends ModelSupport{

	private static final long serialVersionUID = -2095838324000148572L;
	
	private static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	private String customerDetailsId;
	private String listId;
	private String skuCode;
	private String quantity;
	private String salesUnit;
	private String configuration;
	private Integer frequency;
	//private Date createDate;
	//private Date recentDate;
	//private Date deleteDate;
	private String recipeSourceId;
	
	public String getCustomerDetailsId() {
		return customerDetailsId;
	}
	public void setCustomerDetailsId(String customerDetailsId) {
		this.customerDetailsId = customerDetailsId;
	}
	public String getListId() {
		return listId;
	}
	public void setListId(String listId) {
		this.listId = listId;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getSalesUnit() {
		return salesUnit;
	}
	public void setSalesUnit(String salesUnit) {
		this.salesUnit = salesUnit;
	}
	public String getConfiguration() {
		return configuration;
	}
	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}
	public Integer getFrequency() {
		return frequency;
	}
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	/*public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date cretaeDate) {
		this.createDate = createDate;
	}
	public Date getRecentDate() {
		return recentDate;
	}
	public void setRecentDate(Date recentDate) {
		this.recentDate = recentDate;
	}
	public Date getDeleteDate() {
		return deleteDate;
	}
	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}*/
	public String getRecipeSourceId() {
		return recipeSourceId;
	}
	public void setRecipeSourceId(String recipeSourceId) {
		this.recipeSourceId = recipeSourceId;
	}
}
