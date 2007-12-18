/*
 * $Workfile:ErpComplaintModel.java$
 *
 * $Date:8/5/2003 12:27:11 AM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.customer;

import java.util.*;

import com.freshdirect.framework.core.ModelSupport;

/**
 * ErpComplaintModel class
 *
 * @version    $Revision:5$
 * @author     $Author:Mike Rose$
 * @stereotype fd-model
 */
public class ErpComplaintModel extends ModelSupport {

	public static final int STORE_CREDIT	= 0;
	public static final int CASH_BACK		= 1;
	public static final int MIXED			= 2;
	
	private List complaintLines;
	private String description;
	private String createdBy;
	private Date createDate;
	private String approvedBy;
	private Date approvedDate;
	private EnumComplaintType type;
	private EnumComplaintStatus status;
	private ErpCustomerEmailModel customer_email;
    private EnumSendCreditEmail emailOption;
    
    public ErpComplaintModel() {
        this.complaintLines = new ArrayList();
    }

    public List getComplaintLines() { return complaintLines; }
    public void setComplaintLines(List l) { this.complaintLines = l; }
    public void addComplaintLines(List l) { this.complaintLines.addAll(l); }

    public String getDescription() { return description; }
    public void setDescription(String s) { this.description = s; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String s) { this.createdBy = s; }

    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date d) { this.createDate = d; }

    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String s) { this.approvedBy = s; }

    public Date getApprovedDate() { return approvedDate; }
    public void setApprovedDate(Date d) { this.approvedDate = d; }
    
    public EnumComplaintType getType(){
    	return (this.type ==null)?EnumComplaintType.getEnum(getComplaintMethod()):type;
    }
    
    public void setType(EnumComplaintType type) {
    	this.type = type;
    }

    public EnumComplaintStatus getStatus() { return status; }
    public void setStatus(EnumComplaintStatus e) { this.status = e; }

    public int getComplaintMethod() {
		boolean isStoreCredit = false;
		boolean isCashBack = false;
		for (Iterator it = complaintLines.iterator(); it.hasNext(); ) {
			ErpComplaintLineModel line = (ErpComplaintLineModel) it.next();
			if ( line.getMethod().equals(EnumComplaintLineMethod.STORE_CREDIT) ) {
				isStoreCredit = true;
			} else if ( line.getMethod().equals(EnumComplaintLineMethod.CASH_BACK) ) {
				isCashBack = true;
			}
		}
		if (isStoreCredit) {
			if (isCashBack) {
				return MIXED;
			} else {
				return STORE_CREDIT;
			}
		} else {
			return CASH_BACK;
		}
	}

	/**
	 * Retrieves the total for all complaint lines in this complaint.
	 *
	 * @return double total amount
	 */
    public double getAmount() {

		double amount = 0.0;

		for (Iterator it = complaintLines.iterator(); it.hasNext(); ) {
			ErpComplaintLineModel line = (ErpComplaintLineModel) it.next();
			amount += line.getAmount();
		}

		return amount;
	}

	/**
	 * Retrieves the total for STORE CREDIT complaint lines in this complaint.
	 *
	 * @return double total amount
	 */
	public double getStoreCreditAmount() {
		return getAmountForMethod(EnumComplaintLineMethod.STORE_CREDIT);
	}

	/**
	 * Retrieves the total for CASH BACK complaint lines in this complaint.
	 *
	 * @return double total amount
	 */
	public double getCashBackAmount() {
		return getAmountForMethod(EnumComplaintLineMethod.CASH_BACK);
	}
	
	public List getCashBackComplaintLines() {
		
		List lines = new ArrayList();
		for(Iterator i = this.complaintLines.iterator(); i.hasNext(); ) {
			ErpComplaintLineModel line = (ErpComplaintLineModel)i.next();
			if(EnumComplaintLineMethod.CASH_BACK.equals(line.getMethod())) {
				lines.add(line);
			}
		}
		
		return lines;
	}

	/**
	 * Retrieves the total for complaint lines of a given method in this complaint.
	 *
	 * @param EnumComplaintLineMethod method
	 * @return double total amount
	 */
    private double getAmountForMethod(EnumComplaintLineMethod method) {
		double amount = 0.0;
		for (Iterator it = complaintLines.iterator(); it.hasNext(); ) {
			ErpComplaintLineModel line = (ErpComplaintLineModel) it.next();
			if ( line.getMethod().equals(method) )
				amount += line.getAmount();
		}
		return amount;
	}
    
    public final static String GOODWILL = "GDW";
    public final static String TRANSPORTATION = "TRN";
    public final static String EXTRAITEM = "XTR";
    
    public double getGoodwillCreditAmount() {
        return getAmountForDept(GOODWILL);
    }
    
    public double getTransportationCreditAmount() {
        return getAmountForDept(TRANSPORTATION);
    }
    
    public double getExtraItemCreditAmount() {
        return getAmountForDept(EXTRAITEM);
    }
    
    public ErpComplaintLineModel getComplaintLine(String orderlineId) {
    	for(Iterator i = this.complaintLines.iterator(); i.hasNext(); ) {
    		ErpComplaintLineModel cl = (ErpComplaintLineModel) i.next();
    		if(!EnumComplaintLineType.ORDER_LINE.equals(cl.getType())) {
    			continue;
    		}
    		
    		if(cl.getOrderLineId() != null && cl.getOrderLineId().equals(orderlineId)) {
    			return cl;
    		}
    	}
    	
    	return null;
    }
    
	private double getAmountForDept(String dept) {
		double amount = 0.0;
		for (Iterator it = complaintLines.iterator(); it.hasNext(); ) {
			ErpComplaintLineModel line = (ErpComplaintLineModel) it.next();
			if (dept!=null && dept.equalsIgnoreCase(line.getDepartmentCode()))
				amount += line.getAmount();
		}
		return amount;
	}

	public ErpCustomerEmailModel getCustomerEmail() {
		return customer_email;
	}

	public void setCustomerEmail(ErpCustomerEmailModel model) {
		customer_email = model;
	}
	
	public void setEmailOption(EnumSendCreditEmail sendingOption) {
		this.emailOption = sendingOption;
	}
	
	public EnumSendCreditEmail getEmailOption() {
		return emailOption;
	}
	
	public boolean isEmailSent() {
		return customer_email!=null 
			 ? customer_email.isMailSent()
			 : false; 
	}

	public boolean okToSendEmailOnCreate() {
		return EnumSendCreditEmail.SEND_ON_COMPLAINT_CREATION.equals(emailOption);
	}

	public boolean okToSendEmailOnApproval() {
		return EnumSendCreditEmail.SEND_ON_APPROVAL.equals(emailOption);
	}
	
	public boolean dontSendEmail(){
		return EnumSendCreditEmail.DONT_SEND.equals(emailOption);
	}
}

