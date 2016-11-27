package com.freshdirect.cms._import.tasks;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;

import org.apache.log4j.Logger;

import com.freshdirect.cms._import.dao.CMSInfrastructureDao;
import com.freshdirect.framework.conf.ResourceUtil;
import com.freshdirect.framework.xml.XSLTransformer;

public final class LoadCMSDefinitionTask extends TaskBase {
    private static final Logger LOGGER = Logger.getLogger(LoadCMSDefinitionTask.class);

    public LoadCMSDefinitionTask(String basePath) {
        super(basePath);
    }
    
    @Override
    public void run() {
        
        final File storeDefFile = getFile("CMSStoreDef.xml");
        
        LOGGER.info("Load store definition");

        try {
            String xmlStoreDef = ResourceUtil.readResource(storeDefFile.toURI().toString());
            String dbStoreDef = new XSLTransformer().transform(xmlStoreDef, "com/freshdirect/cms/_import/scripts/XmlToDbDataDef.xsl");
    
            Reader dbStoreDefReader = null;
            try {
                dbStoreDefReader = new StringReader(dbStoreDef);
                if (!new CMSInfrastructureDao(outDataSource).runScript(dbStoreDefReader)) {
                    throw new RuntimeException("Definition Load Failure");
                }
            } finally {
                if (dbStoreDefReader != null) {
                    dbStoreDefReader.close();
                }
            }
        } catch (Exception e){
            LOGGER.error("Load definition failed!");
            throw new RuntimeException(e);
        }
        
        LOGGER.info("Load store definition finished");
    }

}
