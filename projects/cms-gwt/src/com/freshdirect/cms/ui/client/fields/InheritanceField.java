package com.freshdirect.cms.ui.client.fields;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.MultiField;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.OneToManyModel;
import com.google.gwt.user.client.Element;

/**
 *	Wraps a content editor field, adds a checkbox for switching the field's value to the inherited value. 
 * @param <TYPE> type of the inner Field
 */
public class InheritanceField<TYPE> extends MultiField<TYPE> {
    Field<TYPE> innerField;
    
    /**
     * Checkbox that controls which type of value to be returned
     * If it's checked the field returns inherited value
     */
    CheckBox checkbox;
    
    TYPE inheritedValue = null;
    TYPE explicitValue = null;

    /**
     * Collections.singleton(..) is not GWT friendly - not able to serialize it.
     */
    final static List<OneToManyModel> CLEAR_LIST = new ArrayList<OneToManyModel>(1);  
    static {
        CLEAR_LIST.add(OneToManyModel.NULL_MODEL); 
    }

    public InheritanceField( Field<TYPE> field, boolean inherit, boolean readonly ) {
        super();

        addStyleName("inheritable");

        this.innerField = field;
        this.innerField.addStyleName("inheritable-value");
        
        checkbox = new CheckBox();
        checkbox.addStyleName("inheritance-checkbox");
        
        this.add( innerField );
		this.add( checkbox );
        

		// adjust for overridden / inherited state
        checkbox.setValue(!inherit);
        innerField.setEnabled(!inherit);

        setReadOnly(readonly);
        
        innerField.setFireChangeEventOnSetValue(true);
        innerField.addListener(Events.Change, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
				fireEvent(Events.Change);				
			}
        	
        });
        
        checkbox.setFireChangeEventOnSetValue(true);
        checkbox.addListener( Events.Change, checkBoxListener );
    }

    /**
     * Handle checkbox events
     */
    public Listener<BaseEvent> checkBoxListener = new Listener<BaseEvent>() {
        public void handleEvent( BaseEvent event ) {
        	final boolean override = checkbox.getValue();
        	
        	innerField.setEnabled(override);
        	
        	if ( override ) {
            	// checkbox = true ==> value is explicitly set / overridden

            	// enable();
                innerField.setValue( explicitValue );
            } else {
            	// checkbox = false ==> value is inherited
            	
            	// disable();
            	explicitValue = innerField.getValue();
                innerField.setValue( inheritedValue );
            }
			fireEvent(Events.Change);				
        }
    };


    
    @Override
    public void disable() {
		super.disable();

		// checkbox value = true =>
    	//   override state =>
    	//   enable inner field
		if ( checkbox.getValue() )
			innerField.enable();    	
    	checkbox.enable();
    }
    
    @Override
    public void enable() {
    	super.enable();
    	
    	// checkbox value = false =>
    	//   inherited state =>
    	//   disable inner field
    	if ( !checkbox.getValue() )
			innerField.disable();
    	checkbox.enable();
    }
    
    @Override
    public void setReadOnly( boolean readOnly ) {
    	super.setReadOnly( readOnly );

//     	if (readOnly) {
//	        checkbox.removeListener( Events.OnClick, checkBoxListener );
//	        checkbox.removeListener( Events.OnKeyPress, checkBoxListener );
//    	} else {
//	        checkbox.addListener( Events.OnClick, checkBoxListener );
//	        checkbox.addListener( Events.OnKeyPress, checkBoxListener );
//    	}

    }

    
    @SuppressWarnings("unchecked")
    @Override
    public TYPE getValue() {
        if ( checkbox.getValue() ) {
            TYPE obj = innerField.getValue();
            if (obj instanceof List<?> && ((List)obj).size() == 0) {
                return (TYPE) CLEAR_LIST;
            }
            return obj;
        }
        if (innerField instanceof HasCustomDefaultValue) {
            return (TYPE) ((HasCustomDefaultValue) innerField).getDefaultValue();
        }
        return null;
    }
    
    public TYPE getEffectiveValue() {
    	TYPE result = getValue();
    	if(result == null) {
    		result = inheritedValue;
    	}
    		
    	return result;
    }
    
    public boolean isOverrideValue() {
        return !checkbox.getValue();
    }
    

    /**
     * Sets value of the field and adjusts its internal state.
     * Non null value means explicit / overridden state.
     */
    public void setValue(TYPE value) {
        // translate between list with one 'null' node, and an empty list
        if (value instanceof List) {
            List l = (List) value;
            if (isNullList(l)) {
                // important, not to change the original 'value' object
                value = (TYPE) new ArrayList<Object>(0);
            } else if (l.size() == 0) {
                // empty list means 'inherit' from parent
                value = null;
            }
        }
    	if (value != null) {
        	explicitValue = value;
    		checkbox.setValue(true);
    		innerField.setValue(value);
    	} else {
    		checkbox.setValue(false);
    		try {
    			innerField.setValue(inheritedValue);
    		}
    		catch(ClassCastException e) {
    			System.out.println(e);
    		}
    	}
    }

    static boolean isNullList(List l) {
        if (l.size() == 1) {
            if (l.get(0) instanceof OneToManyModel) {
                OneToManyModel one = (OneToManyModel) l.get(0);
                return (one.isNullType());
            }
        }
        return false;
    }
    
    public void setExplicitValue(TYPE value) {
        this.explicitValue = value;
        if (checkbox.getValue()) {
            innerField.setValue(value);
        }
    }
    
    @SuppressWarnings("unchecked")
    public void setInheritedValue(TYPE value) {
        this.inheritedValue = value;
        if (!checkbox.getValue()) {
            try {
                innerField.setValue(((TYPE) ((value instanceof List && isNullList((List) value)) ? new ArrayList() : value)));
            } catch (ClassCastException e) {
                System.out.println(e);
            }
        }
    }
    
    @Override
    protected void onRender(Element target, int index) {
    	originalValue = getValue();
    	super.onRender(target, index);
    }
    
    @Override
    public void setWidth(int width) {
    	innerField.setWidth(width);
    }


    public Field<TYPE> getInnerField() {
		return innerField;
	}
}
