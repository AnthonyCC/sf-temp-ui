package com.freshdirect.cms._import.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.log4j.Logger;

import com.freshdirect.cms._import.ImportTool;
import com.freshdirect.cms._import.PlaceholderResolver;

public final class CreateUserTask extends TaskBase {
    public static final Logger LOGGER = Logger.getLogger(CreateUserTask.class);

    public CreateUserTask(String basePath) {
        super(basePath);
    }

    @Override
    public void run() {

        Reader reader = null;
        try {
            InputStream stream = ImportTool.class.getClassLoader().getResourceAsStream("com/freshdirect/cms/_import/scripts/00_NOTE_create_user.sql.input");
            String script = new PlaceholderResolver(new InputStreamReader(stream), toolProps).getString();
            LOGGER.info("Printing user creation script to output...");
            LOGGER.info("Ignoring all parameters except --create_user! Remove --create_user and rerun tool for other options!");
            LOGGER.info("Run the following script with system user:");
            LOGGER.info("---------------SCRIPT BEGIN---------------");
            System.out.println(script);
            LOGGER.info("----------------SCRIPT END----------------");

        } catch (IOException e) {
            LOGGER.error("Error in PropertySubstituterReader", e);
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
