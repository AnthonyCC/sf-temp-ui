package com.freshdirect.framework.mail;

import java.util.*;

public class EmailSupport implements EmailI {

	private String recipient = "";
	private EmailAddress from = null;
	private Collection ccList = new ArrayList();
	private Collection bccList = new ArrayList();
	private String subject = "";
	
	public String getRecipient() {
		return this.recipient;
	}
	
	protected void setRecipient(String recipient){
		this.recipient = recipient;
	}

	public EmailAddress getFromAddress() {
		return this.from;
	}
	
	public void setFromAddress(EmailAddress from){
		this.from = from;
	}

	public String getSubject() {
		return this.subject;
	}
	
	public void setSubject(String subject){
		this.subject = subject;
	}
	
	public Collection getCCList() {
		return this.ccList;
	}

	public void setCCList(Collection cc) {
		this.ccList = cc;
	}

	public Collection getBCCList() {
		return this.bccList;
	}

	public void setBCCList(Collection bcc) {
		this.bccList = bcc;
	}

}