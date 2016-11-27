/**
 * 
 */
package com.freshdirect.cms.ui.client.contenteditor;

import java.io.Serializable;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.widget.form.Field;

public class SectionAlternateRenderer extends AlternateRenderer {
	protected int containerIndex;
	
	public SectionAlternateRenderer(FormItemLayout l, int index) {		
		super(l);
		containerIndex = index;
	}	
	
	@Override
	public Params getParams(Field<Serializable> field, int index, El target) {
		return super.getParams(field, containerIndex++, target);
	}
}
