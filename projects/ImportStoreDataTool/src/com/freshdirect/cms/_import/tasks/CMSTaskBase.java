package com.freshdirect.cms._import.tasks;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms._import.dao.CMSInfrastructureDao;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.UserI;
import com.freshdirect.cms.application.service.SimpleContentService;
import com.freshdirect.cms.application.service.db.DbContentService;
import com.freshdirect.cms.application.service.db.DbTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;

public abstract class CMSTaskBase extends TaskBase {
    private static final Logger LOGGER = Logger.getLogger(CMSTaskBase.class);

    protected static final int BATCH_SIZE = 20000;

    protected static final UserI MASTER = new UserI() {
        @Override
        public String getName() {
            return "CMS Import";
        }

        @Override
        public boolean isAllowedToWrite() {
            return true;
        }
    };


    /**
     * File reference to Store Def file
     * 
     * @see CMSStoreDef.xml in CMS project
     */
    protected final File storeDefFile;

    protected CMSInfrastructureDao infraDao;
    
    protected CMSTaskBase(String basePath) {
        super(basePath);

        this.storeDefFile = getFile("CMSStoreDef.xml");
        
        if (storeDefFile == null || !storeDefFile.exists()) {
            throw new RuntimeException("Missing CMSStoreDef.xml !!");
        }
        
        // create infrastructure DAO
        infraDao = new CMSInfrastructureDao(outDataSource);
    }

    /**
     * Get the folder where storedata files are stored
     * 
     * @return
     */
    protected File getStoreData() {
        File base = getBasePath();

        File storedata = new File(base, "storedata");
        if (storedata.isDirectory()) {
            base = storedata;
        }
        
        return base;
    }


    protected ContentServiceI createInMemoryService() throws MalformedURLException {
        final ContentTypeServiceI typeService = new XmlTypeService( storeDefFile.toURI().toURL().toString() );
        final ContentServiceI tmpStoreManager = new SimpleContentService( typeService ); 
        return tmpStoreManager;
    }


    
    /**
     * Returns CMS manager having Store.xml imported
     * @throws MalformedURLException 
     */
    protected ContentServiceI loadContentsFromXML(File storeDefFile, File xmlFile ) {
        try {
            // Path to CMS definition file (either StoreDef.xml or MediaDef.xml)
            final String defPath = storeDefFile.toURI().toURL().toString();

            final ContentTypeServiceI typeService = new XmlTypeService(defPath);
            final XmlContentService service = new XmlContentService(typeService,
                    new FlexContentHandler(),
                    xmlFile.toURI().toURL().toString());

            return service;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to initialize store service!", e);
        }
    }

    
    protected ContentServiceI loadContentsFromXML(String defPath, File xmlFile ) {
        try {
            final ContentTypeServiceI typeService = new XmlTypeService(defPath);
            final XmlContentService service = new XmlContentService(typeService,
                    new FlexContentHandler(),
                    xmlFile.toURI().toURL().toString());

            return service;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to initialize store service!", e);
        }
    }

    

    protected ContentServiceI createDBService(DataSource ds) {
        if (ds == null)
            return null;
        
        DbTypeService dbService = new DbTypeService();
        dbService.setDataSource(ds);
        dbService.initialize();
        
        DbContentService dbContentService = new DbContentService();
        dbContentService.setContentTypeService(dbService);

        dbContentService.setDataSource(ds);
        
        return dbContentService;
    }


    
    /**
     * Transfer content node groups by types
     * 
     * @param inService
     * @param outService
     * @param source
     */
    protected void transferContentNodes(ContentServiceI inService, ContentServiceI outService, CmsRequest.Source source) {
        final Set<ContentType> types = inService.getTypeService().getContentTypes();
        for (final ContentType t : types) {
            final Set<ContentKey> keysForType = inService.getContentKeysByType(t, DraftContext.MAIN);
            final CmsRequest req = source == null
                    ? new CmsRequest(MASTER)
                    : new CmsRequest(MASTER, source);

            // load nodes
            for (final ContentKey k : keysForType) {
                final ContentNodeI n = inService.getContentNode(k, DraftContext.MAIN);
                req.addNode(n);
            }
            
            // store loaded nodes
            outService.handle(req);
        }
    }

    
    /**
     * Transfer content nodes from inService to outService by batches
     * 
     * @param inService
     * @param outService
     * @param source
     */
    protected void transferContentNodeBatches(ContentServiceI inService, ContentServiceI outService, CmsRequest.Source source) {
        final Set<ContentKey> allKeys = inService.getContentKeys(DraftContext.MAIN);
    
        final int n_batches = (int) Math.ceil((((double) allKeys.size()) / BATCH_SIZE));
        int k = 0;
        int b = 1;

        Iterator<ContentKey> cit = allKeys.iterator();
        CmsRequest req = source == null
                ? new CmsRequest(MASTER)
                : new CmsRequest(MASTER, source);

        while (cit.hasNext()) {
            req.addNode(inService.getContentNode(cit.next(), DraftContext.MAIN));
            k++;

            if (k > 0 && k % BATCH_SIZE == 0) {
                LOGGER.info("Write out batch " + b + "/" + n_batches + " (" + req.getNodes().size() + " nodes)");

                long t0 = System.currentTimeMillis();
                outService.handle(req);
                long t1 = System.currentTimeMillis();
                LOGGER.debug(" ... it took " + Math.round((t1 - t0) / 1000) + " secs");

                req = source == null
                        ? new CmsRequest(MASTER)
                        : new CmsRequest(MASTER, source);

                b++;
            }
        }

        // transfer the remainder
        if (req.getNodes().size() > 0) {
            LOGGER.info("Write out last batch of nodes ("+req.getNodes().size()+")");

            long t0 = System.currentTimeMillis();
            outService.handle(req);
            long t1 = System.currentTimeMillis();
            LOGGER.debug(" ... it took " + Math.round((t1-t0)/1000) + " secs");
        }

    }
}
