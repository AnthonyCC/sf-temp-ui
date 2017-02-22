/*
 * Created on Aug 10, 2005
 */
package com.freshdirect.cms.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.meta.EnumDef;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.cms.publish.service.ERPSConfigurationDataCache;
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

    interface ConfigurationDataAdapter {

        /**
         * Return member material IDs
         * 
         * @param sku CMS Node
         * 
         * @return list of Material IDs
         */
        List<ContentKey> getMaterials(ContentNodeI sku);
        
        /**
         * Determine valid characteristics and char. values for a given SKU.
         * 
         * @param sku
         *            Sku node
         * @return Map of String (characteristic name) -> {@link EnumDef} (char. values)
         */
        Map<String, Set<String>> getDefinitionMap(ContentNodeI sku, ContentServiceI contentService, DraftContext draftContext);

        /**
         * 
         * @param sku
         * @return Map of String (sales unit) -> {@link ContentNodeI} of type <code>ErpSalesUnit</code>
         */
        Set<String> getSalesUnits(ContentNodeI sku, ContentServiceI contentService, DraftContext draftContext);
    }

    /**
     * CMS Content Service based data provider
     * 
     * @see ContentServiceI
     */
    private final static ConfigurationDataAdapter CONTENT_SERVICE_ADAPTER = new ConfigurationDataAdapter() {

        @Override
        public List<ContentKey> getMaterials(ContentNodeI sku) {
            @SuppressWarnings("unchecked")
            List<ContentKey> materialKeys = (List<ContentKey>) sku.getAttributeValue("materials");
            return materialKeys;
        }

        @Override
        public Map<String, Set<String>> getDefinitionMap(ContentNodeI sku, ContentServiceI contentService, DraftContext draftContext) {
            Map<String, Set<String>> mapDefinition = new HashMap<String, Set<String>>();
            Set<ContentKey> charKeys = ContentNodeUtil.collectReachableKeys(sku, FDContentTypes.ERP_CHARACTERISTIC, contentService, draftContext);
            for (ContentKey charKey : charKeys) {
                ContentNodeI charNode = contentService.getContentNode(charKey, draftContext);
                String charName = (String) charNode.getAttributeValue("name");

                Set<String> values = new HashSet<String>();
                Set<ContentKey> cvKeys = charNode.getChildKeys();
                for (ContentKey cvKey : cvKeys) {
                    ContentNodeI cvNode = contentService.getContentNode(cvKey, draftContext);
                    String cvName = (String) cvNode.getAttributeValue("name");

                    values.add(cvName);
                }

                mapDefinition.put(charName, values);
            }
            return mapDefinition;
        }

        /**
         * Returns sales units for the given SKU
         * 
         * @param sku
         *            SKU CMS object
         */
        @Override
        public Set<String> getSalesUnits(ContentNodeI sku, ContentServiceI contentService, DraftContext draftContext) {
            Set<ContentKey> keys = ContentNodeUtil.collectReachableKeys(sku, FDContentTypes.ERP_SALES_UNIT, contentService, draftContext);
            Map<ContentKey, ContentNodeI> m = contentService.getContentNodes(keys, draftContext);
            Set<String> su = new HashSet<String>();
            for (Map.Entry<ContentKey, ContentNodeI> e : m.entrySet()) {
                su.add(e.getKey().getId());
            }
            return su;
        }

    };

    /**
     * ERPS Config Data provider for the new publish
     */
    private final static ConfigurationDataAdapter PUBLISH_ADAPTER = new ConfigurationDataAdapter() {

        private final ERPSConfigurationDataCache cache = ERPSConfigurationDataCache.sharedInstance();

        @Override
        public List<ContentKey> getMaterials(ContentNodeI sku) {
            List<ContentKey> result = new ArrayList<ContentKey>();
            
            final List<String> materialSet = cache.getMaterials(sku.getKey().getId());
            if (materialSet != null) {
                for (String materialId : materialSet) {
                    try {
                        result.add( ContentKey.create(FDContentTypes.ERP_MATERIAL, materialId) );
                    } catch (InvalidContentKeyException e) {
                    }
                }
            }
            
            return result;
        }
        
        @Override
        public Map<String, Set<String>> getDefinitionMap(ContentNodeI sku, ContentServiceI contentService, DraftContext draftContext) {
            return cache.getConfiguration(sku.getKey().getId());
        }

        @Override
        public Set<String> getSalesUnits(ContentNodeI sku, ContentServiceI contentService, DraftContext draftContext) {
            return cache.getSalesUnits(sku.getKey().getId());
        }

    };

    @Override
    public void validate(ContentValidationDelegate delegate, ContentServiceI service, DraftContext draftContext, ContentNodeI node, CmsRequestI request, ContentNodeI oldNode) {
        if (!FDContentTypes.CONFIGURED_PRODUCT.equals(node.getKey().getType())) {
            return;
        }

        final ConfigurationDataAdapter dataProvider = request == null ? PUBLISH_ADAPTER : CONTENT_SERVICE_ADAPTER;

        ContentKey skuKey = (ContentKey) node.getAttributeValue("SKU");
        if (skuKey == null) {
            delegate.record(node.getKey(), "SKU", "No SKU specified");
            return;
        }
        ContentNodeI sku = service.getContentNode(skuKey, draftContext);
        if (sku == null) {
            delegate.record(node.getKey(), "SKU", skuKey.getEncoded() + " not found");
            return;
        }

        // check that the material exists
        List<ContentKey> materialKeys = dataProvider.getMaterials(sku);
        if (materialKeys == null || materialKeys.isEmpty()) {
            delegate.record(node.getKey(), "SKU", "No material found for this SKU");
            return;
        }

        // validate sales unit

        Set<String> salesUnits = dataProvider.getSalesUnits(sku, service, draftContext);
        final String su = (String) node.getAttributeValue("SALES_UNIT");
        if (su == null) {
            delegate.record(node.getKey(), "SALES_UNIT", "No sales unit specified");
        } else {
            if (!salesUnits.contains(su)) {
                SortedSet<String> suKeysSorted = new TreeSet<String>(salesUnits);

                delegate.record(node.getKey(), "SALES_UNIT", "Invalid sales unit '" + su + "' specified, valid units are " + suKeysSorted);
            }
        }

        // validate characteristic values

        Map<String, Set<String>> definitions = dataProvider.getDefinitionMap(sku, service, draftContext);
        Map<String, String> options = getConfigurationOptions(node);

        for (Map.Entry<String, Set<String>> e : definitions.entrySet()) {
            String charName = e.getKey();
            // EnumDef charValues = e.getValue();
            Set<String> charValueSet = e.getValue();
            String charValue = options.get(charName);

            if (charValue == null) {
                delegate.record(node.getKey(), "OPTIONS", "No value for characteristic " + charName);
                continue;
            }

            boolean validCharValue = false;
            for (Object cv : charValueSet) {
                if (cv.equals(charValue)) {
                    validCharValue = true;
                    break;
                }
            }
            if (!validCharValue) {
                SortedSet<Object> charValueSortedSet = new TreeSet<Object>(charValueSet);

                delegate.record(node.getKey(), "OPTIONS",
                        "Invalid characteristic value " + charValue + " for characteristic " + charName + ". Valid options are " + charValueSortedSet);
            }
        }

        // validate parent counts
        Set<ContentKey> parentKeys = service.getParentKeys(node.getKey(), draftContext);
        int catCount = 0;
        int confPrdCount = 0;
        int recSecCount = 0;
        int folderCount = 0;
        int starterListCount = 0;

        for (ContentKey parentKey : parentKeys) {
            ContentType t = parentKey.getType();
            if (FDContentTypes.CONFIGURED_PRODUCT_GROUP.equals(t)) {
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
     * @param configuredProduct
     *            {@link ContentNodeI} of type <code>ConfiguredProduct</code>
     * @return Map of String (characteristic) -> String (char. value)
     */
    public static Map<String, String> getConfigurationOptions(ContentNodeI configuredProduct) {
        String optStr = (String) configuredProduct.getAttributeValue("OPTIONS");
        return optStr == null ? Collections.<String, String> emptyMap() : ErpOrderLineUtil.convertStringToHashMap(optStr);
    }
}
