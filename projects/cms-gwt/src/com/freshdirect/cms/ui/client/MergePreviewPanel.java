package com.freshdirect.cms.ui.client;

import java.io.Serializable;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.layout.AbsoluteData;
import com.extjs.gxt.ui.client.widget.layout.AbsoluteLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.freshdirect.cms.ui.client.contenteditor.CompareNodesUtil;
import com.freshdirect.cms.ui.client.contenteditor.ContentEditorPanel;
import com.freshdirect.cms.ui.client.contenteditor.FormItemLayout;
import com.freshdirect.cms.ui.client.fields.FieldHotSpot;
import com.freshdirect.cms.ui.client.views.ManageStoreView;
import com.freshdirect.cms.ui.model.GwtContextualizedNodeData;
import com.freshdirect.cms.ui.model.GwtContextualizedNodeI;
import com.google.gwt.user.client.Element;

public class MergePreviewPanel extends LayoutContainer {
	Field<Serializable> field; // original
	Field<Serializable> otherField; // original
	String attributeKey;
	Serializable otherValue;
	Serializable startValue;
	private ToolButton closeButton = new ToolButton("collapse-button");
	private Button mergeButton = new Button("Replace");
	private Element formElement;
	private Element formItemElement;
	private ContentPanel c = new ContentPanel();
	private Html arrow = new Html();
	private Text title = new Text();
	private FieldHotSpot hotspot;
	private CompareNodesUtil compareUtil;
		
	public MergePreviewPanel() {
		super();
		this.compareUtil = ((ContentEditorPanel) ManageStoreView.getInstance().getDetailPanel()).getCompareUtil();
		
		setLayout(new AbsoluteLayout());
		addStyleName("fieldpopup");
		setWidth(510);
		
		arrow.setTagName("div");
		arrow.addStyleName("merge-color");
		arrow.setWidth(16);
		arrow.setHeight(30);
		
		FormItemLayout l = new FormItemLayout();
		l.setHideLabels(true);
		c.setLayout(l);
		c.setHeaderVisible(false);
		c.setBodyBorder(false);
		c.addStyleName("merge-border");
		c.setAutoWidth(true);
		
		ToolBar headerBar = new ToolBar();
		headerBar.add(title);
		headerBar.add(new FillToolItem());
		headerBar.add(closeButton);
		c.setTopComponent(headerBar);
		
		closeButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				hide();
			}
		});
		
		mergeButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				doOverride();
				hide();
			}
		});
		
		c.setButtonAlign(HorizontalAlignment.LEFT);
		c.addButton(mergeButton);
		c.setAutoHeight(true);

		add(c, new AbsoluteData(16,0));
		add(arrow, new AbsoluteData(0, 18));
	}
	
	/**
	 * Action to override value of current node
	 */
	protected void doOverride() {
		field.setValue(otherValue );
	}	
	
	protected void removeColor() {
		if(formItemElement != null) {
			formItemElement.removeClassName("merge-border");
			formItemElement.removeClassName("override-border");			
		}
		
	}
	
	public void initialize() {
		Element currentFormElement = hotspot.el().findParentElement("form", 16);

		removeColor();
		formItemElement = hotspot.el().getParent().dom;
		
		if( formElement!=null && formElement != currentFormElement ) {
			currentFormElement.appendChild( formElement.removeChild( this.getElement() ));
		}

		formElement = currentFormElement;			
		
		
		if(isRendered()) {
			return;
		}
				
		render(formElement);
		c.setLayoutOnChange(true);
		setAutoHeight(true);
		c.setAutoHeight(true);
		setStyleAttribute("position", "absolute");
		onAttach();
	}
					
	public void setField(Field<Serializable> field) {
		if(otherField != null) {
			c.remove(otherField);
		}
		otherField = field;
		c.add(otherField);
	}	

	public void preview(FieldHotSpot hs) {
		if (hs == null)
			return;
		
		setHotspot(hs);
		
		attributeKey = hotspot.getKey();
		otherValue = hotspot.getValue();
		

		final GwtContextualizedNodeI n = new GwtContextualizedNodeData(compareUtil.getComparedNode(), compareUtil.getComparedNode().getCurrentContext()); 
		final Field<Serializable> otherField = FieldFactory.createStandardField(n, hs.getKey());
		otherField.setReadOnly(true);

		// original field
		field = hotspot.getAttribute().getFieldObject();
		if(field == null ) {
			System.err.println("field is null");
			return;
		}

		setField(otherField);
		initialize( );
		title.setText(compareUtil.getComparedNode().getNode().getLabel());
		setTop(hs.getPosition(false).y );
		setLeft(hs.getPosition(true).x );
		if(attributeKey.equals("PRIMARY_HOME") || attributeKey.equals("skus") || compareUtil.getEditedNode().isReadonly()) {
			mergeButton.hide();
		}
		else {
			mergeButton.show();
		}
		layout(true);
		repaint();
		show();
	}
	
	public FieldHotSpot getHotspot() {
		return hotspot;
	}

	public void setHotspot(FieldHotSpot hotspot) {
		this.hotspot = hotspot;
	}

	@Override
	public void show() {
		formItemElement.addClassName("merge-border");
		super.show();
	}
	@Override
	public void hide() {
		removeColor();
		super.hide();
	}
	
	public void setTop(int value) {
		el().setTop(value-formElement.getAbsoluteTop()+formElement.getScrollTop()-20);
	}

	public void setLeft(int value) {
		el().setLeft(740);
	}	
	
	public Element getFormElement() {
		return formElement;
	}
	
	@Override
	public void removeFromParent() {
		if(formElement != null) {
			formElement.removeChild(getElement());	
		}

		super.removeFromParent();
	}
	
}