package com.freshdirect.cms.ui.model.attributes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.ui.model.EnumModel;

public class EnumAttribute extends BaseAttribute implements ModifiableAttributeI, Serializable {

    private static final long    serialVersionUID = 30645547074161589L;

    private ArrayList<EnumModel> values;
    private EnumModel            value;

    public EnumAttribute() {
        value = new EnumModel();
        values = new ArrayList<EnumModel>();
    }

	public EnumAttribute( String valueKey, String valueLabel, String label ) {
		this.value = new EnumModel( valueKey, valueLabel );
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
        return value == null || value.getKey() == null ? null : value;
//        // we have to check that no value is not null, we have to correct it, so correct EnumModel is stored.
//        return value != null ? value.getKey() : null;
    }

    public EnumModel getEnumModel() {
        return value;
    }

    public List<EnumModel> getValues() {
        return values;
    }


    public void setValue(Serializable k, String v) {
        value = new EnumModel(k, v);
    }
    


    @Override
    public String toString() {
        return "EnumAttribute[" + label + '=' + value + ", values => " + values + ']';
    }
}
