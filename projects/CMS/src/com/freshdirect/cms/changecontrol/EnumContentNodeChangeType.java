/**
 * @author ekracoff
 * Created on Jan 25, 2005*/

package com.freshdirect.cms.changecontrol;

import java.io.Serializable;


/**
 * Type-safe enum of content node change actions.
 * 
 * @see com.freshdirect.cms.changecontrol.ContentNodeChange
 * 
 */
public enum EnumContentNodeChangeType implements Serializable {

	/** The node was newly created */
	ADD ("CRE", "Create"),
	
	/** The node was removed */
	DELETE ("DEL", "Delete"),
	
	/** The node was updated */
	MODIFY ("MOD", "Modify");

	private String code;
	private String name;

	private EnumContentNodeChangeType(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static EnumContentNodeChangeType getType(String code) {
		for (EnumContentNodeChangeType ct : values()) {
			if (ct.getCode().equals(code)) {
				return ct;
			}
		}
		return null;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

}