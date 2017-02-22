package com.freshdirect.cms.index.configuration;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.StoreContentSource;
import com.freshdirect.cms.search.SynonymDictionary;
import com.freshdirect.cms.search.configuration.SearchServiceConfiguration;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.ConfigHelper;

public class IndexerConfiguration {

    private static final Logger LOGGER = Logger.getLogger(IndexerConfiguration.class);

    private static final String CMS_INDEX_BATCHSIZE = "cms.index.batchsize";
    private static final String DEFAULT_BATCH_SIZE = "10000";
    private static final String PROPERTY_FILE_NAME = "freshdirect.properties";

    private List<SynonymDictionary> customSynonyms;

    private List<SynonymDictionary> customSpellingSynonyms;

    private boolean synonymsDisabled;

    private boolean keywordsDisabled;

    private String indexDirectoryPath;

    private boolean primaryHomeKeywordsEnabled = FDStoreProperties.isPrimaryHomeKeywordsEnabled();

    private boolean recurseParentAttributesEnabled = FDStoreProperties.isSearchRecurseParentAttributesEnabled();

    private StoreContentSource storeContentSource;

    private int dictionaryWriteoutBatchSize;

    public static IndexerConfiguration getDefaultConfiguration() {
        IndexerConfiguration defaultConfiguration = new IndexerConfiguration();
        defaultConfiguration.setKeywordsDisabled(false);
        defaultConfiguration.setSynonymsDisabled(false);
        defaultConfiguration.setIndexDirectoryPath(SearchServiceConfiguration.getInstance().getCmsIndexLocation());
        defaultConfiguration.setStoreContentSource(CmsManager.getInstance());
        return defaultConfiguration;
    }

    public IndexerConfiguration() {
        Properties freshdirectProperties = null;
        try {
            freshdirectProperties = ConfigHelper.getPropertiesFromClassLoader(PROPERTY_FILE_NAME);
        } catch (IOException e) {
            LOGGER.error("Exception while reading freshdirect.properties", e);
        } finally {
            String batchSize = DEFAULT_BATCH_SIZE;
            if (freshdirectProperties != null) {
                batchSize = freshdirectProperties.getProperty(CMS_INDEX_BATCHSIZE, DEFAULT_BATCH_SIZE);
            }
            dictionaryWriteoutBatchSize = Integer.parseInt(batchSize);
        }
    }

    public List<SynonymDictionary> getCustomSynonyms() {
        return customSynonyms;
    }

    public void setCustomSynonyms(List<SynonymDictionary> customSynonyms) {
        this.customSynonyms = customSynonyms;
    }

    public List<SynonymDictionary> getCustomSpellingSynonyms() {
        return customSpellingSynonyms;
    }

    public void setCustomSpellingSynonyms(List<SynonymDictionary> customSpellingSynonyms) {
        this.customSpellingSynonyms = customSpellingSynonyms;
    }

    public boolean isSynonymsDisabled() {
        return synonymsDisabled;
    }

    public void setSynonymsDisabled(boolean synonymsDisabled) {
        this.synonymsDisabled = synonymsDisabled;
    }

    public boolean isKeywordsDisabled() {
        return keywordsDisabled;
    }

    public void setKeywordsDisabled(boolean keywordsDisabled) {
        this.keywordsDisabled = keywordsDisabled;
    }

    public String getIndexDirectoryPath() {
        return indexDirectoryPath;
    }

    public void setIndexDirectoryPath(String indexDirectoryPath) {
        this.indexDirectoryPath = indexDirectoryPath;
    }

    public boolean isPrimaryHomeKeywordsEnabled() {
        return primaryHomeKeywordsEnabled;
    }

    public boolean isRecurseParentAttributesEnabled() {
        return recurseParentAttributesEnabled;
    }

    public StoreContentSource getStoreContentSource() {
        return storeContentSource;
    }

    public void setStoreContentSource(StoreContentSource storeContentSource) {
        this.storeContentSource = storeContentSource;
    }

    public int getDictionaryWriteoutBatchSize() {
        return dictionaryWriteoutBatchSize;
    }

}
