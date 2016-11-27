package com.freshdirect.cms.ui.client.changehistory;

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

public class FieldClearPlugin extends ToolButton implements ComponentPlugin {

	Field <Serializable> field;

	private Listener<BaseEvent> fieldChangeListener = new Listener<BaseEvent>() {
		@Override
		public void handleEvent(BaseEvent be) {
			if( field.getValue() == null ) {
				hide();
			} else {
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
	
	public FieldClearPlugin() {
		super("delete-button");
		addStyleName("field-clear-plugin");
	}


	@SuppressWarnings("unchecked")
	@Override
	public void init(Component component) {
		field = (Field<Serializable>) component;
        field.addListener( Events.Render, fieldRenderListener);
		if( field.getValue() == null ) {
			hide();
		} else {
			show();
		}
	}

	public void render() {
		super.render(field.el().findParentElement("div.x-form-element", 8) , 2);
		sinkEvents( Event.ONCLICK | Event.MOUSEEVENTS | Event.FOCUSEVENTS );
		onAttach();
        field.setFireChangeEventOnSetValue(true);
        field.addListener(Events.Change, fieldChangeListener);
	}
	
	@Override
	protected void onClick(ComponentEvent ce) {
		super.onClick(ce);
		field.clear();
	}
}
