package com.freshdirect.cms.ui.model.attributes;

import java.io.Serializable;
import java.util.Set;

import com.freshdirect.cms.ui.model.ContentNodeModel;

public class OneToOneAttribute extends BaseAttribute implements ModifiableAttributeI, Serializable {

	private static final long serialVersionUID = 6321196833240254522L;
	
	protected ContentNodeModel value;	
	protected Set<String> allowedTypes;	
	
	public OneToOneAttribute() {
		super();
		value = null;		
	}
	
	public OneToOneAttribute( OneToOneAttribute attr ) {
		super( attr );
		value = attr.value;
		allowedTypes = attr.allowedTypes;
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
	
	public void setAllowedTypes(Set<String> aTypes) {
		allowedTypes = aTypes;
	}
	
	public Set<String> getAllowedTypes() {
		return allowedTypes;
	}

	@Override
	public String toString() {
		return "OneToOneAttribute[" + label + ',' + value + ',' + allowedTypes + ']';
	}

}
