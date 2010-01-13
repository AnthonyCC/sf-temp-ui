/*
 * Created on Aug 10, 2005
 */
package com.freshdirect.cms.fdstore;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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

		Map<String,ContentNodeI> salesUnits = getSalesUnits(sku);
		String su = (String) node.getAttribute("SALES_UNIT").getValue();
		if (su == null) {
			delegate.record(node.getKey(), "SALES_UNIT", "No sales unit specified");
		} else {
			if (!salesUnits.keySet().contains(su)) {
				SortedSet<String> suKeysSorted = new TreeSet<String>( salesUnits.keySet() );
				
				delegate.record(node.getKey(), "SALES_UNIT", "Invalid sales unit '"
					+ su
					+ "' specified, valid units are "
					+ suKeysSorted);
			}
		}

		// validate characteristic values

		Map<String,EnumDef> definitions = getDefinitionMap(sku);
		Map<String,String> options = getConfigurationOptions(node);

		for ( Map.Entry<String,EnumDef> e : definitions.entrySet() ) {
			String charName = e.getKey();
			EnumDef charValues = e.getValue();
			String charValue = options.get(charName);
			
			if (charValue == null) {
				delegate.record(node.getKey(), "OPTIONS", "No value for characteristic " + charName);
				continue;
			}
			
			boolean validCharValue = false;
			Set<Object> charValueSet = charValues.getValues().keySet();			
			for ( Object cv : charValueSet ) {
				if (cv.equals(charValue)) {
					validCharValue = true;
					break;
				}
			}
			if (!validCharValue) {
				SortedSet<Object> charValueSortedSet = new TreeSet<Object>(charValueSet);
				
				delegate.record(node.getKey(), "OPTIONS", "Invalid characteristic value "
					+ charValue
					+ " for characteristic "
					+ charName
					+ ". Valid options are "
					+ charValueSortedSet);
			}
		}

		// extraneous characteristics message - disabled this warning by request  	 
		// [APPREQ-680] => cms validation: extraneous characteristics should not be warning
		//
		//		Set extraOptions = new HashSet(options.keySet());
		//		extraOptions.removeAll(definitions.keySet());
		//		if (!extraOptions.isEmpty()) {
		//			delegate.record(node.getKey(), "OPTIONS", "Extraneous characteristics " + extraOptions);
		//		}
		
		// validate parent counts
		Set<ContentKey> parentKeys = service.getParentKeys(node.getKey());
		int catCount = 0;
		int confPrdCount = 0;
		int recSecCount = 0;
		int folderCount = 0;
		int starterListCount = 0;
		
		for ( ContentKey parentKey : parentKeys ) {
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
	public static Map<String,String> getConfigurationOptions(ContentNodeI configuredProduct) {
		String optStr = (String) configuredProduct.getAttribute("OPTIONS").getValue();
		return optStr == null ? new HashMap<String,String>() : ErpOrderLineUtil.convertStringToHashMap(optStr);
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
	public static Map<String,EnumDef> getDefinitionMap(ContentNodeI sku) {
		Map<String,EnumDef> mapDefinition = new HashMap<String,EnumDef>();
		Set<ContentKey> charKeys = ContentNodeUtil.collectReachableKeys(sku, FDContentTypes.ERP_CHARACTERISTIC);
		for ( ContentKey charKey : charKeys ) {
			ContentNodeI charNode = charKey.getContentNode();
			String charName = (String) charNode.getAttribute("name").getValue();
			Map<Object,String> values = new HashMap<Object,String>();
			Set<ContentKey> cvKeys = charNode.getChildKeys();
			for ( ContentKey cvKey : cvKeys ) {
				ContentNodeI cvNode = cvKey.getContentNode();
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
	public static Map<String,ContentNodeI> getSalesUnits(ContentNodeI sku) {
		
		Set<ContentKey> keys = ContentNodeUtil.collectReachableKeys(sku, FDContentTypes.ERP_SALES_UNIT);
		Map<ContentKey, ContentNodeI> m = CmsManager.getInstance().getContentNodes(keys);
		Map<String,ContentNodeI> su = new HashMap<String,ContentNodeI>(m.size());
		
		for ( Map.Entry<ContentKey, ContentNodeI> e : m.entrySet() ) {
			ContentKey k = e.getKey();
			su.put(k.getId(), e.getValue());
		}
		return su;
	}
	
	private static List getMaterials(ContentNodeI sku) {
		AttributeI a = sku.getAttribute("materials");
		return a == null ? Collections.EMPTY_LIST : (List) a.getValue();
	}

}
