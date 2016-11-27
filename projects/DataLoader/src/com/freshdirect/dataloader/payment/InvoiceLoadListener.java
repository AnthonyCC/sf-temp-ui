package com.freshdirect.dataloader.payment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.framework.util.log.LoggerFactory;

public class InvoiceLoadListener implements InvoiceBatchListenerI{
	
	private static Category LOGGER = LoggerFactory.getInstance( InvoiceLoadListener.class );    
    
    String ftpHost = ErpServicesProperties.getProperty( ErpServicesProperties.PROP_INVOICES_FTP_HOST );
    String ftpUser = ErpServicesProperties.getProperty( ErpServicesProperties.PROP_INVOICES_FTP_USER );
    String ftpPasswd = ErpServicesProperties.getProperty( ErpServicesProperties.PROP_INVOICES_FTP_PASSWD );
    
    String ftpLocalDirectory = ErpServicesProperties.getProperty( ErpServicesProperties.PROP_INVOICES_FTP_WORKDIR);
	
	public void processInvoiceBatch(String folder, String fileName) throws LoaderException {
		
		try {
        	fetchInvoiceBatch(folder, fileName);
        	InvoiceLoader loader = new InvoiceLoader();
        	String localFile = ftpLocalDirectory + File.separator + fileName;
        	loader.processInvoiceBatch(localFile);
        } catch (IOException ex) {
        	LOGGER.warn("Failed to fetch files", ex);
        	throw new LoaderException(ex);
        }
		
	}
	
	/**
     * FTP the Invoice Batch to a work directory
     */
    protected void fetchInvoiceBatch(String folder, String fileName) throws IOException {

		// create the directory for downloads
    	File workDir = new File(ftpLocalDirectory);

		// download stuff
		FTPClient client = new FTPClient();
		client.setDefaultTimeout(30000);
		client.setDataTimeout(30000);
		try {
	
	        LOGGER.info("FTP: connecting to host "+ftpHost);
	        client.connect( ftpHost );
	        int reply = client.getReplyCode();
	        if (! FTPReply.isPositiveCompletion(reply) ) {
	        	throw new IOException("Connect failed");
	        }
	
	        LOGGER.info("FTP: logging in as "+ftpUser);
	        if (! client.login(ftpUser, ftpPasswd) ) {
	        	throw new IOException("Login failed");	
	        }
	        
	        if (! client.changeWorkingDirectory(folder) ) {
	        	throw new IOException("Unable to change directory to "+folder);
	        }

	        LOGGER.info("Downloading files...");
			FileOutputStream fos = new FileOutputStream( new File(workDir, fileName) );
			boolean ok = client.retrieveFile(fileName, fos);
			fos.close();
			
			if (!ok) {
				throw new IOException("Unable to retrive file "+fileName);
			}
	
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
	}
}

