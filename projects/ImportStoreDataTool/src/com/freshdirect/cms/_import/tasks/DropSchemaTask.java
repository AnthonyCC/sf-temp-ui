package com.freshdirect.cms._import.tasks;

import org.apache.log4j.Logger;

import com.freshdirect.cms._import.dao.CMSInfrastructureDao;

public final class DropSchemaTask extends TaskBase {
    public static final Logger LOGGER = Logger.getLogger(DropSchemaTask.class);

    public DropSchemaTask(String basePath) {
        super(basePath);
    }


    @Override
    public void run() {
        LOGGER.info("Drop Schema");
        
        final CMSInfrastructureDao dao = new CMSInfrastructureDao(outDataSource);
        
        final String[] scripts = {
            "99_drop_indices.sql",
            "98_drop_publish.sql",
            "97_drop_content.sql",
            "96_drop_typedefs.sql",
            "95_drop_drafts.sql"
        };
        
        for (String script : scripts) {
            if (!runScript(dao, script)) {
                throw new RuntimeException("Schema Drop Failure");
            }
        }

        LOGGER.info("Schema Dropped");
    }

}
