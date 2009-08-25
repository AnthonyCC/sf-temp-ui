package com.freshdirect.cms.ui.client.contenteditor;

import java.io.Serializable;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.widget.form.Field;

public interface ContainerParamFactoryI {

	public Params getParams(Field<Serializable> field, int index, El target);
	
}
