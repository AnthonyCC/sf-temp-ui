/*
 * CrmCaseI.java
 *
 * Created on October 11, 2003, 8:47 PM
 */

package com.freshdirect.crm;

import java.util.*;

import java.io.Serializable;

import com.freshdirect.framework.core.PrimaryKey;

/**
 *
 * @author  mrose
 */
public interface CrmCaseI extends Serializable {
    
    public PrimaryKey getPK();
    
    public CrmCaseOrigin getOrigin();
    
    public void setOrigin(CrmCaseOrigin origin);
    
    public CrmCaseState getState();
    
    public void setState(CrmCaseState state);
    
    public CrmCasePriority getPriority();
    
    public void setPriority(CrmCasePriority priority);
    
    public CrmCaseSubject getSubject();
    
    public void setSubject(CrmCaseSubject subject);
    
    public String getSummary();
    
    public void setSummary(String string);
    
    public PrimaryKey getCustomerPK();
    
    public void setCustomerPK(PrimaryKey key);
    
    public PrimaryKey getSalePK();
    
    public void setSalePK(PrimaryKey key);
    
    public PrimaryKey getAssignedAgentPK();
    
    public void setAssignedAgentPK(PrimaryKey key);
    
    public PrimaryKey getLockedAgentPK();
    
    public void setLockedAgentPK(PrimaryKey key);
    
    public Set getDepartments();
    
    public void setDepartments(Set depts);
    
    public Date getCreateDate();
    
    public long getIdleTime();
    
    public String getCustomerFirstName();
    
    public void setCustomerFirstName(String customerFirstName);
    
    public String getCustomerLastName();
    
    public void setCustomerLastName(String customerLastName);
    
    public Date getLastModDate();
    
    //Transportation issues stuff
    public int getProjectedQuantity();
    
    public void setProjectedQuantity(int qty);
    
    public int getActualQuantity();
    
    public void setActualQuantity(int qty);
    
 }
