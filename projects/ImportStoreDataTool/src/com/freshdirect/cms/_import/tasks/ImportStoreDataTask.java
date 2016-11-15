package com.freshdirect.cms._import.tasks;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;

import com.freshdirect.cms._import.dao.CMSInfrastructureDao;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.fdstore.EnumEStoreId;

public final class ImportStoreDataTask extends CMSTaskBase {
    private static final Logger LOGGER = Logger.getLogger(ImportStoreDataTask.class);

    public ImportStoreDataTask(String basePath) {
        super(basePath);
    }

    @Override
    public void run() {
        try {
            doImportStore();
        } catch (MalformedURLException e) {
            LOGGER.error("Failed to import store", e);
        } catch (IOException e) {
            LOGGER.error("Failed to import store", e);
        }

        doPostOperations();
    }

    protected void doImportStore() throws MalformedURLException, IOException {
        LOGGER.info("Import Store content");
        
        final ContentServiceI tmpStoreManager = createInMemoryService(); 
        
        // -- load phase --

        final File base = getStoreData();
        
        int importedStores = 0;
        for (EnumEStoreId eStore : EnumEStoreId.values()) {
            final File storeContainer = new File(base, eStore.getContentId());

            LOGGER.info("Looking for store file for "+eStore.getContentId()+" ...");
            
            // store container does not exist or not a folder, skip
            if (!storeContainer.isDirectory()) {
                continue;
            }
            
            File storeXMLFile = new File(storeContainer, "Store.xml.gz");
            if (!storeXMLFile.isFile()) {
                continue;
            }
            
            final ContentServiceI inStoreManager = loadContentsFromXML(storeDefFile, storeXMLFile);
            LOGGER.info("Loading nodes from " + storeXMLFile + " ...");
            
            transferContentNodes(inStoreManager, tmpStoreManager, null);
            
            importedStores++;
        }

        LOGGER.info("Loaded " + importedStores + " store contents into memory");


        // -- save phase --
        
        // DROP INDICES
        LOGGER.info("Drop indices");
        infraDao.dropStoreIndices();

        // Flush tables
        {
            LOGGER.info("Cleanup CMS tables");
            long t0 = System.currentTimeMillis();
            infraDao.flushStoreTables();
            long t1 = System.currentTimeMillis();
            LOGGER.debug(" ... it took " + Math.round((t1-t0)/1000) + " secs");
        }


        final ContentServiceI outStoreManager = createDBService(outDataSource);

        // save nodes into DB
        transferContentNodeBatches(tmpStoreManager, outStoreManager, CmsRequest.Source.STORE_IMPORT);
    

        // Create indices #1
        LOGGER.info("Create indices (phase one)");
        infraDao.createStoreIndicesPhaseOne();
        // Create indices #2
        infraDao.createStoreIndicesPhaseTwo();
        infraDao.analyzeStoreTables();

        LOGGER.info("Import Store content finished");
    }



    private void doPostOperations() {
        LOGGER.info("Adjust System Sequence Value");
        final CMSInfrastructureDao outDao = new CMSInfrastructureDao(outDataSource);
        outDao.fixSystemSequenceValue();
    }
}
