package com.freshdirect.cms.changecontrol;

import java.io.Serializable;

/**
 * Detail information for a {@link com.freshdirect.cms.changecontrol.ContentNodeChange}
 * that records the changed attribute with its old and new values.
 */
public class ChangeDetail implements Serializable {

	private String attributeName;
	private String oldValue;
	private String newValue;

	public ChangeDetail() {
	}

	public ChangeDetail(String attributeName, String oldValue, String newValue) {
		this.attributeName = attributeName;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String name) {
		this.attributeName = name;
	}

	public String getNewValue() {
		return newValue;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	
	@Override
        public String toString() {
	    return "[" + attributeName + ':' + oldValue + "->" + newValue + ']';
        }

}
