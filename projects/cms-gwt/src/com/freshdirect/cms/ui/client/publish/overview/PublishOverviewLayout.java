package com.freshdirect.cms.ui.client.publish.overview;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.freshdirect.cms.ui.client.Anchor;
import com.freshdirect.cms.ui.client.publish.PublishListener;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;

public class PublishOverviewLayout extends FormLayout {
	
	Template fieldTemplate;
	private String labelStyle;
	private String elementStyle;
	private List<PublishListener> publishListeners = new ArrayList<PublishListener>();

	public PublishOverviewLayout() {
		super();
	    
	    setLabelWidth(120);
	    setLabelPad(4);
	    setLabelAlign(LabelAlign.RIGHT);
		
	    labelStyle = "width:" + (getLabelWidth()) + "px";
	    elementStyle = "padding-left:" + (getLabelWidth() + getLabelPad()) + "px";
	    
		fieldTemplate = generateFieldTemplate();
	}

	private static Template generateFieldTemplate() {
		Template fieldTemplate;
		StringBuffer sb = new StringBuffer();
		sb.append("<div class='x-form-item {5}' tabIndex='-1'>");
		sb.append("<label id='x-form-label-{0}' for={8} style='{2};{7}' class=x-form-item-label>");		
		sb.append("</label>");
		sb.append("<div class='x-form-element' id='x-form-el-{0}' style='{3}'>");
		sb.append("</div><div class='{6}'></div>");
		sb.append("</div>");		
		fieldTemplate = new Template(sb.toString());
		fieldTemplate.compile();
		return fieldTemplate;
	}

	@Override
	protected void renderField(Field<?> field, int index, El target) {
		if (!(field instanceof PublishOverviewField)) {
			super.renderField(field, index, target);
			return;
		}
		final PublishOverviewField summaryField = (PublishOverviewField) field;
		
		String ls = summaryField.getLabelSeparator() != null ? summaryField
				.getLabelSeparator() : getLabelSeparator();
		summaryField.setLabelSeparator(ls);
		Params p = new Params();
		if (getHideLabels()) {
			summaryField.setHideLabel(true);
		}

		p.add(summaryField.getId());
		p.add(summaryField.getFieldLabel());
		p.add(labelStyle);
		p.add(elementStyle);
		p.add(ls);
		p.add(summaryField.isHideLabel() ? "x-hide-label" : "");
		p.add("x-form-clear-left");
		p.add(summaryField.getLabelStyle());

		String inputId = summaryField.getId();
		p.add(inputId);		

		Anchor label = new Anchor(summaryField.getFieldLabel());
		label.addListener(Events.OnClick, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {				
				firePublishDetailEvent(summaryField.getChangeSetQuery());
			}
		});
		
		fieldTemplate.insert(target.dom, index, p);
		if (summaryField.isRendered()) {
			target.selectNode("#x-form-el-" + summaryField.getId()).appendChild(
					summaryField.getElement());
			target.selectNode("#x-form-label-" + summaryField.getId()).appendChild(label.getElement());
		} else {
			summaryField.render(target.selectNode("#x-form-el-" + summaryField.getId()).dom);
			label.render(target.selectNode("#x-form-label-" + summaryField.getId()).dom);
		}

		if (summaryField.getStyleName().contains("-wrap")) {
			inputId += "-input";
			target.selectNode("#x-form-el-" + summaryField.getId()).previousSibling()
					.setAttribute("for", inputId);
		}
	}
	
	private void firePublishDetailEvent(ChangeSetQuery query) {
		for (PublishListener listener : publishListeners) {
			listener.onDetailRequest(query);
		}
	}
	
	public void addPublishListener(PublishListener listener) {
		if (publishListeners.contains(listener)) {
			return;
		}
		publishListeners.add(listener);
	}
	
	public void removePublishListener(PublishListener listener) {
		if (publishListeners.contains(listener)) {
			publishListeners.remove(listener);
		}
	}

}
