package com.freshdirect.cms._import.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.freshdirect.cms._import.ImportTool;
import com.freshdirect.cms._import.PlaceholderResolver;
import com.freshdirect.cms._import.dao.CMSInfrastructureDao;

import oracle.jdbc.pool.OracleDataSource;

public abstract class TaskBase implements ToolTask {
    private static final Logger LOGGER = Logger.getLogger(TaskBase.class);
    
    protected String basePath = ".";
    
    protected Properties toolProps;

    protected DataSource outDataSource;

    protected TaskBase(String basePath) {
        this.basePath = basePath != null ? basePath : ".";

        this.toolProps = new Properties();
        
        // Locate importtool.properties
        final String propName = "importtool.properties";
        
        File localFile = new File(getBasePath(), propName);
        if (localFile.exists()) {
            try {
                toolProps.load( new FileInputStream(localFile) );
            } catch (FileNotFoundException e) {
                LOGGER.error("Failed to load properties file", e);
            } catch (IOException e) {
                LOGGER.error("Failed to load properties file", e);
            }
        } else {
            // load file from system classpath
            try {
                toolProps.load( ClassLoader.getSystemClassLoader().getResourceAsStream("importtool.properties"));
            } catch (Exception e) {
                LOGGER.warn("Failed to load properties from importtool.properties");
            }
        }
    
        try {
            final OracleDataSource ods = new OracleDataSource();
            ods.setURL(getProperty("jdbc.url"));
            ods.setUser(getProperty("cms.user"));
            ods.setPassword(getProperty("cms.password"));

            this.outDataSource = ods;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize tool",e);
        }
    }
    
    protected File getBasePath(){
        /* if (basePath == null) {
            basePath = ".";
        } */

        File fBase = new File(basePath);
        if (!fBase.isDirectory()) {
            throw new RuntimeException("Path [" +basePath+ "] to storedata folder is not a folder");
        }

        return fBase;
    }
    

    protected File getFile(String fileName){
        File fBase = getBasePath();
        File file = new File(fBase, fileName);
        
        if (!file.isFile()) {
            throw new RuntimeException(fBase + "/" + fileName + " is not a file or does not exist");
        }
        return file;
    }

    

    

    protected boolean runScript(final CMSInfrastructureDao dao, String script) {
        
        Reader reader = null;
        try {
            final String path = "com/freshdirect/cms/_import/scripts/" + script;
            InputStream stream = ImportTool.class.getClassLoader().getResourceAsStream(path);
            if (stream == null) {
                LOGGER.error("Script could not be loaded " + script);
                return false;
            }
            
            
            reader = new InputStreamReader(stream);
            if (script.endsWith(".input")) {
                reader = new PlaceholderResolver(reader, toolProps).getReader();
            }
            if (!dao.runScript(reader)) {
                return false;
            }

        } catch (IOException e) {
            LOGGER.error("Error in PropertySubstituterReader",e);
            return false;
        
        } finally {
            try {
                if (reader != null)
                    reader.close();
                reader = null;
            } catch (IOException e) {
            }
        }
        return true;
    }


    protected String getProperty (String property){
        String value = toolProps.getProperty(property);
        if (value == null)
            throw new NullPointerException("missing property " + property);
        else {
            return value;
        }
    }
}
