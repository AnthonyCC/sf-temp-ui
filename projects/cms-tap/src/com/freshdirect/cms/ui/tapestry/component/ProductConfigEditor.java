package com.freshdirect.cms.ui.tapestry.component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.EnumDefI;
import com.freshdirect.cms.fdstore.ConfiguredProductValidator;
import com.freshdirect.cms.meta.EnumDef;
import com.freshdirect.cms.node.ContentNodeUtil;

public abstract class ProductConfigEditor extends BaseComponent {

	private Map getConfigurationOptions() {
		Map options = ConfiguredProductValidator.getConfigurationOptions(getNode());
		cleanupOptions(options);
		return options;
	}

	private void setConfigurationOptions(Map options) {
		cleanupOptions(options);
		ConfiguredProductValidator.setConfigurationOptions(getNode(), options);
	}

	private void cleanupOptions(Map options) {
		Map def = getConfigurationDefinition();
		options.keySet().retainAll(def.keySet());
	}

	private ContentNodeI getSkuNode() {
		ContentKey skuKey = (ContentKey) getNode().getAttribute("SKU").getValue();
		return skuKey == null ? null : skuKey.getContentNode();
	}

	public Map getConfigurationDefinition() {
		ContentNodeI sku = getSkuNode();
		if (sku == null) {
			return new HashMap();
		}
		return ConfiguredProductValidator.getDefinitionMap(sku);
	}

	public String getCurrCharacteristic() {
		Map.Entry e = (Map.Entry) getCurrDefEntry();
		return (String) e.getKey();
	}

	public void setCurrValue(String value) {
		Map m = getConfigurationOptions();
		m.put(getCurrCharacteristic(), value);
		setConfigurationOptions(m);
	}

	public String getCurrValue() {
		return (String) getConfigurationOptions().get(getCurrCharacteristic());
	}

	public IPropertySelectionModel getCurrValueSelectionModel() {
		Map.Entry e = (Map.Entry) getCurrDefEntry();
		EnumDefI enumDef = (EnumDefI) e.getValue();
		return new EnumDefSelectionModel(enumDef, false);
	}

	public EnumDefI getSalesUnitDefinition() {
		Map m = ConfiguredProductValidator.getSalesUnits(getSkuNode());
		Map values = new TreeMap();
		for (Iterator i = m.values().iterator(); i.hasNext();) {
			ContentNodeI su = (ContentNodeI) i.next();
			String value = su.getKey().getId();
			String label = ContentNodeUtil.getLabel(su);
			values.put(value, label);
		}
		AttributeDefI origDef = getNode().getAttribute("SALES_UNIT").getDefinition();
		EnumDef def = new EnumDef(origDef.getName(), origDef.getLabel(), origDef.isRequired(), origDef.isInheritable(), origDef
			.isReadOnly(), EnumAttributeType.STRING, values);
		return def;
	}

	public abstract ContentNodeI getNode();

	public abstract Object getCurrDefEntry();

}
