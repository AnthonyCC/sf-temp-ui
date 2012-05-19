package com.freshdirect.temails;


/**
 * @author knadeem Date Apr 4, 2005
 */
public class TemplateDescriptor {

	private Class targetClass;

	private String label;

	private String xmlTag;
	
	private String order;
	

	public Class getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(Class targetClass) {
		this.targetClass = targetClass;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getXmlTag() {
		return xmlTag;
	}

	public void setXmlTag(String xmlTag) {
		this.xmlTag = xmlTag;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	

}
