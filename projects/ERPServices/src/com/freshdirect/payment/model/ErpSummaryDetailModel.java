package com.freshdirect.payment.model;

import com.freshdirect.framework.core.*;

/**
 *
 * @author  knadeem
 * @version
 */

public class ErpSummaryDetailModel extends ModelSupport {
	
	private EnumSummaryDetailType summaryDetailType;
	private int numberOfItems;
	private double netAmount;
	private double interchangeFees;
	private double assessmentFees;
	private double transactionFees;
	
	/**
	 * Constructor
	 */
	public ErpSummaryDetailModel(EnumSummaryDetailType type){
		super();
		this.summaryDetailType = type;
	}
	
	/**
	 * Constructor with all arguments
	 */
	public ErpSummaryDetailModel(EnumSummaryDetailType type, int numberOfItems, double netAmount, double interchangeFees, double assessmentFees, double transactionFees){
		super();
		this.summaryDetailType = type;
		this.numberOfItems = numberOfItems;
		this.netAmount = netAmount;
		this.interchangeFees = interchangeFees;
		this.assessmentFees = assessmentFees;
		this.transactionFees = transactionFees;
	}
	
	public int getNumberOfItems(){
		return this.numberOfItems;
	}
	public void setNumberOfItems(int numberOfItems){
		this.numberOfItems = numberOfItems;
	}
	
	public double getNetAmount(){
		return this.netAmount;
	}
	public void setNetAmount(double netAmount){
		this.netAmount = netAmount;
	}
	
	public double getInterchangeFees(){
		return this.interchangeFees;
	}
	public void setInterchangeFees(double interchangeFees){
		this.interchangeFees = interchangeFees;
	}
	
	public double getAssessmentFees(){
		return this.assessmentFees;
	}
	public void setAssessmentFees(double assessmentFees){
		this.assessmentFees = assessmentFees;
	}
	
	public double getTransactionFees(){
		return this.transactionFees;
	}
	public void setTransactionFees(double transactionFees){
		this.transactionFees = transactionFees;
	}
	
	public EnumSummaryDetailType getSummaryDatailType(){
		return this.summaryDetailType;
	}
	
	public boolean equals(Object obj){
		if(!(obj instanceof ErpSummaryDetailModel)){
			return false;
		}
		ErpSummaryDetailModel comp = (ErpSummaryDetailModel)obj;
		return this.assessmentFees == comp.assessmentFees && this.netAmount == comp.netAmount && this.numberOfItems == comp.numberOfItems
			&& this.interchangeFees == comp.interchangeFees && this.summaryDetailType.equals(comp.summaryDetailType);
	}
}
