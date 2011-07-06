package com.freshdirect.cms.si;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.UserI;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.db.DbContentService;
import com.freshdirect.cms.application.service.db.DbTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.si.dao.CMSInfrastructureDao;

public class ImportTool {
	public static final Logger LOGGER = Logger.getLogger(ImportTool.class);
	
	/* File pointing to 'Store.xml.gz' */
	File storeFile;

	/* File pointing to 'Media.xml.gz' */
	File mediaFile;

	private CmsManager inManager = null;

	private DataSource outDataSource = null;

	private CmsManager outManager = null;

	private static final int BATCH_SIZE = 20000;


	public ImportTool(File store, File media) {
		this.storeFile = store;
		this.mediaFile = media;

		if (!init()) {
			throw new RuntimeException("Failed to initialize tool");
		}
	}
	



	public void doImportStore() {
		final CMSInfrastructureDao outDao = new CMSInfrastructureDao(outDataSource);
		final UserI master = new UserI() {
			@Override
			public String getName() {
				return "Darth Vader";
			}

			@Override
			public boolean isAllowedToWrite() {
				return true;
			}
		};
		

		// DROP INDICES
		LOGGER.info("Drop indices");
		outDao.dropIndices();

		
		// Flush tables
		{
			LOGGER.info("Cleanup CMS tables");
    		long t0 = System.currentTimeMillis();
    		outDao.flushCMSTables();
    		long t1 = System.currentTimeMillis();
    		LOGGER.info(" ... it took " + Math.round((t1-t0)/1000) + " secs");
    		
		}
		
    	// Create indices #1
		LOGGER.info("Create indices (phase one)");
		outDao.createIndicesPhaseOne();
		outDao.analyzeTables();
		
		
    	
    	
    	final Set<ContentKey> allKeys = inManager.getContentKeys();
    	
    	final int n_batches = allKeys.size()/BATCH_SIZE;
    	int k=0;
    	int b=0;
    	
    	
    	Iterator<ContentKey> cit = allKeys.iterator();
    	CmsRequest r = new CmsRequest(master);
    	while (cit.hasNext()) {
			r.addNode(inManager.getContentNode(cit.next()));
			k++;
			
			if (k>0 && k%BATCH_SIZE == 0) {
	    		LOGGER.info("Write out batch "+b+"/"+n_batches+" ("+r.getNodes().size()+" nodes)");
	    		
	    		long t0 = System.currentTimeMillis();
				outManager.handle(r, true);
	    		long t1 = System.currentTimeMillis();
	    		LOGGER.info(" ... it took " + Math.round((t1-t0)/1000) + " secs");
	    		
				r = new CmsRequest(master);
				
				b++;
			}
    	}
    	
    	if (r.getNodes().size() > 0) {
    		LOGGER.info("Write out last batch of nodes ("+r.getNodes().size()+")");

    		long t0 = System.currentTimeMillis();
			outManager.handle(r, true);
    		long t1 = System.currentTimeMillis();
    		LOGGER.info(" ... it took " + Math.round((t1-t0)/1000) + " secs");
    	}

	
    	// Create indices #2
    	outDao.createIndicesPhaseTwo();
		outDao.analyzeTables();
		
		LOGGER.info("THE END!");
	}


	protected boolean init() {
		// Initialize input manager
		{
	        List<ContentTypeServiceI> list = new ArrayList<ContentTypeServiceI>();
	        list.add(new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));
	        list.add(new XmlTypeService("classpath:/com/freshdirect/cms/resource/MediaDef.xml"));
	
	        CompositeTypeService typeService = new CompositeTypeService(list);
	
	        StringBuilder resPaths = new StringBuilder();
	        resPaths.append("file:").append(storeFile).append(",file:").append(mediaFile);
	        
	        XmlContentService service = new XmlContentService(typeService, new FlexContentHandler(), resPaths.toString());
	        
	        inManager = new CmsManager(service, null);
	        
	        LOGGER.debug("Loaded " + inManager.getContentKeys().size() + " nodes ...");
		}

        
		// Initialize data source
		try {
		    OracleDataSource ods = new OracleDataSource();

		    // FIXME: put this to configuration file
		    Properties toolProps = new Properties();
		    try {
				toolProps.load( ClassLoader.getSystemClassLoader().getResourceAsStream("tool.properties"));
			} catch (IOException e) {
				LOGGER.error("Failed to load properties", e);
				return false;
			}
		    
//		    ods.setURL("jdbc:oracle:thin:@zetor:1521/DBEU02");
//		    ods.setUser("cmsimp");
//		    ods.setPassword("cmsimp");
			String str = null;
			
		    ods.setURL( toolProps.getProperty("jdbc.url") );
		    
		    str = toolProps.getProperty("jdbc.user");
		    if (str != null)
		    	ods.setUser( str );
		    str = toolProps.getProperty("jdbc.password");
		    if (str != null)
		    	ods.setPassword( str );

		    
		    outDataSource = ods;
		} catch (SQLException e) {
			LOGGER.error(e);
			return false;
		}
		
		// initialize out manager
		{
			DbTypeService dbService = new DbTypeService();
			dbService.setDataSource(outDataSource);
			dbService.initialize();
			
	        DbContentService dbContentService = new DbContentService();
        	dbContentService.setContentTypeService(dbService);

        	dbContentService.setDataSource(outDataSource);
        	
	        outManager = new CmsManager(dbContentService, null);
		}
		
		return true;
	}
	

	protected void addNode(ContentNodeI node, CmsRequest req, CmsManager inManager) {
		req.addNode(node);

		for (ContentKey subKey : node.getChildKeys()) {
			req.addNode(inManager.getContentNode(subKey));
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean showUsage = true;
		ImportTool tool = null;
		
		for (int k=0; k<args.length; k++) {
			if (
					"--storedata".equalsIgnoreCase(args[k]) ||
					"-s".equalsIgnoreCase(args[k])
			) {
				showUsage = false;
				
				String basePath = args[k+1];
				
				if (basePath == null) {
					System.err.println("Path to storedata folder is invalid");
					System.exit(1);
				}
				
				File fBase = new File(basePath);
				if (!fBase.isDirectory()) {
					System.err.println("Path to storedata folder is not a folder");
					System.exit(1);
				}
				
				File fStoreXML = new File(fBase, "Store.xml.gz");
				File fMediaXML = new File(fBase, "Media.xml.gz");
				
				if (!fStoreXML.isFile()) {
					System.err.println("Store.xml.gz is not a file or does not exist.");
					System.exit(1);
				}

				if (!fMediaXML.isFile()) {
					System.err.println("Media.xml.gz is not a file or does not exist.");
					System.exit(1);
				}
				
				tool = new ImportTool(fStoreXML, fMediaXML);
				showUsage = false;
				break;
			}
		}
		
		
		if (showUsage) {
			System.out.println("Usage: ImportStoreDataTool --storedata <path to storedata>");
			System.exit(0);
		}

	
		
		if (tool != null) {
			tool.doImportStore();
			
			System.exit(0);
		} else {
			System.exit(1);
		}
	}
}
