package com.freshdirect.cms.publish.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.publish.flow.PublishFlow;
import com.freshdirect.cms.validation.ConfiguredProductValidator;

/**
 * A very basic ERPS data cache
 *
 * @author segabor
 *
 * @see ConfiguredProductValidator
 * @see PublishFlow
 */
public class ERPSConfigurationDataCache {

    private static final ERPSConfigurationDataCache INSTANCE = new ERPSConfigurationDataCache();

    /**
     * SKUCODE -> MATERIAL association
     */
    private Map<String, String> skuMaterialAssociation = Collections.emptyMap();

    /**
     * MATERIAL Configuration
     */
    private Map<String,Map<String,Set<String>>> materialConfigurations = Collections.emptyMap();

    /**
     * MATERIAL Sales Units
     */
    private Map<String, Set<String>> salesUnits = Collections.emptyMap();

    public static ERPSConfigurationDataCache sharedInstance() {
        return INSTANCE;
    }

    public void updateData(Map<String, String> skuMaterialAssociation, Map<String,Map<String,Set<String>>> materialConfigurations, Map<String, Set<String>> salesUnits) {
        this.skuMaterialAssociation = skuMaterialAssociation;
        this.salesUnits = salesUnits;
        this.materialConfigurations = materialConfigurations;
    }

    public List<String> getMaterials(String skuCode) {
        List<String> materialSet = new ArrayList<String>(1);
        final String materialId = skuMaterialAssociation.get(skuCode);
        if (materialId != null) {
            materialSet.add(materialId);
        }
        return materialSet;
    }

    public Map<String, Set<String>> getConfiguration(String skuCode) {
        Map<String, Set<String>> result = null;

        final String materialId = skuMaterialAssociation.get(skuCode);

        if (materialId != null) {
            result = materialConfigurations.get(materialId);
        }

        if (result == null) {
            result = Collections.emptyMap();
        }

        return result;
    }

    public Set<String> getSalesUnits(String skuCode) {
        Set<String> result = Collections.emptySet();

        String matId = skuMaterialAssociation.get(skuCode);
        if (matId != null) {
            result = Collections.unmodifiableSet(salesUnits.get(matId));
        }

        return result;
    }
}
