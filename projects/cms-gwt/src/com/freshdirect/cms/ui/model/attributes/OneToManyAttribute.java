package com.freshdirect.cms.ui.model.attributes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ui.model.OneToManyModel;

public class OneToManyAttribute extends BaseAttribute implements ModifiableAttributeI, Serializable{
	
	private static final long serialVersionUID = 1340741041297940374L;
	
	private List<OneToManyModel> value;
	private Set<String> allowedTypes;
	private boolean navigable;
	
	public OneToManyAttribute() {
	    
	}
	
	
	public OneToManyAttribute(Set<String> allowedTypes) {
		this.allowedTypes = allowedTypes;
		this.value = new ArrayList<OneToManyModel>();
	}
	
	public void addValue(OneToManyModel v) {
		if (v != null) {
			value.add(v);
		}
	}
	
	public void setValue( List<OneToManyModel> values ) {
		value = values;
	}

	public String getType() {
		return "onetomany";
	}

	@Override
	public Serializable getValue() {
	    return (Serializable) value;
	}
	
	public List<OneToManyModel> getModelValues() {
	    return value;
	}

	public void setAllowedTypes(Set<String> allowedTypes) {
		this.allowedTypes = allowedTypes;
	}

	public Set<String> getAllowedTypes() {
		return allowedTypes;
	}
	
	public boolean isNavigable() {
            return navigable;
        }
	
	public void setNavigable(boolean navigable) {
            this.navigable = navigable;
        }
	

	@Override
	public String toString() {
        return "OneToManyAttribute[" + label + ',' + value +',' + allowedTypes + ']';
	}
}
