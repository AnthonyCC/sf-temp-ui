package com.freshdirect.cms.persistence.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.persistence.erps.data.ErpCharacteristicKey;
import com.freshdirect.cms.persistence.erps.data.MaterialConfig;
import com.freshdirect.cms.persistence.erps.data.MaterialData;
import com.freshdirect.cms.persistence.erps.data.MaterialSalesUnit;
import com.freshdirect.cms.persistence.erps.data.SkuMaterial;
import com.freshdirect.cms.persistence.repository.ERPSDataRepository;

@Profile("database")
@Service
public class ERPSDataService {

    private static final int TIMEOUT_MS = 10;

    private static final Logger LOGGER = LoggerFactory.getLogger(ERPSDataService.class);

    @Autowired
    private ERPSDataRepository repository;

    @Autowired
    private CacheManager cacheManager;

    private Map<String, String> salesUnitLabels = Collections.emptyMap();

    @Cacheable(value = "materialCharacteristicsCache")
    public Map<String, Map<String, Map<String, String>>> fetchMaterialCharacteristics() {
        List<MaterialConfig> materialConfigurations = repository.findAllMaterialConfigurations();
        Map<String, Map<String, Map<String, String>>> result = new HashMap<String, Map<String, Map<String, String>>>();
        String currentMaterial = null;
        Map<String, Map<String, String>> currentCharacteristicValues = null;
        for (MaterialConfig materialConfig : materialConfigurations) {
            if (currentMaterial == null || !currentMaterial.equals(materialConfig.getMaterialId())) {
                if (currentMaterial != null) {
                    result.put(currentMaterial, currentCharacteristicValues);
                }

                // set the new material to work on
                currentMaterial = materialConfig.getMaterialId();
                currentCharacteristicValues = new HashMap<String, Map<String, String>>();
            }

            Map<String, String> characteristicValues = currentCharacteristicValues.get(materialConfig.getCharacteristicName());
            if (characteristicValues == null) {
                characteristicValues = new HashMap<String, String>();
                currentCharacteristicValues.put(materialConfig.getCharacteristicName(), characteristicValues);
            }
            characteristicValues.put(materialConfig.getCharacteristicValue(), materialConfig.getCharacteristicValueDescription());
        }
        if (currentMaterial != null) {
            result.put(currentMaterial, currentCharacteristicValues);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Cacheable(value = "materialCharacteristicsCache")
    public Map<String, Map<String, String>> fetchMaterialCharacteristicsBySku(ContentKey sku) {
        String materialId = checkIfInCacheAndGetMaterialBySku(sku);

        Map<String, Map<String, String>> result = Collections.<String, Map<String, String>>emptyMap();
        if (materialId != null) {
            Cache cache = cacheManager.getCache("materialCharacteristicsCache");
            ValueWrapper valueWrapper = cache.get(materialId);
            if (valueWrapper != null && valueWrapper.get() != null) {
                result = (Map<String, Map<String, String>>) valueWrapper.get();
            }
        }
        return result;
    }

    @Cacheable(value = "materialDataCache")
    public Map<String, MaterialData> fetchMaterialData() {
        List<MaterialData> materialData = repository.findAllMaterialData();
        Map<String, MaterialData> result = new HashMap<String, MaterialData>();
        for (MaterialData materialInfo : materialData) {
            result.put(materialInfo.getSapId(), materialInfo);
        }
        return result;
    }

    @Cacheable(value = "skuMaterialsCache", key = "#sku.id")
    public String fetchMaterialsBySku(ContentKey sku) {
        List<SkuMaterial> associationList = repository.findSkuMaterialAssociation(sku.id);
        for (SkuMaterial entry : associationList) {
            if (sku.id.equals(entry.getSku())) {
                return entry.getMaterialId();
            }
        }
        return null;
    }

    @Cacheable(value = "materialSalesUnitsCache")
    public Map<String, Map<String, String>> fetchSalesUnits() {
        List<MaterialSalesUnit> materialSalesUnits = repository.findAllSalesUnits();
        Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
        for (MaterialSalesUnit materialSalesUnit : materialSalesUnits) {
            Map<String, String> salesUnits = result.get(materialSalesUnit.getMaterialId());
            if (salesUnits == null) {
                salesUnits = new HashMap<String, String>();
                result.put(materialSalesUnit.getMaterialId(), salesUnits);
            }
            salesUnits.put(materialSalesUnit.getSalesUnit(), materialSalesUnit.getSalesUnitDescription());
        }
        return result;
    }

    public Map<String, String> fetchSalesUnitsBySku(ContentKey sku) {
        String materialId = checkIfInCacheAndGetMaterialBySku(sku);
        return materialId != null ? checkIfInCacheAndGetSalesUnitByMaterial(materialId) : Collections.<String, String>emptyMap();
    }

    @Cacheable(value = "skuMaterialsCache")
    public Map<String, String> fetchSkuMaterialAssociations() {
        List<SkuMaterial> skuMaterialAssociations = repository.findAllSkuMaterialAssociations();
        Map<String, String> result = new HashMap<String, String>();
        for (SkuMaterial skuMaterial : skuMaterialAssociations) {
            result.put(skuMaterial.getSku(), skuMaterial.getMaterialId());
        }
        return result;
    }

    public String getSalesUnitLabel(String salesUnitId) {
        return salesUnitLabels.get(salesUnitId);
    }

    public Map<String, String> fetchCharacteristicValues(ErpCharacteristicKey erpKey) {
        Map<String, String> result = repository.findCharacteristicValues(erpKey.getSapId(), erpKey.getCharacteristicName());

        return result;
    }

    public List<String> fetchClasses(String materialId) {
        return repository.findClassesForMaterial(materialId);
    }

    public List<String> fetchCharacteristics(String classId) {
        return repository.findCharacteristicsForClass(classId);
    }

    private void populateMaterialCharacteristicsCache() {
        Map<String, Map<String, Map<String, String>>> materialCharacteristics = fetchMaterialCharacteristics();
        LOGGER.debug("populateMaterialCharacteristicsCache: loaded " + materialCharacteristics.size() + " entries");
        Cache cache = cacheManager.getCache("materialCharacteristicsCache");
        for (Map.Entry<String, Map<String, Map<String, String>>> entry : materialCharacteristics.entrySet()) {
            cache.put(entry.getKey(), entry.getValue());
        }
        cache.put(SimpleKey.EMPTY, materialCharacteristics);
    }

    private void populateMaterialDataCache() {
        Map<String, MaterialData> allMaterialData = fetchMaterialData();
        LOGGER.debug("populateMaterialDataCache: loaded " + allMaterialData.size() + " entries");
        Cache cache = cacheManager.getCache("materialDataCache");
        for (Map.Entry<String, MaterialData> entry : allMaterialData.entrySet()) {
            cache.put(entry.getKey(), entry.getValue());
        }
        cache.put(SimpleKey.EMPTY, allMaterialData);
    }

    private void populateSkuMaterialsCache() {
        Map<String, String> skuMaterialAssociations = fetchSkuMaterialAssociations();
        LOGGER.debug("populateSkuMaterialsCache: loaded " + skuMaterialAssociations.size() + " entries");
        Cache cache = cacheManager.getCache("skuMaterialsCache");
        for (Map.Entry<String, String> entry : skuMaterialAssociations.entrySet()) {
            cache.put(entry.getKey(), entry.getValue());
        }
        cache.put(SimpleKey.EMPTY, skuMaterialAssociations);
    }

    private void populateMaterialSalesUnitsCache() {
        Map<String, Map<String, String>> materialSalesUnits = fetchSalesUnits();
        LOGGER.debug("populateMaterialSalesUnitsCache: loaded " + materialSalesUnits.size() + " entries");
        Map<String, String> salesUnitLabels = new HashMap<String, String>();
        Cache cache = cacheManager.getCache("materialSalesUnitsCache");
        for (Map.Entry<String, Map<String, String>> entry : materialSalesUnits.entrySet()) {
            cache.put(entry.getKey(), entry.getValue());
            salesUnitLabels.putAll(entry.getValue());
        }
        cache.put(SimpleKey.EMPTY, materialSalesUnits);
        this.salesUnitLabels = Collections.unmodifiableMap(salesUnitLabels);
    }

    @PostConstruct
    void populateCaches(){
        LOGGER.info("** Populating ERPS related caches **");

        final ExecutorService pool = Executors.newFixedThreadPool(4);

        pool.submit(new SafeTaskRunner("Material Data") {
            @Override
            void load() {
                populateMaterialDataCache();
            }
        });

        pool.submit(new SafeTaskRunner("Material Sales Units") {
            @Override
            void load() {
                populateMaterialSalesUnitsCache();
            }
        });

        pool.submit(new SafeTaskRunner("Sku-Materials associations") {
            @Override
            void load() {
                populateSkuMaterialsCache();
            }
        });

        pool.submit(new SafeTaskRunner("Material Options") {
            @Override
            void load() {
                populateMaterialCharacteristicsCache();
            }
        });

        pool.shutdown();
        try {
            final long t0 = System.currentTimeMillis();
            while (!pool.awaitTermination(TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                // just sit and wait
            }
            final long t1 = System.currentTimeMillis();
            LOGGER.info("** Populating ERPS related caches completed in " + Long.toString(t1 - t0) + " ms **");
        } catch (RuntimeException e) {
            LOGGER.error("ERPS Cache load execution failed due to error", e);
        } catch (InterruptedException e) {
            LOGGER.error("ERPS Cache load execution aborted due to error", e);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> checkIfInCacheAndGetSalesUnitByMaterial(String materialId) {
        Cache materialSalesUnitsCache = cacheManager.getCache("materialSalesUnitsCache");
        Map<String, String> salesUnits = null;
        ValueWrapper cachedSalesUnits = materialSalesUnitsCache.get(materialId);
        if (cachedSalesUnits != null) {
            salesUnits = (Map<String, String>) cachedSalesUnits.get();
            LOGGER.debug("sales unit for material : " + materialId + " was found in cache");
        } else {
            LOGGER.debug("sales unit for material : " + materialId + " was NOT found in cache");
        }
        return salesUnits;
    }

    private String checkIfInCacheAndGetMaterialBySku(ContentKey sku) {
        Cache skuMaterialCache = cacheManager.getCache("skuMaterialsCache");
        ValueWrapper cachedMaterialId = skuMaterialCache.get(sku.id);
        String materialId = null;
        if (cachedMaterialId != null) {
            LOGGER.debug("material for sku: " + sku + " was found in cache");
            materialId = (String) cachedMaterialId.get();
        } else {
            LOGGER.debug("material for sku: " + sku + " was NOT found in cache");
        }
        return materialId;
    }
}
