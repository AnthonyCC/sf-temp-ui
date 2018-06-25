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
	private String crmCaseMedia;
	
	// holds assigned carton numbers (missing, misloaded, etc)
    private List cartonNumbers;
    
    private CrmAgentModel loginAgent;
    
    public CrmSystemCaseInfo(){
    	
    }
    
	public CrmSystemCaseInfo(CrmCaseSubject subject, String summary){
		this(null, null, subject, summary);
	}
	
	public CrmSystemCaseInfo(PrimaryKey customerPK, CrmCaseSubject subject, String summary){
		this(customerPK, null, subject, summary);
	}
	public CrmSystemCaseInfo(PrimaryKey customerPK, PrimaryKey salePK, CrmCaseSubject subject, String summary){
		this(customerPK, salePK, subject, summary, CrmCaseOrigin.getEnum(CrmCaseOrigin.CODE_SYS), CrmCaseState.getEnum(CrmCaseState.CODE_OPEN));
	}
	public CrmSystemCaseInfo(PrimaryKey customerPK, PrimaryKey salePK, CrmCaseSubject subject, String summary, CrmCaseOrigin origin, CrmCaseState state){
		this.customerPK = customerPK;
		this.salePK = salePK;
		this.subject = subject;
		this.summary = summary;
		
		//default values
		this.origin = origin;
		this.state = state;
	}
	
	
	public CrmSystemCaseInfo(PrimaryKey customerPK, PrimaryKey salePK, CrmCaseSubject subject, String summary
			, String note, List cartonNumbers, CrmAgentModel loginAgent, String crmCaseMedia){
		this.customerPK = customerPK;
		this.salePK = salePK;
		this.subject = subject;
		this.summary = summary;
		this.note = note;
		this.cartonNumbers = cartonNumbers;
		this.loginAgent = loginAgent;
		this.crmCaseMedia = crmCaseMedia;
		
		//default values
		this.origin = CrmCaseOrigin.getEnum(CrmCaseOrigin.CODE_SYS);
		this.state = CrmCaseSubject.CODE_EARLY_DELIVERY_REQEUST.equals(subject.getCode()) ?CrmCaseState.getEnum(CrmCaseState.CODE_CLOSED):CrmCaseState.getEnum(CrmCaseState.CODE_OPEN);
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

	public String getCrmCaseMedia() {
		return crmCaseMedia;
	}

	public void setCrmCaseMedia(String crmCaseMedia) {
		this.crmCaseMedia = crmCaseMedia;
	}

	
	//Introduced Only for Storefront 2.0.
	
	public void setCustomerPK(PrimaryKey customerPK) {
		this.customerPK = customerPK;
	}

	public void setSalePK(PrimaryKey salePK) {
		this.salePK = salePK;
	}

	public void setSubject(CrmCaseSubject subject) {
		this.subject = subject;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	
	
	
	
}