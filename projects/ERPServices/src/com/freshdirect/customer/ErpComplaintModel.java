/*
 * $Workfile:ErpComplaintModel.java$
 *
 * $Date:8/5/2003 12:27:11 AM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.customer;

import java.io.Serializable;
import java.util.*;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.core.ModelSupport;

/**
 * ErpComplaintModel class
 *
 * @version    $Revision:5$
 * @author     $Author:Mike Rose$
 * @stereotype fd-model
 */
public class ErpComplaintModel extends ModelSupport {
	private static final long serialVersionUID = 807721304934675871L;

	public static final int STORE_CREDIT	= 0;
	public static final int CASH_BACK		= 1;
	public static final int MIXED			= 2;
	
	// List<ErpComplaintLineModel>
	private List<ErpComplaintLineModel> complaintLines;
	private String description;
	private String createdBy;
	private Date createDate;
	private String approvedBy;
	private Date approvedDate;
	private EnumComplaintType type;
	private EnumComplaintStatus status;
	private ErpCustomerEmailModel customer_email;
    private EnumSendCreditEmail emailOption;
    private String makegood_sale_id;
    
    /**
     * PK of automatically created case when credit issued
     */
    private String autoCaseId;
    
    public ErpComplaintModel() {
        this.complaintLines = new ArrayList<ErpComplaintLineModel>();
    }

	// List<ErpComplaintLineModel>
    public List<ErpComplaintLineModel> getComplaintLines() { return complaintLines; }
    public void setComplaintLines(List<ErpComplaintLineModel> l) { this.complaintLines = l; }
    public void addComplaintLines(List<ErpComplaintLineModel> l) { this.complaintLines.addAll(l); }

    /**
     * cartonized = complaint lines are following cartonized arrangement
     */
    public boolean isCartonized() {
    	return complaintLines.size() > 0 && ((ErpComplaintLineModel) complaintLines.iterator().next() ).getCartonNumber() != null;
    }
    
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
    public String getMakegood_sale_id() {
		return makegood_sale_id;
	}

	public void setMakegood_sale_id(String makegood_sale_id) {
		this.makegood_sale_id = makegood_sale_id;
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


	/**
	 * 
	 * Complaint can be auto-approved.
	 * 
	 * No approval is required if all of the following are true:
	 *   amount is under threshold
	 *   only contains store credit
	 *   order has been delivered
	 * 
	 *   -- OR --
	 * 
	 *   amount is zero (this is just a complaint with no credit expected)
	 *   order is at least En-route
	 * 
	 *   -- OR --
	 * 
	 * 	MIXED or CASH_BACK and less than auto approve amount and STL
	 * 
	 * @return Complaint can be auto approved or not
	 * 
	 */
	public boolean canBeAutoApproved(EnumSaleStatus orderStatus) {
		final double creditAutoApproveAmount = ErpServicesProperties.getCreditAutoApproveAmount();
		final double amount = getAmount();
		return
			((amount <= creditAutoApproveAmount)
				&& (getComplaintMethod() == ErpComplaintModel.STORE_CREDIT)
				&& (EnumSaleStatus.SETTLED.equals(orderStatus)
					|| EnumSaleStatus.PAYMENT_PENDING.equals(orderStatus)
					|| EnumSaleStatus.CAPTURE_PENDING.equals(orderStatus)))
			|| ((0 == Math.round(amount * 100.0))
				&& (getComplaintMethod() == ErpComplaintModel.STORE_CREDIT)
				&& (EnumSaleStatus.SETTLED.equals(orderStatus)
					|| EnumSaleStatus.PAYMENT_PENDING.equals(orderStatus)
					|| EnumSaleStatus.CAPTURE_PENDING.equals(orderStatus)
					|| EnumSaleStatus.ENROUTE.equals(orderStatus)))
			|| ((getComplaintMethod() == ErpComplaintModel.CASH_BACK
					|| getComplaintMethod() == ErpComplaintModel.MIXED)
					&& amount <= creditAutoApproveAmount
					&& EnumSaleStatus.SETTLED.equals(orderStatus));
		
	}
	
	public String getAutoCaseId() {
		return this.autoCaseId;
	}
	
	public void setAutoCaseId(String autoCaseId) {
		this.autoCaseId = autoCaseId;
	}


	public static java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
	public String describe() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("Creating credits for the following departments:\n");
		buf.append("  Method\t\tDepartment\t\t\tAmount\t\t\tReason\n");
		buf.append("  ------\t\t----------\t\t\t------\t\t\t------\n");
		List<ErpComplaintLineModel> lines = this.getComplaintLines();
		for (Iterator<ErpComplaintLineModel> it = lines.iterator(); it.hasNext();) {
			ErpComplaintLineModel line = it.next();
			buf.append(
				line.getMethod().getStatusCode()
					+ "\t\t"
					+ line.getDepartmentCode()
					+ "\t\t\t"
					+ currencyFormatter.format(line.getAmount())
					+ "\t\t\t"
					+ line.getReason().getReason()
					+ "\n");
		}
		buf.append("  Credit Notes: " + this.getDescription() + "\n");
	
		return buf.toString();
	}
	


	public Set collectCartonNumbers() {
		Set cartonNumbers = new HashSet();
		
		for (Iterator it=complaintLines.iterator(); it.hasNext(); ) {
			ErpComplaintLineModel l = (ErpComplaintLineModel) it.next();
			final String cartonNumber = l.getCartonNumber();
			if (cartonNumber != null)
				cartonNumbers.add(cartonNumber);
		}

		return cartonNumbers;
	}

	

	/**
	 * Find the most important reason in complaint lines. Lowest priority is the best
	 */
	public ErpComplaintReason getTopReason() {
		ErpComplaintReason topReason = null;
		
		for (ErpComplaintLineModel l : complaintLines) {
			if (topReason == null || l.getReason().getPriority() < topReason.getPriority()) {
				topReason = l.getReason();
			}
		}

		return topReason;
	}
	
	
	// Set<EnumDlvIssueType>
	public Set getDeliveryIssues() {
		Set dlvTypes = new HashSet();
		
		for (Iterator it=complaintLines.iterator(); it.hasNext(); ) {
			ErpComplaintLineModel l = (ErpComplaintLineModel) it.next();
			dlvTypes.add(l.getReason().getDeliveryIssueType());
		}
		return dlvTypes;
	}
	
	public int getPriority() {
		int p = 0;
		
		for (Iterator it=getComplaintLines().iterator(); it.hasNext();) {
			int lp = ((ErpComplaintLineModel) it.next()).getReason().getPriority();
			if (lp > p)
				p = lp;
		}
		
		return p;
	}


	/**
	 * Aggregate class for complaint lines belonging to a given department
	 * 
	 * @author segabor
	 *
	 */
	public class AggregatedComplaintLines implements Serializable {
		private static final long serialVersionUID = 236894187132938375L;

		transient String departmentCode;
		Collection complaintLines = new HashSet();
		
		public AggregatedComplaintLines(String deptCode) {
			this.departmentCode = deptCode;
		}
		
		public void addComplaintLine(ErpComplaintLineModel l) {
			complaintLines.add(l);
		}
		
		/**
		 * Returns sum of complaint amounts
		 * @return
		 */
		public double getAmount() {
			double amt = 0;

			for (Iterator it=complaintLines.iterator(); it.hasNext();) {
				amt += ((ErpComplaintLineModel) it.next()).getAmount();
			}
			return amt;
		}

		/**
		 * Picks the first method
		 * @return
		 */
		public EnumComplaintLineMethod getMethod() {
			return complaintLines.size() > 0 ?((ErpComplaintLineModel) complaintLines.iterator().next()).getMethod() : null;
		}
		
		public String getDepartmentCode() {
			return this.departmentCode;
		}
		
		public String getDepartmentName() {
			return complaintLines.size() > 0 ? ((ErpComplaintLineModel) complaintLines.iterator().next()).getDepartmentName() : null;
		}
	}


	// @return Collection<AggregatedComplaintLines>
	public Collection getComplaintLinesAggregated() {
		// Map of dept code to complaint lines
		Map deptSet = new HashMap();
		for (Iterator it=getComplaintLines().iterator(); it.hasNext();) {
			ErpComplaintLineModel l = (ErpComplaintLineModel) it.next();
			final String deptCode = l.getDepartmentCode();
			AggregatedComplaintLines complaintLines = (AggregatedComplaintLines) deptSet.get(deptCode);
			if (complaintLines == null) {
				complaintLines = new AggregatedComplaintLines(deptCode);
				deptSet.put(deptCode, complaintLines);
			}
			complaintLines.addComplaintLine(l);
		}
		

		// do aggregation
		return deptSet.values();
	}
}
