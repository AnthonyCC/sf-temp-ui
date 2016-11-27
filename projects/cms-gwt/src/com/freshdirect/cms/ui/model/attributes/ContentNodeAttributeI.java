package com.freshdirect.cms.ui.model.attributes;

import java.io.Serializable;

import com.extjs.gxt.ui.client.widget.form.Field;

public interface ContentNodeAttributeI extends Serializable {

	public String getLabel();
	public String getType();
	/**
	 * Return a value which can be used on the client side.
	 * @return
	 */
	public Serializable getValue();

	public boolean isReadonly();
	
	public boolean isInheritable();
	
	public Field<? extends Serializable> getFieldObject();
	
	public void setFieldObject(Field<? extends Serializable> obj);
}
