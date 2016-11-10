package com.freshdirect.cms._import;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms._import.dao.CMSInfrastructureDao;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.CmsRequestI.Source;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.UserI;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.db.DbContentService;
import com.freshdirect.cms.application.service.db.DbTypeService;
import com.freshdirect.cms.application.service.media.MediaService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.framework.conf.ResourceUtil;
import com.freshdirect.framework.xml.XSLTransformer;

import oracle.jdbc.pool.OracleDataSource;

public class ImportTool {

	public static final Logger LOGGER = Logger.getLogger(ImportTool.class);
	private static final int BATCH_SIZE = 20000;
	private static final String GRANT_USER_PROPERTY = "grant.user";
	private static final String GRANT_USER_PROPERTY_DELIMITER = ",";
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
	
	private String basePath;
	private File storeFile; // File pointing to 'Store.xml.gz'
	private File mediaFile; // File pointing to 'Media.xml.gz'
	private File storeDefFile; // File pointing to 'CMSStoreDef.xml'
	private DataSource outDataSource;
	private Properties toolProps;

	private enum Command {
		newUser, createSchema, dropSchema, loadDefinition, importData, switchCms
	}


	public ImportTool(String basePath) {
	    this.basePath = basePath;
		toolProps = new Properties();
		
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
	}


	public static void main(String[] args) {

		// LOG4J - apply basic configuration
		BasicConfigurator.configure();

		
		Command cmd = null;
		
		String basePath = null;
		
		for (String arg : args){
			if ("--create_user".equalsIgnoreCase(arg)){
				cmd = Command.newUser;
			} else if ("--create_schema".equalsIgnoreCase(arg)) {
				cmd = Command.createSchema;
			} else if ("--drop_schema".equalsIgnoreCase(arg)) {
				cmd = Command.dropSchema;
			} else if ("--load_definition".equalsIgnoreCase(arg)) {
				cmd = Command.loadDefinition;
			} else if ("--import_data".equalsIgnoreCase(arg)) {
				cmd = Command.importData;
			} else if ("--switch_cms".equalsIgnoreCase(arg)) {
				cmd = Command.switchCms;
			} else {
				if (basePath == null){
					basePath = arg;
				} else {
					throw new RuntimeException("Too many arguments!");
				}
			}
		}


		if (cmd == null){
			showUsage();
			System.exit(0);
		}


		try {
			ImportTool importTool = new ImportTool(basePath);
			switch(cmd) {
				case newUser:
					importTool.printUserCreationScript();
					break;
				case dropSchema:
					importTool.init();
					importTool.doDropSchema();
					break;
				case createSchema:
					importTool.init();
					importTool.doCreateSchema();
					break;
				case loadDefinition:
					importTool.init();
					importTool.initStoreDefFile();
					importTool.doLoadDefinition();
					break;
				case importData:
					importTool.init();
					importTool.initSrcFiles();
					importTool.doImportData();
					break;
				case switchCms:
					importTool.printChangeLinkedCmsUserScript();
					break;
			}
			
		} catch (Throwable t) {
			LOGGER.error(t);
			showUsage();
			System.exit(1);
		}
	}

	protected static void showUsage() {
		System.out.println("See README.txt for configuration and usage details!");
	}

	private void init(){
		try {
		    OracleDataSource ods = new OracleDataSource();
	    	ods.setURL(getProperty("jdbc.url"));
	    	ods.setUser(getProperty("cms.user"));
	    	ods.setPassword(getProperty("cms.password"));
		    outDataSource = ods;
		} catch (SQLException e) {
			throw new RuntimeException("Failed to initialize tool",e);
		}
	}

	private File getBasePath(){
		if (basePath == null) {
			basePath = ".";
		}
		
		File fBase = new File(basePath);
		if (!fBase.isDirectory()) {
			throw new RuntimeException("Path [" +basePath+ "] to storedata folder is not a folder");
		}
		
		return fBase;
	}
	
	private File getFile(String fileName){
		File fBase = getBasePath();
		File file = new File(fBase, fileName);
		
		if (!file.isFile()) {
			throw new RuntimeException(fBase + "/" + fileName + " is not a file or does not exist");
		}
		return file;
	}
	
	private void initSrcFiles(){
		storeFile = getFile("Store.xml.gz");
		mediaFile = getFile("Media.xml.gz");
	}

	private void initStoreDefFile(){
		storeDefFile = getFile("CMSStoreDef.xml");
	}

	
	private String getProperty (String property){
		String value = toolProps.getProperty(property);
	    if (value == null)
	    	throw new NullPointerException("missing property " + property);
	    else {
	    	return value;
	    }
	}
	
	public boolean printUserCreationScript(){
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
	
	public void doCreateSchema() {
		LOGGER.info("Create Schema");
		
		final CMSInfrastructureDao dao = new CMSInfrastructureDao(outDataSource);
		
		final String[] scripts = {
			"01_create_typedefs.sql",
			"02_create_content.sql",
			"03_create_publish.sql",
			"04_create_indices.sql",
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


	public void doDropSchema() {
		LOGGER.info("Drop Schema");
		
		final CMSInfrastructureDao dao = new CMSInfrastructureDao(outDataSource);
		
		final String[] scripts = {
			"99_drop_indices.sql",
			"98_drop_publish.sql",
			"97_drop_content.sql",
			"96_drop_typedefs.sql"
		};
		
		for (String script : scripts) {
			if (!runScript(dao, script)) {
				throw new RuntimeException("Schema Drop Failure");
			}
		}

		LOGGER.info("Schema Dropped");
	}

	private boolean runScript(final CMSInfrastructureDao dao, String script) {
		
		Reader reader = null;
		try {
			InputStream stream = ImportTool.class.getClassLoader().getResourceAsStream("com/freshdirect/cms/_import/scripts/" + script);
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

	public void doLoadDefinition (){
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
	
	public void doImportData() {
		doImportStore();
		doImportMedia();
		doPostOperations();
	}
	
	/**
	 * Imports CMS Nodes from Store.xml to database
	 */
	protected void doImportStore() {
		LOGGER.info("Import Store content");
		ContentServiceI inStoreManager = getStoreManager();
		ContentServiceI outStoreManager = getStoreExportManager(outDataSource);
		
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
		
    	// Create indices #1
		LOGGER.info("Create indices (phase one)");
		outDao.createStoreIndicesPhaseOne();
		outDao.analyzeStoreTables();
		
    	final Set<ContentKey> allKeys = inStoreManager.getContentKeys(DraftContext.MAIN);
    	
    	final int n_batches = (int) Math.ceil( (((double)allKeys.size()) / BATCH_SIZE ));
    	int k=0;
    	int b=1;
    	
    	Iterator<ContentKey> cit = allKeys.iterator();
		CmsRequest req = new CmsRequest(MASTER, Source.STORE_IMPORT);

    	while (cit.hasNext()) {
			req.addNode(inStoreManager.getContentNode(cit.next(), DraftContext.MAIN));
			k++;
			
			if (k>0 && k%BATCH_SIZE == 0) {
	    		LOGGER.info("Write out batch "+b+"/"+n_batches+" ("+req.getNodes().size()+" nodes)");
	    		
	    		long t0 = System.currentTimeMillis();
				// outStoreManager.handle(req, true);
				outStoreManager.handle(req);
	    		long t1 = System.currentTimeMillis();
	    		LOGGER.debug(" ... it took " + Math.round((t1-t0)/1000) + " secs");
	    		
				req = new CmsRequest(MASTER);
				
				b++;
			}
    	}
    	
    	if (req.getNodes().size() > 0) {
    		LOGGER.info("Write out last batch of nodes ("+req.getNodes().size()+")");

    		long t0 = System.currentTimeMillis();
			// outStoreManager.handle(req, true);
			outStoreManager.handle(req);
    		long t1 = System.currentTimeMillis();
    		LOGGER.debug(" ... it took " + Math.round((t1-t0)/1000) + " secs");
    	}
	
    	// Create indices #2
    	outDao.createStoreIndicesPhaseTwo();
		outDao.analyzeStoreTables();

		LOGGER.info("Import Store content finished");
	}

	

	/**
	 * Imports media entries from Media.xml to database
	 */
	protected void doImportMedia() {
		LOGGER.info("Import Media content");
		ContentServiceI inMediaManager = getMediaManager();
		ContentServiceI outMediaManager = getMediaExportManager(outDataSource);
		
		final CMSInfrastructureDao outDao = new CMSInfrastructureDao(outDataSource);
		
		LOGGER.info("Flush media table");
		outDao.dropMediaIndex();
		outDao.flushMediaTable();
		
		LOGGER.info("Import media objects ("+inMediaManager.getContentKeys(DraftContext.MAIN).size()+")");
		
		CmsRequest req = new CmsRequest(MASTER, Source.STORE_IMPORT);
		
		long t0 = System.currentTimeMillis();
		for (ContentKey cKey : inMediaManager.getContentKeys(DraftContext.MAIN)) {
			final ContentNodeI mediaNode = inMediaManager.getContentNode(cKey, DraftContext.MAIN);
			req.addNode(mediaNode);
		}
		// FIXME: media manager does not save anything ...
		outMediaManager.handle(req);
		long t1 = System.currentTimeMillis();
		LOGGER.debug(" ... it took " + Math.round((t1-t0)/1000) + " secs");

		
		LOGGER.info("Enable media index");
		outDao.addMediaIndex();
		LOGGER.info("Import Media content finished");
	}

	
	private void doPostOperations() {
		LOGGER.info("Adjust System Sequence Value");
		final CMSInfrastructureDao outDao = new CMSInfrastructureDao(outDataSource);
		outDao.fixSystemSequenceValue();
	}

	public boolean printChangeLinkedCmsUserScript(){
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
	
	
	protected ContentServiceI getImportManager(String defPath, File inFile) {
        List<ContentTypeServiceI> list = new ArrayList<ContentTypeServiceI>();
        list.add(new XmlTypeService(defPath));

        CompositeTypeService typeService = new CompositeTypeService(list);

        XmlContentService service = new XmlContentService(typeService,
        		new FlexContentHandler(),
        		"file:"+inFile.getPath());

        return service;
	}
	

	/**
	 * Returns cms manager having Store.xml imported
	 */
	protected ContentServiceI getStoreManager() {
		return getImportManager("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml", storeFile);
	}


	/**
	 * Returns cms manager having Store.xml imported
	 */
	protected ContentServiceI getMediaManager() {
		return getImportManager("classpath:/com/freshdirect/cms/resource/MediaDef.xml", mediaFile);
	}


	protected ContentServiceI getStoreExportManager(DataSource ds) {
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
	
	protected ContentServiceI getMediaExportManager(DataSource ds) {
		if (ds == null)
			return null;

		MediaService dbContentService = new MediaService(ds);
		
		return dbContentService;
	}
	
	
	


	protected void addNode(ContentNodeI node, CmsRequest req, ContentServiceI inManager) {
		req.addNode(node);

		for (ContentKey subKey : node.getChildKeys()) {
			req.addNode(inManager.getContentNode(subKey, DraftContext.MAIN));
		}
	}
	

}
