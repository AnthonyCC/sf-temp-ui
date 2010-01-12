package com.freshdirect.cms.ui.client.fields;

import java.io.Serializable;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.freshdirect.cms.ui.client.contenteditor.CompareNodesUtil;
import com.freshdirect.cms.ui.client.contenteditor.ContentEditorPanel;
import com.freshdirect.cms.ui.client.views.ManageStoreView;
import com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;

public class FieldHotSpot extends Html {
	private ContentNodeAttributeI attribute;
	final String key;
	final Serializable value;
	final Listener<BaseEvent> openPopup;
	private boolean active;
	private CompareNodesUtil compareUtil;
	
	
	public FieldHotSpot( ContentNodeAttributeI attribute, final String key, Serializable otherValue ) {
		super();
		addStyleName("merge-hotspot");

		this.key = key;
		this.attribute = attribute;
		this.value = otherValue;
		this.compareUtil = ((ContentEditorPanel) ManageStoreView.getInstance().getDetailPanel()).getCompareUtil();

		this.openPopup = new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				((BorderLayout) ManageStoreView.getInstance().getLayout()).collapse(LayoutRegion.WEST);
				compareUtil.getComparePopup().preview((FieldHotSpot) be.getSource());
			}			
		};
		
		this.attribute.getFieldObject().setFireChangeEventOnSetValue(true);
		
		this.attribute.getFieldObject().addListener(Events.Change, new Listener<BaseEvent>(){
		
			
			@Override
			public void handleEvent(BaseEvent e) {
				if(CompareNodesUtil.compareAttribute(key, compareUtil.getEditedNode(), compareUtil.getComparedNode())) {
					compareUtil.addDifference(key);
					setActive(true);
				}
				else {
					compareUtil.removeDifference(key);
					setActive(false);
				}
				
				updateColors();
			}
			
		});
		
		active = false;
	}
	
	private void updateColors() {
		if(active) {
			addStyleName("merge-hotspot-active");
		}
		else {
			removeStyleName("merge-hotspot-active");
		}
		
		if(compareUtil.isDefferent(key)) {
			addStyleName("merge-color");
		}
		else {
			removeStyleName("merge-color");
		}				
	}

	public void setAttribute(ContentNodeAttributeI attribute) {
		this.attribute = attribute;
	}


	public ContentNodeAttributeI getAttribute() {
		return attribute;
	}
	
	public String getKey() {
		return key;
	}

	public Serializable getValue() {
		return value;
	}
	
	public void render() {
		Field<?> field = attribute.getFieldObject();

		super.render(field.el().findParentElement("div.x-form-item", 8) , 0);
		sinkEvents( Event.ONCLICK | Event.MOUSEEVENTS | Event.FOCUSEVENTS );
		onAttach();
		updateColors();
	}
	
	@Override
	public void removeFromParent() {
		Field<?> field = attribute.getFieldObject();
		Element formItem = (Element) field.el().findParentElement("div.x-form-item", 8);
		formItem.removeClassName("merge-border");
		formItem.removeChild( getElement() );
		super.removeFromParent();
	}

	public void setActive(boolean active) {
		if(active==this.active) {
			return;
		}
		
		this.active=active;
		if(active) {
			addListener(Events.OnClick, openPopup );
		}
		else {
			if(compareUtil.getComparePopup().getHotspot().equals(this)) {
				compareUtil.getComparePopup().hide();
			}
			removeListener(Events.OnClick, openPopup );
		}
		
		updateColors();
	}
}
