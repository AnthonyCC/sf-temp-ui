package com.freshdirect.cms.ui.tapestry.component;

import org.apache.tapestry.BaseComponent;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.EnumAttributeType;

public abstract class ContentField extends BaseComponent {

	public AttributeDefI getDef() {
		return getAttribute().getDefinition();
	}

	public EnumAttributeType getType() {
		return getDef().getAttributeType();
	}

	public boolean isValueSet() {
		return getAttribute().getValue() != null;
	}

	public void setValueSet(boolean valueSet) {
		if (isValueSet() == valueSet) {
			return;
		}
		if (valueSet) {
			Object value = getDef().getEmptyValue();
			getAttribute().setValue(value);
		} else {
			getAttribute().setValue(null);
		}
	}

	public abstract AttributeI getAttribute();

}