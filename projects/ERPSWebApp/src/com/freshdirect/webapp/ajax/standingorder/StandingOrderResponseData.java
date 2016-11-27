package com.freshdirect.webapp.ajax.standingorder;

public class StandingOrderResponseData {

	private String id;
	private String name ;
	private String productCount;
	private String message ;
	private double amount ;
	private boolean activate=false;
	private String error;
	private boolean success=true;
	
	private boolean isAlcohol=false;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProductCount() {
		return productCount;
	}
	public void setProductCount(String productCount) {
		this.productCount = productCount;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public boolean isActivate() {
		return activate;
	}
	public void setActivate(boolean activate) {
		this.activate = activate;
	}
	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}
	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}
	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}
	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
	/**
	 * @return the isAlcohol
	 */
	public boolean isAlcohol() {
		return isAlcohol;
	}
	/**
	 * @param isAlcohol the isAlcohol to set
	 */
	public void setAlcohol(boolean isAlcohol) {
		this.isAlcohol = isAlcohol;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	
}
