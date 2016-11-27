package com.freshdirect.cms.ui.client.fields;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.freshdirect.cms.ui.model.EnumModel;
import com.freshdirect.cms.ui.model.attributes.EnumAttribute;

public class EnumField extends ComboBox<EnumModel> {
	EnumAttribute attribute;


	public EnumField(EnumAttribute attribute) {
		this(attribute, null);
	}
	
	public EnumField(EnumAttribute attr, EnumModel value) {
		this.attribute = attr;
		
		ListStore<EnumModel> store = new ListStore<EnumModel>();
		store.add( attr.getValues() );
		
		setStore( store );
		setValueField( "key" );
		setDisplayField( "label" );
		setEditable( false );
		setForceSelection( true );
		setTriggerAction(TriggerAction.ALL);
				
		if (value != null)
			setValue( value );
	}


	@Override
	public void setValue(EnumModel value) {
		if (value != null && !attribute.getValues().contains(value)) {
			value = null;
		}

		super.setValue(value);
	}
}
