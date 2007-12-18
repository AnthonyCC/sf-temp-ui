/*
 * $Workfile:$
 *
 * $Date:$
 *
 * Copyright (c) 2003 FreshDirect
 *
 */
 
package com.freshdirect.dataloader.email;


public class MailInfo {
    
    private String email = "";
    private String firstName = "";
    private boolean html = true;
    
    public MailInfo() {
        super();
    }
    
    public MailInfo(String email, String firstName, boolean html) {
        this();
        this.email = email;
        this.firstName = firstName;
        this.html = html;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFirstName() {
        return this.firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public boolean isHtmlEmail() {
        return this.html;
    }
    
    public void isHtmlEmail(boolean html) {
        this.html = html;
    }
    
    public String toString() {
        return email + " : " + firstName + " : " + html;
    }
    
}