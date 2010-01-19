/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.freshdirect.cms.ui.client.contenteditor;

import java.io.Serializable;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.Element;


/**
 * Template that renders a node form item
 * 
 */
public class FormItemLayout extends FormLayout {
  
	private ContainerParamFactoryI parameterFactory;
	private Template fieldTemplate;

	public FormItemLayout() {
		this( LabelAlign.RIGHT );
	}

	public FormItemLayout(LabelAlign labelAlign) {
		super(labelAlign);

		StringBuffer sb = new StringBuffer();
		sb.append("<div class='x-form-item {5}' tabIndex='-1' style=\"overflow:hidden\">\n");
		sb.append("  <label for='{0}' style='{2};{7}' class='x-form-item-label'>{1}{4}");
		sb.append("<br><span style='color: #777777; font-size: 70%; font-style:italic;'>{8}</span>");
		sb.append("</label>");
		sb.append("  <div class='x-form-element' id='x-form-el-{0}' style='{3};float:left'>");
		sb.append("  </div>");
		sb.append("  <div class='{6}'></div>");
		sb.append("</div>");
		
		setLabelWidth( 200 );
		setLabelPad( 4 );
		setLabelSeparator("");
		
		fieldTemplate = new Template(sb.toString());
		fieldTemplate.compile();
	}
	
	public FormItemLayout(LabelAlign labelAlign, Template t) {
		super(labelAlign);
		fieldTemplate = t;
		t.compile();
	}
	
	public void setParameterFactory(ContainerParamFactoryI fact) {
		parameterFactory = fact;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void renderComponent(Component component, int index, El target) {
		if (component instanceof Field && !(component instanceof HiddenField)) {

			Field<Serializable> f = (Field<Serializable>) component;
			renderField((Field<Serializable>) component, index, target);
			FormData formData = (FormData) getLayoutData(f);
			if (formData == null) {
				formData = f.getData("formData");
			}
			
			f.setWidth(450);
			if (formData != null) {
				if (formData.getWidth() > 0) {
					f.setWidth(formData.getWidth());
				}
				if (formData.getHeight() > 0) {
					f.setHeight(formData.getHeight());
				}
			}
			
		} else {
			super.renderComponent(component, index, target);
		}
	}

	@SuppressWarnings( "unchecked" )
	@Override
	protected void renderField(Field<?> field, int index, El target) {		
		if (field != null && !field.isRendered()) {
			String ls = field.getLabelSeparator() != null ? field.getLabelSeparator() : getLabelSeparator();
			field.setLabelSeparator(ls);
			String labelStyle = "";
			String elementStyle = "";

			if (getHideLabels()) {
				labelStyle = "display:none";
				elementStyle = "padding-left:0; width:auto;";
			} else {
				labelStyle = "width:" + getLabelWidth() + "px";
				elementStyle = "padding-left:" + (getLabelWidth() + 5) + "px; width:auto;";
				if (getLabelAlign() == LabelAlign.TOP) {
					labelStyle = "width:auto;";
					elementStyle = "padding-left:0;";
				}
			}

			Params p;
			if (parameterFactory == null) {
				p = new Params();
				p.add(field.getId());
				p.add(field.getFieldLabel());
				p.add(labelStyle);
				p.add(elementStyle);
				p.add(ls);
				p.add(field.isHideLabel() ? "x-hide-label" : "");
				p.add("x-form-clear-left");
				p.add(field.getLabelStyle());
				p.add(field.getData("contentKey") == null ? "" : field.getData("contentKey"));				
			}
			else {
				p = parameterFactory.getParams((Field<Serializable>)field, index, target);
			}				
			
			fieldTemplate.insert(target.dom, index, p);

			Element parent = XDOM.getElementById("x-form-el-" + field.getId());
			field.render(parent);
		} else {
			super.renderComponent(field, index, target);
		}
	}
}
