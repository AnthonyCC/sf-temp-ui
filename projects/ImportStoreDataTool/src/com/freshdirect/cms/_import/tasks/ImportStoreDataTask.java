package com.freshdirect.cms._import.tasks;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms._import.dao.CMSInfrastructureDao;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.CmsRequestI.Source;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.UserI;
import com.freshdirect.cms.application.service.SimpleContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.fdstore.EnumEStoreId;

public final class ImportStoreDataTask extends TaskBase {
    private static final Logger LOGGER = Logger.getLogger(ImportStoreDataTask.class);

    private static final UserI MASTER = new UserI() {
        @Override
        public String getName() {
            return "CMS Import";
        }

        @Override
        public boolean isAllowedToWrite() {
            return true;
        }
    };

    private static final int BATCH_SIZE = 20000;

    private File storeDefFile;
    
    public ImportStoreDataTask(String basePath) {
        super(basePath);
    }

    @Override
    public void run() {
        storeDefFile = getFile("CMSStoreDef.xml");

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
        
        // final String xmlStoreDef = ResourceUtil.readResource(storeDefFile.toURI().toURL().toString());
        final ContentTypeServiceI typeService = new XmlTypeService( storeDefFile.toURI().toURL().toString() );
        final ContentServiceI tmpStoreManager = new SimpleContentService( typeService ); 
        
        // -- load phase --
        
        File base = getBasePath();
        File storedata = new File(base, "storedata");
        if (storedata.isDirectory()) {
            base = storedata;
        }


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
            
            final ContentServiceI inStoreManager = getStoreManager(storeDefFile, storeXMLFile);
            LOGGER.info("Loading nodes from " + storeXMLFile + " ...");
            
            final Set<ContentType> types = inStoreManager.getTypeService().getContentTypes();
            for (final ContentType t : types) {
                final Set<ContentKey> keysForType = inStoreManager.getContentKeysByType(t, DraftContext.MAIN);
                CmsRequest req = new CmsRequest(MASTER);

                // load nodes
                for (final ContentKey k : keysForType) {
                    final ContentNodeI n = inStoreManager.getContentNode(k, DraftContext.MAIN);
                    req.addNode(n);
                }
                
                // store loaded nodes
                tmpStoreManager.handle(req);

                LOGGER.debug(".. loaded " + req.getNodes().size() + " nodes with type:"+t.toString()+" ...");
            }
            
            importedStores++;
        }

        LOGGER.info("Loaded " + importedStores + " store contents into memory");


        // -- save phase --
        
        final CMSInfrastructureDao outDao = new CMSInfrastructureDao(outDataSource);

        // DROP INDICES
        LOGGER.info("Drop indices");
        outDao.dropStoreIndices();

        // Flush tables
        {
            LOGGER.info("Cleanup CMS tables");
            long t0 = System.currentTimeMillis();
            outDao.flushStoreTables();
            long t1 = System.currentTimeMillis();
            LOGGER.debug(" ... it took " + Math.round((t1-t0)/1000) + " secs");
        }
        
        final ContentServiceI outStoreManager = getStoreExportManager(outDataSource);
        final Set<ContentKey> allKeys = tmpStoreManager.getContentKeys(DraftContext.MAIN);
        
        final int n_batches = (int) Math.ceil( (((double)allKeys.size()) / BATCH_SIZE ));
        int k=0;
        int b=1;
        
        Iterator<ContentKey> cit = allKeys.iterator();
        CmsRequest req = new CmsRequest(MASTER, Source.STORE_IMPORT);

        while (cit.hasNext()) {
            req.addNode(tmpStoreManager.getContentNode(cit.next(), DraftContext.MAIN));
            k++;
            
            if (k>0 && k%BATCH_SIZE == 0) {
                LOGGER.info("Write out batch "+b+"/"+n_batches+" ("+req.getNodes().size()+" nodes)");
                
                long t0 = System.currentTimeMillis();
                outStoreManager.handle(req);
                long t1 = System.currentTimeMillis();
                LOGGER.debug(" ... it took " + Math.round((t1-t0)/1000) + " secs");
                
                req = new CmsRequest(MASTER, Source.STORE_IMPORT);
                
                b++;
            }
        }

        if (req.getNodes().size() > 0) {
            LOGGER.info("Write out last batch of nodes ("+req.getNodes().size()+")");

            long t0 = System.currentTimeMillis();
            outStoreManager.handle(req);
            long t1 = System.currentTimeMillis();
            LOGGER.debug(" ... it took " + Math.round((t1-t0)/1000) + " secs");
        }
    
        // Create indices #1
        LOGGER.info("Create indices (phase one)");
        outDao.createStoreIndicesPhaseOne();
        // Create indices #2
        outDao.createStoreIndicesPhaseTwo();
        outDao.analyzeStoreTables();

        LOGGER.info("Import Store content finished");
    }



    private void doPostOperations() {
        LOGGER.info("Adjust System Sequence Value");
        final CMSInfrastructureDao outDao = new CMSInfrastructureDao(outDataSource);
        outDao.fixSystemSequenceValue();
    }
}
