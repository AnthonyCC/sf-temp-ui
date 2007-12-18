package com.freshdirect.rules;

/**
 * @author knadeem Date Apr 4, 2005
 */
public class ClassDescriptor {

	private Class targetClass;

	private String label;

	private String xmlTag;

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

}
