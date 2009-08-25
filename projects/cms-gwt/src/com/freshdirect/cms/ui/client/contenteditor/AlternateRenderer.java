/**
 * 
 */
package com.freshdirect.cms.ui.client.contenteditor;

import java.io.Serializable;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.widget.form.Field;

public class AlternateRenderer implements ContainerParamFactoryI {

	private TemplateFormLayout layout;
	
	public AlternateRenderer() {			
	}
	
	public AlternateRenderer(TemplateFormLayout l) {
		layout = l;
	}
	
	public Params getParams(Field<Serializable> field, int index, El target) {
		String ls = field.getLabelSeparator() != null ? field.getLabelSeparator() : layout.getLabelSeparator();
		Params p = new Params();
		p.add(field.getId());
		p.add(field.getFieldLabel());
		p.add("width:" + layout.getLabelWidth() + "px");
		p.add("padding-left:" + (layout.getLabelWidth() + 5) + "px");
		p.add(ls);
		p.add(field.isHideLabel() ? "x-hide-label" : "" + (index % 2  == 0 ? "" : " gray"));
		p.add("x-form-clear-left");
		p.add(field.getLabelStyle());
		return p;
	}
	
}