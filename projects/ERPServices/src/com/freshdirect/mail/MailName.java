/*
 * MailName.java
 *
 * Created on September 27, 2001, 8:06 PM
 */

package com.freshdirect.mail;

/**
 *
 * @author  jmccarter
 * @version
 */
public interface MailName {
	public final static String XSL					= "xslPath";
	public final static String IS_HTML				= "isHtml";
    public final static String TYPE 				= "emailType";
    public final static String TITLE	 			= "emailTitle";
    public final static String TO_ADDRESS 			= "emailToAddress";
    
    public final static String CC_ADDRESS 			= "emailCcAddress"; 
    public final static String BCC_ADDRESS 			= "emailBccAddress"; 
       
    public final static String FROM_ADDRESS 		= "emailFromAddress";
    public final static String FROM_ADDRESS_NAME 	= "emailFromAddressName";
    public final static String ORDER_HISTORY 		= "orderHistory";
    
    public final static String GC_FTL_PATH 		= "gcFtlPath";
    public final static String FTL_PARAMETERS 		= "ftlParameters";
    public final static String IS_FTL 		= "IS_FTL";
    
    public final static String GC_RECIPIENT_EMAIL = "gcRecipientEmail";
    
} // class MailName