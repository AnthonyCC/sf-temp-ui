package com.freshdirect.crm;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

public class CrmCaseModel extends ModelSupport implements CrmCaseI {

	private CrmCaseInfo caseInfo;

	/** List of CrmCaseAction */
	private List actions = new ArrayList();

	public CrmCaseModel() {
		this.caseInfo = new CrmCaseInfo();
	}

	public CrmCaseModel(PrimaryKey pk) {
		this.caseInfo = new CrmCaseInfo(pk);
	}

	public CrmCaseModel(CrmCaseInfo caseInfo) {
		this.caseInfo = caseInfo;
	}

	public PrimaryKey getPK() {
		return this.caseInfo.getPK();
	}

	public CrmCaseOrigin getOrigin() {
		return this.caseInfo.getOrigin();
	}

	public void setOrigin(CrmCaseOrigin origin) {
		this.caseInfo.setOrigin(origin);
	}

	public CrmCaseState getState() {
		return this.caseInfo.getState();
	}

	public void setState(CrmCaseState state) {
		this.caseInfo.setState(state);
	}

	public CrmCasePriority getPriority() {
		return this.caseInfo.getPriority();
	}

	public void setPriority(CrmCasePriority priority) {
		this.caseInfo.setPriority(priority);
	}

	public CrmCaseSubject getSubject() {
		return this.caseInfo.getSubject();
	}
	
	public String getSubjectName() {
		return this.caseInfo.getSubjectName();
	}

	public void setSubject(CrmCaseSubject subject) {
		this.caseInfo.setSubject(subject);
	}

	public String getSummary() {
		return this.caseInfo.getSummary();
	}

	public void setSummary(String string) {
		this.caseInfo.setSummary(string);
	}

	public PrimaryKey getCustomerPK() {
		return this.caseInfo.getCustomerPK();
	}

	public void setCustomerPK(PrimaryKey key) {
		this.caseInfo.setCustomerPK(key);
	}

	public PrimaryKey getSalePK() {
		return this.caseInfo.getSalePK();
	}

	public void setSalePK(PrimaryKey key) {
		this.caseInfo.setSalePK(key);
	}

	public PrimaryKey getAssignedAgentPK() {
		return this.caseInfo.getAssignedAgentPK();
	}

	public void setAssignedAgentPK(PrimaryKey key) {
		this.caseInfo.setAssignedAgentPK(key);
	}

	public PrimaryKey getLockedAgentPK() {
		return this.caseInfo.getLockedAgentPK();
	}

	public void setLockedAgentPK(PrimaryKey key) {
		this.caseInfo.setLockedAgentPK(key);
	}

	/** @return Set of CrmDepartment */
	public Set getDepartments() {
		return this.caseInfo.getDepartments();
	}

	/** @param departments Set of CrmDepartment */
	public void setDepartments(Set depts) {
		this.caseInfo.setDepartments(depts);
	}

	public List getActions() {
		return Collections.unmodifiableList(actions);
	}

	public void setActions(List list) {
		actions = list;
	}

	public void addAction(CrmCaseAction action) {
		actions.add(action);
	}

	public Date getCreateDate() {
		return this.caseInfo.getCreateDate();
	}

	public Date getLastModDate() {
		return this.caseInfo.getLastModDate();
	}

	/** @return idle time in milliseconds */
	public long getIdleTime() {
		return this.caseInfo.getIdleTime();
	}

	public String getCustomerFirstName() {
		return this.caseInfo.getCustomerFirstName();
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.caseInfo.setCustomerFirstName(customerFirstName);
	}

	public String getCustomerLastName() {
		return this.caseInfo.getCustomerLastName();
	}

	public void setCustomerLastName(String customerLastName) {
		this.caseInfo.setCustomerLastName(customerLastName);
	}

	public CrmCaseInfo getCaseInfo() {
		return this.caseInfo;
	}

	public void setCaseInfo(CrmCaseInfo caseInfo) {
		this.caseInfo = caseInfo;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.caseInfo.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.caseInfo.removePropertyChangeListener(listener);
	}
	
	public void setProjectedQuantity(int qty) {
		this.caseInfo.setProjectedQuantity(qty);
    }
	
    public int getProjectedQuantity() {
    	return this.caseInfo.getProjectedQuantity();
    }
    
    public void setActualQuantity(int qty) {
    	this.caseInfo.setActualQuantity(qty);
    }
    
    public int getActualQuantity() {
    	return this.caseInfo.getActualQuantity();
    }

    public String getCrmCaseMedia() {
		return this.caseInfo.getCrmCaseMedia();
	}

	public void setCrmCaseMedia(String crmCaseMedia) {
		this.caseInfo.setCrmCaseMedia(crmCaseMedia);
	}

	public String getFirstContactForIssue() {
		return caseInfo.getFirstContactForIssue();
	}

	public void setFirstContactForIssue(String firstContactForIssue) {
		this.caseInfo.setFirstContactForIssue(firstContactForIssue);
	}

	public String getFirstContactResolved() {
		return this.caseInfo.getFirstContactResolved();
	}

	public void setFirstContactResolved(String firstContactResolved) {
		this.caseInfo.setFirstContactResolved(firstContactResolved);
	}

	public String getMoreThenOneIssue() {
		return this.caseInfo.getMoreThenOneIssue();
	}

	public void setMoreThenOneIssue(String moreThenOneIssue) {
		this.caseInfo.setMoreThenOneIssue(moreThenOneIssue);
	}

	public String getResonForNotResolve() {
		return this.caseInfo.getResonForNotResolve();
	}

	public void setResonForNotResolve(String resonForNotResolve) {
		this.caseInfo.setResonForNotResolve(resonForNotResolve) ;
	}
	
	public String getCustomerTone() {
		return this.caseInfo.getCustomerTone();
	}

	public void setCustomerTone(String customerTone) {
		this.caseInfo.setCustomerTone(customerTone);
	}

	public String getSatisfiedWithResolution() {
		return this.caseInfo.getSatisfiedWithResolution();
	}

	public void setSatisfiedWithResolution(String satisfiedWithResolution) {
		this.caseInfo.setSatisfiedWithResolution(satisfiedWithResolution);
	}


	

	public List getCartonNumbers() {
		return this.caseInfo.getCartonNumbers();
	}

	public void setCartonNumbers(List cartons) {
		this.caseInfo.setCartonNumbers(cartons);
	}	
}
