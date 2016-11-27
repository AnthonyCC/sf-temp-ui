package com.freshdirect.cms._import.tasks;

import org.apache.log4j.Logger;

import com.freshdirect.cms._import.dao.CMSInfrastructureDao;

public final class CreateSchemaTask extends TaskBase {
    private static final Logger LOGGER = Logger.getLogger(CreateSchemaTask.class);

    public CreateSchemaTask(String basePath) {
        super(basePath);
    }

    @Override
    public void run() {
        LOGGER.info("Create Schema");
        
        final CMSInfrastructureDao dao = new CMSInfrastructureDao(outDataSource);
        
        final String[] scripts = {
            "01_create_typedefs.sql",
            "02_create_content.sql",
            "03_create_publish.sql",
            "031_create_drafts.sql",
            "04_create_indices.sql",
            "031_grant_drafts.sql.input",
            "05_grant_typedefs.sql.input",
            "06_grant_content.sql.input",
            "07_grant_publish.sql.input"
        };
        
        for (String script : scripts) {
            if (!runScript(dao, script)) {
                throw new RuntimeException("Schema Create Failure");
            }
        }

        LOGGER.info("Schema Created");
    }

}
