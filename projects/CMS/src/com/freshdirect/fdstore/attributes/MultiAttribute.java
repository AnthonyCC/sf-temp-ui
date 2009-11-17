package com.freshdirect.fdstore.attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiAttribute extends Attribute {
    
	private List<Object> valueList;

    public MultiAttribute(EnumAttributeType type, String key) {
		this(type, key, false);
    }

    public MultiAttribute(EnumAttributeType type, String key, boolean inheritable) {
		super(type, key, inheritable);
		valueList = new ArrayList<Object>();
    }
    
    @Override
    public Object getValue() {
		return getValues();
    }
    
    public int numberOfValues() {
        return valueList.size();
    }

	public Object getValue( int idx ) {
		if ( idx < valueList.size() )
			return valueList.get(idx);
		else 
			return null;
	}
	
    @SuppressWarnings( "unchecked" )
	@Override
	public void setValue(Object valueList) {
    	if ( valueList instanceof List<?> ) {
    		this.valueList = (List<Object>)valueList;
    	}
	}

	public void addValue(Object value) {
		// !!! check value for EnumAttributeType -> throw new IllegalArgumentException();
		valueList.add(value);
	}
    
    public List<Object> getValues() {
        return Collections.unmodifiableList( valueList );
    }
	
    @Override
    public String toString() {
        return "MultiAttribute[" + getKey() + " - " + getType().getName() + " - " + numberOfValues() + " values ]";
    }
	
    @Override
	public Attribute copy() {
		MultiAttribute ma = new MultiAttribute(getType(),getKey(),isInheritable());
		for (Object o : valueList ) {
			ma.addValue( o );
		}
		return ma;
	}
}
