package com.freshdirect.routing.model;

import java.io.Serializable;

public class OrderEstimationResult implements Serializable {
	
	private IPackagingModel packagingModel;
	private double calculatedOrderSize;
	
	public IPackagingModel getPackagingModel() {
		return packagingModel;
	}
	public double getCalculatedOrderSize() {
		return calculatedOrderSize;
	}
	public void setPackagingModel(IPackagingModel packagingModel) {
		this.packagingModel = packagingModel;
	}
	public void setCalculatedOrderSize(double calculatedOrderSize) {
		this.calculatedOrderSize = calculatedOrderSize;
	}
	
	
	
}
