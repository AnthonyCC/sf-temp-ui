package com.freshdirect.cms.ui.client.fields;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.MultiField;

/**
 *	Wraps a content editor field, adds a checkbox for switching the field's value to the inherited value. 
 * @param <TYPE> type of the inner Field
 */
public class InheritanceField<TYPE> extends MultiField<TYPE> {

    Field<TYPE> innerField;
    CheckBox checkbox;
    String attributeKey;
    
    TYPE inheritedValue = null;
    TYPE explicitValue = null;
    
    protected boolean readonly;
    
    public InheritanceField( Field<TYPE> field, boolean override, String attrKey, boolean readonly ) {
    	
        super();
        addStyleName("inheritable");
        
        this.attributeKey = attrKey;
        this.readonly = readonly;
        
        this.innerField = field;
        this.innerField.addStyleName("inheritable-value");
        
        checkbox = new CheckBox();
        checkbox.addStyleName("inheritance-checkbox");
        
        this.add( innerField );
		this.add( checkbox );
        
        if ( override ) {
            checkbox.setValue(false);            
            disable();
        } else {
            checkbox.setValue(true);
        	enable();
        }
        
        if ( readonly ) {
        	disable();
        	checkbox.setReadOnly( true );
        	checkbox.disable();
        	
    		innerField.setReadOnly( true );
    		innerField.disable();
    		
			innerField.addInputStyleName( "cms-field-disabled" );
			innerField.addStyleName( "cms-field-disabled" );
			
        } else {        
	        checkbox.addListener( Events.OnClick, checkBoxListener );
	        checkbox.addListener( Events.OnKeyPress, checkBoxListener );
        }
    }
    
    public Listener<BaseEvent> checkBoxListener = new Listener<BaseEvent>() {
        public void handleEvent( BaseEvent event ) {
            if ( checkbox.getValue() ) {
            	enable();
            	inheritedValue = innerField.getValue();
                innerField.setValue( explicitValue );
            } else {
            	disable();
            	explicitValue = innerField.getValue();
                innerField.setValue( inheritedValue );
            }
        }
    };

    @Override
    public void disable() {
		super.disable();
		if ( checkbox.getValue() )
			innerField.enable();    	
    	checkbox.enable();
    }
    
    @Override
    public void enable() {
    	super.enable();
    	if ( !checkbox.getValue() )
			innerField.disable();
    	checkbox.enable();
    }
    
    @Override
    public void setReadOnly( boolean readOnly ) {
    	super.setReadOnly( readOnly );
    	innerField.setReadOnly( readOnly );
    	if ( readOnly ) {
    		disable();
    		innerField.disable();
    		
			innerField.addInputStyleName( "cms-field-disabled" );
			innerField.addStyleName( "cms-field-disabled" );
			
        	checkbox.disable();    		
    	} else {
			enable();
			checkbox.enable();
		}
    }

    
    @SuppressWarnings("unchecked")
    @Override
    public TYPE getValue() {
        if ( checkbox.getValue() ) {
            return innerField.getValue();
        }
        if (innerField instanceof HasCustomDefaultValue) {
            return (TYPE) ((HasCustomDefaultValue) innerField).getDefaultValue();
        }
        return null;
    }
    
    public boolean isOverrideValue() {
        return !checkbox.getValue();
    }
    
    public void setInheritedValue(TYPE value) {
    	this.inheritedValue = value;
    	if ( !checkbox.getValue() ) {
    		innerField.setValue( value );
    	}
    }
}
