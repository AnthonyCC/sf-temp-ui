package com.freshdirect.cms.ui.model;

import java.io.Serializable;

import com.extjs.gxt.ui.client.data.BaseModel;

public class EnumModel extends BaseModel implements Comparable<EnumModel> {
	
	private static final long serialVersionUID = -6495531896299729410L;

	public EnumModel() {
		super();
	}
	
	public EnumModel(Serializable key, String value) {
		super();
		set("key", key);
		set("label", value + " [" + key + "]");
	}
	
	public Serializable getKey() {
		return get("key");
	}
	
	public String getLabel() {
		return get("label");
	}
	
	public void setKey(Serializable key) {
		set("key", key);
		set("label", get("label") + " [" + key + "]");
	}
	
	public void setLabel(String label) {
		set("label", label + " [" + get("key")+ "]");
	}
	
	@Override
	public String toString() {
	    return "EnumModel["+getKey()+'='+getLabel()+']';
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (obj instanceof EnumModel) {
	        Serializable key = getKey();
	        Serializable otherKey = ((EnumModel) obj).getKey();
	        return (key != null) ? key.equals(otherKey) : (otherKey == null);
	    }
	    return false;
	}

	@Override
	public int compareTo( EnumModel o ) {
		return getKey().toString().compareTo( o.getKey().toString() );
	}
	
}