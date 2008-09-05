/*
 * CrmCaseInfo.java
 *
 * Created on October 11, 2003, 7:54 PM
 */

package com.freshdirect.crm;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

/**
 *
 * @author  mrose
 */
public class CrmCaseInfo extends ModelSupport implements CrmCaseI {
    
    private final PropertyChangeSupport change = new PropertyChangeSupport(this);
    
    /** Holds value of property assignedAgentPK. */
    private PrimaryKey assignedAgentPK;
    
    /** Holds value of property createDate. */
    private Date createDate;
    
    /** Holds value of property lastModDate. */
    private Date lastModDate;
    
    /** Holds value of property customerFirstName. */
    private String customerFirstName;
    
    /** Holds value of property customerLastName. */
    private String customerLastName;
    
    /** Set of String */
    private Set departmentCodes = new HashSet();
    
    /** Holds value of property lockedAgentPK. */
    private PrimaryKey lockedAgentPK;
    
    /** Holds value of property origin. */
    private String originCode;
    
    /** Holds value of property priority. */
    private String priorityCode;
    
    /** Holds value of property customerPK. */
    private PrimaryKey customerPK;
    
    /** Holds value of property salePK. */
    private PrimaryKey salePK;
    
    /** Holds value of property state. */
    private String stateCode;
    
    /** Holds value of property subject. */
    private String subjectCode;
    
    /** Holds value of subject's name. */
    private String subjectName;
    
    /** Holds value of property summary. */
    private String summary;
    
    // holds the number that represents the reported amount (damages/missing etc)
    private int projectedQuantity;
    
    // holds the number that represents the actual amount (damages/missing etc)
    private int actualQuantity;
    
    private String crmCaseMedia=null;
        
    private boolean moreThenOneIssue; 
    
    private boolean firstContactForIssue;
    
    private boolean firstContactResolved;
    
    private String resonForNotResolve=null;
    
    private boolean SatisfiedWithResolution;
    
    private String customerTone;
    
    
    public CrmCaseInfo() {
        super();
    }
    
    public CrmCaseInfo(PrimaryKey pk) {
        this();
        this.setPK(pk);
    }
    
    public CrmCaseInfo(PrimaryKey customerPK, PrimaryKey salePK, CrmCaseSubject subject, String summary) {
        setCustomerPK(customerPK);
        setSalePK(salePK);
        setSubject(subject);
        setPriority(subject.getPriority());
        setSummary(summary);
    }
    
    public CrmCaseInfo(CrmCaseI info) {
        this();
        if (info != null) {
            setPK(info.getPK());
            setAssignedAgentPK(info.getAssignedAgentPK());
            setCreateDate(info.getCreateDate());
            setCustomerFirstName(info.getCustomerFirstName());
            setCustomerLastName(info.getCustomerLastName());
            setCustomerPK(info.getCustomerPK());
            setDepartments(info.getDepartments());
            setLastModDate(info.getLastModDate());
            setLockedAgentPK(info.getLockedAgentPK());
            setOrigin(info.getOrigin());
            setPriority(info.getPriority());
            setSalePK(info.getSalePK());
            setState(info.getState());
            setSubject(info.getSubject());
            setSummary(info.getSummary());
            setProjectedQuantity(info.getProjectedQuantity());
            setActualQuantity(info.getActualQuantity());
        }
    }
    
    public String toString(){
    	return "\n"+this.customerFirstName + "/n"+
    		this.customerLastName+ "\n"+
    		this.originCode+ "\n"+
    		this.priorityCode+ "\n" +
    		this.projectedQuantity+ "\n"+
    		this.stateCode+ "\n"+
    		this.subjectName + "\n"+ 
    		this.summary;
    	
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        change.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        change.removePropertyChangeListener(listener);
    }
    
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (!this.isAnonymous()) {
            if (oldValue == null && newValue == null) {
                return;
            }
            this.change.firePropertyChange(propertyName, oldValue, newValue);
        }
    }
    
    public CrmCaseOrigin getOrigin() {
        return CrmCaseOrigin.getEnum(originCode);
    }
    
    public void setOrigin(CrmCaseOrigin origin) {
        CrmCaseOrigin old = this.getOrigin();
        this.originCode = origin == null ? null : origin.getCode();
        this.firePropertyChange("origin", old, origin);
    }
    
    public CrmCaseState getState() {
        return CrmCaseState.getEnum(stateCode);
    }
    
    public void setState(CrmCaseState state) {
        CrmCaseState old = this.getState();
        this.stateCode = state == null ? null : state.getCode();
        this.firePropertyChange("state", old, state);
    }
    
    public CrmCasePriority getPriority() {
        return CrmCasePriority.getEnum(priorityCode);
    }
    
    public void setPriority(CrmCasePriority priority) {
        CrmCasePriority old = this.getPriority();
        this.priorityCode = priority == null ? null : priority.getCode();
        this.firePropertyChange("priority", old, priority);
    }
    
    public CrmCaseSubject getSubject() {
        return CrmCaseSubject.getEnum(subjectCode);
    }
    
    public void setSubject(CrmCaseSubject subject) {
        CrmCaseSubject old = this.getSubject();
        this.subjectCode = subject == null ? null : subject.getCode();
        this.firePropertyChange("subject", old, subject);
    }
    
    public void setSubjectName(String subjectName){
    	this.subjectName = subjectName;
    }
    
    public String getSubjectName(){
    	return this.subjectName;
    }
    
    public String getSummary() {
        return summary;
    }
    
    public void setSummary(String string) {
        String old = this.getSummary();
        summary = string;
        this.firePropertyChange("summary", old, string);
    }
    
    public PrimaryKey getCustomerPK() {
        return customerPK;
    }
    
    public void setCustomerPK(PrimaryKey key) {
        PrimaryKey old = this.getCustomerPK();
        customerPK = key;
        this.firePropertyChange("customerPK", old, key);
    }
    
    public PrimaryKey getSalePK() {
        return salePK;
    }
    
    public void setSalePK(PrimaryKey key) {
        PrimaryKey old = this.getSalePK();
        salePK = key;
        this.firePropertyChange("salePK", old, key);
    }
    
    public PrimaryKey getAssignedAgentPK() {
        return assignedAgentPK;
    }
    
    public void setAssignedAgentPK(PrimaryKey key) {
        PrimaryKey old = this.getAssignedAgentPK();
        assignedAgentPK = key;
        this.firePropertyChange("assignedAgentPK", old, key);
    }
    
    public PrimaryKey getLockedAgentPK() {
        return lockedAgentPK;
    }
    
    public void setLockedAgentPK(PrimaryKey key) {
        PrimaryKey old = this.getLockedAgentPK();
        lockedAgentPK = key;
        this.firePropertyChange("lockedAgentPK", old, key);
    }
    
    /** @return Set of CrmDepartment */
    public Set getDepartments() {
        Set depts = new HashSet();
        for (Iterator i = this.departmentCodes.iterator(); i.hasNext();) {
            depts.add(CrmDepartment.getEnum((String) i.next()));
        }
        return depts;
    }
    
    /** @param departments Set of CrmDepartment */
    public void setDepartments(Set depts) {
        Set old = this.getDepartments();
        Set deptCodes = new HashSet();
        for (Iterator i = depts.iterator(); i.hasNext();) {
            deptCodes.add(((CrmDepartment) i.next()).getCode());
        }
        this.departmentCodes = deptCodes;
        this.firePropertyChange("departments", old, this.getDepartments());
    }
    
    public Date getCreateDate() {
        return this.createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    /** @return idle time in milliseconds */
    public long getIdleTime() {
        if (this.getLockedAgentPK() != null || this.stateCode.equals(CrmCaseState.CODE_CLOSED)) {
            // someone is working on it, or it's done
            return 0;
        }
        long lastT = lastModDate.getTime();
        long currT = System.currentTimeMillis();
        return currT - lastT;
    }
    
    public String getCustomerFirstName() {
        return this.customerFirstName;
    }
    
    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }
    
    public String getCustomerLastName() {
        return this.customerLastName;
    }
    
    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }
    
    /** Getter for property lastModDate.
     * @return Value of property lastModDate.
     *
     */
    public Date getLastModDate() {
        return this.lastModDate;
    }
    
    /** Setter for property lastModDate.
     * @param lastModDate New value of property lastModDate.
     *
     */
    public void setLastModDate(Date lastModDate) {
        this.lastModDate = lastModDate;
    }
    
    public void setProjectedQuantity(int qty) {
       	Integer oldQ = new Integer(this.projectedQuantity);
    	this.projectedQuantity = qty;
        this.firePropertyChange("reported_quantity", oldQ, new Integer(this.projectedQuantity));
    }
    
    public int getProjectedQuantity() {
    	return this.projectedQuantity;
    }
    
    public void setActualQuantity(int qty) {
    	Integer oldQ = new Integer(this.actualQuantity);
    	this.actualQuantity = qty;
        this.firePropertyChange("actual_Quantity", oldQ, new Integer(this.actualQuantity));
    }
    
    public int getActualQuantity() {
    	return this.actualQuantity;
    }

	public String getCrmCaseMedia() {
		return crmCaseMedia;
	}

	public void setCrmCaseMedia(String crmCaseMedia) {		
		 String old = this.getCrmCaseMedia();
	     this.crmCaseMedia = crmCaseMedia;
	     this.firePropertyChange("crmCaseMedia", old, crmCaseMedia);		
	}

	public boolean isFirstContactForIssue() {
		return firstContactForIssue;
	}

	public void setFirstContactForIssue(boolean firstContactForIssue) {
		 
		 boolean old = this.isFirstContactForIssue();
		 this.firstContactForIssue = firstContactForIssue;
	     this.firePropertyChange("firstContactForIssue", new Boolean(old), new Boolean(firstContactForIssue));;										 				
	}

	public boolean isFirstContactResolved() {
		return firstContactResolved;
	}

	public void setFirstContactResolved(boolean firstContactResolved) {
		 boolean old = this.isFirstContactResolved();
		 this.firstContactResolved = firstContactResolved;
	     this.firePropertyChange("firstContactResolved", new Boolean(old), new Boolean(firstContactResolved));;										 								
	}

	public boolean isMoreThenOneIssue() {
		return moreThenOneIssue;
	}

	public void setMoreThenOneIssue(boolean moreThenOneIssue) {		
		 boolean old = this.isMoreThenOneIssue();
		 this.moreThenOneIssue = moreThenOneIssue;
	     this.firePropertyChange("moreThenOneIssue", new Boolean(old), new Boolean(moreThenOneIssue));;										 													
	}

	public String getResonForNotResolve() {
		return resonForNotResolve;
	}

	public void setResonForNotResolve(String resonForNotResolve) {		
	    String old = this.getResonForNotResolve();
	    this.resonForNotResolve = resonForNotResolve;
	    this.firePropertyChange("resonForNotResolve", old, resonForNotResolve);
	}

	public String getCustomerTone() {
		return customerTone;
	}

	public void setCustomerTone(String customerTone) {
	    String old = this.getCustomerTone();
	    this.customerTone = customerTone;
	    this.firePropertyChange("customerTone", old, customerTone);				
	}

	public boolean isSatisfiedWithResolution() {
		return SatisfiedWithResolution;
	}

	public void setSatisfiedWithResolution(boolean satisfiedWithResolution) {
		 boolean old = this.isSatisfiedWithResolution();
		 SatisfiedWithResolution = satisfiedWithResolution;
	     this.firePropertyChange("SatisfiedWithResolution", new Boolean(old), new Boolean(SatisfiedWithResolution));;										 												
	}
    
}
