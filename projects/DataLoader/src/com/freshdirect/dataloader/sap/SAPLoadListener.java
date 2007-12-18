/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.sap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.sap.ejb.SAPLoaderHome;
import com.freshdirect.dataloader.sap.ejb.SAPLoaderSB;
import com.freshdirect.dataloader.sap.jco.SapBatchListenerI;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * this class processes a set of batch export files from SAP grouped together
 * in a directory.  it parses all the files in the batch, validates them as
 * best it can, and then sends the parsed structures to a session bean that
 * performs all of the updates/deletions/creations
 *
 * @version $Revision$
 * @author $Author$
 */
public class SAPLoadListener implements SapBatchListenerI {

	private static Category LOGGER = LoggerFactory.getInstance(SAPLoadListener.class);

	String ftpHost = ErpServicesProperties.getProperty(ErpServicesProperties.PROP_LOADER_FTP_HOST);
	String ftpUser = ErpServicesProperties.getProperty(ErpServicesProperties.PROP_LOADER_FTP_USER);
	String ftpPasswd = ErpServicesProperties.getProperty(ErpServicesProperties.PROP_LOADER_FTP_PASSWD);

	String ftpLocalDirectory = ErpServicesProperties.getProperty(ErpServicesProperties.PROP_LOADER_FTP_WORKDIR);

	//
	// builder
	//
	/** the object that coallates the results of the parsers
	 */
	TreeBuilder treeBuilder = null;

	//
	// list of exceptions
	//
	/** a list of exceptions that occurred during parsing
	 */
	List exceptionList = null;

	/** default constructor
	 */
	public SAPLoadListener() {
		//
		// list of exceptions found parsing a file
		//
		this.exceptionList = new LinkedList();
	}

	/**
	 * FTP the files to a work directory
	 */
	protected String fetchFiles(String path, String prefix) throws IOException {

		// create the directory for downloads
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String dirName = ftpLocalDirectory + df.format(new Date());
		File workDir = new File(dirName);
		workDir.mkdirs();

		// download stuff
		FTPClient client = new FTPClient();
		client.setDefaultTimeout(30000);
		client.setDataTimeout(30000);
		try {

			LOGGER.info("FTP: connecting to host " + ftpHost);
			client.connect(ftpHost);
			int reply = client.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				throw new IOException("Connect failed");
			}

			LOGGER.info("FTP: logging in as " + ftpUser);
			if (!client.login(ftpUser, ftpPasswd)) {
				throw new IOException("Login failed");
			}

			if (!client.changeWorkingDirectory(path)) {
				throw new IOException("Unable to change directory to " + path);
			}

			LOGGER.info("Downloading files...");
			fetchFile(client, workDir, prefix + "_materials.txt");
			fetchFile(client, workDir, prefix + "_prices.txt");
			fetchFile(client, workDir, prefix + "_units.txt");
			fetchFile(client, workDir, prefix + "_variants.txt");
			fetchFile(client, workDir, prefix + "_variant_prices.txt");

			LOGGER.info("FTP: logging out");
			client.logout();

		} finally {
			if (client.isConnected()) {
				try {
					LOGGER.info("FTP: disconnecting");
					client.disconnect();
				} catch (IOException ex) {
					LOGGER.warn("FTP: ignored problem on disconnect", ex);
				}
			}
		}

		return dirName;
	}

	private void fetchFile(FTPClient client, File workDir, String fileName) throws IOException {
		FileOutputStream fos = new FileOutputStream(new File(workDir, fileName));
		boolean ok = client.retrieveFile(fileName, fos);
		fos.close();
		if (!ok) {
			throw new IOException("Unable to retrive file " + fileName);
		}
	}

	public void processErpsBatch(String ftpPath, String prefix) throws LoaderException {

		String destination;
		try {
			destination = fetchFiles(ftpPath, prefix);
		} catch (IOException ex) {
			LOGGER.warn("Failed to fetch files", ex);
			throw new LoaderException(ex);
		}

		LOGGER.info("----- SAP Loader Starting -----");

		this.exceptionList.clear();

		//
		// create builder and parsers
		//
		this.treeBuilder = new TreeBuilder();
		this.treeBuilder.setMaterialParser(new MaterialParser());
		this.treeBuilder.setMaterialPriceParser(new MaterialPriceParser());
		this.treeBuilder.setSalesUnitParser(new SalesUnitParser());
		this.treeBuilder.setVariantParser(new VariantParser());
		this.treeBuilder.setVariantPriceParser(new VariantPriceParser());

		try {
			//
			// parse the raw files
			//
			this.parseBatch(destination, prefix);

			//
			// build the model trees
			//
			this.buildTree(true);

			//
			// bail if any problems were found with the data
			//
			if ((!this.parseSuccessful()) || (!this.buildSuccessful())) {
				List bdes = new ArrayList();
				bdes.addAll(this.getParsingExceptions());
				bdes.addAll(this.getBuildExceptions());
				throw new LoaderException(
					(BadDataException[]) bdes.toArray(new BadDataException[0]),
					"There were errors in the SAP data.  Please investigate loader log for details.");
			}
			//
			// upload the model trees
			//
			this.doLoad(true);

		} catch (BadDataException bde) {
			LOGGER.warn("Bad data during parsing", bde);
			throw new LoaderException(bde);
		} catch (LoaderException le) {
			LOGGER.warn("Unable to load data", le);
			throw le;
		} catch (RemoteException re) {
			LOGGER.warn("Error talking to remote components", re);
			throw new LoaderException(re);
		} catch (Exception ex) {
			LOGGER.warn("Unexpected exception occured", ex);
			throw new LoaderException(ex);
		}

		LOGGER.info("----- SAP Loader Done -----");

	}

	public TreeBuilder getTreeBuilder() {
		return this.treeBuilder;
	}

	/**
	 * indicates whether any parsing errors were found during a run
	 *
	 * @return true if no exceptions occurred during loading
	 */
	public boolean parseSuccessful() {
		return (this.exceptionList.size() == 0);
	}

	public boolean buildSuccessful() {
		return this.treeBuilder.buildSuccessful();
	}

	/** @return list of BadDataExceptions */
	public List getParsingExceptions() {
		return this.exceptionList;
	}

	/** @return list of BadDataExceptions */
	public List getBuildExceptions() {
		return this.treeBuilder.getBuildExceptions();
	}

	/** loads and parses all of the information from a set of files in a single batch
	 * @param batchRepository the path to the directory containing batch export folders
	 * @param batchFolder the directory within the batchRepository that contains a set of export files
	 * @param department the prefix for the names of the files within a batch
	 * @throws BadDataException any unrecoverable errors encountered during loading
	 */
	public void parseBatch(String batchFolder, String prefix) throws BadDataException {

		String baseFilename = batchFolder + File.separator + prefix;

		LOGGER.info("----- Load Materials -----");
		treeBuilder.getMaterialParser().parseFile(baseFilename + "_materials.txt");
		this.exceptionList.addAll(treeBuilder.getMaterialParser().getExceptions());

		LOGGER.info("----- Load Material Prices -----");
		treeBuilder.getMaterialPriceParser().parseFile(baseFilename + "_prices.txt");
		this.exceptionList.addAll(treeBuilder.getMaterialPriceParser().getExceptions());

		LOGGER.info("----- Load Sales Units -----");
		treeBuilder.getSalesUnitParser().parseFile(baseFilename + "_units.txt");
		this.exceptionList.addAll(treeBuilder.getSalesUnitParser().getExceptions());

		LOGGER.info("----- Load Variants -----");
		treeBuilder.getVariantParser().parseFile(baseFilename + "_variants.txt");
		this.exceptionList.addAll(treeBuilder.getVariantParser().getExceptions());

		LOGGER.info("----- Load Variant Prices -----");
		treeBuilder.getVariantPriceParser().parseFile(baseFilename + "_variant_prices.txt");
		this.exceptionList.addAll(treeBuilder.getVariantPriceParser().getExceptions());

	}

	/** coallates the results of all the parsers and does some integrity checking and validation
	 * @throws BadDataException any unrecoverable problems encountered during building
	 */
	public void buildTree() throws BadDataException {
		buildTree(false);
	}

	/** coallates the results of all the parsers and does some integrity checking and validation
	 * optionally prints a report of what it built
	 * @param verbose indicates whether the build process should output a report of what it put together
	 * @throws BadDataException any unrecoverable errors encountered during building
	 */
	public void buildTree(boolean verbose) throws BadDataException {
		LOGGER.info("----- Build Model Tree -----");
		treeBuilder.build(verbose);
	}

	/** send the results of the parsing and building to the session bean on the server
	 * that performs the updates
	 * @throws LoaderException any problems encountered by the session bean as it modifies the system
	 * @throws RemoteException any system level problems encountered communicating with the session bean
	 */
	public void doLoad() throws LoaderException, RemoteException {
		doLoad(true);
	}

	/** send the results of the parsing and building to the session bean on the server
	 * that performs the updates.  optionally, can be told not to actually perform the load
	 * @param doit if false, the load is not actually performed
	 * @throws LoaderException any problems encountered by the session bean as it modifies the system
	 * @throws RemoteException any system level problems encountered communicating with the session bean
	 */
	public void doLoad(boolean doit) throws LoaderException, RemoteException {

		if (!doit)
			return;

		LOGGER.info("----- starting doLoad() -----");

		HashMap classes = treeBuilder.getClasses();
		HashMap activeMaterials = treeBuilder.getActiveMaterials();
		HashMap characteristicValuePrices = treeBuilder.getCharacteristicValuePrices();

		Context ctx = null;
		try {
			ctx = com.freshdirect.ErpServicesProperties.getInitialContext();
			SAPLoaderHome home = (SAPLoaderHome) ctx.lookup("freshdirect.dataloader.SAPLoader");

			SAPLoaderSB sapl = home.create();

			sapl.loadData(classes, activeMaterials, characteristicValuePrices);

			LOGGER.info("----- loaded data -----");
			
			LOGGER.info("----- normally exiting doLoad() -----");

		} catch (CreateException ce) {
			LOGGER.warn("Unable to create session bean", ce);
		} catch (NamingException ne) {
			LOGGER.warn("Failed to look up session bean home", ne);
		} finally {
			try {
				if (ctx != null)
					ctx.close();
			} catch (NamingException ne) {
				LOGGER.warn("Error closing naming context", ne);
			}
		}

	}

}
