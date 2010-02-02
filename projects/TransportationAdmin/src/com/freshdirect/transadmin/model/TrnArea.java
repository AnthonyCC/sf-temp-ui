package com.freshdirect.transadmin.model;

import java.math.BigDecimal;

public class TrnArea  implements java.io.Serializable, TrnBaseEntityI {
	
	private String code;
	private String name;
	private String description;
	
	private String active;
	private String prefix;
	
	private String isNew;
	
	private String deliveryModel;
	
	private BigDecimal loadBalanceFactor;
	
	private BigDecimal deliveryRate;
		
	private String balanceBy;
	
	private String needsLoadBalance;
	
	private String isDepot;

	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	
	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public boolean isObsoleteEntity() {
		return false;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getDeliveryModel() {
		return deliveryModel;
	}

	public void setDeliveryModel(String deliveryModel) {
		this.deliveryModel = deliveryModel;
	}

	public String getBalanceBy() {
		return balanceBy;
	}

	public void setBalanceBy(String balanceBy) {
		this.balanceBy = balanceBy;
	}

	public BigDecimal getLoadBalanceFactor() {
		return loadBalanceFactor;
	}

	public void setLoadBalanceFactor(BigDecimal loadBalanceFactor) {
		this.loadBalanceFactor = loadBalanceFactor;
	}

	public String getNeedsLoadBalance() {
		return needsLoadBalance;
	}

	public void setNeedsLoadBalance(String needsLoadBalance) {
		this.needsLoadBalance = needsLoadBalance;
	}

	public String getIsDepot() {
		return isDepot;
	}

	public void setIsDepot(String isDepot) {
		this.isDepot = isDepot;
	}
	
	public BigDecimal getDeliveryRate() {
		return deliveryRate;
	}

	public void setDeliveryRate(BigDecimal deliveryRate) {
		this.deliveryRate = deliveryRate;
	}

	
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TrnArea other = (TrnArea) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

}
