package com.freshdirect.cms.ui.model.attributes;

import java.io.Serializable;
import java.util.HashSet;

import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;

public class OneToOneAttribute extends BaseAttribute implements ModifiableAttributeI, Serializable {

	private static final long serialVersionUID = 6321196833240254522L;
	
	private ContentNodeModel value;	
	private HashSet<String> allowedTypes;	
	
	public OneToOneAttribute() {
		value = null;		
	}
	
	public String getType() {		
		return "onetoone";
	}
	
	public ContentNodeModel getValue() { 
		return value;
	}
	
	public void setValue(ContentNodeModel v) {
		value = v;
	}
	
	public void setAllowedTypes(HashSet<String> aTypes) {
		allowedTypes = aTypes;
	}
	
	public HashSet<String> getAllowedTypes() {
		return allowedTypes;
	}

	@Override
	public String toString() {
		return "OneToOneAttribute[" + label + ',' + value + ',' + allowedTypes + ']';
	}

}
