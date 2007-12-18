package com.freshdirect.cms.ui.tapestry.component;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.IValidator;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.EnumDefI;
import com.freshdirect.cms.context.ContextualContentNodeI;

public abstract class PrimitiveField extends BaseComponent {

	public AttributeDefI getDef() {
		AttributeDefI override = getDefinition();
		return override != null ? override : getAttribute().getDefinition();
	}

	public EnumAttributeType getType() {
		return getDef().getAttributeType();
	}

	public int getSize() {
		if (EnumAttributeType.DOUBLE.equals(getType())) {
			return 6;
		}
		if (EnumAttributeType.INTEGER.equals(getType())) {
			return 4;
		}
		return 64;
	}

	public boolean isDisabled() {
		return isInherited() || getDef().isReadOnly();
	}

	public boolean isInherited() {
		return getAttribute().getValue() == null && getDef().isInheritable();
	}

	public Object getAttributeValue() {
		Object o = getAttribute().getValue();
		if (isInherited() && getContextNode() != null) {
			AttributeI ctxAttr = getContextNode().getAttribute(getAttribute().getName());
			o = ctxAttr.getValue();
		}
		return o;
	}

	public void setAttributeValue(Object o) {
		if (!isInherited()) {
			getAttribute().setValue(o);
		}
	}

	public IValidator getValidator() {
		return (IValidator) getBeans().getBean(getDef().getAttributeType().getName() + "_validator");
	}

	public IPropertySelectionModel getEnumSelectionModel() {
		return new EnumDefSelectionModel((EnumDefI) getDef(), isInherited());
	}

	public abstract AttributeI getAttribute();

	public abstract ContextualContentNodeI getContextNode();

	public abstract AttributeDefI getDefinition();

}