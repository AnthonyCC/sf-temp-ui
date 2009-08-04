/**
 * @author ekracoff
 * Created on Jan 25, 2005*/

package com.freshdirect.cms.changecontrol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * Type-safe enum of content node change actions.
 * 
 * @see com.freshdirect.cms.changecontrol.ContentNodeChange
 * 
 * @FIXME should be an {@link org.apache.commons.lang.enums.Enum}.
 */
public class EnumContentNodeChangeType implements Serializable {

	/** The node was newly created */
	public static EnumContentNodeChangeType ADD = new EnumContentNodeChangeType("CRE", "Create");
	
	/** The node was removed */
	public static EnumContentNodeChangeType DELETE = new EnumContentNodeChangeType("DEL", "Delete");
	
	/** The node was updated */
	public static EnumContentNodeChangeType MODIFY = new EnumContentNodeChangeType("MOD", "Modify");

	private String code;
	private String name;

	public EnumContentNodeChangeType(String code, String name) {
		this.code = code;
		this.name = name;
	}

	private static List types = null;

	static {
		List t = new ArrayList();
		t.add(ADD);
		t.add(DELETE);
		t.add(MODIFY);

		types = Collections.unmodifiableList(t);
	}

	public static List getTypes() {
		return types;
	}

	public static EnumContentNodeChangeType getType(String code) {
		for (Iterator i = types.iterator(); i.hasNext();) {
			EnumContentNodeChangeType ct = (EnumContentNodeChangeType) i.next();
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