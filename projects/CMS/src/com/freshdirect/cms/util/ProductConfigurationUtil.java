package com.freshdirect.cms.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.meta.EnumDef;
import com.freshdirect.cms.node.ContentNodeUtil;

public class ProductConfigurationUtil {

    private ProductConfigurationUtil() {

    }

    /**
     * Determine valid characteristics and char. values for a given SKU.
     * 
     * @param sku
     *            Sku node
     * @return Map of String (characteristic name) -> {@link EnumDef} (char. values)
     */
    public static Map<String, EnumDef> getDefinitionMap(ContentNodeI sku, ContentServiceI contentService, DraftContext draftContext) {
        Map<String, EnumDef> mapDefinition = new HashMap<String, EnumDef>();
        Set<ContentKey> charKeys = ContentNodeUtil.collectReachableKeys(sku, FDContentTypes.ERP_CHARACTERISTIC, contentService, draftContext);
        for (ContentKey charKey : charKeys) {
            ContentNodeI charNode = contentService.getContentNode(charKey, draftContext);
            String charName = (String) charNode.getAttributeValue("name");
            Map<Object, String> values = new HashMap<Object, String>();
            Set<ContentKey> cvKeys = charNode.getChildKeys();
            for (ContentKey cvKey : cvKeys) {
                ContentNodeI cvNode = contentService.getContentNode(cvKey, draftContext);
                String cvName = (String) cvNode.getAttributeValue("name");
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
    public static Map<String, ContentNodeI> getSalesUnits(ContentNodeI sku, ContentServiceI contentService, DraftContext draftContext) {
        Set<ContentKey> keys = ContentNodeUtil.collectReachableKeys(sku, FDContentTypes.ERP_SALES_UNIT, contentService, draftContext);
        Map<ContentKey, ContentNodeI> m = contentService.getContentNodes(keys, draftContext);
        Map<String, ContentNodeI> su = new HashMap<String, ContentNodeI>(m.size());

        for (Map.Entry<ContentKey, ContentNodeI> e : m.entrySet()) {
            ContentKey k = e.getKey();
            su.put(k.getId(), e.getValue());
        }
        return su;
    }

    /**
     * Returns ERPS materials assigned to the SKU
     * @param sku
     * @return list of content keys
     */
    public static List<ContentKey> getMaterials(ContentNodeI sku) {
        @SuppressWarnings("unchecked")
        List<ContentKey> result = (List<ContentKey>) sku.getAttributeValue("materials");
        return result != null ? result : Collections.<ContentKey> emptyList();
    }

}
