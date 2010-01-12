package com.freshdirect.cms.ui.client.fields;

import java.io.Serializable;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentPlugin;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.google.gwt.user.client.Event;

public class FieldResetPlugin extends ToolButton implements ComponentPlugin {

	Field <Serializable> field;

	private Listener<BaseEvent> fieldChangeListener = new Listener<BaseEvent>() {
		@Override
		public void handleEvent(BaseEvent be) {
			Serializable oldValue = field.getOriginalValue();
			Serializable fieldValue = field.getValue();
			System.out.println("changed: " + field.getFieldLabel() + " " + oldValue +" -> " + fieldValue);
			if( (fieldValue!=null && fieldValue.equals(oldValue)) || (fieldValue==null && oldValue==null) ) {
				hide();
			}
			else {
				show();
			}
		}
		
	};
	
	private Listener<BaseEvent> fieldRenderListener = new Listener<BaseEvent>() {
		
		@Override
		public void handleEvent(BaseEvent be) {
			render();
		}
	};
	
	public FieldResetPlugin() {
		super("undo-icon");
		addStyleName("changetracking");
	}


	@SuppressWarnings("unchecked")
	@Override
	public void init(Component component) {
		field = (Field<Serializable>) component;
        field.addListener( Events.Render, fieldRenderListener);
        hide();
	}

	public void render() {
		super.render(field.el().findParentElement("div.x-form-item", 8) , 2);
		sinkEvents( Event.ONCLICK | Event.MOUSEEVENTS | Event.FOCUSEVENTS );
		onAttach();
        field.setFireChangeEventOnSetValue(true);
        field.addListener(Events.Change, fieldChangeListener);
	}
	
	@Override
	protected void onClick(ComponentEvent ce) {
		super.onClick(ce);
		field.reset();
	}
	
	
}
