/*
 * Created on Jul 30, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.freshdirect.customer;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * ErpComplaintEmailModel class
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpCustomerEmailModel extends ModelSupport {
	private boolean mailSent;
	private String customMessage;
	private String signature;
	
	public ErpCustomerEmailModel (){
		super();
	}
	
	
	public ErpCustomerEmailModel (PrimaryKey pk){
		super();
		setPK(pk);
	}
	
	
	public String getCustomMessage() {
		return this.customMessage;
	}

	public boolean isMailSent() {
		return mailSent;
	}

	public String getSignature() {
		return signature;
	}

	public void setCustomMessage(String cm) {
		customMessage = cm;
	}

	public void setMailSent(boolean sentFlag) {
		mailSent = sentFlag;
	}

	public void setSignature(String sig) {
		signature = sig;
	}
	
	public String getHtmlXslPath() {
		return "h_credit_general.xsl";
	}
	public String getPlainTextXslPath() {
		return "x_credit_general.xsl";
	}
}
