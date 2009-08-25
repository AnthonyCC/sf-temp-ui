
package com.freshdirect.cms.ui.client.fields;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.widget.form.Field;

/**
 *	Event type to signal attribute changes. Used primarily by special components like variaton matrix.
 */
@SuppressWarnings("unchecked")
class AttributeChangeEvent extends BaseEvent {

    public static final EventType TYPE = new EventType();
    
    Field field;
    
    public AttributeChangeEvent(Field field) {
        super(TYPE);
        this.field = field;
    }
    
    public Field getField() {
        return field;
    }
}