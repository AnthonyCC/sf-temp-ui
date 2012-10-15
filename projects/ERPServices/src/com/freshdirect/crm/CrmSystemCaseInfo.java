package com.freshdirect.crm;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.freshdirect.framework.core.PrimaryKey;

public class CrmSystemCaseInfo implements Serializable {
	
	private PrimaryKey customerPK;
	private PrimaryKey salePK;
	private CrmCaseSubject subject;
	private String summary;
	
	private CrmCaseOrigin origin;
	private CrmCaseState state;
	private String note;
	
	// holds assigned carton numbers (missing, misloaded, etc)
    private List cartonNumbers;
    
    private CrmAgentModel loginAgent;
	
	public CrmSystemCaseInfo(CrmCaseSubject subject, String summary){
		this(null, null, subject, summary);
	}
	
	public CrmSystemCaseInfo(PrimaryKey customerPK, CrmCaseSubject subject, String summary){
		this(customerPK, null, subject, summary);
	}
	
	public CrmSystemCaseInfo(PrimaryKey customerPK, PrimaryKey salePK, CrmCaseSubject subject, String summary){
		this.customerPK = customerPK;
		this.salePK = salePK;
		this.subject = subject;
		this.summary = summary;
		
		//default values
		this.origin = CrmCaseOrigin.getEnum(CrmCaseOrigin.CODE_SYS);
		this.state = CrmCaseState.getEnum(CrmCaseState.CODE_OPEN);
	}
	
	public CrmSystemCaseInfo(PrimaryKey customerPK, PrimaryKey salePK, CrmCaseSubject subject, String summary, String note, List cartonNumbers,CrmAgentModel loginAgent){
		this.customerPK = customerPK;
		this.salePK = salePK;
		this.subject = subject;
		this.summary = summary;
		this.cartonNumbers = cartonNumbers;
		this.note = note;
		this.loginAgent = loginAgent;
		
		//default values
		this.origin = CrmCaseOrigin.getEnum(CrmCaseOrigin.CODE_SYS);
		this.state = CrmCaseState.getEnum(CrmCaseState.CODE_OPEN);
	}
	
	public PrimaryKey getCustomerPK(){
		return this.customerPK;
	}
	
	public PrimaryKey getSalePK(){
		return this.salePK;
	}
	
	public CrmCaseSubject getSubject(){
		return this.subject;
	}
	
	public CrmCasePriority getPriority(){
		return this.subject.getPriority();
	}
	
	public CrmCaseOrigin getOrigin(){
		return this.origin;
	}
	
	public void setOrigin(CrmCaseOrigin origin){
		this.origin = origin;
	}
	
	public CrmCaseState getState(){
		return this.state;
	}
	
	public void setState(CrmCaseState state){
		this.state = state;
	}
	
	public String getSummary(){
		return this.summary;
	}
	
	public String getNote(){
		return this.note;
	}
	
	public void setNote(String note){
		this.note = note;
	}
	// List<String>
	public List getCartonNumbers() {
		return this.cartonNumbers == null ? Collections.EMPTY_LIST : this.cartonNumbers;
	}

	public void setCartonNumbers(List cartons) {
		this.cartonNumbers = cartons;
	}

	public CrmAgentModel getLoginAgent() {
		return loginAgent;
	}

	public void setLoginAgent(CrmAgentModel loginAgent) {
		this.loginAgent = loginAgent;
	}
	
	
}