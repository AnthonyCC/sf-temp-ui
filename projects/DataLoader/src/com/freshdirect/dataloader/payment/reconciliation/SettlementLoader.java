package com.freshdirect.dataloader.payment.reconciliation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.DataLoaderProperties;
import com.freshdirect.dataloader.payment.reconciliation.detail.DetailParser;
import com.freshdirect.dataloader.payment.reconciliation.summary.SummaryParser;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.ejb.ReconciliationHome;
import com.freshdirect.payment.ejb.ReconciliationSB;
import com.freshdirect.sap.command.SapSendSettlement;
import com.freshdirect.sap.ejb.SapException;

public class SettlementLoader {

	private static final Category LOGGER = LoggerFactory.getInstance(SettlementLoader.class);
	private static final SimpleDateFormat SF = new SimpleDateFormat("yyyy_MM_dd");

	private static final boolean DEBUG = false;

	public static void main(String[] args) {
		SettlementLoader loader = new SettlementLoader();
		boolean doFtp = true;
		if (args.length > 0) {
			String firstArg = args[0];
			if ("false".equalsIgnoreCase(firstArg)) {
				doFtp = false;
			}
		}

		try {
			loader.loadSettlements(doFtp);
		} catch (Exception e) {
			LOGGER.fatal("Failed to load settlement", e);
		}
	}

	public void loadSettlements(boolean doFtp) throws RemoteException, EJBException, IOException, CreateException, SapException {
		String timestamp = SF.format(new Date());

		//make summary file name
		File summaryFile = new File(DataLoaderProperties.getWorkingDir() + "summary_file_" + timestamp + ".txt");

		//make transaction file name
		File detailFile = new File(DataLoaderProperties.getWorkingDir() + "detail_file_" + timestamp + ".txt");

		//get the files from payment processor
		if (doFtp) {
			downloadFiles(summaryFile, detailFile);
		}
		//load the files in ERPS and generate SAP reconciliation file
		String fileName = loadFiles(summaryFile, detailFile);
		//now ftp the settlement File to SAP
		this.uploadFileToSap(fileName);

		//tell sap to the file is there
		SapSendSettlement command = new SapSendSettlement(fileName, DataLoaderProperties.getSapUploadFolder());
		command.execute();

	}

	private void uploadFileToSap(String fileName) throws IOException {

		LOGGER.info("started uploading file to sap");
		FTPClient client = new FTPClient();
		//5 minutes
		client.setDefaultTimeout(600000);
		client.setDataTimeout(600000);
		try {
			boolean ok = true;
			FileInputStream ifs = null;

			if (DEBUG)
				LOGGER.debug("connecting...");

			client.connect(DataLoaderProperties.getSapFtpIp());
			int reply = client.getReplyCode();
			ok = FTPReply.isPositiveCompletion(reply);
			if (ok) {
				if (DEBUG)
					LOGGER.debug("logging in...");
				ok = client.login(DataLoaderProperties.getSapFtpUser(), DataLoaderProperties.getSapFtpPassword());
			}
			if (ok) {
				if (DEBUG)
					LOGGER.debug("uploading reconciliation file... " + fileName);

				ifs = new FileInputStream(new File(DataLoaderProperties.getWorkingDir() + fileName));
				ok = client.storeFile(fileName, ifs);

			}
			if (ok) {
				if (DEBUG)
					LOGGER.debug("logging out...");
				client.logout();
			}
		} finally {
			if (client.isConnected()) {
				try {
					if (DEBUG)
						LOGGER.debug("disconnecting...");
					client.disconnect();
				} catch (IOException ie) {
					LOGGER.warn("IOException while trying to cleanup after FTP", ie);
				}
			}
			if (DEBUG)
				LOGGER.debug("done...");
		}

		LOGGER.info("finished uploading the file to sap");
	}

	private void downloadFiles(File summaryFile, File detailFile) throws IOException {
		LOGGER.info("started ftpying the files");

		FTPClient client = new FTPClient();
		//5 minutes
		client.setDefaultTimeout(600000);
		client.setDataTimeout(600000);

		try {
			boolean ok = true;
			FileOutputStream ofs = null;

			if (DEBUG)
				LOGGER.debug("connecting...");

			client.connect(DataLoaderProperties.getFtpIp());
			int reply = client.getReplyCode();
			ok = FTPReply.isPositiveCompletion(reply);
			if (ok) {
				if (DEBUG)
					LOGGER.debug("logging in...");
				ok = client.login(DataLoaderProperties.getFtpUser(), DataLoaderProperties.getFtpPassword());
			}
			if (ok) {
				if (DEBUG)
					LOGGER.debug("retreiving summary file... " + summaryFile.getName());

				ofs = new FileOutputStream(summaryFile);
				ok = client.retrieveFile(DataLoaderProperties.getSummaryFileName(), ofs);

				if (DEBUG)
					LOGGER.debug("retreiving transaction file... " + detailFile.getName());

				ofs = new FileOutputStream(detailFile);
				ok = client.retrieveFile(DataLoaderProperties.getTransactionFileName(), ofs);
				ofs.close();
			}
			if (ok) {
				if (DEBUG)
					LOGGER.debug("logging out...");
				client.logout();
			}
		} finally {
			if (client.isConnected()) {
				try {
					if (DEBUG)
						LOGGER.debug("disconnecting...");
					client.disconnect();
				} catch (IOException ie) {
					LOGGER.warn("IOException while trying to cleanup after FTP", ie);
				}
			}
			if (DEBUG)
				LOGGER.debug("done...");
		}
		LOGGER.info("finished ftpying the files");
	}

	private String loadFiles(File summaryFile, File detailFile)
		throws IOException, RemoteException, EJBException, CreateException {

		ReconciliationSB reconSB = this.lookupReconciliationHome().create();
		SapFileBuilder builder = new SapFileBuilder();

		LOGGER.info("starting to load summary file");
		InputStream is = new FileInputStream(summaryFile);

		//create summary parser and set summary loader as its client
		SummaryParser summaryParser = new SummaryParser();
		summaryParser.setClient(new SummaryParserClient(builder, reconSB));
		summaryParser.parseFile(is);

		is.close();
		LOGGER.info("finished loading summary file");

		LOGGER.info("starting to load detail file");
		is = new FileInputStream(detailFile);

		//create detail parser and set detail loader as its client 
		DetailParser detailParser = new DetailParser();
		detailParser.setClient(new DetailParserClient(builder, reconSB));
		detailParser.parseFile(is);

		is.close();
		LOGGER.info("finished loading detail file");
		String fileName = DataLoaderProperties.getSapFileNamePrefix() + "_" + SF.format(new Date()) + ".txt";

		File f = new File(DataLoaderProperties.getWorkingDir() + fileName);
		builder.writeTo(f);
		return fileName;

	}

	private ReconciliationHome lookupReconciliationHome() throws EJBException {
		Context ctx = null;
		try {
			ctx = getInitialContext();
			return (ReconciliationHome) ctx.lookup("freshdirect.payment.Reconciliation");
		} catch (NamingException ex) {
			throw new EJBException(ex);
		} finally {
			try {
				ctx.close();
			} catch (NamingException ne) {
				LOGGER.debug(ne);
			}
		}
	}
	
	/** 
	 * helper method to find the naming context for locating objects on a server
	 * 
	 * @throws NamingException any problems encountered locating the remote server
	 * @return the naming context to use to locate remote components on the server
	 */
	protected Context getInitialContext() throws NamingException {

		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL()); //t3://localhost:7006
		env.put(Context.INITIAL_CONTEXT_FACTORY, weblogic.jndi.WLInitialContextFactory.class.getName());
		return new InitialContext(env);

	}

}
