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
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.sap.ejb.SAPLoaderHome;
import com.freshdirect.dataloader.sap.ejb.SAPLoaderSB;

/**
 * this class processes a set of batch export files from SAP grouped together
 * in a directory.  it parses all the files in the batch, validates them as
 * best it can, and then sends the parsed structures to a session bean that
 * performs all of the updates/deletions/creations
 *
 * @version $Revision$
 * @author $Author$
 */
public class SAPLoader {

	/** the url of the server that hosts the loader session bean
	 */
	//String serverUrl = "t3://ems1.nyc1.freshdirect.com:8000";
	String serverUrl = "t3://localhost:7001";

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

	/** run me
	 * @link dependency
	 * @param args
	 */
	/*#SAPLoaderSB lnkSAPLoaderSB;*/

	public static void main(String[] args) {

		System.out.println("\n----- Loader Starting -----");

		SAPLoader loader = new SAPLoader();

		try {
			//
			// parse the raw files
			//
			//loader.load("C:\\FreshDirect\\projects\\FDWebSite\\docroot\\test\\adServing", "20040405_160105", "B");
//			loader.load("C:\\Manoj", "ProduceRating", "B");
			loader.load("C:\\bea10\\freshdirect\\testingIdocs", "20090804_125830", "B");
			//loader.load("\\\\File1\\Corporate Shares\\R3doc\\exports_by_client\\200\\product_data", "20020208_132554", "cheese");
			if (!loader.parseSuccessful()) {
				loader.reportParsingExceptions();
			}
			//
			// build the model trees
			//
			loader.buildTree(true);
			if (!loader.buildSuccessful()) {
				loader.reportBuildExceptions();
			}
			//
			// bail if any problems were found with the data
			//
			if ((!loader.parseSuccessful()) || (!loader.buildSuccessful()))
				return;
			//
			// upload the model trees
			//
			loader.doLoad();

		} catch (BadDataException bde) {
			bde.printStackTrace();
		} catch (LoaderException le) {
			le.printStackTrace();
		} catch (RemoteException re) {
			re.printStackTrace();
		}

		System.out.println("\n\n----- Loader Done -----");

	}

	/** default constructor
	 */
	public SAPLoader() {
		//
		// list of exceptions found parsing a file
		//
		this.exceptionList = new LinkedList();
		//
		// create builder and parsers
		//
		this.treeBuilder = new TreeBuilder();
		this.treeBuilder.setMaterialParser(new MaterialParser());
		this.treeBuilder.setMaterialPriceParser(new MaterialPriceParser());
		this.treeBuilder.setSalesUnitParser(new SalesUnitParser());
		this.treeBuilder.setVariantParser(new VariantParser());
		this.treeBuilder.setVariantPriceParser(new VariantPriceParser());
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

	/**
	 * prints all the exceptions found during parsing
	 *
	 */
	public void reportParsingExceptions() {
		Iterator exIter = exceptionList.iterator();
		while (exIter.hasNext()) {
			BadDataException bde = (BadDataException) exIter.next();
			System.out.println(bde);
		}
	}

	public void reportBuildExceptions() {
		Iterator exIter = this.treeBuilder.getBuildExceptions().iterator();
		while (exIter.hasNext()) {
			BadDataException bde = (BadDataException) exIter.next();
			System.out.println(bde);
		}
	}

	/** loads and parses all of the information from a set of files in a single batch
	 * @param batchRepository the path to the directory containing batch export folders
	 * @param batchFolder the directory within the batchRepository that contains a set of export files
	 * @param department the prefix for the names of the files within a batch
	 * @throws BadDataException any unrecoverable errors encountered during loading
	 */
	public void load(String batchRepository, String batchFolder, String department) throws BadDataException {

		String baseFilename = batchRepository + File.separator + batchFolder + File.separator + department;

		System.out.println("\n----- Load Materials -----");
		treeBuilder.getMaterialParser().parseFile(baseFilename + "_materials.txt");
		this.exceptionList.addAll(treeBuilder.getMaterialParser().getExceptions());

		System.out.println("\n----- Load Material Prices -----");
		treeBuilder.getMaterialPriceParser().parseFile(baseFilename + "_prices.txt");
		this.exceptionList.addAll(treeBuilder.getMaterialPriceParser().getExceptions());

		System.out.println("\n----- Load Sales Units -----");
		treeBuilder.getSalesUnitParser().parseFile(baseFilename + "_units.txt");
		this.exceptionList.addAll(treeBuilder.getSalesUnitParser().getExceptions());

		System.out.println("\n----- Load Variants -----");
		treeBuilder.getVariantParser().parseFile(baseFilename + "_variants.txt");
		this.exceptionList.addAll(treeBuilder.getVariantParser().getExceptions());

		System.out.println("\n----- Load Variant Prices -----");
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
		System.out.println("\n----- Build Model Tree -----");
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

		System.out.println("\n----- starting doLoad() -----");

		HashMap classes = treeBuilder.getClasses();
		HashMap activeMaterials = treeBuilder.getActiveMaterials();
		HashMap characteristicValuePrices = treeBuilder.getCharacteristicValuePrices();

		Context ctx = null;
		try {
			ctx = getInitialContext();
			SAPLoaderHome home = (SAPLoaderHome) ctx.lookup("freshdirect.dataloader.SAPLoader");

			SAPLoaderSB sapl = home.create();

			sapl.loadData(classes, activeMaterials, characteristicValuePrices);

			System.out.println("\n----- normally exiting doLoad() -----");

		} catch (CreateException ce) {
			ce.printStackTrace();
		} catch (NamingException ne) {
			ne.printStackTrace();
		} finally {
			try {
				if (ctx != null)
					ctx.close();
			} catch (NamingException ne) {
				ne.printStackTrace();
			}
		}

	}

	/** helper method to find the naming context for locating objects on a server
	 * @throws NamingException any problems encountered locating the remote server
	 * @return the naming context to use to locate remote components on the server
	 */
	protected Context getInitialContext() throws NamingException {

		Hashtable env = new Hashtable();
		env.put(Context.PROVIDER_URL, serverUrl);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		return new InitialContext(env);

	}

}
