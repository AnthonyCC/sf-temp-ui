package com.freshdirect.crm;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

public class CrmCaseTemplate extends ModelSupport {

	private String originCode;
	private Set stateCodes = new HashSet();
	private int totalRows;
	private String priorityCode;
	private int startRecord = 0;
	private int endRecord = 100;
	private String queueCode;
	private String subjectCode;
	private String summary;
	private Date startDate;
	private Date endDate;
	private String sortBy="CREATE_DATE";
	private String sortOrder="DESC";
	private PrimaryKey customerPK;
	private PrimaryKey salePK;
	private PrimaryKey assignedAgentPK;
	private PrimaryKey lockedAgentPK;
	private String assignedAgentId;
	private String lockedAgentId;

	public CrmCaseTemplate() {
	}

	public boolean isBlank() {
		return getPK() == null
			&& originCode == null
			&& stateCodes.size() == 0
			&& priorityCode == null
			&& queueCode == null
			&& startDate == null
			&& endDate == null 
			&& subjectCode == null
			&& summary == null
			&& customerPK == null
			&& salePK == null
//			&& assignedAgentPK == null
//			&& lockedAgentPK == null;
			&& assignedAgentId == null
			&& lockedAgentId == null;
	}
	
	public String toString(){
		return " \n Origin Code: "+ this.originCode + "\n" +
			"State Code: "+ this.stateCodes + "\n" +
			"Priority Code: "+ this.priorityCode + "\n" +
			"Queue Code: "+ this.queueCode + "\n" +
			"Subject Code: "+ this.subjectCode + "\n" +
			"Summary : "+ this.summary + "\n" +
			"CustomerPK : "+ this.customerPK + "\n" +
			"SalePK: "+ this.salePK + "\n" +
			"Start Date : "+ this.startDate + "\n" +
			"End Date: "+ this.endDate + "\n" +
			"Sort Order: "+ this.sortOrder + "\n"+
			"Sort By: "+this.sortBy+"\n"+
//			"AssignedAgent PK: "+ this.assignedAgentPK + "\n" +
//			"Locked Agent PK: "+ this.lockedAgentPK + "\n" ;
			"AssignedAgent ID: "+ this.assignedAgentId + "\n" +
			"Locked Agent ID: "+ this.lockedAgentId + "\n" ;
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof CrmCaseTemplate)) {
			return false;
		}
		CrmCaseTemplate t = (CrmCaseTemplate) o;
			
		return eq(getPK(), t.getPK())
			&& eq(originCode, t.originCode)
			&& eq(stateCodes, t.stateCodes)
			&& eq(priorityCode, t.priorityCode)
			&& eq(queueCode, t.queueCode)
			&& eq(subjectCode, t.subjectCode)
			&& eq(startDate, t.startDate) 
			&& eq(endDate, t.endDate) 
			&& (startRecord == t.startRecord)
			&& (endRecord == t.endRecord)
			&& eq(summary, t.summary)
			&& eq(sortBy, t.sortBy)
			&& eq(sortOrder, t.sortOrder)
			&& (totalRows == t.totalRows)
			&& eq(customerPK, t.customerPK)
			&& eq(salePK, t.salePK)
//			&& eq(assignedAgentPK, t.assignedAgentPK)
//			&& eq(lockedAgentPK, t.lockedAgentPK);
			&& eq(assignedAgentId, t.assignedAgentId)
			&& eq(lockedAgentId, t.lockedAgentId);
	}

	private boolean eq(Object o1, Object o2) {
		if (o1 == null || o2 == null) {
			return o1 == null && o2 == null;
		}
		return o1.equals(o2);
	}

	public void setPK(PrimaryKey pk) {
		super.setPK(pk);
	}

	public CrmCaseOrigin getOrigin() {
		return CrmCaseOrigin.getEnum(originCode);
	}

	public void setOrigin(CrmCaseOrigin origin) {
		this.originCode = origin == null ? null : origin.getCode();
	}
	
	public void setSortBy(String sortBy){
		if(sortBy != null) {
			this.sortBy = sortBy;
		}
	}
	
	public String getSortBy(){
		return this.sortBy;
	}
	
	public void setSortOrder(String sortOrder){
		if(sortOrder != null) {
			this.sortOrder = sortOrder;
		}
	}
	
	public String getSortOrder(){
		return this.sortOrder;
	}

	public Set getStates() {
		if (stateCodes == null) {
			return null;
		}
		Set states = new HashSet();
		for (Iterator i = this.stateCodes.iterator(); i.hasNext();) {
			states.add(CrmCaseState.getEnum((String) i.next()));
		}
		return states;
	}

	public void setStates(Set states) {
		Set sc = new HashSet();
		for (Iterator i = states.iterator(); i.hasNext();) {
			sc.add(((CrmCaseState) i.next()).getCode());
		}
		this.stateCodes = sc;
	}
	
	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getEndRecord() {
		return endRecord;
	}

	public void setEndRecord(int endRecord) {
		this.endRecord = endRecord;
	}

	public int getStartRecord() {
		return startRecord;
	}

	public void setStartRecord(int startRecord) {
		this.startRecord = startRecord;
	}

	public CrmCasePriority getPriority() {
		return CrmCasePriority.getEnum(priorityCode);
	}

	public void setPriority(CrmCasePriority priority) {
		this.priorityCode = priority == null ? null : priority.getCode();
	}

	public CrmCaseQueue getQueue() {
		return CrmCaseQueue.getEnum(queueCode);
	}
	
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setQueue(CrmCaseQueue queue) {
		this.queueCode = queue == null ? null : queue.getCode();
	}
	
	public void setQueue(String queueCode){
		this.queueCode = queueCode;
	}

	public CrmCaseSubject getSubject() {
		return CrmCaseSubject.getEnum(subjectCode);
	}

	public void setSubject(CrmCaseSubject subject) {
		this.subjectCode = subject == null ? null : subject.getCode();
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String string) {
		summary = string;
	}

	public PrimaryKey getCustomerPK() {
		return customerPK;
	}

	public void setCustomerPK(PrimaryKey key) {
		customerPK = key;
	}

	public PrimaryKey getSalePK() {
		return salePK;
	}

	public void setSalePK(PrimaryKey key) {
		salePK = key;
	}

	public PrimaryKey getAssignedAgentPK() {
		return assignedAgentPK;
	}

	public void setAssignedAgentPK(PrimaryKey key) {
		assignedAgentPK = key;
	}

	public PrimaryKey getLockedAgentPK() {
		return lockedAgentPK;
	}

	public void setLockedAgentPK(PrimaryKey key) {
		lockedAgentPK = key;
	}

	public String getAssignedAgentId() {
		return assignedAgentId;
	}

	public void setAssignedAgentId(String assignedAgentId) {
		this.assignedAgentId = assignedAgentId;
	}

	public String getLockedAgentId() {
		return lockedAgentId;
	}

	public void setLockedAgentId(String lockedAgentId) {
		this.lockedAgentId = lockedAgentId;
	}

}
