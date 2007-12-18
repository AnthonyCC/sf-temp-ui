/*
 * Created on Aug 10, 2005
 */
package com.freshdirect.cms.fdstore;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.meta.EnumDef;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidatorI;
import com.freshdirect.customer.ejb.ErpOrderLineUtil;

/**
 * {@link com.freshdirect.cms.validation.ContentValidatorI} that ensures
 * that configuration options are consistent with the selected SKU on
 * <code>ConfiguredProduct</code> instances. Ensures that:
 * <ul>
 *  <li>a SKU is specified</li>
 *  <li>the corresponding SKU has a material</li>
 *  <li>the sales unit is consistent with the material</li>
 *  <li>all characteristics of the material are specified and valid</li>
 *  <li>no extraneous characteristics are specified</li>
 *  <li>contained in either:
 *   <ul>
 *    <li>0..n Category or</li>
 *    <li>0..1 ConfiguredProductGroup or</li>
 *    <li>0..1 RecipeSection</li>
 *    <li>0..1 StarterList</li>
 *   </ul>
 *  </li>	
 * </ul>
 * 
 * @TODO validate quantity (min/max/increment)
 */
public class ConfiguredProductValidator implements ContentValidatorI {

	public void validate(ContentValidationDelegate delegate, ContentServiceI service, ContentNodeI node, CmsRequestI request) {
		if (!FDContentTypes.CONFIGURED_PRODUCT.equals(node.getKey().getType())) {
			return;
		}

		ContentKey skuKey = (ContentKey) node.getAttribute("SKU").getValue();
		if (skuKey == null) {
			delegate.record(node.getKey(), "SKU", "No SKU specified");
			return;
		}
		ContentNodeI sku = skuKey.getContentNode();
		if (sku == null) {
			delegate.record(node.getKey(), "SKU", skuKey.getEncoded() + " not found");
			return;
		}
		
		// check that the material exists
		List materialKeys = getMaterials(sku);
		if (materialKeys.isEmpty()) {
			delegate.record(node.getKey(), "SKU", "No material found for this SKU");
			return;
		}

		// validate sales unit

		Map salesUnits = getSalesUnits(sku);
		String su = (String) node.getAttribute("SALES_UNIT").getValue();
		if (su == null) {
			delegate.record(node.getKey(), "SALES_UNIT", "No sales unit specified");
		} else {
			if (!salesUnits.keySet().contains(su)) {
				delegate.record(node.getKey(), "SALES_UNIT", "Invalid sales unit '"
					+ su
					+ "' specified, valid units are "
					+ salesUnits.keySet());
			}
		}

		// validate characteristic values

		Map definitions = getDefinitionMap(sku);
		Map options = getConfigurationOptions(node);

		for (Iterator i = definitions.entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Entry) i.next();
			String charName = (String) e.getKey();
			EnumDef charValues = (EnumDef) e.getValue();
			String charValue = (String) options.get(charName);
			if (charValue == null) {
				delegate.record(node.getKey(), "OPTIONS", "No value for characteristic " + charName);
				continue;
			}
			boolean validCharValue = false;
			Set charValueSet = charValues.getValues().keySet();
			for (Iterator j = charValueSet.iterator(); j.hasNext();) {
				String cv = (String) j.next();
				if (cv.equals(charValue)) {
					validCharValue = true;
					break;
				}
			}
			if (!validCharValue) {
				delegate.record(node.getKey(), "OPTIONS", "Invalid characteristic value "
					+ charValue
					+ " for characteristic "
					+ charName
					+ ". Valid options are "
					+ charValueSet);
			}
		}

		Set extraOptions = new HashSet(options.keySet());
		extraOptions.removeAll(definitions.keySet());
		if (!extraOptions.isEmpty()) {
			delegate.record(node.getKey(), "OPTIONS", "Extraneous characteristics " + extraOptions);
		}
		
		// validate parent counts
		Set parentKeys = service.getParentKeys(node.getKey());
		int catCount = 0;
		int confPrdCount = 0;
		int recSecCount = 0;
		int folderCount = 0;
		int starterListCount = 0;
		for (Iterator i = parentKeys.iterator(); i.hasNext(); ) {
			ContentKey parentKey = (ContentKey)i.next();
			ContentType t = parentKey.getType();
			if (FDContentTypes.CONFIGURED_PRODUCT_GROUP.equals(t)){
				confPrdCount++;
			} else if (FDContentTypes.CATEGORY.equals(t)) {
				catCount++;
			} else if (FDContentTypes.RECIPE_SECTION.equals(t)) {
				recSecCount++;
			} else if (FDContentTypes.FDFOLDER.equals(t)) {
				folderCount++;
			} else if (FDContentTypes.STARTER_LIST.equals(t)) {
				starterListCount++;
			} else {
				delegate.record(node.getKey(), "Unexpected parent " + parentKey.getEncoded());
			}
		}
		int sum = (catCount > 0 ? 1 : 0) + confPrdCount + recSecCount + folderCount + starterListCount;
		if (sum > 1) {
			delegate.record(node.getKey(), "Invalid parent counts: " + parentKeys);
		}
	}

	/**
	 * @param configuredProduct {@link ContentNodeI} of type <code>ConfiguredProduct</code>
	 * @return Map of String (characteristic) -> String (char. value)
	 */
	public static Map getConfigurationOptions(ContentNodeI configuredProduct) {
		String optStr = (String) configuredProduct.getAttribute("OPTIONS").getValue();
		return optStr == null ? new HashMap() : ErpOrderLineUtil.convertStringToHashMap(optStr);
	}

	public static void setConfigurationOptions(ContentNodeI configuredProduct, Map options) {
		String optStr = ErpOrderLineUtil.convertHashMapToString(options);
		configuredProduct.getAttribute("OPTIONS").setValue(optStr);
	}

	/**
	 * Determine valid characteristics and char. values for a given SKU.
	 * 
	 * @param sku Sku node
	 * @return Map of String (characteristic name) -> {@link EnumDef} (char. values)
	 */
	public static Map getDefinitionMap(ContentNodeI sku) {
		Map mapDefinition = new HashMap();
		Set charKeys = ContentNodeUtil.collectReachableKeys(sku, FDContentTypes.ERP_CHARACTERISTIC);
		for (Iterator chi = charKeys.iterator(); chi.hasNext();) {
			ContentNodeI charNode = ((ContentKey) chi.next()).getContentNode();
			String charName = (String) charNode.getAttribute("name").getValue();
			Map values = new HashMap();
			Set cvKeys = charNode.getChildKeys();
			for (Iterator cvi = cvKeys.iterator(); cvi.hasNext();) {
				ContentNodeI cvNode = ((ContentKey) cvi.next()).getContentNode();
				String cvName = (String) cvNode.getAttribute("name").getValue();
				String label = cvNode.getLabel();
				values.put(cvName, label);
			}
			EnumDef enumDef = new EnumDef(charName, charName, true, false, false, EnumAttributeType.STRING, values);
			mapDefinition.put(charName, enumDef);
		}
		return mapDefinition;
	}

	/**
	 * 
	 * @param sku
	 * @return Map of String (sales unit) -> {@link ContentNodeI} of type <code>ErpSalesUnit</code>
	 */
	public static Map getSalesUnits(ContentNodeI sku) {
		Set keys = ContentNodeUtil.collectReachableKeys(sku, FDContentTypes.ERP_SALES_UNIT);
		Map m = CmsManager.getInstance().getContentNodes(keys);
		Map su = new HashMap(m.size());
		for (Iterator i = m.entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			ContentKey k = (ContentKey) e.getKey();
			su.put(k.getId(), e.getValue());
		}
		return su;
	}
	
	private static List getMaterials(ContentNodeI sku) {
		AttributeI a = sku.getAttribute("materials");
		return a == null ? Collections.EMPTY_LIST : (List) a.getValue();
	}

}
