package com.freshdirect.fdstore.mail;

import java.util.HashMap;
import java.util.Map;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.framework.mail.EmailSupport;
import com.freshdirect.framework.mail.XMLEmailI;

public class FDInfoEmail extends EmailSupport implements XMLEmailI {

	private final FDCustomerInfo customer;

	private String htmlXsl;
	private String textXsl;
	
	public FDInfoEmail(FDCustomerInfo customer) {
		super();
		this.customer = customer;
	}

	/**
	 * @see com.freshdirect.framework.mail.AbstractEmail#getRecipient()
	 */
	public String getRecipient() {
		return this.customer.getEmailAddress();
	}

	/**
	 * @see com.freshdirect.framework.mail.XMLEmail#isHtmlEmail()
	 */
	public boolean isHtmlEmail() {
		return this.customer.isHtmlEmail();
	}

	public void setXslPath(String htmlXsl, String textXsl) {
		this.htmlXsl = ErpServicesProperties.getMailerXslHome() + htmlXsl;
		this.textXsl = ErpServicesProperties.getMailerXslHome() + textXsl;
	}

	/**
	 * @see com.freshdirect.framework.mail.XMLEmail#getXslPath()
	 */
	public String getXslPath() {
		return isHtmlEmail() ? this.htmlXsl : this.textXsl;
	}

	/**
	 * @see com.freshdirect.framework.mail.XMLEmail#getXML()
	 */
	public final String getXML() {
		FDXMLSerializer s = new FDXMLSerializer();
		Map map = new HashMap();
		this.decorateMap(map);
		return s.serialize("fdemail", map);
	}

	protected void decorateMap(Map map) {
		map.put("customer", this.customer);
	}

}
