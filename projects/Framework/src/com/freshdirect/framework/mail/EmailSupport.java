package com.freshdirect.framework.mail;

import java.util.*;

public class EmailSupport implements EmailI {

	private static final long	serialVersionUID	= 8501265368515620808L;
	
	private String recipient = "";
	private EmailAddress from = null;
	private Collection<String> ccList = new ArrayList<String>();
	private Collection<String> bccList = new ArrayList<String>();
	private String subject = "";
	
	public String getRecipient() {
		return this.recipient;
	}
	
	public void setRecipient(String recipient){
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
	
	public Collection<String> getCCList() {
		return this.ccList;
	}

	public void setCCList(Collection<String> cc) {
		this.ccList = cc;
	}

	public Collection<String> getBCCList() {
		return this.bccList;
	}

	public void setBCCList(Collection<String> bcc) {
		this.bccList = bcc;
	}

}