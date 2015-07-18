package com.freshdirect.cms.ui.model.attributes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.freshdirect.cms.ui.model.EnumModel;

public class MultiEnumAttribute extends BaseAttribute implements ModifiableAttributeI, Serializable {

    private static final long    serialVersionUID = 30645547074161589L;

    private ArrayList<EnumModel> values;
    private List<EnumModel>      value;

    public MultiEnumAttribute() {
        value = new ArrayList<EnumModel>();
        values = new ArrayList<EnumModel>();
    }

	public MultiEnumAttribute( String valueKey, String valueLabel, String label ) {
		this.value = Arrays.asList(new EnumModel( valueKey, valueLabel ));
		this.values = new ArrayList<EnumModel>();
		this.label = label;
	}

    public String getType() {
        return "enum";
    }

	public void addValue( Serializable k, String l ) {
		values.add( new EnumModel( k, l ) );
	}
    
    public void addValues( List<EnumModel> list ) {
    	values.addAll( list );
    }

    public Serializable getValue() {
        return value == null ? null : (Serializable)value;
    }

    public List<EnumModel> getEnumModel() {
        return value;
    }

    public List<EnumModel> getValues() {
        return values;
    }


    public void addSelectedValue(Serializable k, String v) {
        value.add(new EnumModel(k, v));
    }

    @Override
    public String toString() {
        return "[" + label + '=' + value + ']';
    }
}