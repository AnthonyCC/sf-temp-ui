package com.freshdirect.cms._import.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.freshdirect.cms._import.ImportTool;
import com.freshdirect.cms._import.PlaceholderResolver;

public final class SwitchCMSTask extends TaskBase {
    private static final Logger LOGGER = Logger.getLogger(SwitchCMSTask.class);

    private static final String GRANT_USER_PROPERTY = "grant.user";
    private static final String GRANT_USER_PROPERTY_DELIMITER = ",";

    public SwitchCMSTask(String basePath) {
        super(basePath);
    }

    @Override
    public void run() {
        Reader reader = null;
        try {
            String [] grantUsers = toolProps.getProperty(GRANT_USER_PROPERTY).split(GRANT_USER_PROPERTY_DELIMITER);

            LOGGER.info("Printing linked cms user change and permission grant script to output...");
            LOGGER.info("Run the following script with the system user:");
            LOGGER.info("---------------SCRIPT BEGIN---------------");

            for (String grantUser : grantUsers){
                Properties toolPropForGrantUser = new Properties(toolProps);
                toolPropForGrantUser.setProperty(GRANT_USER_PROPERTY, grantUser);
                InputStream stream = ImportTool.class.getClassLoader().getResourceAsStream("com/freshdirect/cms/_import/scripts/08_NOTE_fduser_synonyms.sql.input");
                String script = new PlaceholderResolver(new InputStreamReader(stream), toolPropForGrantUser).getString();
                System.out.println(script);
            }
            
            InputStream stream = ImportTool.class.getClassLoader().getResourceAsStream("com/freshdirect/cms/_import/scripts/09_NOTE_erps_grants.sql.input");
            String script = new PlaceholderResolver(new InputStreamReader(stream), toolProps).getString();
            System.out.println(script);
            LOGGER.info("----------------SCRIPT END----------------");

        } catch (IOException e) {
            LOGGER.error("Error in PropertySubstituterReader",e);
            return;
        
        } finally {
            try {
                if (reader != null)
                    reader.close();
                reader = null;
            } catch (IOException e) {
            }
        }
    }

}
