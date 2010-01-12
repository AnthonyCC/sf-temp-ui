/**
 * 
 */
package com.freshdirect.cms.ui.client.contenteditor;

import java.io.Serializable;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.widget.form.Field;

public class AlternateRenderer implements ContainerParamFactoryI {

	protected FormItemLayout layout;
	
	public AlternateRenderer(FormItemLayout l) {
		layout = l;
	}
	
	/**
	 * Generates a parameters set from specified field
	 * 
	 * Parameters
	 * 
	 * 0 - Field ID
	 * 1 - Field Label
	 * 2 - label width
	 * 3 - left padding
	 * 4 - label separator
	 * 5 - background color
	 * 6 - 'x-form-clear-left'
	 * 7 - label style
	 */
	@Override
	public Params getParams(Field<Serializable> field, int index, El target) {
		Params p = new Params();		
		p.add(field.getId());
		p.add(field.getFieldLabel());
		p.add("width:" + layout.getLabelWidth() + "px");
		p.add("padding-left:0px");
		p.add(field.getLabelSeparator() != null ? field.getLabelSeparator() : layout.getLabelSeparator());
		p.add(field.isHideLabel() ? "x-hide-label" : "" + (index % 2  == 0 ? "" : " gray"));
		p.add("x-form-clear-left");
		p.add(field.getLabelStyle());
		return p;
	}
}
